package com.example.andreyteterevkov.Service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.os.Vibrator;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import ApiRequest.Api;
import Main.R;
import eliteproxy_biz.MainActivity;

import java.util.concurrent.TimeUnit;

public class MonitoringService extends Service {
    public static String LOG_TAG = "my_log";
    public static final String APP_PREFERENCES = "mysettings";
    private static final String ApiKey = "apikey";
    private String key_string;

    private static final String nullTh = "nullTh";
    private static final String manyTh = "manyTh";

    SharedPreferences sPref;


    public MonitoringService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }


    public void onCreate() {
        super.onCreate();
        Log.d(LOG_TAG, "onCreate");
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(LOG_TAG, "onStartCommand");
        stopSelf();
        someTask();
        return super.onStartCommand(intent, flags, startId);

    }

    public void onDestroy() {
        super.onDestroy();
        Log.d(LOG_TAG, "onDestroy");
    }


    void someTask() {
        new Thread(new Runnable() {
            public void run() {
                while (true){
                    loadApiKey();
                    Log.d(LOG_TAG, "Работаем в сервисе");
                    Boolean vibro = false;
                    Api api = new Api();
                    String data = api.getDataResult(key_string);

                    JSONObject dataJsonObj = null;

                    try {
                        dataJsonObj = new JSONObject(data);

                        try {
                            assert dataJsonObj != null;
                            int concnt = dataJsonObj.getInt("concnt");

                            if(concnt > 299 && loadChecked(manyTh)){
                                NotificationMessage("WARNING! Очень много потоков", "Внимание!", "Очень много потоков");
                                vibro = true;
                            } else if(concnt == 0 && loadChecked(nullTh)){
                                NotificationMessage("ERROR!Что-то случилось", "Ошибка!", "Нет ни одного потока");
                                vibro = true;
                            }

                            if (vibro){
                                // Get instance of Vibrator from current Context
                                Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                                v.vibrate(400);
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    } catch (JSONException e) {
                        //stopSelf();
                        //NotificationMessage("WARNING! Неправильный API KEY", "Внимание!", "У вас неверный API KEY");
                    }

                    try {
                        TimeUnit.SECONDS.sleep(600);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        }).start();
    }

    void loadApiKey(){
        sPref = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        key_string = sPref.getString(ApiKey, "no_key");
        Log.d(LOG_TAG, sPref.getString(ApiKey, "no_key"));
    }

    private boolean loadChecked(String key){
        SharedPreferences status = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        return status.getBoolean(key, false);
    }


    void NotificationMessage(String Ticker, String Title, String Text){
        Context context = getApplicationContext();

        Intent notificationIntent = new Intent(context, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context,
                0, notificationIntent,
                PendingIntent.FLAG_CANCEL_CURRENT);

        Resources res = context.getResources();
        Notification.Builder builder = new Notification.Builder(context);

        builder.setContentIntent(contentIntent)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(res, R.mipmap.ic_launcher))
                .setTicker(Ticker)
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true)
                .setContentTitle(Title)
                .setContentText(Text);
        Notification notification = builder.build();

        NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(101, notification);
    }
}
