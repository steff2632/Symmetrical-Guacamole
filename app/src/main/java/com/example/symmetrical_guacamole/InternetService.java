package com.example.symmetrical_guacamole;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by stefanmay on 03/02/2016.
 */
public class InternetService extends IntentService {

    public InternetService() {
        super("example_internet_service");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(this);
        Intent resultIntent;
        switch (intent.getDataString()) {
            case Actions.GET_STRING:
                String json = getJson(intent.getStringExtra(Extras.URL_EXTRA));

                resultIntent = new Intent(Results.GET_STRING);
                resultIntent.putExtra(Extras.STRING_EXTRA, json);

                manager.sendBroadcast(resultIntent);
                break;
            case Actions.GET_BYTES:
                byte[] bytes = getBytes(intent.getStringExtra(Extras.URL_EXTRA));

                resultIntent = new Intent(Results.GET_BYTES);
                resultIntent.putExtra(Extras.BYTES_EXTRA, bytes);
                resultIntent.putExtra(Extras.ITEM_EXTRA, intent.getSerializableExtra(Extras.ITEM_EXTRA));

                manager.sendBroadcast(resultIntent);
                break;
        }
    }

    private InputStream connect(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

        return new BufferedInputStream(urlConnection.getInputStream());
    }

    private String getJson(String urlString) {

        try{
            InputStream in = connect(urlString);

            return StreamUtils.convertStreamToString(in);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public byte[] getBytes(String urlString) {
        try{
            InputStream in = connect(urlString);

            return StreamUtils.getBytesFromInputStream(in);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public class Actions {
        public static final String GET_STRING = "get_string";
        public static final String GET_BYTES = "get_bytes";
    }

    public class Extras {
        public static final String URL_EXTRA = "url_extra";
        public static final String STRING_EXTRA = "string_extra";
        public static final String BYTES_EXTRA = "bytes_extra";
        public static final String ITEM_EXTRA = "item_extra";
    }

    public class Results {
        public static final String GET_STRING = "get_string";
        public static final String GET_BYTES = "get_bytes";
    }
}
