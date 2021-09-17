package com.example.log;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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

public class changepassword extends AppCompatActivity {

    EditText op,np,cp;
    Button btn;
    ProgressDialog pd;
    SharedPreferences shp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changepassword);
        op=findViewById(R.id.op);
        np=findViewById(R.id.np);
        cp=findViewById(R.id.cp);
        btn=findViewById(R.id.btn);
        shp=getSharedPreferences("mypref",0);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Cp().execute();

            }
        });


    }

    class Cp extends AsyncTask<Void,Void,Void>
    {
         String id,oldp,newp,res;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd=new ProgressDialog(changepassword.this);
            pd.setMessage("Wait...");
            pd.show();
          oldp=op.getText().toString();
          newp=np.getText().toString();
          id=shp.getString("uid","");


        }


        @Override
        protected Void doInBackground(Void... voids) {


            WebserviceCall obj=new WebserviceCall();
            List<NameValuePair> list=new ArrayList<>();
            list.add(new BasicNameValuePair("id",id));
            list.add(new BasicNameValuePair("op",oldp));
            list.add(new BasicNameValuePair("np",newp));


            res= obj.PostData(GlobalLink.cpurl,list);

            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
             pd.dismiss();
            try {
                JSONObject jobj=new JSONObject(res);
                String msg=jobj.getString("msg");
                Toast.makeText(changepassword.this,msg,Toast.LENGTH_LONG).show();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

}
