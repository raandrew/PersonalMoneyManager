package com.domain.moneymanager;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import android.widget.EditText;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.FileNotFoundException;
import java.io.IOException;

import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlPullParserException;

public class WelcomeActivity extends AppCompatActivity {

    String username = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Boolean saved = isSaved();
        if (saved == true) {
            Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
            startActivity(intent);
            //Toast.makeText(this, "Save content found! Content:" + username, Toast.LENGTH_LONG).show();
        } else {
            setContentView(R.layout.welcome_activity);
            //Toast.makeText(this, "Response: " + isSaved(), Toast.LENGTH_LONG).show();
        }
    }

    public void submitUsername(View view) {
        saveUser();
        Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
        startActivity(intent);
    }

    public boolean isSaved() {
        try {
            XmlPullParserFactory Xml = XmlPullParserFactory.newInstance();
            XmlPullParser parser = Xml.newPullParser();
            FileInputStream fIn = openFileInput("Profile.ini");
            InputStreamReader isr = new InputStreamReader(fIn);

            // auto-detect the encoding from the stream
            parser.setInput(isr);
            int eventType = parser.getEventType();
            boolean done = false;
            while (eventType != XmlPullParser.END_DOCUMENT && !done){
                String name = null;
                switch (eventType){
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        name = parser.getName();
                        if (name.equalsIgnoreCase("name")){
                            username = name;
                            return true;
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        name = parser.getName();
                        /*
                        if (name.equalsIgnoreCase("category") &&
                                currentCategory != null){
                            categories.add(0, currentCategory);
                        } else if (name.equalsIgnoreCase("packingList")){
                            done = true;
                        }
                        */
                        break;
                }
                eventType = parser.next();
            }
        } catch (FileNotFoundException e) {
            // TODO
        } catch (IOException e) {
            // TODO
        } catch (Exception e){
            // TODO
        }
        return false;
    }

    public void saveUser() {
        try {
            FileOutputStream fOut = openFileOutput("Profile.ini", MODE_WORLD_READABLE);
            OutputStreamWriter osw = new OutputStreamWriter(fOut);

            EditText txt = (EditText) findViewById(R.id.editText);
            String name = "<name>" + txt.getText().toString() + "</name>";
            Toast.makeText(this, "Name: " + name, Toast.LENGTH_LONG).show();
            osw.write(name);
            osw.flush();
            osw.close();

        } catch (FileNotFoundException e) {
            Toast.makeText(this, "Error: FileNotFoundException", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Toast.makeText(this, "Error: IOException", Toast.LENGTH_LONG).show();
        }
    }
}
