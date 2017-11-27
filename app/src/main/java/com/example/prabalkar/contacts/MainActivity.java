package com.example.prabalkar.contacts;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private String TAG = MainActivity.class.getSimpleName();

    private ProgressDialog pDialog;

    private ListView lv;

    private static String url = "http://api.androidhive.info/contacts/";

    ArrayList<HashMap<String, String>> contactlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        contactlist =  new ArrayList<>();

        lv = (ListView) findViewById(R.id.listview);

        new GetContacts().execute();
    }

    private class GetContacts extends AsyncTask<Void, Void, Void>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("loading...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            HttpHandler sh = new HttpHandler();

            String jsonStr =  sh.makeServiceCall(url);

            Log.e(TAG, "Resposnse from url:" +jsonStr);

            if (jsonStr != null){
                try{
                    JSONObject jsonObject = new JSONObject(jsonStr);

                    JSONArray contacts =jsonObject.getJSONArray("contacts");

                    for (int i=0; i< contacts.length(); i++){
                        JSONObject c = contacts.getJSONObject(i);


                        String id = contacts.getJSONObject(i).getString("id");
                        String name = contacts.getJSONObject(i).getString("name");
                        String email = contacts.getJSONObject(i).getString("email");
                        String address = contacts.getJSONObject(i).getString("address");
                        String gender = contacts.getJSONObject(i).getString("gender");


                        JSONObject phone = c.getJSONObject("phone");
                        String mobile = phone.getString("mobile");
                        String office = phone.getString("office");
                        String home = phone.getString("home");

                        HashMap<String, String> contact = new HashMap<>();

                        contact.put("id", id);
                        contact.put("name", name);
                        contact.put("email", email);
                        contact.put("mobile", mobile);

                        contactlist.add(contact);

                    }
                }catch (final JSONException e){
                    Log.e(TAG, "json parsing error: " +e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this,
                                    "json parsing error: " +e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }else {
                Log.e(TAG, "couldn't get json from server: ");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this,
                                "couldn't get json from server:",
                                Toast.LENGTH_SHORT).show();
                    }
                });

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (pDialog.isShowing()){
                pDialog.dismiss();

                ListAdapter adapter = new SimpleAdapter(
                        MainActivity.this, contactlist,
                        R.layout.list_contacts, new String[]{"name", "email", "mobile"},
                        new  int[]{R.id.name, R.id.email, R.id.mobile});

                lv.setAdapter(adapter);
            }
        }
    }
}
