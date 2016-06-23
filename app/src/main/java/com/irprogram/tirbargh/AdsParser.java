package com.irprogram.tirbargh;

import android.util.Base64;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AdsParser
{
    public List<HashMap<String , Object>> parse( String json )
    {
        List<HashMap<String , Object>> all_ads = new ArrayList<>();

        try
        {
            //JSONObject jObj = new JSONObject( json );

            JSONArray jArr = new JSONArray( json );

            for( int i = 0; i < jArr.length(); i ++ )
            {
                JSONObject temp = jArr.getJSONObject( i );

                HashMap<String , Object> ads = new HashMap<String , Object>();

                ads.put( "id" , temp.getString( "id" ) );
                ads.put( "title" , temp.getString( "title" ) );
                //ads.put( "intro" , temp.getString( "intro" ) );
                ads.put( "desc" , temp.getString( "description" ) );
                //ads.put( "image" , R.drawable.download );
                if(!temp.isNull("image"))
                ads.put( "image" , Base64.decode(temp.getString("image"),Base64.DEFAULT ));
                //ads.put( "image_path" , temp.getString( "image" ) );
                //ads.put( "seller" , temp.getString( "seller" ) );
                //ads.put( "email" , temp.getString( "email" ) );
                ads.put( "phone" , temp.getString( "price" ) );
                //ads.put( "date" , temp.getString( "date" ) );
                ads.put( "cat" , temp.getString( "categoryId" ) );

                all_ads.add( ads );
            }

        }
        catch ( Exception e )
        {
            /*
             * Log.i( "MatiMessage" , "error in AdsParser in parse() -> " + e.toString() );
             */
        }

        return ( all_ads );
    }
}
