package com.zukron.jadwalsholat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zukron.jadwalsholat.R;
import com.zukron.jadwalsholat.model.Shalat;

import org.threeten.bp.LocalTime;

import java.util.ArrayList;

/**
 * Project name is Jadwal Sholat
 * Created by Zukron Alviandy R on 7/22/2020
 */
public class ShalatAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<String> names;
    private ArrayList<LocalTime> times;

    public ShalatAdapter(Context context, Shalat shalat) {
        this.context = context;
        this.names = shalat.getName();
        this.times = shalat.getTimes();
    }

    @Override
    public int getCount() {
        return names.size();
    }

    @Override
    public Object getItem(int i) {
        return names.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;

        if (view == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            view = layoutInflater.inflate(R.layout.item_shalat, viewGroup, false);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.tvNameItemShalat.setText(names.get(i));
        viewHolder.tvTimeItemShalat.setText(times.get(i).toString());

        return view;
    }

    private static class ViewHolder{
        private TextView tvNameItemShalat, tvTimeItemShalat;

        private ViewHolder(View itemView) {
            this.tvNameItemShalat = itemView.findViewById(R.id.tv_name_item_shalat);
            this.tvTimeItemShalat = itemView.findViewById(R.id.tv_time_item_shalat);
        }
    }
}
