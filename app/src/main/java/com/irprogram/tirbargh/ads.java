package com.irprogram.tirbargh;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ads extends AppCompatActivity
{
    private boolean go_next = false;

    private String url_ads;
    private int current_page = 0;

    private List<HashMap<String , Object>> all_ads = new ArrayList<>();

    private ListView lv;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ads);
context = this;
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);

        /*------------------------------------------------------------*/

        clear_cache();

        ProgressBar pb = (ProgressBar)findViewById(R.id.progressBarAds);
        pb.setVisibility(View.VISIBLE);

        lv = (ListView) findViewById( R.id.ads_list );

        Bundle address = getIntent().getExtras();

        if( address.getString( "flag" ) == null )
        {
            url_ads = address.getString("url");

            make_all_ads_list();

            lv.setOnScrollListener(
                    new AbsListView.OnScrollListener() {
                        @Override
                        public void onScrollStateChanged(AbsListView view, int scrollState) {
                            if (scrollState == 1) {
                                if (view.getId() == lv.getId()) {
                                    int currentFirstVisibleItem = lv.getFirstVisiblePosition();
                                    int mLastFirstVisibleItem = lv.getLastVisiblePosition();

                                    if (currentFirstVisibleItem > mLastFirstVisibleItem) {
                                        // go up
                                    } else if (currentFirstVisibleItem < mLastFirstVisibleItem) {
                                        // go down
                                        if (go_next == true) {
                                            go_next = false;

                                            make_all_ads_list();
                                        }
                                    }
                                }
                            }
                        }

                        @Override
                        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                        }
                    }
            );
        }
        else
        {
            url_ads = address.getString( "url_by_cat" );

            make_ads_list_by_cat();
        }

        lv.setOnItemClickListener(
            new AdapterView.OnItemClickListener()
            {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                {
                    Intent intent = new Intent(getApplicationContext(), show_full_ads.class);

                    intent.putExtra("ads", all_ads.get(position));

                    startActivity(intent);
                }
            }
        );
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();

        clear_cache();
    }

    public void make_all_ads_list()
    {
        try
        {
            DownloadTask dl = new DownloadTask();

            dl.execute(url_ads + current_page);

            current_page ++;
        }
        catch ( Exception e )
        {
            /*
             * Log.i( "MatiMessage" , "error in ads in make_all_ads_list() -> " + e.toString() );
             */
        }
    }

    public void make_ads_list_by_cat()
    {
        try
        {
            DownloadTask dl = new DownloadTask();

            dl.execute(url_ads);
        }
        catch ( Exception e )
        {
            /*
             * Log.i( "MatiMessage" , "error in ads in make_ads_list_by_cat() -> " + e.toString() );
             */
        }
    }

    private class DownloadTask extends AsyncTask<String , Void , String>
    {
        @Override
        protected String doInBackground(String... params)
        {
            String temp = "";

            try
            {
                JSONDownloader jd = new JSONDownloader();

                //temp = jd.downloadURL( params[0] );
                //url_insert_ads = "http://192.168.173.1:29818/api/Ads/InsertAds";

                //temp = jd.downloadURL("http://192.168.173.1:29818/api/Ads/ApprovedAdds");
                JSONParser jParser = new JSONParser();
                temp = jParser.getJsonStringUTF8FromUrlWithAuth(GlobalVars.server_Addr + "/api/Ads/ApprovedAdds",null);
            }
            catch ( Exception e )
            {
                /*
                 * Log.i( "MatiMessage" , "error in DownloadTask -> " + e.toString() );
                 */
            }

            return ( temp );
        }

        @Override
        protected void onPostExecute(String s)
        {
            ProgressBar pb = (ProgressBar)findViewById(R.id.progressBarAds);
            pb.setVisibility(View.INVISIBLE);
            ListViewLoaderTask loader = new ListViewLoaderTask();

            loader.execute( s );
        }
    }

    private class ListViewLoaderTask extends AsyncTask<String , Void , ApplicationAdapter>
    {
        @Override
        protected ApplicationAdapter doInBackground(String... params)
        {
            try
            {
                AdsParser parser = new AdsParser();

                all_ads.addAll( parser.parse(params[0]) );
            }
            catch ( Exception e )
            {
                /*
                 * Log.i( "MatiMessage" , "error in ListViewLoaderTask -> " + e.toString() );
                 */
            }

            String[] from = { "image" , "title" , "desc" , "phone" , "cat" };

            int[] to = { R.id.ads_img , R.id.ads_title , R.id.ads_intro ,
                         R.id.ads_date , R.id.ads_cat };

            SimpleAdapter adb = new SimpleAdapter(
                getBaseContext() , all_ads , R.layout.ads_list_row , from , to
            );

            ApplicationAdapter adapter = new ApplicationAdapter(context,all_ads);
            return adapter;
        }

        @Override
        protected void onPostExecute(ApplicationAdapter adapter)
        {
            lv.setAdapter( adapter );

            /*for (int i = 0; i < adapter.getCount(); i++)
            {
                HashMap<String , Object> hm =
                        (HashMap<String , Object>) adapter.getItem( i );

                String imgURL = (String) hm.get("image_path");

                HashMap<String , Object> forDownload = new HashMap<>();

                forDownload.put( "image_path" , imgURL );
                forDownload.put( "position" , i );

                ImageDownloaderTask imgDownloader = new ImageDownloaderTask();

                //imgDownloader.execute( forDownload );
            }*/

            //go_next = true;
        }
    }

    private class ImageDownloaderTask extends
            AsyncTask<HashMap<String , Object> , Void , HashMap<String , Object> >
    {
        @Override
        protected HashMap<String, Object> doInBackground(HashMap<String, Object>... params)
        {
            InputStream myStream;

            String imgUrl = (String) params[0].get( "image_path" );
            int position = (Integer) params[0].get( "position" );

            try
            {
                URL url = new URL( imgUrl );

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                connection.setDoInput(true);

                connection.connect();

                myStream = connection.getInputStream();

                File cacheDirectory = getBaseContext().getCacheDir();

                File temp = new File( cacheDirectory.getPath()
                        + "/image_" + position + "_" + current_page + ".png" );

                FileOutputStream outStream = new FileOutputStream( temp );

                Bitmap b = BitmapFactory.decodeStream( myStream );

                b.compress(Bitmap.CompressFormat.PNG, 100, outStream);

                outStream.flush();

                outStream.close();

                HashMap<String , Object> bitmap = new HashMap<>();

                bitmap.put( "image" , temp.getPath() );
                bitmap.put( "position" , position );

                return ( bitmap );
            }
            catch ( Exception e )
            {
                /*
                 * Log.i( "MatiMessage" , "error in ImageDownloaderTask -> " + e.toString() );
                 */
            }

            return null;
        }

        @Override
        protected void onPostExecute(HashMap<String, Object> result)
        {
            String image = (String) result.get( "image" );

            int position = (Integer) result.get( "position" );

            SimpleAdapter adb = (SimpleAdapter) lv.getAdapter();

            HashMap<String , Object> hm = (HashMap<String , Object>)
                    adb.getItem( position );

            hm.put( "image" , image );

            adb.notifyDataSetChanged();
        }
    }

    public void clear_cache()
        {
        try
        {
            File[] f = getBaseContext().getCacheDir().listFiles();

            for (File file : f) {
                file.delete();
            }
        }
        catch ( Exception e )
        {
            /*
             * Log.i( "MatiMessage" , "error in clear_cache() -> " + e.toString() );
             */
        }
    }

    public void onBtnBackToHomeClick( View v )
    {
        finish();
    }
}
