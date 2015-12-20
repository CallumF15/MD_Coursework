package com.example.callum.md_coursework_v1;

import android.os.AsyncTask;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.Attributes;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * Created by Callum on 27/11/2015.
 */
public class ParserRSS extends AsyncTask<String, Void, List<String>>{

    //Variables
    private String title;
    private String link;
    private String description;
    private String publicationDate;
    private String urlSite;

    public int limit;

    public List<String> stringList;

    //Getters & Setters

    //get the limit of number of items that can be parsed
    public int getLimit() {
        return limit;
    }
    //sets limit of number of items to be parsed
    public void setLimit(int limit) {
        this.limit = limit;
    }

    //Constructor
    public ParserRSS(String URL) {
        this.urlSite = URL; //assign value
    }

    //Methods

    protected void onPreExecute() {
        //display progress dialog.
    }

    protected List<String> doInBackground(String... urls) {
        fetchXML(); //call method

        return stringList; //return list
    }

    protected void onPostExecute(Void result) {
        // dismiss progress dialog and update ui
    }

    //parse the xml retrieved
    public void parseXML(XmlPullParser myParser) {

        String text = null;
        boolean isTrue = false;
        int counter = 0;
        //intiialize a new instance
        stringList = new ArrayList<String>();

        try {

            //event EventType to determine state of parser
            int eventType = myParser.getEventType();

            //parses xml until "item" tag is found
            while (eventType != XmlPullParser.END_DOCUMENT && !isTrue) {

                //get name from parser
                String name = myParser.getName();

                switch (eventType) {
                    case XmlPullParser.START_TAG: //find starting XML tag
                        if (name.equals("item")) {  //if tag is equal to string
                            isTrue = true;
                        }
                        break;
                }
                eventType = myParser.next(); //move to next xml tag
            }

            //when "item" tag is found, the relevant tag information will be parsed
            while (eventType != XmlPullParser.END_DOCUMENT && isTrue && counter < limit) {

                //get name from parser
                String name = myParser.getName();

                switch (eventType) {

                    case XmlPullParser.START_TAG: //find satrting xml tag

                        if (name.equalsIgnoreCase("title")) { //if tag is equal to string
                            title = myParser.nextText(); //assign text to variable
                            stringList.add(title); //add variable to list
                            counter += 1; //increment counter
                        } else if (name.equalsIgnoreCase("link")) {
                            link = myParser.nextText();
                            stringList.add(link);
                        } else if (name.equalsIgnoreCase("description")) {
                            description = myParser.nextText();
                            stringList.add(description);
                        } else if(name.equalsIgnoreCase("pubDate")) {
                            publicationDate = myParser.nextText();
                            stringList.add(publicationDate);
                        }else if(name.equalsIgnoreCase("media:content")){
                           urlSite =  myParser.getAttributeValue(null, "url");
                            stringList.add(urlSite);
                        }
                        break;

                    case XmlPullParser.END_TAG: //if tag is end XML tag
                        break;

                    case XmlPullParser.TEXT:
                        //retrieve text content
                        text = myParser.getText();
                        break;
                }
                //move to next xml tag
                eventType = myParser.next();
            }
        } catch (XmlPullParserException parseError) {
            Log.e("MyTag", "Parsing Error" + parseError.toString());
        } catch (IOException ioError) {
            Log.e("MyTag", "IO error during passing" + ioError.toString());
        }
    }

    //retrieve XML from site
    public void fetchXML() {

        try {
            URL url = new URL(urlSite); //pass URL link
            HttpURLConnection conn = (HttpURLConnection) url.openConnection(); //open URL connection

            //Sets the maximum time to wait for an input stream read to complete before giving up.
            conn.setReadTimeout(5000 /* milliseconds */); /* 10 seconds */
            //Sets the maximum time in milliseconds to wait while connecting.
            conn.setConnectTimeout(6000 /* milliseconds */); /* 15 seconds */
            //Returns the request method which will be used to make the request to the remote HTTP server
            conn.setRequestMethod("GET");
            //Specifies whether this URLConnection allows receiving data.
            conn.setDoInput(true);

            //connect to URL
            conn.connect();
            //Get data from stream
            InputStream stream = conn.getInputStream();

            //create new instance of XmlPullParserFactory
            XmlPullParserFactory xmlFactoryObject = XmlPullParserFactory.newInstance();
            //Specifies that the parser produced by this factory will provide support for XML namespaces.
            xmlFactoryObject.setNamespaceAware(true);

            //Creates a new instance of a XML Pull Parser using the currently configured factory features.
            XmlPullParser parser = xmlFactoryObject.newPullParser();
            //set the general behaviour of the parser
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            //pass stream data to parser
            parser.setInput(stream, null);

            //pass parser object to method
            parseXML(parser);
            //close stream
            stream.close();

        } catch (Exception e) {
            Log.e("myTag", e.toString());
        }
    }
}
