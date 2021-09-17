package com.example.log;

import android.app.ProgressDialog;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class addcart extends AppCompatActivity {

    String sid ;
    ListView lv;
    ProgressDialog pd ;
    SharedPreferences sharedPreferences ;
    ArrayList<String> aa_iv, aa_pname, aa_qty, aa_price, aa_sid,aa_cartid ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addcart);
        lv=findViewById(R.id.lv);
        sharedPreferences = getSharedPreferences("mypref",0);
        sid = sharedPreferences.getString("uid","");
        Toast.makeText(addcart.this,sid,Toast.LENGTH_SHORT).show();
        new LoadCart().execute();
    }



    class Deletecart extends AsyncTask<Void,Void,Void>
    {

        String cid,res ;
        public Deletecart(String cid) {
            this.cid = cid;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(addcart.this);
            pd.setMessage("Wait");
            pd.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            WebserviceCall service = new WebserviceCall();
            List<NameValuePair> data = new ArrayList<>();
            data.add(new BasicNameValuePair("cart_id",cid));
            res = service.PostData(GlobalLink.deletecart,data);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            pd.dismiss();;
            JSONObject object = null;
            String msg ="";
            try {
                object = new JSONObject(res);
                msg = object.getString("msg");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Toast.makeText(addcart.this,msg,Toast.LENGTH_SHORT).show();
            new LoadCart().execute();
        }
    }
    class MyBase extends BaseAdapter {


        LayoutInflater inflater  ;
        MyBase(){
            inflater = LayoutInflater.from(addcart.this);
        }
        class ViewHolder
        {
            ImageView iv;
            TextView pname,qty,price,total;
            Button delte,placeorder;
        }
        @Override
        public int getCount() {
            return aa_sid.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }
        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            ViewHolder viewHolder ;
            if(view == null)
            {
                viewHolder = new ViewHolder();
                view = inflater.inflate(R.layout.cartdetails,null);
                viewHolder.iv=view.findViewById(R.id.iv);
                viewHolder.pname = view.findViewById(R.id.pname);
                viewHolder.price = view.findViewById(R.id.price);
                viewHolder.qty = view.findViewById(R.id.qty);
                //viewHolder.total = view.findViewById(R.id.total);

                viewHolder.delte = view.findViewById(R.id.delete);
                viewHolder.placeorder = view.findViewById(R.id.place);

                view.setTag(viewHolder);
            }else{
                viewHolder = (ViewHolder) view.getTag();
            }

            viewHolder.pname.setText(aa_pname.get(i));
            viewHolder.price.setText(aa_price.get(i));
            viewHolder.qty.setText(aa_qty.get(i));
            String path=GlobalLink.photopath+aa_iv.get(i).replace("\\","/");
            Glide.with(addcart.this).load(path).into(viewHolder.iv);

            viewHolder.delte.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new Deletecart(aa_cartid.get(i)).execute();

                }
            });
            return view;
        }


    }


    class LoadCart extends AsyncTask<Void,Void,Void>
    {

        String res ;
        @Override
        protected void onPreExecute() {
            Toast.makeText(addcart.this,sid,Toast.LENGTH_LONG).show();
            super.onPreExecute();
            aa_sid = new ArrayList<>();
            aa_pname = new ArrayList<>();
            aa_price =  new ArrayList<>();
            aa_qty = new ArrayList<>();
            aa_cartid = new ArrayList<>();
            aa_iv = new ArrayList<>();

        }
        @Override
        protected Void doInBackground(Void... voids) {
            WebserviceCall obj=new WebserviceCall();
            List<NameValuePair> list=new ArrayList<>();
            list.add(new BasicNameValuePair("sid",sid));
            res=obj.PostData(GlobalLink.getcart,list);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            try {
                Toast.makeText(addcart.this,res,Toast.LENGTH_LONG).show();
                JSONObject jsonObject = new JSONObject(res);
                JSONArray jarr = jsonObject.getJSONArray("msg");
                for (int i = 0 ;i < jarr.length() ; i++)
                {
                    JSONObject jsonObject1 = jarr.getJSONObject(i);
                    aa_sid.add(jsonObject1.getString("Sop_Id"));
                    aa_cartid.add(jsonObject1.getString("cart_id"));
                    aa_pname.add(jsonObject1.getString("pname"));
                    aa_iv.add(jsonObject1.getString("photo"));
                    aa_price.add(jsonObject1.getString("price"));
                    aa_qty.add(jsonObject1.getString("qty"));


                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
            lv.setAdapter(new MyBase());
        }

        class placeoreder extends AsyncTask<Void,Void,Void>
        {
            String place;

            @Override
            protected Void doInBackground(Void... voids) {
                WebserviceCall obj=new WebserviceCall();
                List<NameValuePair> list=new ArrayList<>();
                list.add(new BasicNameValuePair("place",place));
                res=obj.PostData(GlobalLink.addorder,list);

                return null;

            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
            }
        }
    }

}
