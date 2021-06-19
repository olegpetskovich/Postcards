package com.olegro.cristianpostcards.mvvm.view.adapter.pagedlist;

import com.olegro.cristianpostcards.data.repository.Repository;
import com.olegro.cristianpostcards.data.retrofit.json_response.json_get_postcards_response.GetPostcards;
import com.olegro.cristianpostcards.data.retrofit.json_response.json_get_postcards_response.PostcardsServer;
import com.olegro.cristianpostcards.mvvm.model.PostCardModel;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.paging.PageKeyedDataSource;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyPageKeyedDataSource extends PageKeyedDataSource<Integer, PostCardModel> {
    private Repository repository;
    private int idCategory;
    private boolean isSortNew;
    private String tag;

    private int page = 1;

    public MyPageKeyedDataSource(Repository repository, int idCategory, boolean isSortNew, String tag) {
        this.repository = repository;
        this.idCategory = idCategory;
        this.isSortNew = isSortNew;
        this.tag = tag;
    }

    interface CallBackOnResult {
        void setOnResult(ArrayList<PostCardModel> postcards);
    }

    public void loadData(Repository repository, CallBackOnResult callback, int page) {
        repository
                .getPostcards(idCategory, isSortNew, page, tag)
                .enqueue(new Callback<GetPostcards>() {
                    @Override
                    public void onResponse(Call<GetPostcards> call, Response<GetPostcards> response) {
                        if (response.body() != null) {
                            ArrayList<PostCardModel> postcards = getPostCardModels(response.body().successData.cards);
                            callback.setOnResult(postcards);
                        }
                    }

                    @Override
                    public void onFailure(Call<GetPostcards> call, Throwable t) {

                    }
                });
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull LoadInitialCallback<Integer, PostCardModel> callback) {
        loadData(repository, (postcards) -> callback.onResult(postcards, null, page + 1), page);

    }

    @Override
    public void loadBefore(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, PostCardModel> callback) {
        loadData(repository, (postcards) -> callback.onResult(postcards, params.key + 1), params.key);
    }

    @Override
    public void loadAfter(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, PostCardModel> callback) {
        loadData(repository, (postcards) -> callback.onResult(postcards, params.key + 1), params.key);
    }

    private ArrayList<PostCardModel> getPostCardModels(PostcardsServer[] successData) {
        ArrayList<PostCardModel> postcards = new ArrayList<>();
        for (PostcardsServer aSuccessData : successData) {
            PostCardModel model = new PostCardModel();
            model.setURL(aSuccessData.image);
            model.setLiked(aSuccessData.isMyLike);
            model.setCategoryId(aSuccessData.categoryId);
            model.setId(aSuccessData.id);
            model.setDataCreate(aSuccessData.dataCreate);
            model.setNumberLike(aSuccessData.numberLike);

            postcards.add(model);
        }
        return postcards;
    }
}
