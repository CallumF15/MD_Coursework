package com.example.callum.md_coursework_v1;

import android.os.AsyncTask;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Callum on 27/11/2015.
 */
public class ParserRSS extends AsyncTask<String, Void, Long> {
    
    //Variables
    private String title = "title";
    private String link = "link";
    private String description = "description";
    private String urlSite;

    //Getters & Setters
    public String getUrlSite() {
        return urlSite;
    }
    public void setUrlSite(String urlSite) {
        this.urlSite = urlSite;
    }

    //Constructor
    public ParserRSS(String URL){
        this.urlSite = URL;
    }

    //methods

    protected void onPreExecute() {
        //display progress dialog.
    }

    protected Long doInBackground(String... urls) {
        fetchXML();

        return null;
    }

    protected void onPostExecute(Void result) {
        // dismiss progress dialog and update ui
    }

    public void parseXML(XmlPullParser myParser)  {

        String text = null;
        boolean isTrue = false;

        try {

            int eventType = myParser.getEventType();

            //parses xml until "item" tag is found
            while (eventType != XmlPullParser.END_DOCUMENT && !isTrue) {

                String name = myParser.getName();

                switch(eventType) {
                    case XmlPullParser.START_TAG:
                        if (name.equals("item")) {
                            isTrue = true;
                        }
                        break;
                }
                eventType = myParser.next();
            }

            //when "item" tag is found, the relevant tag information will be parsed
            while (eventType != XmlPullParser.END_DOCUMENT && isTrue) {

                String name = myParser.getName();

                switch (eventType) {

                    case XmlPullParser.START_TAG:

                            if (name.equalsIgnoreCase("title")) {
                                title = myParser.nextText();
                                System.out.println("title: " + title + "\n");
                            } else if (name.equalsIgnoreCase("link")) {
                                link = myParser.nextText();
                                System.out.println("link: " + link  + "\n");
                            } else if (name.equalsIgnoreCase("description")) {
                                description = myParser.nextText();
                                System.out.println("description: " + description + "\n");
                            }
                        break;

                    case XmlPullParser.END_TAG:
                        break;

                    case XmlPullParser.TEXT:
                        text = myParser.getText();
                        break;
                }
                eventType = myParser.next();
            }

        }catch(XmlPullParserException parseError){
            Log.e("MyTag", "Parsing Error" + parseError.toString());
        }catch(IOException ioError){
            Log.e("MyTag", "IO error during passing" + ioError.toString());
        }
    }

    public void fetchXML() {

        try {
            URL url = new URL(urlSite);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();


            conn.setReadTimeout(5000 /* milliseconds */); /* 10 seconds */
            conn.setConnectTimeout(6000 /* milliseconds */); /* 15 seconds */
            conn.setRequestMethod("GET");
            conn.setDoInput(true);

            String responseMsg = conn.getResponseMessage();
            int responseCode = conn.getResponseCode();

            conn.connect();
            InputStream stream = conn.getInputStream();


            XmlPullParserFactory xmlFactoryObject = XmlPullParserFactory.newInstance();
            xmlFactoryObject.setNamespaceAware(true);

            XmlPullParser parser = xmlFactoryObject.newPullParser();

            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(stream, null);

            parseXML(parser);
            stream.close();

        } catch (Exception e) {
            Log.e("myTag", e.toString());
        }
    }
}
