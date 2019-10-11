package com.example.giphugifs;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Arrays;

//Class for saving data to SharedPrefs
public class SaveData {

    private SharedPreferences preferences;

    public SaveData(Context appContext) {
        preferences = PreferenceManager.getDefaultSharedPreferences(appContext);
    }

    public ArrayList<String> getListString(String key) {
        return new ArrayList<String>(Arrays.asList(TextUtils.split(preferences.getString(key, ""), "‚‗‚")));
    }

    public void putListString(String key, ArrayList<String> stringList) {
        checkForNullKey(key);
        String[] myStringList = stringList.toArray(new String[stringList.size()]);
        preferences.edit().putString(key, TextUtils.join("‚‗‚", myStringList)).apply();
    }

    public String getString(String key){
        return preferences.getString(key,"");
    }

    public void saveSring(String key, String aParam){
        checkForNullKey(key);
        preferences.edit().putString(key, aParam).apply();
    }

    public int getInt(String key){
        return preferences.getInt(key,0);
    }

    public void saveInt(String key, int aParam){
        checkForNullKey(key);
        preferences.edit().putInt(key, aParam).apply();
    }



    public void checkForNullKey(String key){
        if (key == null){
            throw new NullPointerException();
        }
    }
}
