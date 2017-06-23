package com.bassamworks.weathcheck.utilities;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

/**
 * Created by Bassam on 6/21/2017.
 */

public class JSONResponseHTTPRequest {

    private final String LOG_TAG = getClass().getSimpleName();

    public String getJSONStringResponse(String URLString) {

        URL url = makeURLFromString(URLString);

        String JSONStringResponse = null;
        try {
            JSONStringResponse = fetchJSONResponseFromURL(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error getting the JSON response from the URL", e);
        }

        if (JSONStringResponse.equals("")) {
            Log.e(LOG_TAG, "Empty JSON string response!");
        }

        return JSONStringResponse;

    }

    private String fetchJSONResponseFromURL(URL url) throws IOException {

        String JSONResponse = "";

        if (url == null) {
            Log.e(LOG_TAG, "NULL URL!");
            return JSONResponse;
        }

        HttpURLConnection httpURLConnection = null;
        InputStream inputStream = null;

        try {
            httpURLConnection = (HttpURLConnection) url.openConnection();

            httpURLConnection.setConnectTimeout(10000);
            httpURLConnection.setReadTimeout(10000);
            httpURLConnection.setRequestMethod("GET");

            httpURLConnection.connect();

            if (httpURLConnection.getResponseCode() == 200) {
                inputStream = httpURLConnection.getInputStream();

                JSONResponse = getStringFromInputStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error with thr URL response code!");
            }

        } catch (IOException e) {
            Log.e(LOG_TAG, "Error retreaving the connection", e);
        } finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }


        return JSONResponse;
    }

    private String getStringFromInputStream(InputStream inputStream) throws IOException {

        StringBuilder stringBuilder = new StringBuilder();

        if (inputStream != null) {

            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));

            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String readLine = bufferedReader.readLine();

            while (readLine != null) {
                stringBuilder.append(readLine);
                readLine = bufferedReader.readLine();
            }
        }

        return stringBuilder.toString();
    }

    private URL makeURLFromString(String URLString) {

        URL url = null;

        try {
            url = new URL(URLString);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Malformed URL!", e);
        }

        return url;
    }
}
