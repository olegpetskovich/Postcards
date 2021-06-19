package com.olegro.cristianpostcards.data.repository;

import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

import com.olegro.cristianpostcards.data.retrofit.UserClient;
import com.olegro.cristianpostcards.data.retrofit.Web;
import com.olegro.cristianpostcards.data.retrofit.json_response.json_authorization.Authorization;
import com.olegro.cristianpostcards.data.retrofit.json_response.json_get_likes.GetLikes;
import com.olegro.cristianpostcards.data.retrofit.json_response.json_get_postcards_response.GetPostcards;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Repository {
    private Context context;

    private SaveLoadData saveLoadData;
    private UserClient client;

    private final String TOKEN_KEY = "token_key";

    public Repository(Context context) {
        this.context = context;
        saveLoadData = new SaveLoadData(context);
        client = new Web(context).createRetrofit();
    }

    public void authorization(String deviceKey, String fbToken) {
        Call<Authorization> call = client.authorization(deviceKey, fbToken);
        call.enqueue(new Callback<Authorization>() {
            @Override
            public void onResponse(Call<Authorization> call, Response<Authorization> response) {
                if (response.body() != null) {
                    String token = response.body().successData;
                    saveToken(token);
                    Handler mainHandler = new Handler(context.getMainLooper());
                    Runnable myRunnable = () -> Toast.makeText(context, "User is registered", Toast.LENGTH_SHORT).show();
                    mainHandler.post(myRunnable);
                }
            }

            @Override
            public void onFailure(Call<Authorization> call, Throwable t) {

            }
        });
    }

    public Call<GetPostcards> getPostcards(int idCategory, boolean isSortNew, int page, String tag) {
        String token = loadToken();
        return client.getPostcards(token, idCategory, isSortNew, page, tag);
    }

    public Call<GetLikes> setCardLike(int idCard) {
        String token = loadToken();
        return client.setCardLike(token, idCard);
    }

    public Call<GetPostcards> getLikedCards() {
        String token = loadToken();
        return client.getLikedCards(token);
    }

    private void saveToken(String token) {
        saveLoadData.saveString(TOKEN_KEY, token);
    }

    private String loadToken() {
        return saveLoadData.loadString(TOKEN_KEY);
    }
}
