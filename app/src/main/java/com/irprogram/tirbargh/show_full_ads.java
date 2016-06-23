package com.irprogram.tirbargh;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.File;
import java.util.HashMap;

public class show_full_ads extends AppCompatActivity
{
    private ImageView ads_img;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_full_ads);

        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);

        /*---------------------------------------------------------*/

        Bundle data = getIntent().getExtras();

        HashMap<String , Object> hm =
                (HashMap<String , Object>) data.get("ads");

        ads_img = (ImageView) findViewById(R.id.full_ads_img);
        TextView ads_title = (TextView) findViewById(R.id.full_ads_title);
        TextView ads_intro = (TextView) findViewById(R.id.full_ads_intro);
        TextView ads_desc = (TextView) findViewById(R.id.full_ads_desc);
        TextView ads_seller = (TextView) findViewById(R.id.full_ads_seller);
        TextView ads_email = (TextView) findViewById(R.id.full_ads_email);
        TextView ads_phone = (TextView) findViewById(R.id.full_ads_phone);
        TextView ads_cat = (TextView) findViewById(R.id.full_ads_cat);
        TextView ads_date = (TextView) findViewById(R.id.full_ads_date);

        boolean flag;

        try {
            byte[] t = (byte[]) hm.get("image");
            flag = true;
        } catch (Exception e) {
            flag = false;
        }

        //if( flag == false )
        {
            //File imgFile = new File( hm.get("image").toString() );
            byte[] imgFile = (byte[]) hm.get("image");
            if (imgFile!= null)
            {
                Bitmap myBitmap = BitmapFactory.decodeByteArray(imgFile,0,imgFile.length);
                ads_img.setImageBitmap(myBitmap);
            }
            else
            {
                ads_img.setImageResource(R.drawable.no_picture);
            }
        }
       // else {
            //ads_img.setImageResource(R.drawable.no_picture);
        //}

        ads_title.setText((String) hm.get("title"));
        ads_intro.setText((String) hm.get("intro"));
        ads_desc.setText((String) hm.get("desc"));
        ads_seller.setText((String) hm.get("seller"));
        ads_email.setText((String) hm.get("email"));
        ads_phone.setText((String) hm.get("phone"));
        ads_cat.setText((String) hm.get("cat"));
        ads_date.setText((String) hm.get("date"));
    }

    public void LoadImageFullScreen( View v )
    {
        try
        {
            final AlertDialog.Builder imageLoader = new AlertDialog.Builder(this);

            LayoutInflater inflater = (LayoutInflater)
                    this.getSystemService(LAYOUT_INFLATER_SERVICE);

            View layout = inflater.inflate(R.layout.full_screen_image,
                    (ViewGroup) findViewById(R.id.full_img_layout_root));

            ImageView bigImage = (ImageView) layout.findViewById(R.id.full_img_img);

            bigImage.setImageDrawable(ads_img.getDrawable());

            TextView imgTitle = (TextView) layout.findViewById(R.id.full_img_title);

            imgTitle.setText(R.string.big_image_title);

            imageLoader.setView(layout);

            imageLoader.setCancelable(false);

            imageLoader.setPositiveButton(R.string.btn_Back_to_home,
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
        catch (Exception e)
        {
            /*
             * Log.i( "MatiMessage" , "error 1 -> " + e.toString() );
             */
        }
    }

    public void onBtnBackToPreviousPageClick( View v )
    {
        finish();
    }
}
