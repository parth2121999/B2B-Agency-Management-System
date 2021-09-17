package com.example.log;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class vieworder<string> extends AppCompatActivity {
    String sid;
    ListView lv;
    SharedPreferences sharedPreferences;
    ArrayList<string> aa_lv, aa_cart_id, aa_Sop_id, aa_pname, aa_qty, aa_price;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vieworder);
        lv=findViewById(R.id.lv);

    }

}
