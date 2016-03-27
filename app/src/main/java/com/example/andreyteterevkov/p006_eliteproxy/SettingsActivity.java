package com.example.andreyteterevkov.p006_eliteproxy;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import Main.R;

public class SettingsActivity extends AppCompatActivity {

    public static final String APP_PREFERENCES = "mysettings";
    private static final String ApiKey = "apikey";
    SharedPreferences sPref;
    EditText etText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        etText = (EditText) findViewById(R.id.editText);
        loadText();

    }


    public void saveApiKey(View v){
        saveText();
        loadText();
    }


    void saveText(){
        sPref = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putString(ApiKey, etText.getText().toString());
        ed.commit();
        Toast.makeText(this, "ApiKey saved", Toast.LENGTH_SHORT).show();
    }

    void loadText(){
        sPref = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        String savedText = sPref.getString(ApiKey, "");
        etText.setText(savedText);
    }
}
