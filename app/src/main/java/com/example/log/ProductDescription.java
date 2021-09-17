package com.example.log;

import android.app.Notification;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class ProductDescription extends AppCompatActivity {

    TextView tv1,tv2,tv3,tv4,tv5;
    ImageView iv;
    Button btn,btn1,btn2;
    int i=0 ;
    String pid;
    ProgressDialog pd;
    SharedPreferences shp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_description);

        iv=findViewById(R.id.iv);
        tv1=findViewById(R.id.tv1);
        tv2=findViewById(R.id.tv2);
        tv3=findViewById(R.id.tv3);
        tv4=findViewById(R.id.tv4);
        tv5=findViewById(R.id.tv5);

        btn=findViewById(R.id.btn);
        btn1=findViewById(R.id.btn1);
        btn2=findViewById(R.id.btn2);
        shp=getSharedPreferences("mypref",0);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv5.setText(String.valueOf(i));
                i++;

            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv5.setText(String.valueOf(i));
                if(i>=0) {
                    i--;
                }
                
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new InsertCart().execute();
            }
        });



        Intent i=getIntent();
        Bundle b=i.getExtras();
           pid=b.getString("pid");
         tv1.setText("Name : "+b.getString("name"));
        tv2.setText("Price : "+b.getString("price")+"Rs.");
        tv3.setText("Category : "+b.getString("cat"));
        tv4.setText(b.getString("desc"));

        String path=GlobalLink.photopath+b.getString("photo").replace("\\","/");
        Glide.with(ProductDescription.this).load(path).into(iv);

    }

    class InsertCart extends AsyncTask<Void,Void,Void>
    {

        String res;
        String qty,Sop_Id;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd=new ProgressDialog(ProductDescription.this);
            pd.setMessage("Wait...");
            pd.show();
            qty=tv5.getText().toString();
            Sop_Id=shp.getString("uid","");
        }

        @Override
        protected Void doInBackground(Void... voids) {
            WebserviceCall obj=new WebserviceCall();
            List<NameValuePair> list=new ArrayList<>();
            list.add(new BasicNameValuePair("Sop_Id",Sop_Id));
            list.add(new BasicNameValuePair("pro_id",pid));
            list.add(new BasicNameValuePair("qty",qty));

            obj.PostData(GlobalLink.addcart,list);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            pd.dismiss();
            Intent i=new Intent(ProductDescription.this,addcart.class);
            startActivity(i);
            finish();
        }
    }
}
