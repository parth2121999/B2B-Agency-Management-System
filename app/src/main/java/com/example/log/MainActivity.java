package com.example.log;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
{
    EditText username;
    EditText password;
    Button login;
    ProgressDialog pd;
 SharedPreferences shp;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login);

         shp=getSharedPreferences("mypref",0);

        login.setOnClickListener(new View.OnClickListener()
        {

                @Override
                public void onClick(View v) {

                   CheckLogin obj=new CheckLogin();
                   obj.execute();

                     }

        });
    }


    class CheckLogin extends AsyncTask<Void,Void,Void>
    {
       String l,p,res;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pd=new ProgressDialog(MainActivity.this);
         pd.setMessage("Wait...");
         pd.show();
          l=username.getText().toString();
          p=password.getText().toString();

             }

        @Override
        protected Void doInBackground(Void... voids) {
            WebserviceCall obj=new WebserviceCall();
            List<NameValuePair> list=new ArrayList<>();
            list.add(new BasicNameValuePair("username",l));
            list.add(new BasicNameValuePair("password",p));

           res= obj.PostData(GlobalLink.loginurl,list);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
           pd.dismiss();
           Log.i("res",res);
            Toast.makeText(MainActivity.this,res,Toast.LENGTH_LONG).show();

            try {

                JSONObject jobj=new JSONObject(res);
                String str=jobj.getString("msg");
                if(str.equals("Invalid"))
                {
                    Toast.makeText(MainActivity.this,"Invalid loginid & Password",Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(MainActivity.this,jobj.getString("utype"),Toast.LENGTH_SHORT).show();
                    if(jobj.getString("utype").equals("u")) {
                        String uid = jobj.getString("uid");
                        String name = jobj.getString("uname");

                        SharedPreferences.Editor ed = shp.edit();
                        ed.putString("uid", uid);
                        ed.putString("na", name);
                        ed.commit();
                        Intent i = new Intent(MainActivity.this, Welcome.class);
                        startActivity(i);
                        finish();
                    }
                    else
                    {

                        String sid = jobj.getString("sid");
                        String name = jobj.getString("name");

                        SharedPreferences.Editor ed = shp.edit();
                        ed.putString("sid", sid);
                        ed.putString("na", name);
                        ed.commit();
                        Intent i = new Intent(MainActivity.this, Welcome_Salesman.class);
                        startActivity(i);
                        finish();

                    }
                }
            } catch (JSONException e) {

                e.printStackTrace();
            }

        }
    }

}

