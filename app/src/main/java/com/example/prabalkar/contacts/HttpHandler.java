package com.example.prabalkar.contacts;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by prabalkar on 25/11/17.
 */

public class HttpHandler {

    public static final String TAG = HttpHandler.class.getSimpleName();

    public HttpHandler(){


    }

    public String makeServiceCall(String reqUrl){
        String response = null;
        try{
            URL url = new URL(reqUrl);
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            InputStream in = new BufferedInputStream(conn.getInputStream());
            response = convertStreamToString(in);

        }catch (MalformedURLException e){
            Log.e(TAG, "malformedUrl: " +e.getMessage());
        }catch (ProtocolException e){
            Log.e(TAG, "protocolexecption: " +e.getMessage());
        }catch (IOException e){
            Log.e(TAG, "Ioexpection: " +e.getMessage());
        }catch (Exception e){
            Log.e(TAG,"exception: " +e.getMessage());
        }
        return response;
    }

    private String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line;
        try {
            while ((line = reader.readLine()) != null){
                sb.append(line).append("\n");

            }
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            try{
                is.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        }return sb.toString();
    }



}
