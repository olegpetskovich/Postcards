package com.olegro.cristianpostcards.utility;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.UUID;

public class UniqueID {
    private static SharedPreferences preferences;

    private static String uniqueID;
    private static final String DEVICE_KEY = "device_key";

    public static String getUniqueID(Context context) {
        if (uniqueID == null) {
            preferences = context.getSharedPreferences(DEVICE_KEY, Context.MODE_PRIVATE);
            uniqueID = preferences.getString(DEVICE_KEY, null);
            if (uniqueID == null) {
                uniqueID = UUID.randomUUID().toString();
                SharedPreferences.Editor prefEditor = preferences.edit();
                prefEditor.putString(DEVICE_KEY, uniqueID);
                prefEditor.apply();
            }
        }
        return uniqueID;
    }
}
