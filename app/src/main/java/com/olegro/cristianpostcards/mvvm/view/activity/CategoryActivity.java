package com.olegro.cristianpostcards.mvvm.view.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import hotchemi.android.rate.AppRate;
import spencerstudios.com.bungeelib.Bungee;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.transition.ChangeBounds;
import android.view.MenuItem;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipDrawable;
import com.google.android.material.chip.ChipGroup;
import com.olegro.cristianpostcards.R;
import com.olegro.cristianpostcards.mvvm.view.adapter.pagedlist.PLAdapter;
import com.olegro.cristianpostcards.mvvm.viewmodel.CategoryViewModel;
import com.olegro.cristianpostcards.utility.SaveLoadData;

public class CategoryActivity extends AppCompatActivity implements PLAdapter.ShareImageInterface {
    SaveLoadData saveLoadData;

    Toolbar toolbar;

    MaterialCardView cardView;
    ImageView iconCategory;
    TextView toolbarTitle;

    MaterialButton btnPopularPostcards;
    MaterialButton btnNewPostcards;

    RecyclerView recyclerView;

    ProgressBar progressBar;

    CategoryViewModel viewModel;

    HorizontalScrollView horizontalScroll;
    ChipGroup chipGroup;

    int categoryID;
    boolean isSortNew;
    String tag = "";

    Bitmap imageForSending;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        saveLoadData = new SaveLoadData(this);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressBar = findViewById(R.id.progressBar);

        categoryID = getIntent().getIntExtra("categoryID", 0);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ChangeBounds bounds = new ChangeBounds();
            bounds.setDuration(150);
            getWindow().setSharedElementEnterTransition(bounds);
        }

        btnPopularPostcards = findViewById(R.id.btnPopularPostcards);
//        btnPopularPostcards.setTextColor(ContextCompat.getColor(this, getIntent().getIntExtra("color", 1)));
        btnPopularPostcards.setOnClickListener(v -> {
            isSortNew = false;
            viewModel.actionNewValuesToDataSource(categoryID, isSortNew, tag);

            //Означает, что нажата кнопка "Популярные"
            btnPopularPostcards.setAlpha(0.6f);
            btnPopularPostcards.setTextSize(15);

            btnNewPostcards.setAlpha(1f);
            btnNewPostcards.setTextSize(17);
        });

        btnNewPostcards = findViewById(R.id.btnNewPostcards);
//        btnNewPostcards.setTextColor(ContextCompat.getColor(this, getIntent().getIntExtra("color", 1)));
        btnNewPostcards.setOnClickListener(v -> {
            isSortNew = true;
            viewModel.actionNewValuesToDataSource(categoryID, isSortNew, tag);

            //Означает, что нажата кнопка "Новые"
            btnNewPostcards.setAlpha(0.6f);
            btnNewPostcards.setTextSize(15);

            btnPopularPostcards.setAlpha(1f);
            btnPopularPostcards.setTextSize(17);
        });

        cardView = findViewById(R.id.cardView);
        cardView.setBackgroundColor(ContextCompat.getColor(this, getIntent().getIntExtra("color", 1)));

        iconCategory = findViewById(R.id.iconCategory);
        iconCategory.setImageResource(getIntent().getIntExtra("iconCategory", 1));

        toolbarTitle = findViewById(R.id.toolbarTitle);
        toolbarTitle.setText(getIntent().getStringExtra("titleCategory"));
        toolbarTitle.setTextColor(ContextCompat.getColor(this, getIntent().getIntExtra("color", 1)));

        //Означает, что нажата кнопка "Популярные"
        btnPopularPostcards.setAlpha(0.4f);
        btnPopularPostcards.setTextSize(15);
        btnNewPostcards.setAlpha(1f);
        btnNewPostcards.setTextSize(17);

        viewModel = ViewModelProviders.of(this).get(CategoryViewModel.class);
        viewModel.actionDataToViewModel(this, getIntent().getIntExtra("categoryID", 0), false, tag);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        PLAdapter adapter = new PLAdapter(this);
        adapter.setLikedStateListener(viewModel);
        adapter.setShareImageListener(this);
        viewModel.getPagedList().observe(this, adapter::submitList);

        recyclerView.setAdapter(adapter);

        setTags();
        showAds();
    }

    private void showAds() {
        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void setTags() {
        horizontalScroll = findViewById(R.id.horizontalScroll);

        chipGroup = findViewById(R.id.chipGroup);
        chipGroup.setSingleSelection(true);

        viewModel.getTags(categoryID).observe(this, strings -> {
            Handler mainHandler = new Handler(getMainLooper());
            Runnable myRunnable = () -> {
                if (strings != null || !strings.isEmpty()) {
                    horizontalScroll.setVisibility(View.VISIBLE);
                    for (int i = 0; i < strings.size(); i++) {
                        String tg = strings.get(i);
                        Chip chip = new Chip(chipGroup.getContext());
                        chip.setChipDrawable(ChipDrawable.createFromResource(CategoryActivity.this, R.xml.chip));
                        chip.setTag(tg);
                        chip.setText(tg);
                        chip.setClickable(true);
                        chip.setCheckable(true);
                        chip.setOnClickListener(v -> {
                            Chip chip1 = (Chip) v;
                            for (int b = 0; b < chipGroup.getChildCount(); b++) {
                                Chip eachChip = (Chip) chipGroup.getChildAt(b);
                                if (eachChip.isChecked()) {
                                    viewModel.actionNewValuesToDataSource(categoryID, isSortNew, tag);
                                    break;
                                }
                                if (b == chipGroup.getChildCount() - 1) {
                                    tag = "";
                                    viewModel.actionNewValuesToDataSource(categoryID, isSortNew, tag);
                                }
                            }
                            if (chip1.isChecked()) {
                                tag = chip1.getTag().toString();
                                viewModel.actionNewValuesToDataSource(categoryID, isSortNew, tag);
                            }
                        });
                        chipGroup.addView(chip);
                    }
                }
            };
            mainHandler.post(myRunnable);
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            chipGroup.setVisibility(View.GONE);
//            btnNewPostcards.setVisibility(View.GONE);
//            btnPopularPostcards.setVisibility(View.GONE);
//            recyclerView.setVisibility(View.GONE);
            supportFinishAfterTransition();
        } else {
            Bungee.zoom(this);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void shareImage(Bitmap bitmap) {
        boolean b = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED;
        boolean b1 = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED;

        if (b && b1) {
            imageForSending = bitmap;
            ActivityCompat.requestPermissions(CategoryActivity.this,
                    new String[]{
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE},
                    1);
        } else {
            sendImage(bitmap);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                sendImage(imageForSending);
                Toast.makeText(this, "Отправка открыток разрешена", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Отправка открыток запрещена. Разрешите доступ к галерее", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void sendImage(Bitmap bitmap) {
        try {
            progressBar.setVisibility(View.VISIBLE);
            String bitmapPath = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "sometitle", null);
            Uri bitmapUri = Uri.parse(bitmapPath);
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("image/jpeg");
            shareIntent.putExtra(Intent.EXTRA_TEXT, "Ещё открытки: " + "http://bit.ly/2Y2aBNn");
            shareIntent.putExtra(Intent.EXTRA_STREAM, bitmapUri);
            startActivity(Intent.createChooser(shareIntent, getString(R.string.share_picture)));

            if (saveLoadData.loadInt("key") == -1) return;
            saveLoadData.saveInt("key", saveLoadData.loadInt("key") + 1);

        }catch (RuntimeException e){
            Toast.makeText(this, getString(R.string.unknown_error_send_image), Toast.LENGTH_LONG).show();
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
