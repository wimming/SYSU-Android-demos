package com.ym.lab9;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by ym on 16-11-30.
 */

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.ViewHolder>{
    private ArrayList<Weather> weather_list;
    private LayoutInflater mInflater;
    public interface OnItemClickListener
    {
        void onItemClick(View view, int position, Weather item);
    }
    private OnItemClickListener mOnItemClickListener;
    public void OnItemClickListener(OnItemClickListener mOnItemClickLitener)
    {
        this.mOnItemClickListener = mOnItemClickLitener;
    }
    public WeatherAdapter(Context context, ArrayList<Weather> items) {
        super();
        weather_list = items;
        mInflater = LayoutInflater.from(context);
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.weather_item, viewGroup, false);
        ViewHolder holder = new ViewHolder(view);
        holder.Date = (TextView)view.findViewById(R.id.date);
        holder.Weather_description =(TextView)view.findViewById(R.id.weather_description);
        holder.Temperature = (TextView)view.findViewById(R.id.temperature);
        return holder;
    }
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        viewHolder.Date.setText(weather_list.get(i).getDate());
        viewHolder.Weather_description.setText(weather_list.get(i).getWeather_description());
        viewHolder.Temperature.setText(weather_list.get(i).getTemperature());
        if (mOnItemClickListener != null)
        {
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    mOnItemClickListener.onItemClick(viewHolder.itemView, i,weather_list.get(i));
                }
            });
        }
    }
    @Override
    public int getItemCount() {
        return weather_list.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder{
        public ViewHolder(View itemView) {
            super(itemView);
        }
        TextView Date;
        TextView Weather_description;
        TextView Temperature;
    }

}