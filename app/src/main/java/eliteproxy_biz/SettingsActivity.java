package eliteproxy_biz;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import Main.R;
import ApiRequest.Api;
import Var.MyVariable;

public class SettingsActivity extends AppCompatActivity {

    MyVariable eliteVar = new MyVariable();

    private static final String Ip = "ip";

    SharedPreferences sPref;
    EditText editApikey;
    EditText editIp;
    CheckBox NullThead;
    CheckBox ManyThead;

    String loadApiKey;
    String loadIp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        editApikey = (EditText) findViewById(R.id.editApikey);
        editIp = (EditText) findViewById(R.id.editIp);

        NullThead = (CheckBox) findViewById(R.id.checkBoxErrorCountNull);
        ManyThead = (CheckBox) findViewById(R.id.checkBoxWorningCountBig);



        loadApikey();
        loadIp();
        loadCheckedNullTh();
        loadCheckedManyTh();;
    }


    public void saveApiKey(View v){

        saveApikey();

        SaveIP();

        if(NullThead.isChecked()){
            saveNullTh(true);
        } else{
            saveNullTh(false);
        }


        if(ManyThead.isChecked()){
            saveManyTh(true);
        } else{
            saveManyTh(false);
        }

        loadApikey();
        loadIp();

        String resultChangeIp = new Api().getResultChangeIp(loadApiKey, loadIp);

        Log.d(eliteVar.Log(), "LOG API: Result Change IP " + resultChangeIp);
        Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
    }


    void saveApikey(){
        sPref = getSharedPreferences(eliteVar.VarApp(), Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putString(eliteVar.APP_API_KEY(), editApikey.getText().toString());
        ed.commit();
        //Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
    }


    void loadApikey(){
        sPref = getSharedPreferences(eliteVar.VarApp(), Context.MODE_PRIVATE);
        loadApiKey = sPref.getString(eliteVar.APP_API_KEY(), "");
        editApikey.setText(loadApiKey);
    }


    void SaveIP(){
        sPref = getSharedPreferences(eliteVar.VarApp(), Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putString(Ip, editIp.getText().toString());
        ed.commit();
    }


    void loadIp(){
        sPref = getSharedPreferences(eliteVar.VarApp(), Context.MODE_PRIVATE);
        loadIp = sPref.getString(Ip, "");
        editIp.setText(loadIp);
    }

    void loadCheckedNullTh(){
        SharedPreferences status = getSharedPreferences(eliteVar.VarApp(), Context.MODE_PRIVATE);
        if(status.getBoolean(eliteVar.APP_NULL_TH(), false)){
            NullThead.setChecked(true);
        } else {
            NullThead.setChecked(false);
        }
    }

    void saveNullTh(Boolean status){
        SharedPreferences s = getSharedPreferences(eliteVar.VarApp(), Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = s.edit();
        ed.putBoolean(eliteVar.APP_NULL_TH(), status);
        ed.apply();
    }


    void loadCheckedManyTh(){
        SharedPreferences status = getSharedPreferences(eliteVar.VarApp(), Context.MODE_PRIVATE);
        if(status.getBoolean(eliteVar.APP_MANY_TH(), false)){
            ManyThead.setChecked(true);
        } else {
            ManyThead.setChecked(false);
        }
    }

    void saveManyTh(Boolean status){
        SharedPreferences s = getSharedPreferences(eliteVar.VarApp(), Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = s.edit();
        ed.putBoolean(eliteVar.APP_MANY_TH(), status);
        ed.apply();
    }
}
