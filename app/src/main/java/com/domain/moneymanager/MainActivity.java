package com.domain.moneymanager;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        final TabHost tabHost = (TabHost) findViewById(R.id.tabhost);
        tabHost.setup();

        TabSpec spec1 = tabHost.newTabSpec("Graph");
        spec1.setContent(R.id.tab1);
        spec1.setIndicator("Graph", null);
        tabHost.addTab(spec1);

        TabSpec spec2 = tabHost.newTabSpec("Ledger");
        spec2.setContent(R.id.tab2);
        spec2.setIndicator("Ledger", null);
        tabHost.addTab(spec2);

        TabSpec spec3 = tabHost.newTabSpec("Tips");
        spec3.setContent(R.id.tab3);
        spec3.setIndicator("Tips", null);
        tabHost.addTab(spec3);
    }
}
