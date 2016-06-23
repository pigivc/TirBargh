package com.irprogram.tirbargh;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register, menu);
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

    public void onBtnRegisterClick(View v)
    {
        EditText txtCompany = (EditText)findViewById(R.id.company_name);
        EditText txtCeo = (EditText)findViewById(R.id.ceo_name);
        EditText txtIntro = (EditText)findViewById(R.id.intro);
        EditText txtAddress = (EditText)findViewById(R.id.Address);
        EditText txtTel = (EditText) findViewById(R.id.tel);
        EditText txtEmail = (EditText) findViewById(R.id.email);
        EditText txtPassword = (EditText) findViewById(R.id.password);

        if(txtCompany.getText().toString().isEmpty()) {
            Toast.makeText(this, "نام شرکت را وارد نمایید", Toast.LENGTH_SHORT).show();
            return;
        }

        if(txtCeo.getText().toString().isEmpty()){
        Toast.makeText(this,"نام شرکت را وارد نمایید",Toast.LENGTH_SHORT).show();
            return;
        }

        if(txtIntro.getText().toString().isEmpty()){
            Toast.makeText(this,"قسمت مربوط به معرفی را پر نمایید",Toast.LENGTH_SHORT).show();
            return;
        }

        if(txtAddress.getText().toString().isEmpty()){
            Toast.makeText(this,"آدرس را وارد نمایید",Toast.LENGTH_SHORT).show();
            return;
        }

        if(txtTel.getText().toString().isEmpty()){
            Toast.makeText(this,"تلفن را وارد نمایید",Toast.LENGTH_SHORT).show();
            return;
        }

        if(txtEmail.getText().toString().isEmpty()){
            Toast.makeText(this,"ایمیل را وارد نمایید",Toast.LENGTH_SHORT).show();
            return;
        }

        if(txtPassword.getText().toString().isEmpty()){
            Toast.makeText(this,"رمز عبور را وارد نمایید",Toast.LENGTH_SHORT).show();
            return;
        }

        HashMap<String,String> map = new HashMap<String,String>();

        map.put("company",txtCompany.getText().toString());
        map.put("ceo",txtCeo.getText().toString());
        map.put("intro",txtIntro.getText().toString());
        map.put("address",txtAddress.getText().toString());
        map.put("tel",txtTel.getText().toString());
        map.put("email",txtEmail.getText().toString());
        map.put("password", txtPassword.getText().toString());

        RegisterTask task = new RegisterTask(map,this);
        task.execute();

    }

    private class RegisterTask extends AsyncTask<Void,Void,String>
    {
        private Context _context;
        private HashMap<String,String> _map;

        public RegisterTask(HashMap<String,String> map,Context context)
        {
            _map = map;
            _context = context;
        }

        @Override
        protected String doInBackground(Void... params)
        {
            ArrayList<NameValuePair> data = new ArrayList<NameValuePair>();
            data.add(new BasicNameValuePair("CompanyName",_map.get("company")));
            data.add(new BasicNameValuePair("CEOName",_map.get("ceo")));
            data.add(new BasicNameValuePair("Intro",_map.get("intro")));
            data.add(new BasicNameValuePair("Address",_map.get("address")));
            data.add(new BasicNameValuePair("Tel",_map.get("tel")));
            data.add(new BasicNameValuePair("Email",_map.get("email")));
            data.add(new BasicNameValuePair("Password",_map.get("password")));
            data.add(new BasicNameValuePair("ConfirmPassword",_map.get("password")));

            HttpParams myHttpParams = new BasicHttpParams();

            HttpConnectionParams.setConnectionTimeout(myHttpParams, 1000 * 30);

            HttpConnectionParams.setSoTimeout(myHttpParams, 1000 * 30);

            HttpClient client = new DefaultHttpClient( myHttpParams );

            HttpPost post = new HttpPost( GlobalVars.server_Addr + "/api/account/register" );

            //post.setHeader("Authorization", "bearer TfcP_JJQtRN8bScyXfFMJ4n97KFGbV7eLLcDg6dBL43W9p5IoUx-jKrE3Jr6NGbUMOu4cwXIRRhv_1ACupVjphHleeSbtJeFEXgzYXqmgp1yY6A6WUBWOe0jzkwBo_s5PRQ5aZ3sR5RPN3wIRTb7NTqsFZhAA358TDr-BRYvQuQ87TZQhEvyZ0CrCD-39P1BjAski-fGlY9FuP-nctAThOUn-MiXGuMg9HHLQHy0ptA2qGBO-ktXedJzXUNhwH8KAfz99VBNGVWE8PH22AlyEtzjr-TCvuf0lH9ay8yWxfjnaOp132eVJG74Anb-A2AofMoBaZDZaiLMDF4xqiYzUuPswq9XnnU5EnecCBS203Jp6_Jlbq9W6O0b0LYu6i821UYEpxqMRK7eAP5fU3K8Np0UDnLlGu_cppWnk3ewUriZo4K1xHzAxfIxpp1roN-xsEDtk_kriaplELo6c1TJ8FX9W3sO20VcouKltTjZoqTLDi3RckvKyuFclQJ0YN5A");

            try {

                post.setEntity(new UrlEncodedFormEntity(data, "UTF-8"));

                HttpResponse res = client.execute(post);

                int status = res.getStatusLine().getStatusCode();

                if(status == 200)
                    return "Success";

                String eee = EntityUtils.toString(res.getEntity());

                JSONObject obj = new JSONObject(eee);
                JSONObject obj2 =(JSONObject)obj.get("ModelState");
                Iterator<String> keys = obj2.keys();

                StringBuilder sb = new StringBuilder();
                for (int i = 0;i<obj2.names().length();i++)
                {
                     sb.append(obj2.get(obj2.names().get(i).toString()).toString());
                }


                
                /*InputStream is = res.getEntity().getContent();

                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        is, "iso-8859-1"), 8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                is.close();
                String json = sb.toString();
                Log.e("JSON", json);*/
                return sb.toString();

            } catch ( Exception e ) {
                /*
                 * Log.i( "MatiMessage" , "error in posting data -> " + e.toString() );
                 */
                return "Error";
            }

        }

        @Override
        protected void onPostExecute(String result)
        {
            if(result  == "Success")
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(_context);
                builder.setMessage(result)
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                onBackPressed();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
            else {
                try {
                    Toast.makeText(_context, result, Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    return;
                }
            }

        }
    }

}
