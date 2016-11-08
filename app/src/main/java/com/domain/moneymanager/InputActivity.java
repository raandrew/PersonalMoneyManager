package com.domain.moneymanager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class InputActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.input_activity);
    }

    public void submitInput(View view) {
        String str = "";
        EditText pEditText = (EditText) findViewById(R.id.editText2);
        String p = pEditText.getText().toString();
        EditText aEditText = (EditText) findViewById(R.id.editText5);
        String a = aEditText.getText().toString();
        Spinner cEditText = (Spinner) findViewById(R.id.spinner);
        String c = cEditText.getSelectedItem().toString();
        str = p + "\t" + a + "\t" + c + "\n";
        try {
            OutputStreamWriter out = new OutputStreamWriter(openFileOutput("Ledger.txt", MODE_APPEND));
            out.write(str);
            out.close();

        }

        catch (Throwable t) {
            Toast.makeText(this, "Exception: "+t.toString(), Toast.LENGTH_LONG).show();
        }

        loadLedger();

        // Return to MainActivity
        Intent i = new Intent(InputActivity.this, MainActivity.class);
        startActivity(i);
        Toast.makeText(getApplicationContext(),"Data has been submitted.", Toast.LENGTH_LONG).show();
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

                TableLayout tl = (TableLayout)findViewById(R.id.ledgerTable);
                TableRow tr = new TableRow(this);
                tr.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

                TextView test = new TextView(this);
                test.setText(buf);
                test.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

                RelativeLayout l = (RelativeLayout)findViewById(R.id.ledgerLayout);
                l.addView(test);
                //tr.addView(test);
                //tl.addView(tr);
            } else {
                Toast.makeText(this, "File is null?", Toast.LENGTH_LONG).show();
                System.out.println("File is null?");
            }
        }
        catch (java.io.FileNotFoundException e) {
            Toast.makeText(this, "File not found", Toast.LENGTH_LONG).show();
            System.out.println("File not found.");
        }
        catch (Throwable t) {
           // Toast.makeText(this, "Exception: "+t.toString(), Toast.LENGTH_LONG).show();
            System.out.println("Exception: " + t.toString());
        }
    }
}

