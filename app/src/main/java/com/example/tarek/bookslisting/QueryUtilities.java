package com.example.tarek.bookslisting;

import android.graphics.drawable.Drawable;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public final class QueryUtilities {

    public static final String LOG_TAG = QueryUtilities.class.getSimpleName();


    /**
     * Create a private constructor because no one should ever create a {@link QueryUtilities} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */

    private QueryUtilities(){

    }

    /**
     * Return a list of {@link BookSrc} objects that has been built up from
     * parsing a JSON response.
     */

    public static List<BookSrc> extract(String JSON_RESPONSE) {

        // Create an empty ArrayList that we can start adding earthquakes to
        List<BookSrc> earthquakes = new ArrayList<>();

        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            // TODO: Parse the response given by the SAMPLE_JSON_RESPONSE string and

            // build up a list of Earthquake objects with the corresponding data.

            JSONObject jasonRoot =new JSONObject(JSON_RESPONSE);
            JSONArray items =jasonRoot.getJSONArray("items");
            for(int i=0;i<items.length();i++)
            {

                JSONObject currentObject = items.getJSONObject(i);

                JSONObject volumeInfo = currentObject.getJSONObject("volumeInfo");

                 String title=volumeInfo.getString("title");
                String author=null;
                try {
                    JSONArray authors = volumeInfo.getJSONArray("authors");
                    author = authors.getString(0);
                }
                catch (Exception e){
                    author=" ";
                }
               //  JSONObject readingModes =volumeInfo.optJSONObject("readingModes");
                 // String animage=readingModes.getString("image");

                String imageUrl = null;
               try {

                   JSONObject imageLinks = volumeInfo.getJSONObject("imageLinks");
                   imageUrl = imageLinks.getString("smallThumbnail");
                }catch (Exception e){
                   imageUrl=null;
               }
                JSONObject saleInfo = currentObject.getJSONObject("saleInfo");

               String sale= saleInfo.getString("saleability");
              JSONObject accessInfo =currentObject.getJSONObject("accessInfo");
                String webReaderLink=null;
                if(sale.equals("FOR_SALE"))
                    webReaderLink=saleInfo.getString("buyLink");
                else
                 webReaderLink = accessInfo.getString("webReaderLink");

             //   String previewLink = volumeInfo.getString("infoLink");

                BookSrc earthQuakeSrc1 =new BookSrc(title,author,imageUrl ,webReaderLink);
                earthquakes.add(earthQuakeSrc1);
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.

            Log.e("QueryUtilities", "Problem parsing the Book JSON results", e);


        }

        // Return the list of earthquakes
        return earthquakes;
    }


    public static List<BookSrc> fetchBookData(String requestUrl) {

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream", e);
        }

        /**Extract relevant fields from the JSON response and create an {@link BookSrc } object*/
        List<BookSrc> bookList = extract(jsonResponse);

        // Return the {@link Event}
        return bookList;
    }
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating URL ", e);
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }



}
