package com.domain.moneymanager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import android.widget.RelativeLayout;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;

import android.graphics.Color;
import android.util.Log;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private static String TAG = "MainActivity";

    private float[] yData = {30.3f,25.3f};
    private String[] xData = {"Michael", "Dallas"};
    PieChart pieChart;

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
        loadLedger();

        Log.d(TAG, "on create: starting to create chart");
        pieChart = (PieChart) findViewById(R.id.idPieChart);
        pieChart.setRotationEnabled(true);
        pieChart.setHoleRadius(25f);
        pieChart.setTransparentCircleAlpha(0);
        pieChart.setCenterText("Amount of Money Spent in $");
        pieChart.setCenterTextSize(10);
        pieChart.setDrawEntryLabels(true);

        addDataSet();

        pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                Log.d(TAG, "onValueSelected: Value select from chart");
                Log.d(TAG, "onValueSelected: " +e.toString());
                Log.d(TAG, "onValueSelected: " +h.toString());
                int pos1 = e.toString().indexOf("y: ");
                Log.d(TAG, "onValueSelected: " +pos1);
                String sales = e.toString().substring(pos1+2);

                for(int i = 0; i<yData.length; i++){
                    if(yData[i] == Float.parseFloat(sales)){
                        pos1 = i;
                        break;
                    }
                }
                String employee = xData[pos1+1];
                Toast.makeText(MainActivity.this, "Employee" + employee + "\n"+ "Sales: $" + sales + "K", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected() {

            }
        });
    }

    public void updateLedger(View view) {
        Intent i = new Intent(MainActivity.this, InputActivity.class);
        startActivity(i);
    }

    public void loadLedger() {
        try {
            InputStream in = openFileInput("Ledger.txt");

            if (in != null) {

                InputStreamReader tmp=new InputStreamReader(in);
                BufferedReader reader=new BufferedReader(tmp);
                String str;
                StringBuilder buf=new StringBuilder();

                while ((str = reader.readLine()) != null) {
                    buf.append(str+"\n");
                }

                in.close();

                addData(buf);

            } else {
                Toast.makeText(this, "File is null?", Toast.LENGTH_LONG).show();
            }
        }
        catch (java.io.FileNotFoundException e) {
            Toast.makeText(this, "File not found.", Toast.LENGTH_LONG).show();
        }
        catch (Throwable t) {
            Toast.makeText(this, "Exception: "+t.toString(), Toast.LENGTH_LONG).show();
        }
    }

    public void addData(StringBuilder str) {
        TableLayout ll = (TableLayout) findViewById(R.id.ledgerTable);
        String lines[] = str.toString().split("\\n");
        Double total = 0.0;

        for (int i = 0; i <lines.length; i++)
        {
            TableRow row= new TableRow(this);
            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
            row.setLayoutParams(lp);

            String columns[] = lines[i].split("\t");

//            for (int z=0; z<columns.length; z++) {
//                System.out.println("z[" + z + "]= " + columns[z]);
//            }
            total += Double.parseDouble(columns[1]);
            for (int j = 0; j<columns.length; j++) {
                TextView txtView = new TextView(this);
                txtView.setPadding(20, 20, 20, 20);
                txtView.setText(columns[j]);

                row.addView(txtView);
            }

            ll.addView(row,i);
        }
    }

    public void clearLedger(View view) {
        try {
            File file = new File("/data/user/0/com.domain.moneymanager/files/Ledger.txt");
            file.delete();
            StringBuilder sb = new StringBuilder();
            addData(sb);
        } catch (Exception ex) {
            System.out.println("Exception: " + ex.toString());
        }
    }

    //====================================================

    private void addDataSet() {
        Log.d(TAG, "addDataSet started");
        ArrayList<PieEntry> yEntries = new ArrayList<>();
        ArrayList<String>xEntries = new ArrayList<>();

        for(int i=0;i<yData.length; i++){
            yEntries.add(new PieEntry(yData[i] , i));
        }
        for(int i=0;i<xData.length; i++){
            xEntries.add(xData[i]);
        }
        PieDataSet pieDataSet = new PieDataSet(yEntries, "Amount Spent");
        pieDataSet.setSliceSpace(2);
        pieDataSet.setValueTextSize(12);

        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.RED);
        colors.add(Color.BLUE);

        pieDataSet.setColors(colors);

        Legend legend = pieChart.getLegend();
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setPosition(Legend.LegendPosition.LEFT_OF_CHART);

        PieData pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);
        pieChart.invalidate();

    }
}
