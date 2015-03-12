package com.example.android.wifiscan;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class AccessPointActivity extends Activity {
    public static final String EXTRAS_SSID = "SSID";
    public static final String EXTRAS_BSSID = "BSSID";
    public static final String EXTRAS_CAPABILITIES = "CAPABILITIES";
    public static final String EXTRAS_FREQUENCY = "FREQUENCY";
    public static final String EXTRAS_LEVEL = "LEVEL";
    public static final String EXTRAS_TIMESTAMP = "TIMESTAMP";

    private boolean mConnected = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_access_point);

        final Intent intent = getIntent();
        String mSSID = intent.getStringExtra(EXTRAS_SSID);
        String mBSSID = intent.getStringExtra(EXTRAS_BSSID);
        String mCapabilities = intent.getStringExtra(EXTRAS_CAPABILITIES);
        String mFrequency = intent.getStringExtra(EXTRAS_FREQUENCY);
        String mLevel = intent.getStringExtra(EXTRAS_LEVEL);
        String mTimestamp = intent.getStringExtra(EXTRAS_TIMESTAMP);

        // Sets up UI references.
        ((TextView) findViewById(R.id.ap_ssid)).setText(mSSID);
        ((TextView) findViewById(R.id.ap_bssid)).setText(mBSSID);
        ((TextView) findViewById(R.id.ap_capabilities)).setText(mCapabilities);
        ((TextView) findViewById(R.id.ap_frequency)).setText(mFrequency + " MHz");
        ((TextView) findViewById(R.id.main_row_rssi)).setText(mLevel + " dBm");
        ((TextView) findViewById(R.id.ap_timestamp)).setText(mTimestamp);

        getActionBar().setTitle(mSSID);
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.ap, menu);
        if (mConnected) {
            menu.findItem(R.id.menu_connect).setVisible(false);
            menu.findItem(R.id.menu_disconnect).setVisible(true);
        } else {
            menu.findItem(R.id.menu_connect).setVisible(true);
            menu.findItem(R.id.menu_disconnect).setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_connect:
                // TODO: connect
                return true;
            case R.id.menu_disconnect:
                // TODO: disconnect
                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}