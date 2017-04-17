package com.ym.lab9;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private final String url = "http://ws.webxml.com.cn/WebServices/WeatherWS.asmx/getWeather";

    private String uid = "b3ee954a63d94069b85fa97db49dd056";
//    private String uid = "";

    private final int UPDATE_CONTENT = 0;

    private TextView city;
    private Button search_btn;
    private ListView listView;
    private LinearLayout toggle_layout;

    private TextView tag_city;
    private TextView tag_time;
    private TextView tag_temperature;
    private TextView tag_temperature_range;
    private TextView tag_wet;
    private TextView tag_air;
    private TextView tag_wing;

    private RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        search_btn = (Button)findViewById(R.id.search_btn);
        city = (TextView)findViewById(R.id.city);
        listView = (ListView)findViewById(R.id.listView);
        toggle_layout = (LinearLayout)findViewById(R.id.toggle_layout);

        tag_city = (TextView)findViewById(R.id.tag_city);
        tag_time = (TextView)findViewById(R.id.tag_time);
        tag_temperature = (TextView)findViewById(R.id.tag_temperature);
        tag_temperature_range = (TextView)findViewById(R.id.tag_temperature_range);
        tag_wet = (TextView)findViewById(R.id.tag_wet);
        tag_air = (TextView)findViewById(R.id.tag_air);
        tag_wing = (TextView)findViewById(R.id.tag_wing);

        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);

        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRequestWithHttpURLConnection();
            }
        });
    }

    private void sendRequestWithHttpURLConnection() {

        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo == null || !networkInfo.isConnected()) {
            Toast.makeText(MainActivity.this, "当前没有可用网络！", Toast.LENGTH_SHORT).show();
            return;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                    HttpURLConnection connection = null;
                    try {
                        connection = (HttpURLConnection)((new URL(url).openConnection()));
                        connection.setRequestMethod("POST");
                        connection.setReadTimeout(8000);
                        connection.setConnectTimeout(8000);

                        OutputStream out = connection.getOutputStream();
                        String request = URLEncoder.encode(city.getText().toString(), "utf-8");
                        out.write(("theCityCode="+request+"&theUserID="+uid).getBytes());

                        if (connection.getResponseCode() == 200) {
                            InputStream inputStream = connection.getInputStream();
                            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                            StringBuilder response = new StringBuilder();
                            String line;
                            while ((line = reader.readLine()) != null) {
                                response.append(line);
                            }
                            Log.e("response", response+"");

                            Message message = new Message();
                            message.what = UPDATE_CONTENT;
                            message.obj = response;
                            handler.sendMessage(message);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        if (connection != null) {
                            connection.disconnect();
                        }
                    }
                }
        }).start();
    }

    private Handler handler = new Handler() {
        public void handleMessage(Message message) {
            if (message.what == UPDATE_CONTENT) {
                try {
                    XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                    XmlPullParser parser = factory.newPullParser();
                    parser.setInput(new StringReader(message.obj.toString()));
                    parser.next();
                    int eventType = parser.getEventType();
                    parser.require(XmlPullParser.START_TAG, null, "ArrayOfString");
                    List<String> tags = new ArrayList<>();
                    while (eventType != XmlPullParser.END_DOCUMENT) {
//                        Log.e("eventType", eventType+"");
                        switch (eventType) {
                            case XmlPullParser.START_DOCUMENT:
                                break;
                            case XmlPullParser.START_TAG:
                                if ("string".equals(parser.getName())) {
                                    String str = parser.nextText();
                                    Log.e("<string>", str+"");
                                    tags.add(str);
                                }
                                break;
                            case XmlPullParser.END_TAG:
                                break;
                            default:
                                break;
                        }
                        eventType = parser.next();
                    }

                    if (tags.size() == 1) {
                        if (tags.get(0).contains("查询结果为空")) {
                            Toast.makeText(MainActivity.this, "当前城市不存在，请重新输入", Toast.LENGTH_SHORT).show();
                        }
                        else if (tags.get(0).contains("高速访问")) {
                            Toast.makeText(MainActivity.this, "您点击的速度过快，二次查询间隔<600ms", Toast.LENGTH_SHORT).show();
                        }
                        else if (tags.get(0).contains("小时内访问")) {
                            Toast.makeText(MainActivity.this, "免费用户24小时内访问超过规定数量50次", Toast.LENGTH_SHORT).show();
                        }
                    }

                    String str0 = tags.get(3);
                    String str1 = tags.get(4);
                    String str2 = tags.get(5);
                    String str3 = tags.get(6);
                    String str4 = tags.get(8);

                    String day0 = tags.get(7);
                    String day00 = tags.get(8);
                    String day1 = tags.get(12);
                    String day11 = tags.get(13);
                    String day2 = tags.get(17);
                    String day22 = tags.get(18);
                    String day3 = tags.get(22);
                    String day33 = tags.get(23);
                    String day4 = tags.get(27);
                    String day44 = tags.get(28);
                    String day5 = tags.get(32);
                    String day55 = tags.get(33);
                    String day6 = tags.get(37);
                    String day66 = tags.get(38);

                    ArrayList<Weather> weathers = new ArrayList<>();
                    weathers.add(new Weather(day0.substring(0, day0.indexOf("日")+2), day0.substring(day0.indexOf("日")+2, day0.length()), day00));
                    weathers.add(new Weather(day1.substring(0, day1.indexOf("日")+2), day1.substring(day1.indexOf("日")+2, day1.length()), day11));
                    weathers.add(new Weather(day2.substring(0, day2.indexOf("日")+2), day2.substring(day2.indexOf("日")+2, day2.length()), day22));
                    weathers.add(new Weather(day3.substring(0, day3.indexOf("日")+2), day3.substring(day3.indexOf("日")+2, day3.length()), day33));
                    weathers.add(new Weather(day4.substring(0, day4.indexOf("日")+2), day4.substring(day4.indexOf("日")+2, day4.length()), day44));
                    weathers.add(new Weather(day5.substring(0, day5.indexOf("日")+2), day5.substring(day5.indexOf("日")+2, day5.length()), day55));
                    weathers.add(new Weather(day6.substring(0, day6.indexOf("日")+2), day6.substring(day6.indexOf("日")+2, day6.length()), day66));

                    recyclerView.setAdapter(new WeatherAdapter(MainActivity.this, weathers));

                    tag_city.setText(tags.get(1));
                    tag_time.setText(str0.substring(11, 19)+" 更新");
                    tag_temperature.setText(str1.substring(str1.indexOf("气温：")+3, str1.indexOf("气温：")+6));
                    tag_temperature_range.setText(str4);
                    tag_wet.setText(str1.substring(str1.indexOf("湿度："), str1.indexOf("湿度：")+6));
                    tag_air.setText(str2.substring(str2.indexOf("空气质量："), str2.length()-1));
                    tag_wing.setText(str1.substring(str1.indexOf("风向/风力：")+6, str1.indexOf("风向/风力：")+10));

                    List<Map<String, Object>> list = new ArrayList<>();
                    Map<String, Object> map;
                    map = new HashMap<>();
                    map.put("text1", "紫外线指数");
                    map.put("text2", str3.substring(str3.indexOf("紫外线指数")+6, str3.indexOf("感冒指数")-3));
                    list.add(map);
                    map = new HashMap<>();
                    map.put("text1", "感冒指数");
                    map.put("text2", str3.substring(str3.indexOf("感冒指数")+5, str3.indexOf("穿衣指数")-3));
                    list.add(map);
                    map = new HashMap<>();
                    map.put("text1", "穿衣指数");
                    map.put("text2", str3.substring(str3.indexOf("穿衣指数")+5, str3.indexOf("洗车指数")-3));
                    list.add(map);
                    map = new HashMap<>();
                    map.put("text1", "洗车指数");
                    map.put("text2", str3.substring(str3.indexOf("洗车指数")+5, str3.indexOf("运动指数")-3));
                    list.add(map);
                    map = new HashMap<>();
                    map.put("text1", "运动指数");
                    map.put("text2", str3.substring(str3.indexOf("运动指数")+5, str3.length()-1));
                    list.add(map);

                    SimpleAdapter simpleAdapter = new SimpleAdapter(MainActivity.this, list, R.layout.list_item, new String[] {"text1", "text2"}, new int[] {R.id.text1, R.id.text2});
                    listView.setAdapter(simpleAdapter);

                    toggle_layout.setVisibility(View.VISIBLE);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    };
}
