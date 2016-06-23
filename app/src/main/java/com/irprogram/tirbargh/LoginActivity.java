package com.irprogram.tirbargh;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class LoginActivity extends AppCompatActivity {

    Context _context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        _context = this;
        SharedPreferences pref = getSharedPreferences(GlobalVars.s_preference_name,_context.MODE_PRIVATE);
        String auth = pref.getString("Authorization","");
        if(!auth.isEmpty())
        {
            Intent i = new Intent(this,welcome.class);
            startActivity(i);
        }
        Button btnLogin = (Button)findViewById(R.id.button);
        btnLogin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {


                EditText txtUsername = (EditText)findViewById(R.id.login_user);
                EditText txtPassword = (EditText)findViewById(R.id.login_password);

                if(txtUsername.getText().toString().isEmpty())
                {
                    Toast.makeText(_context,"لطفا نام کاربری را وارد نمایید",Toast.LENGTH_LONG).show();
                    return;
                }
                if(txtPassword.getText().toString().isEmpty())
                {
                    Toast.makeText(_context,"لطفا رمز عبور را وارد نمایید",Toast.LENGTH_LONG).show();
                    return;
                }

                HashMap<String,String> map = new HashMap<String, String>();
                map.put("user",txtUsername.getText().toString());
                map.put("pass",txtPassword.getText().toString());

                LoginTask task = new LoginTask(map,_context);
                task.execute();
            }
        });
    }

    private class LoginTask extends AsyncTask<Void,Void,String > {
        private Context _context;
        private HashMap<String, String> _map;

        public LoginTask(HashMap<String, String> map, Context context) {
            _map = map;
            _context = context;
        }

        @Override
        protected String doInBackground(Void... params) {
            ArrayList<NameValuePair> data = new ArrayList<NameValuePair>();

            data.add(new BasicNameValuePair("grant_type", "password"));
            data.add(new BasicNameValuePair("username", _map.get("user")));
            data.add(new BasicNameValuePair("password", _map.get("pass")));


            HttpParams myHttpParams = new BasicHttpParams();

            HttpConnectionParams.setConnectionTimeout(myHttpParams, 1000 * 30);

            HttpConnectionParams.setSoTimeout(myHttpParams, 1000 * 30);

            HttpClient client = new DefaultHttpClient(myHttpParams);

            HttpPost post = new HttpPost(GlobalVars.server_Addr + "/token");

            //post.setHeader("Authorization", "bearer TfcP_JJQtRN8bScyXfFMJ4n97KFGbV7eLLcDg6dBL43W9p5IoUx-jKrE3Jr6NGbUMOu4cwXIRRhv_1ACupVjphHleeSbtJeFEXgzYXqmgp1yY6A6WUBWOe0jzkwBo_s5PRQ5aZ3sR5RPN3wIRTb7NTqsFZhAA358TDr-BRYvQuQ87TZQhEvyZ0CrCD-39P1BjAski-fGlY9FuP-nctAThOUn-MiXGuMg9HHLQHy0ptA2qGBO-ktXedJzXUNhwH8KAfz99VBNGVWE8PH22AlyEtzjr-TCvuf0lH9ay8yWxfjnaOp132eVJG74Anb-A2AofMoBaZDZaiLMDF4xqiYzUuPswq9XnnU5EnecCBS203Jp6_Jlbq9W6O0b0LYu6i821UYEpxqMRK7eAP5fU3K8Np0UDnLlGu_cppWnk3ewUriZo4K1xHzAxfIxpp1roN-xsEDtk_kriaplELo6c1TJ8FX9W3sO20VcouKltTjZoqTLDi3RckvKyuFclQJ0YN5A");

            try {

                post.setEntity(new UrlEncodedFormEntity(data, "UTF-8"));

                HttpResponse res = client.execute(post);

                int status = res.getStatusLine().getStatusCode();

                /*if (status == 200)
                    return "Success";*/

                String eee = EntityUtils.toString(res.getEntity());

                JSONObject obj = new JSONObject(eee);
                if(status == 400) {
                    return obj.getString("error_description");

                }
                else
                    if(status == 200)
                    {
                        SharedPreferences pref = getSharedPreferences(GlobalVars.s_preference_name,_context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("Authorization", "bearer " + obj.getString("access_token"));
                        editor.commit();
                        return "Success";
                    }


                return "Error";
            } catch (Exception e) {
                /*
                 * Log.i( "MatiMessage" , "error in posting data -> " + e.toString() );
                 */
                return "Error";
            }
        }

        @Override
        protected void onPostExecute(String result)
        {
            if(result == "Success") {
                Intent i = new Intent(LoginActivity.this, welcome.class);
                startActivity(i);
            }
            else
                Toast.makeText(_context,result,Toast.LENGTH_LONG).show();
        }
    }

    public void onBtnRegisterClick(View v)
    {
        Intent i = new Intent(this,RegisterActivity.class);
        startActivity(i);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_login, menu);
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
}
