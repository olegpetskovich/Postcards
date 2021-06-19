package com.olegro.cristianpostcards.mvvm.view.adapter;

import android.content.Context;
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
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.olegro.cristianpostcards.R;
import com.olegro.cristianpostcards.data.retrofit.json_response.json_get_postcards_response.PostcardsServer;
import com.olegro.cristianpostcards.mvvm.model.PostCardModel;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public class LikedRVAdapter extends RecyclerView.Adapter<LikedRVAdapter.ViewHolder> {
    private Context context;
    private ArrayList<PostCardModel> postcards;

    private ShareImageInterface shareImage;
    private LikedStateInterface likedState;

    public LikedRVAdapter(Context context) {
        this.context = context;
    }

    public interface ShareImageInterface {
        void shareImage(ImageView imageView);
    }

    public interface LikedStateInterface {
        void setLikedState(int idCard);
    }

    public void setShareImageListener(ShareImageInterface shareImage) {
        this.shareImage = shareImage;
    }

    public void setLikedStateListener(LikedStateInterface likedState) {
        this.likedState = likedState;
    }

    @NonNull
    @Override
    public LikedRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_postcard, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LikedRVAdapter.ViewHolder holder, int position) {
        PostCardModel model = postcards.get(position);

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

            holder.btnShare.setOnClickListener(v -> shareImage.shareImage(holder.imageView));

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

    @Override
    public int getItemCount() {
        return postcards.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        LikeButton btnFavourite;
        ImageButton btnShare;

        ProgressBar progressBar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.ad_images);

            btnFavourite = itemView.findViewById(R.id.btnFavourite);
            btnShare = itemView.findViewById(R.id.btnShare);

            progressBar = itemView.findViewById(R.id.progressBar);

        }
    }

    public void setData(PostcardsServer[] successData) {
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

        this.postcards = postcards;
    }
}
