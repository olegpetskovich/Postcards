package com.olegro.cristianpostcards.utility;

import android.content.Context;
import android.content.SharedPreferences;

public class SaveLoadData {
    private SharedPreferences preferences;

    public SaveLoadData(Context context) {
        preferences = context.getSharedPreferences("preferences", Context.MODE_PRIVATE);
    }

    public void saveInt(String key, int value) {
        // Извлеките редактор, чтобы изменить Общие настройки.

        SharedPreferences.Editor prefEditor = preferences.edit();
        prefEditor.putInt(key, value);

        prefEditor.apply();
    }

    public int loadInt(String key) {
        return preferences.getInt(key, 0);
    }

    public void saveString(String key, String text) {
        // Извлеките редактор, чтобы изменить Общие настройки.

        SharedPreferences.Editor prefEditor = preferences.edit();
        prefEditor.putString(key, text);

        prefEditor.apply();
    }

    public String loadString(String key) {
        return preferences.getString(key, null);
    }
}
