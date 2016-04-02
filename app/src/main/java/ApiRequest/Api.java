package ApiRequest;


import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;


public class Api {

    public static String LOG_TAG = "my_log";
    private String keyString;
    private String ip;
    HttpURLConnection urlConnection = null;
    BufferedReader reader = null;
    String resultJson = "";
    String apiUrl = "http://eliteproxy.biz/api/access.php?apikey=";

    // Во время загрузки

    private String getKey(){
        return this.keyString;
    }

    private void setKey(String key){
        this.keyString = key;
    }

    private void setIp(String ip){
        this.ip = Uri.encode(ip, "UTF-8");
    }

    private String getIp(){
        return this.ip;
    }

    private String getData (){
    // получаем данные с внешнего ресурса
        try {
            Log.d(LOG_TAG, "LOG API: " + apiUrl + getKey());
            URL url = new URL(apiUrl + getKey());
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();


            InputStream inputStream = urlConnection.getInputStream();
            StringBuilder buffer = new StringBuilder();

            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }

            resultJson = buffer.toString();
            Log.d(LOG_TAG, "LOG API: " + resultJson);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultJson;
    }

    private String changeIp(){
        try{
            // http://eliteproxy.biz/api/access.php?apikey=&ip=

            Log.d(LOG_TAG, "LOG API: changeIp -> " +apiUrl + getKey() + "&ip=" + getIp());
            URL url = new URL(apiUrl + getKey() + "&ip=" + getIp());
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();


            InputStream inputStream = urlConnection.getInputStream();
            StringBuilder buffer = new StringBuilder();

            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }

            resultJson = buffer.toString();
            Log.d(LOG_TAG, "LOG API: changeIp --> " + resultJson);

        }catch (Exception e){
            e.printStackTrace();
        }

        return  resultJson;
    }

    private class ChangeHttpIP extends AsyncTask<Void, Void, String>{

        @Override
        protected String doInBackground(Void... params) {
            return changeIp();
        }
    }




    public String getDataResult(String key){
        setKey(key);
        return getData();
    }

    public String getResultChangeIp(String key, String ip) {
        setKey(key);
        setIp(ip);
        new ChangeHttpIP().execute();
        return null;
    }

}


