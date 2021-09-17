package com.example.log;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ProductList extends AppCompatActivity {

    GridView gv;
    ProgressDialog pd;
    ArrayList<String> aa_pid,aa_name,aa_cname,aa_price,aa_desc,aa_photo;
    String cid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        gv=findViewById(R.id.gv);
        Intent i=getIntent();
        Bundle b=i.getExtras();
        cid=b.getString("cid");

        LoadProduct obj=new LoadProduct();
        obj.execute();

        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent i=new Intent(ProductList.this,ProductDescription.class);
                i.putExtra("pid",aa_pid.get(position));
                i.putExtra("name",aa_name.get(position));
                i.putExtra("cat",aa_cname.get(position));
                i.putExtra("price",aa_price.get(position));
                i.putExtra("desc",aa_desc.get(position));
                i.putExtra("photo",aa_photo.get(position));

                startActivity(i);
            }
        });
    }

    class LoadProduct extends AsyncTask<Void,Void,Void>
    {
        String res;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pd=new ProgressDialog(ProductList.this);
            pd.setMessage("Wait..");
            pd.show();
            aa_pid=new ArrayList<>();
            aa_name=new ArrayList<>();
            aa_price=new ArrayList<>();
            aa_cname=new ArrayList<>();
            aa_desc=new ArrayList<>();
            aa_photo=new ArrayList<>();

        }

        @Override
        protected Void doInBackground(Void... voids) {
            WebserviceCall obj=new WebserviceCall();
            List<NameValuePair> list=new ArrayList<>();
            list.add(new BasicNameValuePair("cat_id",cid));
            res=obj.PostData(GlobalLink.getproduct,list);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

              pd.dismiss();
            try {
                JSONObject obj=new JSONObject(res);
                JSONArray jarr=obj.getJSONArray("msg");
                for(int i=0;i<jarr.length();i++)
                {
                    JSONObject data=jarr.getJSONObject(i);
                    aa_pid.add(data.getString("pid"));
                    aa_name.add(data.getString("name"));
                    aa_price.add(data.getString("price"));
                    aa_cname.add(data.getString("category"));
                    aa_desc.add(data.getString("discription"));
                    aa_photo.add(data.getString("photo"));

                }

                mybase mobj=new mybase();
                gv.setAdapter(mobj);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }



    class mybase extends BaseAdapter
    {

        LayoutInflater inflater;
        mybase()
        {
            inflater=LayoutInflater.from(ProductList.this);
        }
        @Override
        public int getCount() {
            return aa_pid.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        class ViewHolder
        {
            ImageView iv;
            TextView tv1,tv2;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder h;
            if(convertView==null)
            {
                h=new ViewHolder();
                convertView=inflater.inflate(R.layout.productrow,null);
                h.iv=convertView.findViewById(R.id.iv);
                h.tv1=convertView.findViewById(R.id.tv1);
                h.tv2=convertView.findViewById(R.id.tv2);

                convertView.setTag(h);
            }
            else
            {
                h=(ViewHolder)convertView.getTag();
            }

            h.tv1.setText("Name : "+aa_name.get(position));
            h.tv2.setText("Price : "+aa_price.get(position)+"Rs.");

            String path=GlobalLink.photopath+aa_photo.get(position).replace("\\","/");
            Glide.with(ProductList.this).load(path).into(h.iv);

            return convertView;
        }
    }


}
