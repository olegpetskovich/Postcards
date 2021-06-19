package com.olegro.cristianpostcards.mvvm.view.adapter.pagedlist;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.CustomViewTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.olegro.cristianpostcards.R;
import com.olegro.cristianpostcards.mvvm.model.PostCardModel;
import com.olegro.cristianpostcards.mvvm.view.activity.MainActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

public class PLAdapter extends androidx.paging.PagedListAdapter<PostCardModel, PLAdapter.MyViewHolder> {
    private Context context;

    private LikedStateInterface likedState;
    private ShareImageInterface shareImage;

    public PLAdapter(Context context) {
        super(DIFF_CALLBACK);
        this.context = context;
    }

    public interface LikedStateInterface {
        void setLikedState(int idCard);
    }

    public void setLikedStateListener(LikedStateInterface likedState) {
        this.likedState = likedState;
    }

    public interface ShareImageInterface {
        void shareImage(Bitmap bitmap);
    }

    public void setShareImageListener(ShareImageInterface shareImage) {
        this.shareImage = shareImage;
    }

    private static DiffUtil.ItemCallback<PostCardModel> DIFF_CALLBACK = new DiffUtil.ItemCallback<PostCardModel>() {
        @Override
        public boolean areItemsTheSame(@NonNull PostCardModel oldItem, @NonNull PostCardModel newItem) {
            return oldItem.getURL().equals(newItem.getURL());
        }

        @Override
        public boolean areContentsTheSame(@NonNull PostCardModel oldItem, @NonNull PostCardModel newItem) {
            return oldItem.equals(newItem);
        }
    };

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_postcard, parent, false);

        return new MyViewHolder(view);
    }

    @SuppressLint("CheckResult")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.imageView.setImageBitmap(null);
        PostCardModel model = getItem(position);

        holder.progressBar.setVisibility(View.VISIBLE);

        if (model != null) {
            Glide.with(context)
                    .asBitmap()
                    .load("http://2.59.40.106:8081/images/" + model.getURL())
                    .listener(new RequestListener<Bitmap>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                            holder.progressBar.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                            holder.progressBar.setVisibility(View.GONE);
                            return false;
                        }
                    })
//                    .into(holder.imageView);
                    .into(new CustomTarget<Bitmap>(700, 400) {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            holder.imageView.setImageBitmap(resource);
                            model.setBitmap(resource);
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {

                        }
                    });

            holder.btnShare.setOnClickListener(v -> shareImage.shareImage(model.getBitmap()));


            holder.btnFavourite.setLiked(model.isLiked());

            holder.btnFavourite.setOnLikeListener(new OnLikeListener() {
                @Override
                public void liked(LikeButton likeButton) {
                    if (model != null) {
                        likedState.setLikedState(model.getId());
                    }
                }

                @Override
                public void unLiked(LikeButton likeButton) {
                    if (model != null) {
                        likedState.setLikedState(model.getId());
                    }
                }
            });
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        LikeButton btnFavourite;
        ImageButton btnShare;

        ProgressBar progressBar;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.ad_images);

            btnFavourite = itemView.findViewById(R.id.btnFavourite);
            btnShare = itemView.findViewById(R.id.btnShare);

            progressBar = itemView.findViewById(R.id.progressBar);

        }
    }
}
