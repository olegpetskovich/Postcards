package com.olegro.cristianpostcards.mvvm.viewmodel;

import android.content.Context;

import com.olegro.cristianpostcards.data.repository.Repository;
import com.olegro.cristianpostcards.data.retrofit.json_response.json_get_likes.GetLikes;
import com.olegro.cristianpostcards.data.retrofit.json_response.json_get_postcards_response.GetPostcards;
import com.olegro.cristianpostcards.mvvm.model.PostCardModel;
import com.olegro.cristianpostcards.mvvm.view.adapter.pagedlist.PLAdapter;
import com.olegro.cristianpostcards.mvvm.view.adapter.pagedlist.SourceFactory;
import com.olegro.cristianpostcards.utility.SingleLiveEvent;

import java.util.ArrayList;
import java.util.Arrays;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PageKeyedDataSource;
import androidx.paging.PagedList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryViewModel extends ViewModel implements PLAdapter.LikedStateInterface {
    private LiveData<PagedList<PostCardModel>> itemPagedList;
    private LiveData<PageKeyedDataSource<Integer, PostCardModel>> liveDataSource;
    private Repository repository;
    private SourceFactory dataSourceFactory;

    public void actionDataToViewModel(Context context, int idCategory, boolean isSortNew, String tag) {
        repository = new Repository(context);
        dataSourceFactory = new SourceFactory(repository, idCategory, isSortNew, tag);
        liveDataSource = dataSourceFactory.getLiveDataSource();

        PagedList.Config config = new PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPageSize(10)
                .build();

        itemPagedList = new LivePagedListBuilder<Integer, PostCardModel>(dataSourceFactory, config).build();
    }

    public LiveData<PagedList<PostCardModel>> getPagedList() {
        return itemPagedList;
    }

    public void actionNewValuesToDataSource(int idCategory, boolean isSortNew, String tag) {
        dataSourceFactory.setNewValuesToDataSource(idCategory, isSortNew, tag);
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

    public SingleLiveEvent<ArrayList<String>> getTags(int categoryID) {
        SingleLiveEvent<ArrayList<String>> tags = new SingleLiveEvent<>();
        repository
                .getPostcards(categoryID, false, 1, "")
                .enqueue(new Callback<GetPostcards>() {
                    @Override
                    public void onResponse(Call<GetPostcards> call, Response<GetPostcards> response) {
                        if (response.body() != null && response.body().successData.tags != null) {
                            tags.postValue(new ArrayList<>(Arrays.asList(response.body().successData.tags)));
                        }
                    }

                    @Override
                    public void onFailure(Call<GetPostcards> call, Throwable t) {

                    }
                });
        return tags;
    }
}
