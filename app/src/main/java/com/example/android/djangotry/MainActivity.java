package com.example.android.djangotry;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import Models.User;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.read).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findViewById(R.id.textres).setVisibility(View.VISIBLE);
                findViewById(R.id.inputs).setVisibility(View.INVISIBLE);
                Log.d("LOG","read selected");
                new read().execute();
            }
        });
        findViewById(R.id.other).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findViewById(R.id.textres).setVisibility(View.INVISIBLE);
                findViewById(R.id.inputs).setVisibility(View.VISIBLE);
            }
        });

        findViewById(R.id.put).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new put().execute();
            }
        });
        findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        findViewById(R.id.post).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new post().execute();
            }
        });
    }
    class read extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            String url_select = "http://192.168.1.208/getAllUsers/";
            URLConnection urlConn = null;
            BufferedReader bufferedReader = null;
            URL url = null;
            try {
                url = new URL(url_select);
                urlConn = url.openConnection();
                bufferedReader = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));

                StringBuilder stringBuffer = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null)
                {
                    stringBuffer.append(line);
                }

                return stringBuffer.toString();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "not found";
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            List<User> userList=new ArrayList<>();
            try {
                JSONArray jArray = new JSONArray(result);
                for(int i=0; i < jArray.length(); i++) {
                    JSONObject jObject = jArray.getJSONObject(i);
                    userList.add(new User(jObject.getInt("uid"),
                            jObject.getString("Name"),jObject.getString("email"),
                            jObject.getString("password")));
                }
            } catch (JSONException e) {
                Log.e("JSONException", "Error: " + e.toString());
            }
            ((TextView)findViewById(R.id.res)).append("\n------------------GET-------------------");
            for(User user :userList){
                ((TextView)findViewById(R.id.res)).append("\n"+user.toString());
            }
        }
    }
    class post extends AsyncTask<Void,Void,Boolean>{

        User user;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            int uid=Integer.parseInt(((EditText)findViewById(R.id.ID)).getText().toString());
            String Name=((EditText)findViewById(R.id.Name)).getText().toString();
            String email=((EditText)findViewById(R.id.email)).getText().toString();
            String password=((EditText)findViewById(R.id.password)).getText().toString();
            user=new User(uid,Name,email,password);
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            String url_select = "http://192.168.1.208/getAllUsers/";
            HttpURLConnection urlConn = null;
            BufferedReader bufferedReader = null;
            URL url = null;
            try {
                url = new URL(url_select);
                urlConn = (HttpURLConnection) url.openConnection();
                urlConn.setDoInput(true);
                urlConn.setDoOutput(true);
                urlConn.setRequestMethod("POST");
                urlConn.setUseCaches(false);
                urlConn.setRequestProperty("content-type","application/json; charset=utf-8");
                OutputStreamWriter wr = new OutputStreamWriter(urlConn.getOutputStream());
                wr.write(user.getJsonObject().toString());
                Log.d("Log",""+user.getJsonObject().toString());
                wr.flush();
                wr.close();
//                DataOutputStream send=new DataOutputStream(urlConn.getOutputStream());
//                send.writeChars(user.getJsonObject().toString());
//                send.flush();
//                send.close();

                bufferedReader = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));

                StringBuilder stringBuffer = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null)
                {
                    stringBuffer.append(line);
                }

                Log.d("Log",stringBuffer.toString());
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean s) {
            super.onPostExecute(s);
            ((TextView)findViewById(R.id.res)).append("\n----------------POST---------------------");
            if(s){
                Toast.makeText(getApplicationContext(),"User added",Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(getApplicationContext(),"User add Failed !!",Toast.LENGTH_LONG).show();
            }
        }
    }
    class put extends AsyncTask<Void,Void,Boolean>{

        User user;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            int uid=Integer.parseInt(((EditText)findViewById(R.id.ID)).getText().toString());
            String Name=((EditText)findViewById(R.id.Name)).getText().toString();
            String email=((EditText)findViewById(R.id.email)).getText().toString();
            String password=((EditText)findViewById(R.id.password)).getText().toString();
            user=new User(uid,Name,email,password);
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            String url_select = "http://192.168.1.208/getAllUsers/";
            HttpURLConnection urlConn = null;
            BufferedReader bufferedReader = null;
            URL url = null;
            try {
                url = new URL(url_select);
                urlConn = (HttpURLConnection) url.openConnection();
                urlConn.setDoInput(true);
                urlConn.setDoOutput(true);
                urlConn.setRequestMethod("PUT");
                urlConn.setUseCaches(false);
                urlConn.setRequestProperty("content-type","application/json; charset=utf-8");
                OutputStreamWriter wr = new OutputStreamWriter(urlConn.getOutputStream());
                wr.write(user.getJsonObject().toString());
                Log.d("Log",""+user.getJsonObject().toString());
                wr.flush();
                wr.close();
//                DataOutputStream send=new DataOutputStream(urlConn.getOutputStream());
//                send.writeChars(user.getJsonObject().toString());
//                send.flush();
//                send.close();

                bufferedReader = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));

                StringBuilder stringBuffer = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null)
                {
                    stringBuffer.append(line);
                }

                Log.d("Log",stringBuffer.toString());
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean s) {
            super.onPostExecute(s);
            ((TextView)findViewById(R.id.res)).append("\n----------------PUT---------------------");
            if(s){
                Toast.makeText(getApplicationContext(),"User updated",Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(getApplicationContext(),"User doesn't exists !!",Toast.LENGTH_LONG).show();
            }
        }
    }
}
