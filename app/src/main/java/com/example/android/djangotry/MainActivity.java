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

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Models.User;

import static com.android.volley.Request.*;


public class MainActivity extends AppCompatActivity {

    List<User> userlist=new ArrayList<User>();
    final String URL="http://192.168.1.208/getAllUsers/";
    private User getUserWithID(int id){
        for(User u:userlist){
            if (u.getUid()==id)
                return u;
        }
        return null;
    }

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
//                new read().execute();
                JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(Method.GET,
                        URL, null,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                ((TextView) findViewById(R.id.res)).append("\n----------GET---------");
                                for(int i=0;i<response.length();i++){
                                    try {
                                        userlist.add(new User(response.getJSONObject(i)));
                                        ((TextView) findViewById(R.id.res)).append("\n"
                                                +userlist.get(i).toString());
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        ((TextView) findViewById(R.id.res)).append("\n----------GET---------");
                        ((TextView) findViewById(R.id.res)).append(""+error.getMessage());
                    }
                });
                SingletoneClass.getInstance(getApplicationContext()).addToRequestQue(jsonArrayRequest);
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
//                new put().execute();
                final User updateUser=getUserData();
                StringRequest stringRequest=new StringRequest(Method.PUT, URL,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                ((TextView)findViewById(R.id.res)).append("\n--------------PUT--------------");
                                ((TextView)findViewById(R.id.res)).append("\n"+response);
                                Toast.makeText(getApplicationContext(),response,Toast.LENGTH_SHORT).show();
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        ((TextView)findViewById(R.id.res)).append("\n--------------PUT--------------");
                        ((TextView)findViewById(R.id.res)).append("\n"+error.getMessage());
                    }
                }){
                    @Override
                    public String getBodyContentType() {
                        return "application/json; charset=utf-8";
                    }

                    @Override
                    public byte[] getBody() throws AuthFailureError {
                        try {
                            return updateUser.getJsonObject().toString().getBytes("utf-8");
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }
                };
                SingletoneClass.getInstance(getApplicationContext()).addToRequestQue(stringRequest);
            }
        });
        findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new delete().execute();
            }

        });
        findViewById(R.id.post).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            final User newUserData=getUserData();
            StringRequest stringRequest=new StringRequest(Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    ((TextView)findViewById(R.id.res)).append("\n--------------POST--------------");
                    ((TextView)findViewById(R.id.res)).append("\n"+response);
                    Toast.makeText(getApplicationContext(),response,Toast.LENGTH_SHORT).show();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    ((TextView)findViewById(R.id.res)).append("\n--------------POST--------------");
                    ((TextView)findViewById(R.id.res)).append("\n"+error.getMessage());
                }
            }){
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return newUserData.getJsonObject().toString().getBytes("utf-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    return null;
                }
            };
            SingletoneClass.getInstance(getApplicationContext()).addToRequestQue(stringRequest);
            }
        });
    }

    private User getUserData(){
        int uid=Integer.parseInt(((EditText)findViewById(R.id.ID)).getText().toString());
        String Name=((EditText)findViewById(R.id.Name)).getText().toString();
        String email=((EditText)findViewById(R.id.email)).getText().toString();
        String password=((EditText)findViewById(R.id.password)).getText().toString();
        return new User(uid,Name,email,password);
    }

    class delete extends AsyncTask<Void,Void,Boolean>{

        User user;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            int uid=Integer.parseInt(((EditText)findViewById(R.id.ID)).getText().toString());
            user=new User(uid,"","","");
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            String url_select = URL;
            HttpURLConnection urlConn = null;
            BufferedReader bufferedReader = null;
            URL url = null;
            try {
                url = new URL(url_select);
                urlConn = (HttpURLConnection) url.openConnection();
                urlConn.setDoInput(true);
                urlConn.setDoOutput(true);
                urlConn.setRequestMethod("DELETE");
                urlConn.setUseCaches(false);
                urlConn.setRequestProperty("content-type","application/json; charset=utf-8");
                OutputStreamWriter wr = new OutputStreamWriter(urlConn.getOutputStream());
                wr.write(user.getJsonObject().toString());
                Log.d("Log",""+user.getJsonObject().toString());
                wr.flush();
                wr.close();

                bufferedReader = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
                StringBuilder stringBuffer = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null){
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
                Toast.makeText(getApplicationContext(),"User deleted",Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(getApplicationContext(),"User doesn't exists !!",Toast.LENGTH_LONG).show();
            }
        }
    }
}
