package com.example.maouusama.midtermexam.Query;

import android.text.TextUtils;
import android.util.Log;

import com.example.maouusama.midtermexam.Model.Album;
import com.example.maouusama.midtermexam.Model.Image;

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

/**
 * Created by Maouusama on 2/13/2017.
 */

public final class Query {
    public static final String LOG_TAG = Query.class.getSimpleName();

    private Query() {
    }

    public static List<Album> fetchAlbumData(String requestUrl) {
        URL url = createUrl(requestUrl);
        String jsonResponse = null;
        try {
            jsonResponse = makehttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error in making http request", e);
        }
        List<Album> result = extractAlbums(jsonResponse);
        return result;
    }

    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error in Creating URL", e);
        }
        return url;
    }

    private static String makehttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        for (int retries = 0; retries < 3; retries++) {
            try {
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setReadTimeout(10000 /* milliseconds */);
                urlConnection.setConnectTimeout(15000 /* milliseconds */);
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                retries = 3;


                if (urlConnection.getResponseCode() == 200) {
                    inputStream = urlConnection.getInputStream();
                    jsonResponse = readFromStream(inputStream);
                } else {
                    Log.e(LOG_TAG, "Error in connection! Bad Response ");
                }

            } catch (IOException e) {
                Log.e(LOG_TAG, "Problem retrieving album JSON results.", e);
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
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

    private static List<Album> extractAlbums(String albumJSON) {
        if (TextUtils.isEmpty(albumJSON)) {
            return null;
        }
        ArrayList<Album> albumsList = new ArrayList<>();

        try {
            JSONObject baseJsonResponse = new JSONObject(albumJSON);
            JSONObject results = baseJsonResponse.getJSONObject("results");
            JSONObject albumMatches = results.getJSONObject("albummatches");
            JSONArray albumArray = albumMatches.getJSONArray("album");

            for (int i = 0; i < albumArray.length(); i++) {
                JSONObject thisAlbum = albumArray.getJSONObject(i);

                String url = thisAlbum.getString("url");
                String name = thisAlbum.getString("name");
                String artist = thisAlbum.getString("artist");

                JSONArray imageList = thisAlbum.getJSONArray("image");

                ArrayList<Image> imageArrayList = new ArrayList<>();

                for (int x = 0; x < imageList.length(); x++) {
                    JSONObject thisImage = imageList.getJSONObject(x);
                    String size = thisImage.getString("size");
                    String imgUrl = thisImage.getString("#text");
                    Image image = new Image(imgUrl,size);
                    imageArrayList.add(image);
                }

                String streamableString = thisAlbum.getString("streamable");
                int streamable = Integer.parseInt(streamableString);
                String mbid = thisAlbum.getString("mbid");


                Album article = new Album(name, artist, url, imageArrayList, streamable, mbid);
                albumsList.add(article);
            }

        } catch (JSONException e) {
            Log.e(LOG_TAG, "Error in fetching data", e);
        }
        return albumsList;
    }
}
