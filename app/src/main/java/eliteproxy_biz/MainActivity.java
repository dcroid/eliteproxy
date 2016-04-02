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

import com.example.andreyteterevkov.Service.MonitoringService;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.PointsGraphSeries;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import Main.R;

import ApiRequest.Api;

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

        startService(new Intent(this, MonitoringService.class));
        loadApiKey();

        setContentView(R.layout.activity_main);

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


//        GraphView graph = (GraphView) findViewById(R.id.graph);
//        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(new DataPoint[]{
//                new DataPoint(0,100),
//                new DataPoint(1,123),
//                new DataPoint(2,143),
//                new DataPoint(3,250),
//                new DataPoint(4,189)
//
//        });
//
//        graph.addSeries(series);


    }

    public void onClickUpBtn(View view) {
        new ParseTask().execute();
    }

    private class ParseTask extends AsyncTask<Void, Void, String> {

        String resultJson = "";
        // Во время загрузки
        @Override
        protected String doInBackground(Void... params) {
            Api api = new Api();

            try {
                resultJson = api.getDataResult(key_string);

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
