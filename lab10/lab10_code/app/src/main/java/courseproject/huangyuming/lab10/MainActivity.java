package courseproject.huangyuming.lab10;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.CoordinateConverter;

import static android.location.LocationManager.NETWORK_PROVIDER;

public class MainActivity extends AppCompatActivity {

    private ToggleButton toggleButton;

    private MapView mMapView;
    private LocationManager mLocationManager;
    private SensorManager mSensorManager;
    private Sensor mMagneticSensor;
    private Sensor mAccelerometerSensor;

    private String mProvider;

    private LatLng mDesLatLng;

    private boolean scrollTo = true;

    private int shakeLock = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_main);

        toggleButton = (ToggleButton)findViewById(R.id.toggleButton);

        mMapView = (MapView) findViewById(R.id.bmapView);
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mMagneticSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        mAccelerometerSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        if (mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            mProvider = LocationManager.NETWORK_PROVIDER;
        }
        else if (mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            mProvider = LocationManager.GPS_PROVIDER;
        }

//        Criteria criteria = new Criteria();
//        criteria.setAccuracy(Criteria.ACCURACY_FINE);
//        criteria.setAltitudeRequired(false);
//        criteria.setBearingRequired(false);
//        criteria.setCostAllowed(true);
//        mProvider = mLocationManager.getBestProvider(criteria, true);

//        Toast.makeText(MainActivity.this, mLocationManager.getLastKnownLocation(provider).getLongitude()+"", Toast.LENGTH_SHORT).show();

        mLocationManager.requestLocationUpdates(mProvider, 0, 0, mLocationListener);


        Bitmap bitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.pointer), 100, 100, true);
        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(bitmap);
        mMapView.getMap().setMyLocationEnabled(true);
        MyLocationConfiguration configuration = new MyLocationConfiguration(MyLocationConfiguration.LocationMode.NORMAL, true, bitmapDescriptor);
        mMapView.getMap().setMyLocationConfigeration(configuration);

        LatLng desLatLng = convertDesLatLng(mLocationManager.getLastKnownLocation(mProvider));
        movePointerToDes(desLatLng);
        moveScreenToDes(desLatLng);
        mDesLatLng = desLatLng;

        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                scrollTo = b;

                if (b) {
                    LatLng desLatLng = convertDesLatLng(mLocationManager.getLastKnownLocation(mProvider));
                    movePointerToDes(desLatLng);
                    moveScreenToDes(desLatLng);
                }

            }
        });

        mMapView.getMap().setOnMapTouchListener(new BaiduMap.OnMapTouchListener() {
            @Override
            public void onTouch(MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_MOVE:
                        if (scrollTo) {
                            toggleButton.setChecked(false);
                            scrollTo = false;
                        }
                        break;
                    default:
                        break;
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        mMapView.onDestroy();
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();

        mSensorManager.registerListener(mSensorEventListener, mMagneticSensor, SensorManager.SENSOR_DELAY_GAME);
        mSensorManager.registerListener(mSensorEventListener, mAccelerometerSensor, SensorManager.SENSOR_DELAY_GAME);

    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();

        mSensorManager.unregisterListener(mSensorEventListener);
    }

    private SensorEventListener mSensorEventListener = new SensorEventListener() {

        float [] accValues;
        float [] magValues;
        long lastShakeTime = 0;

        float newRotationDegree = 0;

        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            switch (sensorEvent.sensor.getType()) {
                case Sensor.TYPE_ACCELEROMETER:
                    accValues = sensorEvent.values;

                    if (accValues[0] > 15) {
                        if (shakeLock == 0) {
                            shakeLock = 100;
                            Toast.makeText(MainActivity.this, "marked!", Toast.LENGTH_SHORT).show();

                            // 定义Maker坐标点和图片
                            LatLng point = convertDesLatLng(mLocationManager.getLastKnownLocation(mProvider));
                            BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(android.R.drawable.star_on);

                            // 构建MarkerOption，用于在地图上添加Marker
                            MarkerOptions options = new MarkerOptions().position(point).icon(bitmap);

                            // 在地图上添加Marker，并显示
                            mMapView.getMap().addOverlay(options);
                        }
                    }

                    break;
                case Sensor.TYPE_MAGNETIC_FIELD:
                    magValues = sensorEvent.values;
                    break;
            }


            if (shakeLock > 0) {
                shakeLock--;
            }

            if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER || sensorEvent.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
                newRotationDegree = 0;
                if (accValues != null && magValues != null) {
                    float[] R = new float[9];
                    float[] values = new float[3];

                    SensorManager.getRotationMatrix(R, null, accValues, magValues);
                    SensorManager.getOrientation(R, values);

                    newRotationDegree = (float) Math.toDegrees(values[0]);

                    MyLocationData.Builder builder = new MyLocationData.Builder();
                    builder.direction(newRotationDegree);
                    builder.latitude(mDesLatLng.latitude);
                    builder.longitude(mDesLatLng.longitude);
                    mMapView.getMap().setMyLocationData(builder.build());
//                Log.e("newRotationDegree", newRotationDegree+"");


                }

            }

        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    };

    private LocationListener mLocationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            // processing new location
//            Log.e("---+++---", location.getLongitude()+""+location.getAltitude());
//            Toast.makeText(MainActivity.this, location.getLongitude()+""+location.getAltitude()+"", Toast.LENGTH_SHORT).show();

            LatLng desLatLng = convertDesLatLng(mLocationManager.getLastKnownLocation(mProvider));
            movePointerToDes(desLatLng);
            mDesLatLng = desLatLng;

            if (scrollTo) {
                moveScreenToDes(desLatLng);
            }
        }
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };

    private LatLng convertDesLatLng(Location location) {
        CoordinateConverter converter = new CoordinateConverter();
        converter.from(CoordinateConverter.CoordType.GPS);
        converter.coord(new LatLng(location.getLatitude(), location.getLongitude()));
        return converter.convert();
    }

    private void movePointerToDes(LatLng desLatLng) {
        MyLocationData.Builder builder = new MyLocationData.Builder();
        builder.latitude(desLatLng.latitude);
        builder.longitude(desLatLng.longitude);

        mMapView.getMap().setMyLocationData(builder.build());
    }

    private void moveScreenToDes(LatLng desLatLng) {
        MapStatus mapStatus = new MapStatus.Builder().target(desLatLng).build();
        MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mapStatus);
        mMapView.getMap().setMapStatus(mapStatusUpdate);
    }

}
