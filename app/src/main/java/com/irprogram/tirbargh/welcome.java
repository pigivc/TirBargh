package com.irprogram.tirbargh;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.List;

public class welcome extends AppCompatActivity
{
    private final String url_cat = "http://irprogram.matirad.com/get_cat.php";
    private final String url_ads = "http://irprogram.matirad.com/get_data.php?page=";
    private final String url_ads_by_cat = "http://irprogram.matirad.com/get_data_by_cat.php?cat=";
    private final String url_insert_ads = "http://irprogram.matirad.com/set_data.php";

    private List<HashMap<String , Object>> all_cat;

    private ListView lv_cat;

    public boolean isConnected()
    {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo[] ni = cm.getAllNetworkInfo();

        for( int i=0; i < ni.length; i ++ )
        {
            if( ni[i].getState() == NetworkInfo.State.CONNECTED )
            {
                return true;
            }
        }

        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        if( isConnected() == false )
        {
            AlertDialog.Builder alert = new AlertDialog.Builder( welcome.this );
            alert.setCancelable( false );
            alert.setTitle(R.string.internet_error_title);
            alert.setMessage(R.string.internet_error_message);
            alert.setPositiveButton(R.string.btn_exit,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            System.exit(0);
                        }
                    }
            );
            alert.create();
            alert.show();
        }

        startService(new Intent(getApplicationContext(),NotificationService.class));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);

        navigation_drawer nd = (navigation_drawer)
            getSupportFragmentManager().findFragmentById( R.id.fragment_navigation_drawer);

        nd.setUp(
                (DrawerLayout) findViewById(R.id.welcome_layout)
        );

        /*--------------------------------------------------------*/

        lv_cat = (ListView) findViewById(R.id.category_list);

        //make_category_list();

        lv_cat.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent i = new Intent(getApplicationContext(), ads.class);

                        i.putExtra("url_by_cat",
                                url_ads_by_cat + all_cat.get(position).get("id").toString()
                        );

                        i.putExtra("flag", "1");

                        startActivity(i);
                    }
                }
        );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_welcome, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            SharedPreferences pref = getSharedPreferences(GlobalVars.s_preference_name,MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            editor.putString("Authorization", "");
            editor.commit();

            Intent i2 = new Intent(welcome.this,LoginActivity.class);
            startActivity(i2);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onBtnShowAdsClick( View v )
    {
        Intent i = new Intent( this , ads.class );
        i.putExtra( "url" , url_ads );
        startActivity(i);
    }



    public void onBtnInsertAdsClick( View v )
    {
        Intent intent = new Intent( this , insert_ads.class );

        /*intent.putExtra("url", url_insert_ads);

        String[] id = new String[all_cat.size()];
        String[] name = new String[all_cat.size()];

        for (int i = 0; i < all_cat.size(); i++)
        {
            id[i] = all_cat.get( i ).get( "id" ).toString();
            name[i] = all_cat.get( i ).get( "name" ).toString();
        }

        intent.putExtra( "cat_id" , id );
        intent.putExtra( "cat_name" , name );*/

        startActivity(intent);
    }

    public void onBtnExitClick( View v )
    {
        finish();
    }

    /* these methods are in drawer */

    private class DownloadCats extends AsyncTask<String , Void , String>
    {
        @Override
        protected String doInBackground(String... params)
        {
            String data = "";

            try
            {
                JSONDownloader jd = new JSONDownloader();

                data = jd.downloadURL( params[0] );
            }
            catch ( Exception e )
            {
                /*
                 * Log.i( "MatiMessage" , "error in DownloadCats -> " + e.toString() );
                 */
            }

            return data;
        }

        @Override
        protected void onPostExecute( String json )
        {
            CatParser parser = new CatParser();

            all_cat = parser.parse( json );

            String[] from = {"name", "amount"};

            int[] to = {R.id.name_cat, R.id.amount_cat};

            SimpleAdapter myAdapter = new SimpleAdapter(
                    getBaseContext(), all_cat ,
                    R.layout.cat_list_row, from, to
            );

            lv_cat.setAdapter(myAdapter);
        }
    }

    public void make_category_list()
    {
        DownloadCats dl_cat = new DownloadCats();

        dl_cat.execute( url_cat );
    }

    public void onBtnAboutMeClick( View v )
    {
        alert_me( getString( R.string.alert_about_title ) ,
                  getString( R.string.alert_about_body ) , true );
    }

    public void onBtnContactMeClick( View v )
    {
        alert_me(getString(R.string.alert_contact_title),
                 getString(R.string.alert_contact_body), true);
    }

    public void alert_me( String title , String body , boolean cancelable )
    {
        AlertDialog.Builder alert = new AlertDialog.Builder( welcome.this );
        alert.setCancelable( cancelable );
        alert.setTitle( title );
        alert.setMessage( body );
        alert.show();
    }
}
