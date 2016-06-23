package com.irprogram.tirbargh;

        import android.app.AlertDialog;
        import android.content.Context;
        import android.content.DialogInterface;
        import android.content.Intent;
        import android.content.SharedPreferences;
        import android.content.pm.PackageManager;
        import android.database.Cursor;
        import android.graphics.Bitmap;
        import android.graphics.BitmapFactory;
        import android.graphics.drawable.BitmapDrawable;
        import android.net.Uri;
        import android.os.AsyncTask;
        import android.provider.MediaStore;
        import android.provider.OpenableColumns;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.util.Base64;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.AdapterView;
        import android.widget.ArrayAdapter;
        import android.widget.EditText;
        import android.widget.ImageView;
        import android.widget.ListView;
        import android.widget.Spinner;
        import android.widget.TextView;
        import android.widget.Toast;

        import org.apache.http.NameValuePair;
        import org.apache.http.client.HttpClient;
        import org.apache.http.client.entity.UrlEncodedFormEntity;
        import org.apache.http.client.methods.HttpPost;
        import org.apache.http.impl.client.DefaultHttpClient;
        import org.apache.http.message.BasicNameValuePair;
        import org.apache.http.params.BasicHttpParams;
        import org.apache.http.params.HttpConnectionParams;
        import org.apache.http.params.HttpParams;
        import java.io.ByteArrayOutputStream;
        import java.io.File;
        import java.util.ArrayList;
        import java.util.HashMap;

public class insert_ads extends AppCompatActivity
{
    private String url_insert_ads;

    private String[] id;
    private String[] name;

    private ListView lv_cat;

    private Spinner spinner;

    private EditText title , intro , desc , seller , email , phone;

    private TextView selected_cat , selected_img_txt;

    private ImageView selected_img;

    private String[] selected_cat_info = new String[2];

