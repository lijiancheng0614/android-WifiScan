package com.example.android.wifiscan;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends Activity {
    private WifiManager mWifiManager;
    private WifiReceiver mWifiReceiver;

    private ListView mList;
    private TextView mTextCount;

    private ScanResultsAdapter mScanResultsAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Use this check to determine whether Wi-Fi is supported on the device.
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_WIFI)) {
            Toast.makeText(this, R.string.wifi_not_supported, Toast.LENGTH_SHORT).show();
            finish();
        }

        mWifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        mWifiReceiver = new WifiReceiver();

        mList = (ListView) findViewById(R.id.main_list);
        mTextCount = (TextView) findViewById(R.id.main_text_count);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_scan:
                scanAP();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mWifiReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        scanAP();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mWifiReceiver);
    }

    private void scanAP() {
        // Ensures Wi-Fi is enabled on the device.
        if (!mWifiManager.isWifiEnabled()) {
            Toast.makeText(this, R.string.wifi_not_enabled, Toast.LENGTH_LONG).show();
            mWifiManager.setWifiEnabled(true);
            return;
        }

        if (mScanResultsAdapter == null) {
            mScanResultsAdapter = new ScanResultsAdapter(this);
            mList.setAdapter(mScanResultsAdapter);
            mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    final ScanResult result = mScanResultsAdapter.getResult(position);
                    if (result == null) return;
                    final Intent intent = new Intent(parent.getContext(), AccessPointActivity.class);
                    intent.putExtra(AccessPointActivity.EXTRAS_SSID, result.SSID);
                    intent.putExtra(AccessPointActivity.EXTRAS_BSSID, result.BSSID);
                    intent.putExtra(AccessPointActivity.EXTRAS_CAPABILITIES, result.capabilities);
                    intent.putExtra(AccessPointActivity.EXTRAS_FREQUENCY, Integer.toString(result.frequency));
                    intent.putExtra(AccessPointActivity.EXTRAS_LEVEL, Integer.toString(result.level));
                    String timestamp = "";
                    try {
                        if (Build.VERSION.SDK_INT >= 17) {
                            Date date = new Date(System.currentTimeMillis() - SystemClock.elapsedRealtime() + (result.timestamp / 1000));
                            timestamp = date.toString();
                        }
                    } catch (Exception e) {

                    }
                    intent.putExtra(AccessPointActivity.EXTRAS_TIMESTAMP, timestamp);
                    startActivity(intent);
                }
            });
        }

        if (mWifiManager.startScan()) {
            mScanResultsAdapter.updateList((ArrayList<ScanResult>) mWifiManager.getScanResults());
        } else {
            mScanResultsAdapter.clearList();
            switch (mWifiManager.getWifiState()) {
                case WifiManager.WIFI_STATE_DISABLING:
                    Toast.makeText(this, R.string.wifi_state_disabling, Toast.LENGTH_LONG).show();
                    break;
                case WifiManager.WIFI_STATE_DISABLED:
                    Toast.makeText(this, R.string.wifi_state_disabled, Toast.LENGTH_LONG).show();
                    break;
                case WifiManager.WIFI_STATE_ENABLING:
                    Toast.makeText(this, R.string.wifi_state_enabling, Toast.LENGTH_LONG).show();
                    break;
                case WifiManager.WIFI_STATE_ENABLED:
                    Toast.makeText(this, R.string.wifi_state_enabled, Toast.LENGTH_LONG).show();
                    break;
                case WifiManager.WIFI_STATE_UNKNOWN:
                    Toast.makeText(this, R.string.wifi_state_unknown, Toast.LENGTH_LONG).show();
                    break;
            }
        }

        mScanResultsAdapter.notifyDataSetChanged();
        mTextCount.setText(String.format(getString(R.string.label_networks_found), mScanResultsAdapter.getCount()));
    }

    class WifiReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context c, Intent intent) {
            if (mScanResultsAdapter != null) {
                mScanResultsAdapter.updateList((ArrayList<ScanResult>) mWifiManager.getScanResults());
                mScanResultsAdapter.notifyDataSetChanged();
                mTextCount.setText(String.format(getString(R.string.label_networks_found), mScanResultsAdapter.getCount()));
            }
        }
    }
}