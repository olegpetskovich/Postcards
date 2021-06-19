package com.olegro.cristianpostcards.mvvm.view.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.olegro.cristianpostcards.R;
import com.olegro.cristianpostcards.data.repository.Repository;
import com.olegro.cristianpostcards.data.retrofit.json_response.json_get_postcards_response.GetPostcards;
import com.olegro.cristianpostcards.mvvm.view.adapter.LikedRVAdapter;
import com.olegro.cristianpostcards.mvvm.viewmodel.FavoritesViewModel;
import com.olegro.cristianpostcards.utility.SaveLoadData;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import hotchemi.android.rate.AppRate;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavoritesActivity extends AppCompatActivity implements LikedRVAdapter.ShareImageInterface {
    SaveLoadData saveLoadData;

    Toolbar toolbar;

    RecyclerView recyclerView;
    LikedRVAdapter adapter;

    Repository repository;

    FavoritesViewModel viewModel;

    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);
        saveLoadData = new SaveLoadData(this);


        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressBar = findViewById(R.id.progressBar);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new LikedRVAdapter(FavoritesActivity.this);

        repository = new Repository(this);

        viewModel = ViewModelProviders.of(this).get(FavoritesViewModel.class);
        viewModel.setRepository(repository);

        repository
                .getLikedCards()
                .enqueue(new Callback<GetPostcards>() {
                    @Override
                    public void onResponse(Call<GetPostcards> call, Response<GetPostcards> response) {
                        if (response.body() != null && response.body().successData.cards.length > 0) {
                            adapter.setLikedStateListener(viewModel);
                            adapter.setShareImageListener(FavoritesActivity.this);
                            adapter.setData(response.body().successData.cards);
                            recyclerView.setAdapter(adapter);
                        } else
                            Toast.makeText(FavoritesActivity.this, getString(R.string.toast_no_favorite_postcards), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure(Call<GetPostcards> call, Throwable t) {

                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void shareImage(ImageView imageView) {
        sendImage(imageView);
    }

    private void sendImage(ImageView imageView) {

        try {
            progressBar.setVisibility(View.VISIBLE);
            BitmapDrawable bitmapDrawable = ((BitmapDrawable) imageView.getDrawable());
            Bitmap bitmap = bitmapDrawable.getBitmap();
            String bitmapPath = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "sometitle", null);
            Uri bitmapUri = Uri.parse(bitmapPath);
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("image/jpeg");
            shareIntent.putExtra(Intent.EXTRA_TEXT, "Ещё открытки: " + "http://bit.ly/2Y2aBNn");
            shareIntent.putExtra(Intent.EXTRA_STREAM, bitmapUri);
            startActivity(Intent.createChooser(shareIntent, getString(R.string.share_picture)));

            if (saveLoadData.loadInt("key") == -1) return;
            saveLoadData.saveInt("key", saveLoadData.loadInt("key") + 1);

        } catch (RuntimeException e) {
            Toast.makeText(this, getString(R.string.unknown_error_send_image), Toast.LENGTH_LONG).show();
            Crashlytics.logException(e);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);

        if (saveLoadData.loadInt("key") == 2) {
            AppRate
                    .with(this)
                    .setShowLaterButton(false)
                    .showRateDialog(this);
            saveLoadData.saveInt("key", -1);
        }
    }
}
