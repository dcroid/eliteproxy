package Var;

public class MyVariable {

    private static String LOG_TAG = "my_log";
    private static final String APP_PREFERENCES = "mysettings";
    private static final String ApiKey = "apikey";

    private static final String nullTh = "nullTh";
    private static final String manyTh = "manyTh";

    private static final String apiUrl = "http://eliteproxy.biz/api/access.php?apikey=";


    public String Log(){
        return LOG_TAG;
    }

    public String VarApp(){
        return  APP_PREFERENCES;
    }

    public String APP_API_KEY(){
        return  ApiKey;
    }

    public String APP_NULL_TH(){
        return nullTh;
    }

    public String APP_MANY_TH(){
        return manyTh;
    }

    public String API_URL_ACCESS(){
        return apiUrl;
    }

}
