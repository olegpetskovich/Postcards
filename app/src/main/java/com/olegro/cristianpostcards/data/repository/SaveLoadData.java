package com.olegro.cristianpostcards.data.repository;

import android.content.Context;
import android.content.SharedPreferences;

public class SaveLoadData {
    private SharedPreferences preferences;

    public SaveLoadData(Context context) {
        preferences = context.getSharedPreferences("preferences", Context.MODE_PRIVATE);
    }

    public void saveInt(String key, int positionOnCoordinate) {
        // Извлеките редактор, чтобы изменить Общие настройки.

        SharedPreferences.Editor prefEditor = preferences.edit();
        prefEditor.putInt(key, positionOnCoordinate);

        prefEditor.apply();
    }

    public int loadInt(String key) {
        return preferences.getInt(key, -1);
    }

    public void saveString(String key, String token) {
        // Извлеките редактор, чтобы изменить Общие настройки.

        SharedPreferences.Editor prefEditor = preferences.edit();
        prefEditor.putString(key, token);

        prefEditor.apply();
    }

    public String loadString(String key) {
        return preferences.getString(key, null);
    }
}
