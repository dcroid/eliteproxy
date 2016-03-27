package eliteproxy_biz;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import Main.R;


public class MainActivity extends AppCompatActivity {


    public static String LOG_TAG = "my_log";

    public static final String APP_PREFERENCES = "mysettings";
    private static final String ApiKey = "apikey";
    SharedPreferences sPref;
    public String key_string;

    ProgressBar pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        loadApiKey();
        pb = (ProgressBar) findViewById(R.id.progressBar);

        Button apikey = (Button) findViewById(R.id.getActivityBtnSettings);

        apikey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });

        new ParseTask().execute();
    }

    public void onClickUpBtn(View view) {
        new ParseTask().execute();
    }

    private class ParseTask extends AsyncTask<Void, Void, String> {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String resultJson = "";
        String apiUrl = "http://eliteproxy.biz/api/access.php?apikey=";

        // Во время загрузки
        @Override
        protected String doInBackground(Void... params) {
            // получаем данные с внешнего ресурса
            try {
//                URL url = new URL(apiUrl + "7d8348f0d483b48ee27f033900a34555");


                Log.d(LOG_TAG, apiUrl + key_string);
                URL url = new URL(apiUrl + key_string);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();

                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }

                resultJson = buffer.toString();

            } catch (Exception e) {
                e.printStackTrace();
            }
            return resultJson;
        }


        // Начало загрузки
        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            pb.setVisibility(ProgressBar.VISIBLE);
        }


        // Завершение загрузки
        @Override
        protected void onPostExecute(String strJson) {
            super.onPostExecute(strJson);

            pb.setVisibility(ProgressBar.INVISIBLE);

            Log.d(LOG_TAG, strJson);

            JSONObject dataJsonObj = null;

            try{ // {"ip":"188.117.110.1","expired":"2016-04-14 23:59","concnt":269}
                dataJsonObj = new JSONObject(strJson);

                String ip = dataJsonObj.getString("ip");
                String expired = dataJsonObj.getString("expired");
                String concnt = dataJsonObj.getString("concnt");

                TextView ip_resut = (TextView)findViewById(R.id.ip_result);
                ip_resut.setText(ip);

                TextView expired_result = (TextView)findViewById(R.id.expired_result);
                expired_result.setText(expired);

                TextView concnt_result = (TextView)findViewById(R.id.concnt_result);
                concnt_result.setText(concnt);

                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                String currentDateandTime = sdf.format(new Date());

                TextView time_update_res = (TextView)findViewById(R.id.time_update_res);
                time_update_res.setText(currentDateandTime);

                Log.d(LOG_TAG, ip + " " + expired + " " + concnt);
            } catch (JSONException e){
                e.printStackTrace();
                TextView error = (TextView) findViewById(R.id.errorMessage);
                error.setText("Произошла ошибка!\nПроверьте API KEY и Интернет");
                error.setTextColor(Color.parseColor("#FFFF4040"));
            }
        }

    }


    void loadApiKey(){
        sPref = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        key_string = sPref.getString(ApiKey, "no_key");
        Log.d(LOG_TAG, key_string);
    }
}
