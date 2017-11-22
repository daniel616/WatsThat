package com.example.danielli.watsthat;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import clarifai2.dto.model.output.ClarifaiOutput;
import clarifai2.dto.prediction.Concept;

public class ItemInfo extends AppCompatActivity {
    private String[] itemName;
    private TextView wikiDescriptor;
    private TextView pageTitle;
    private String wikiPrefix="https://en.wikipedia.org/wiki/";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_info);
        wikiDescriptor=(TextView) findViewById(R.id.wiki_info);
        pageTitle=(TextView) findViewById(R.id.item_name_display);

        Intent originIntent=getIntent();
        if(originIntent.hasExtra("tags")){
            new fetchWikiInfo().execute(originIntent.getStringArrayExtra("tags"));
        }


    }

    private class fetchWikiInfo extends AsyncTask<String, Void, String>{
        //TODO: figure out how to close connection
        @Override
        protected String doInBackground(String... strings) {
            if(strings.length==0){
                return null;
            }

            String displayText="";

            for(String tag:strings){
                try {
                    String wikiURL=wikiPrefix+tag;
                    Document doc= Jsoup.connect(wikiURL).get();
                    Elements paragraphs = doc.select("p");
                    if(paragraphs.size()>1){
                        displayText+=paragraphs.get(1).text()+"\n\n\n";
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return displayText;
        }

        @Override
        protected void onPostExecute(String s) {
            wikiDescriptor.setText(s);
        }
    }
}