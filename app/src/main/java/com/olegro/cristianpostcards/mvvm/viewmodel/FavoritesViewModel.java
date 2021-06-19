package com.olegro.cristianpostcards.mvvm.viewmodel;

import com.olegro.cristianpostcards.data.repository.Repository;
import com.olegro.cristianpostcards.data.retrofit.json_response.json_get_likes.GetLikes;
import com.olegro.cristianpostcards.mvvm.view.adapter.LikedRVAdapter;

import androidx.lifecycle.ViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavoritesViewModel extends ViewModel implements LikedRVAdapter.LikedStateInterface {
    private Repository repository;

    public void setRepository(Repository repository) {
        this.repository = repository;
    }

    @Override
    public void setLikedState(int idCard) {
        repository
                .setCardLike(idCard)
                .enqueue(new Callback<GetLikes>() {
                    @Override
                    public void onResponse(Call<GetLikes> call, Response<GetLikes> response) {

                    }

                    @Override
                    public void onFailure(Call<GetLikes> call, Throwable t) {

                    }
                });
    }
}
