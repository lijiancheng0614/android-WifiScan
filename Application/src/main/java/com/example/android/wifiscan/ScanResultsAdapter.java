package com.example.android.wifiscan;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ScanResultsAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<ScanResult> mResultList;

    public ScanResultsAdapter(Context context) {
        super();
        this.context = context;
        mResultList = new ArrayList<>();
    }

    public void clearList() {
        mResultList.clear();
    }

    public void updateList(ArrayList<ScanResult> list) {
        mResultList = list;
    }

    public ScanResult getResult(int position) {
        if (position < mResultList.size()) {
            return mResultList.get(position);
        }
        return null;
    }

    @Override
    public int getCount() {
        return mResultList.size();
    }

    @Override
    public Object getItem(int position) {
        return mResultList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        // General ListView optimization code.
        if (convertView == null) {
            LayoutInflater mInflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = mInflator.inflate(R.layout.main_list_row, null);
            viewHolder = new ViewHolder();
            viewHolder.apName = (TextView) convertView.findViewById(R.id.main_row_name);
            viewHolder.apAddress = (TextView) convertView.findViewById(R.id.main_row_address);
            viewHolder.apRssi = (TextView) convertView.findViewById(R.id.main_row_rssi);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        ScanResult result = mResultList.get(position);
        viewHolder.apName.setText(result.SSID);
        viewHolder.apAddress.setText(result.BSSID);
        viewHolder.apRssi.setText(String.format("RSSI: %d", result.level));

        return convertView;
    }

    static class ViewHolder {
        TextView apName;
        TextView apAddress;
        TextView apRssi;
    }
}