    HashMap<String,String> spinnerMap;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_ads);

        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);

        /*----------------------------------------------------------*/

        selected_cat = (TextView) findViewById( R.id.txt_insert_ads_selected_cat );
        selected_img_txt = (TextView) findViewById( R.id.insert_ads_selected_img_txt );
        selected_img = (ImageView) findViewById( R.id.insert_ads_selected_img );

        title = (EditText) findViewById( R.id.insert_ads_title );
        intro = (EditText) findViewById( R.id.insert_ads_intro );
        desc = (EditText) findViewById( R.id.insert_ads_desc );
        seller = (EditText) findViewById( R.id.insert_ads_seller );
        email = (EditText) findViewById( R.id.insert_ads_email );
        phone = (EditText) findViewById( R.id.insert_ads_phone );

        spinner = (Spinner) findViewById(R.id.spinner);

        String[] spinnerArray = new String[2];
        spinnerMap = new HashMap<String, String>();
        spinnerArray[0] = "برنامه نویس";
        spinnerArray[1] = "بازاریاب";
        spinnerMap.put(spinnerArray[0], "1");
        spinnerMap.put(spinnerArray[1], "2");

        ArrayAdapter<String> adapter =new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, spinnerArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);


        url_insert_ads = "http://192.168.1.55:29818/api/Ads/InsertAds";

        url_insert_ads = "http://192.168.173.1:29818/api/Ads/InsertAds";
        url_insert_ads = GlobalVars.server_Addr + "/api/Ads/InsertAds";
        /*for (int i = 0; i < Province_ID.size(); i++)
        {
            spinnerMap.put(Province_NAME.get(i),Province_ID.get(i));
            spinnerArray[i] = Province_NAME.get(i);
        }*/

        /*----------------------------------------------------------*/

        /*Bundle data = getIntent().getExtras();

        url_insert_ads = data.getString( "url" );


        id = data.getStringArray("cat_id");
        name = data.getStringArray("cat_name");

        ArrayList<String> cats = new ArrayList<>();

        for (int i = 0; i < id.length ; i++)
        {
            cats.add( i , name[i] );
        }

        ArrayAdapter<String> adb = new ArrayAdapter<String>(
                this , android.R.layout.simple_list_item_1 , cats
        );

        lv_cat = (ListView) findViewById( R.id.insert_ads_cat_list );

        lv_cat.setAdapter(adb);

        lv_cat.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long _id) {
                        selected_cat_info[0] = id[position];
                        selected_cat_info[1] = name[position];

                        selected_cat.setText(name[position]);
                    }
                }
        );*/

    }

    private int my_requestCode_gallery = 2;

    public void onBtnGalleryClick( View v )
    {
        Intent i = new Intent( Intent.ACTION_PICK ,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        );

        startActivityForResult( i , my_requestCode_gallery );
    }

    private int my_requestCode = 1;
    private Bitmap my_bitmap;
    private String my_final_image;

    public void onBtnCameraClick( View v )
    {
        if( getPackageManager().hasSystemFeature( PackageManager.FEATURE_CAMERA_ANY ) )
        {
            Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            startActivityForResult(i, my_requestCode);
        }
        else
        {
            Toast.makeText( getApplicationContext() ,
                    getString( R.string.no_camera_error ) ,
                    Toast.LENGTH_LONG ).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if( requestCode == my_requestCode && resultCode == RESULT_OK )
        {
            Bundle e = data.getExtras();

            my_bitmap = (Bitmap) e.get( "data" );

            show_captured_image();
        }
        else if( requestCode == my_requestCode_gallery && resultCode == RESULT_OK )
        {
            Uri image = data.getData();

            show_internal_image( image );
        }
        else
        {
            Toast.makeText( getApplicationContext() ,
                    getString( R.string.get_image_error ) ,
                    Toast.LENGTH_LONG ).show();
        }
    }

    public void show_captured_image()
    {
        try
        {
            AlertDialog.Builder imageLoader = new AlertDialog.Builder(this);

            LayoutInflater inflater = (LayoutInflater)
                    this.getSystemService(LAYOUT_INFLATER_SERVICE);

            View layout = inflater.inflate(R.layout.full_screen_image,
                    (ViewGroup) findViewById(R.id.full_img_layout_root));

            ImageView bigImage = (ImageView) layout.findViewById(R.id.full_img_img);

            bigImage.setImageBitmap(my_bitmap);

            TextView imgTitle = (TextView) layout.findViewById(R.id.full_img_title);

            imgTitle.setText(R.string.captured_img_title);

            imageLoader.setView(layout);

            imageLoader.setCancelable(false);

            imageLoader.setPositiveButton(R.string.captured_img_btn_ok,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            ByteArrayOutputStream bos = new ByteArrayOutputStream();
                            my_bitmap.compress(Bitmap.CompressFormat.JPEG, 70, bos);
                            Bitmap cmpBitmap = BitmapFactory.decodeByteArray(bos.toByteArray(), 0, bos.toByteArray().length);

                            selected_img.setImageBitmap( cmpBitmap );

                            selected_img_txt.setText( R.string.captured_img_is_true );

                            dialog.dismiss();
                        }
                    }
            );

            imageLoader.setNegativeButton(R.string.captured_img_btn_again,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();

                            onBtnCameraClick(null);
                        }
                    }
            );

            imageLoader.setNeutralButton(R.string.btn_Back_to_home,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }
            );

            imageLoader.create();

            imageLoader.show();
        }
        catch ( Exception e )
        {
            /*
             * Log.i( "MatiMessage" , "error 1 -> " + e.toString() );
             */
        }
    }

    public void show_internal_image( final Uri imageUri )
    {
        /*File f = new File(imageUri.getPath());
        Uri fileUri = imageUri;
        Cursor cursor = this.getContentResolver().query(fileUri,
                null, null, null, null);
        cursor.moveToFirst();
        long size = cursor.getLong(cursor.getColumnIndex(OpenableColumns.SIZE));
        cursor.close();
        Bitmap bmp = BitmapFactory.decodeFile(imageUri.getPath());
        if(f.length() > 550000)
        {
            Toast.makeText(this,"not more than 500kb",Toast.LENGTH_SHORT);
            return;
        }*/
        try
        {
            AlertDialog.Builder imageLoader = new AlertDialog.Builder(this);

            LayoutInflater inflater = (LayoutInflater)
                    this.getSystemService(LAYOUT_INFLATER_SERVICE);

            View layout = inflater.inflate(R.layout.full_screen_image,
                    (ViewGroup) findViewById(R.id.full_img_layout_root));

            final ImageView bigImage = (ImageView) layout.findViewById(R.id.full_img_img);

            bigImage.setImageURI(imageUri);

            TextView imgTitle = (TextView) layout.findViewById(R.id.full_img_title);

            imgTitle.setText(R.string.captured_img_title);

            imageLoader.setView(layout);

            imageLoader.setCancelable(false);

            imageLoader.setPositiveButton(R.string.captured_img_btn_ok,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            Bitmap bmap =((BitmapDrawable) bigImage.getDrawable()).getBitmap();
                            ByteArrayOutputStream bos = new ByteArrayOutputStream();
                            bmap.compress(Bitmap.CompressFormat.JPEG, 70, bos);
                            Bitmap cmpBitmap = BitmapFactory.decodeByteArray(bos.toByteArray(), 0, bos.toByteArray().length);
                            selected_img.setImageBitmap(bmap);

                            selected_img_txt.setText( R.string.captured_img_is_true );

                            dialog.dismiss();
                        }
                    }
            );

            imageLoader.setNegativeButton(R.string.captured_img_btn_again,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();

                            onBtnGalleryClick(null);
                        }
                    }
            );

            imageLoader.setNeutralButton(R.string.btn_Back_to_home,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }
            );

            imageLoader.create();

            imageLoader.show();
        }
        catch ( Exception e )
        {
            /*
             * Log.i( "MatiMessage" , "error 1 -> " + e.toString() );
             */
        }
    }

    public void onBtnInsertAdsClick( View v )
    {
        if( title.getText().length() > 1 )
        {
            //if( intro.getText().length() > 1 )
            {
                if( desc.getText().length() > 1 )
                {
                    //if( seller.getText().length() > 1 )
                    {
                        //if( email.getText().length() > 1 )
                        {
                            if( phone.getText().length() > 1 )
                            {
                                if( spinner.getSelectedItem() != null )
                                {
                                    if( selected_img_txt.getText() != getString( R.string.insert_ads_form_sel_cat_text ) )
                                    {
                                        Bitmap image = ( (BitmapDrawable) selected_img.getDrawable() ).getBitmap();

                                        HashMap<String , String> my_data = new HashMap<>();
                                        my_data.put( "title" , title.getText().toString() );
                                        my_data.put( "intro" , intro.getText().toString() );
                                        my_data.put( "description" , desc.getText().toString() );
                                        my_data.put("seller", seller.getText().toString());
                                        my_data.put("email", email.getText().toString());
                                        my_data.put("price", phone.getText().toString());

                                        String name = spinner.getSelectedItem().toString();
                                        String id = spinnerMap.get(name);

                                        my_data.put("categoryId", id);
                                        UploadImage upload = new UploadImage( my_data , image , this );

                                        upload.execute();
                                    }
                                    else
                                    {
                                        Toast.makeText( getApplicationContext() ,
                                                getString( R.string.insert_ads_form_error_sel_img ) ,
                                                Toast.LENGTH_LONG ).show();
                                    }
                                }
                                else
                                {
                                    Toast.makeText( getApplicationContext() ,
                                            getString( R.string.insert_ads_form_error_sel_cat ) ,
                                            Toast.LENGTH_LONG ).show();
                                }
                            }
                            else
                            {
                                phone.setHint(R.string.insert_ads_form_error_hint);
                            }
                        }
                        /*else
                        {
                            email.setHint( R.string.insert_ads_form_error_hint );
                        }*/
                    }
                    /*else
                    {
                        seller.setHint( R.string.insert_ads_form_error_hint );
                    }*/
                }
                else
                {
                    desc.setHint( R.string.insert_ads_form_error_hint );
                }
            }
            /*else
            {
                intro.setHint( R.string.insert_ads_form_error_hint );
            }*/
        }
        else
        {
            title.setHint( R.string.insert_ads_form_error_hint );
        }
    }

    private class UploadImage extends AsyncTask<Void , Void , Boolean>
    {
        private Context main_con;
        private HashMap<String , String> main_hm;
        private Bitmap main_image;

        public UploadImage( HashMap<String , String> hm , Bitmap image , Context con )
        {
            main_hm = hm;
            main_image = image;
            main_con = con;
        }

        @Override
        protected Boolean doInBackground(Void... params)
        {
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();

            main_image.compress( Bitmap.CompressFormat.JPEG , 100 , outStream );

            String encodedImage = Base64.encodeToString(
                    outStream.toByteArray() , Base64.DEFAULT
            );

            ArrayList<NameValuePair> dataToSend = new ArrayList<>();

            dataToSend.add( new BasicNameValuePair("title" , main_hm.get("title") ) );
            dataToSend.add( new BasicNameValuePair("intro" , main_hm.get("intro") ) );
            dataToSend.add( new BasicNameValuePair("description" , main_hm.get("description") ) );
            dataToSend.add( new BasicNameValuePair("seller" , main_hm.get("seller") ) );
            dataToSend.add( new BasicNameValuePair("email" , main_hm.get("email") ) );
            dataToSend.add( new BasicNameValuePair("price" , main_hm.get("price") ) );
            dataToSend.add( new BasicNameValuePair("categoryId" , main_hm.get("categoryId") ) );
            dataToSend.add(new BasicNameValuePair("imageString", encodedImage));

            HttpParams myHttpParams = new BasicHttpParams();

            HttpConnectionParams.setConnectionTimeout(myHttpParams, 1000 * 30);

            HttpConnectionParams.setSoTimeout(myHttpParams, 1000 * 30);

            HttpClient client = new DefaultHttpClient( myHttpParams );

            HttpPost post = new HttpPost( url_insert_ads );

            SharedPreferences pref = getSharedPreferences(GlobalVars.s_preference_name,main_con.MODE_PRIVATE);
            String auth = pref.getString("Authorization","");
            //post.setHeader("Authorization","bearer TfcP_JJQtRN8bScyXfFMJ4n97KFGbV7eLLcDg6dBL43W9p5IoUx-jKrE3Jr6NGbUMOu4cwXIRRhv_1ACupVjphHleeSbtJeFEXgzYXqmgp1yY6A6WUBWOe0jzkwBo_s5PRQ5aZ3sR5RPN3wIRTb7NTqsFZhAA358TDr-BRYvQuQ87TZQhEvyZ0CrCD-39P1BjAski-fGlY9FuP-nctAThOUn-MiXGuMg9HHLQHy0ptA2qGBO-ktXedJzXUNhwH8KAfz99VBNGVWE8PH22AlyEtzjr-TCvuf0lH9ay8yWxfjnaOp132eVJG74Anb-A2AofMoBaZDZaiLMDF4xqiYzUuPswq9XnnU5EnecCBS203Jp6_Jlbq9W6O0b0LYu6i821UYEpxqMRK7eAP5fU3K8Np0UDnLlGu_cppWnk3ewUriZo4K1xHzAxfIxpp1roN-xsEDtk_kriaplELo6c1TJ8FX9W3sO20VcouKltTjZoqTLDi3RckvKyuFclQJ0YN5A");
            post.setHeader("Authorization",auth);
            try {

                post.setEntity( new UrlEncodedFormEntity( dataToSend, "UTF-8" ));

                client.execute( post );

                return true;

            } catch ( Exception e ) {
                /*
                 * Log.i( "MatiMessage" , "error in posting data -> " + e.toString() );
                 */
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean result)
        {
            String message = "";

            if( result == true )
            {
                message = getString( R.string.result_of_insert_message_success );
            }
            else
            {
                message = getString( R.string.result_of_insert_message_failure );
            }

            AlertDialog.Builder alert = new AlertDialog.Builder( main_con );
            alert.setCancelable(false);
            alert.setTitle(R.string.result_of_insert_title);
            alert.setMessage(message);
            alert.setPositiveButton(R.string.btn_Back_to_home,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }
            );
            alert.create();
            alert.show();
        }
    }

    public void onBtnBackClick( View v )
    {
        finish();
    }
}
