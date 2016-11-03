package com.domain.moneymanager;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import android.widget.EditText;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
//import org.xml.sax.*;
//import org.w3c.dom.*;

import java.io.IOException;
import java.io.FileOutputStream;

/*
    XML example:

    <?xml version="1.0" encoding="UTF-8" standalone="no"?>
    <!DOCTYPE ..
    <profile>
        <name>Name-here</name>
    </profile>
 */
public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (isSaved() == true) {
            Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
            startActivity(intent);
            Toast.makeText(this, "Save content found!", Toast.LENGTH_LONG).show();
        } else {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.welcome_activity);
            Toast.makeText(this, "No saved content found!", Toast.LENGTH_LONG).show();
        }
    }

    public void submitUsername(View view) {
        saveUser();
        Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
        startActivity(intent);
    }

    public boolean isSaved() {
        String xml = "Profile.ini";
        Document dom;
        String name = "";
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            dom = db.parse(xml);

            Element doc = dom.getDocumentElement();

            name = getTextValue(name, doc, "name");
            return true;
        } catch (ParserConfigurationException pce) {
            return false;
        } catch (SAXException se) {
            return false;
        } catch (IOException ioe) {
            return false;
        }
    }

    public String getTextValue(String def, Element doc, String tag) {
        String value = def;
        NodeList nl = doc.getElementsByTagName(tag);
        if (nl.getLength() > 0 && nl.item(0).hasChildNodes()) {
            value = nl.item(0).getFirstChild().getNodeValue();
        }
        return value;
    }

    public void saveUser() {
        String xml = "Profile.ini";
        Document dom;
        Element e = null;
        EditText txt = (EditText) findViewById(R.id.editText);
        String name = txt.getText().toString();

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            dom = db.newDocument();

            Element rootEle = dom.createElement("Profile");

            e = dom.createElement("name");
            e.appendChild(dom.createTextNode(name));
            rootEle.appendChild(e);

            // Can add more to profile here..

            dom.appendChild(rootEle);

            try {
                Transformer tr = TransformerFactory.newInstance().newTransformer();
                tr.setOutputProperty(OutputKeys.INDENT, "yes");
                tr.setOutputProperty(OutputKeys.METHOD, "xml");
                tr.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
                tr.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, "roles.dtd");
                tr.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

                tr.transform(new  DOMSource(dom), new StreamResult(new FileOutputStream(xml)));
            } catch (TransformerException te) {

            } catch (IOException ioe) {

            }
        } catch (ParserConfigurationException pce) {

        }
    }
}
