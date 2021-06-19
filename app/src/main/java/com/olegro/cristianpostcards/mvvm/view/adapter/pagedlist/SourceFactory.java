package com.olegro.cristianpostcards.mvvm.view.adapter.pagedlist;

import com.olegro.cristianpostcards.data.repository.Repository;
import com.olegro.cristianpostcards.mvvm.model.PostCardModel;
import com.olegro.cristianpostcards.utility.SingleLiveEvent;

import androidx.paging.DataSource;
import androidx.paging.PageKeyedDataSource;

public class SourceFactory extends DataSource.Factory {
    private final Repository storage;
    private int idCategory;
    private boolean isSortNew;
    private String tag;

    private MyPageKeyedDataSource pageKeyedDataSource;
    SingleLiveEvent<PageKeyedDataSource<Integer, PostCardModel>> liveDataSource;

    public SourceFactory(Repository storage, int idCategory, boolean isSortNew, String tag) {
        this.storage = storage;
        this.idCategory = idCategory;
        this.isSortNew = isSortNew;
        this.tag = tag;
    }

    @Override
    public DataSource create() {
        pageKeyedDataSource = new MyPageKeyedDataSource(storage, idCategory, isSortNew, tag);
        liveDataSource = new SingleLiveEvent<>();
        liveDataSource.postValue(pageKeyedDataSource);
        return pageKeyedDataSource;
    }

    public SingleLiveEvent<PageKeyedDataSource<Integer, PostCardModel>> getLiveDataSource() {
        return liveDataSource;
    }

    public void setNewValuesToDataSource(int idCategory, boolean isSortNew, String tag) {
        this.idCategory = idCategory;
        this.isSortNew = isSortNew;
        this.tag = tag;
        pageKeyedDataSource.invalidate();
    }
}
