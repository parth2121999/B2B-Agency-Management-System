package com.example.log;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.file.StandardWatchEventKinds;
import java.util.ArrayList;
import java.util.List;

public class Welcome<welcome> extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    SharedPreferences shp;
    GridView gv;
    ArrayList<String> aa_cid,aa_cname,aa_photo;

    ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        gv=findViewById(R.id.gv);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

         shp=getSharedPreferences("mypref",0);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
       LoadCategory obj=new LoadCategory();
       obj.execute();

       gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

               Intent i=new Intent(Welcome.this,ProductList.class);
               i.putExtra("cid",aa_cid.get(position));
               startActivity(i);

           }
       });
    }


    class LoadCategory extends AsyncTask<Void,Void,Void>
    {

        String res;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd=new ProgressDialog(Welcome.this);
            pd.setMessage("Wait..");
            pd.show();
            aa_cid=new ArrayList<>();
            aa_cname=new ArrayList<>();
            aa_photo=new ArrayList<>();

        }

        @Override
        protected Void doInBackground(Void... voids) {
            WebserviceCall obj=new WebserviceCall();
            List<NameValuePair> list=new ArrayList<>();
            res=obj.PostData(GlobalLink.getcategory,list);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
          pd.dismiss();
            try {
                JSONObject jobj=new JSONObject(res);
                JSONArray jarr=jobj.getJSONArray("msg");
                for(int i=0;i<jarr.length();i++)
                {
                    JSONObject data=jarr.getJSONObject(i);
                   aa_cid.add(data.getString("cid"));
                    aa_cname.add(data.getString("catname"));
                    aa_photo.add(data.getString("photo"));
                }

                gv.setAdapter(new mybase());

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
            inflater=LayoutInflater.from(Welcome.this);
        }
        @Override
        public int getCount() {
            return aa_cid.size();
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
            TextView tv;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
          ViewHolder h;
          if(convertView==null)
          {
              h=new ViewHolder();
              convertView=inflater.inflate(R.layout.catrow,null);
              h.iv=convertView.findViewById(R.id.iv);
              h.tv=convertView.findViewById(R.id.tv);
              convertView.setTag(h);
          }
          else
          {
              h=(ViewHolder)convertView.getTag();
          }

            h.tv.setText(aa_cname.get(position));
            String path=GlobalLink.photopath+aa_photo.get(position).replace("\\","/");
            Glide.with(Welcome.this).load(path).into(h.iv);

            return convertView;
        }
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.welcome, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_mycart) {
            Intent i = new Intent(Welcome.this,addcart.class);
            startActivity(i);

        } else if (id == R.id.nav_profile) {

            Intent i = new Intent(Welcome.this,profile.class);
            startActivity(i);

        }
        else if (id == R.id.nav_password) {

            Intent i=new Intent(Welcome.this,changepassword.class);
            startActivity(i);
        }
        else if (id == R.id.nav_logout) {

            AlertDialog.Builder aa=new AlertDialog.Builder(Welcome.this);
            aa.setMessage("Do you want to logout?");
            aa.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    SharedPreferences.Editor ed=shp.edit();
                    ed.clear();
                    ed.commit();
                    Intent i=new Intent(Welcome.this,MainActivity.class);
                    startActivity(i);
                    finish();
                }
            });

            aa.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });

            aa.show();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
