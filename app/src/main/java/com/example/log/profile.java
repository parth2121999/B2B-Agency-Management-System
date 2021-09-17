package com.example.log;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;

public class profile extends AppCompatActivity {
    TextView name,sname,add,area,phone,email;
    Button submit;
    SharedPreferences shp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        name = findViewById(R.id.nme);
        sname = findViewById(R.id.sname);
        add = findViewById(R.id.address);
        phone = findViewById(R.id.phone);
        email = findViewById(R.id.email);
      //  submit = findViewById(R.id.submit);
        shp=getSharedPreferences("mypref",0);
                pr p = new pr();
                p.execute();




    }
    class pr extends AsyncTask<Void,Void,Void>
    {
        String id, res,name1,add1,sname1,email1,phone1;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            id = shp.getString("uid", "0");
        }

        @Override
        protected Void doInBackground(Void... voids) {
            WebserviceCall obj = new WebserviceCall();
            List<NameValuePair> list = new ArrayList<>();
            list.add(new BasicNameValuePair("id", id));
            res = obj.PostData(GlobalLink.sprofile,list);
            return null;
        }


        @Override
        protected void onPostExecute (Void aVoid){
            super.onPostExecute(aVoid);
            try {
                JSONObject jobj = new JSONObject(res);

                name1 = jobj.getString("uname");
                sname1 = jobj.getString("sname");
                add1 = jobj.getString("Address");
                email1 = jobj.getString("Emailid");
                phone1 = jobj.getString("Phone");

                name.setText(name1);
                sname.setText(sname1);
                add.setText(add1);
                email.setText(email1);
                phone.setText(phone1);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }
}