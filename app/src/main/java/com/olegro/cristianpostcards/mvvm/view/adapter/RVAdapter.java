package com.olegro.cristianpostcards.mvvm.view.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.card.MaterialCardView;
import com.olegro.cristianpostcards.R;
import com.olegro.cristianpostcards.mvvm.model.EnumHolidays;
import com.olegro.cristianpostcards.mvvm.view.activity.CategoryActivity;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.RecyclerView;

public class RVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private EnumHolidays[] holidaysList;

    private int lastPosition = -1;

    public RVAdapter(Context context) {
        this.context = context;
        holidaysList = EnumHolidays.values();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category_holiday, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        setAnimation(holder.itemView, position);

//        if (holidaysList[position].equals(EnumHolidays.DAILY) || holidaysList[position].equals(EnumHolidays.CHRISTIAN) || holidaysList[position].equals(EnumHolidays.CHILDREN)) {
//            ((MyViewHolder) holder).cardView.setCardBackgroundColor(Color.WHITE);
//            ((MyViewHolder) holder).textCategory.setText(holidaysList[position].holidayName);
//            ((MyViewHolder) holder).iconCategory.setBackground(null);
////            ((MyViewHolder) holder).iconCategory.setImageResource(holidaysList[position].iconID);
//            ((MyViewHolder) holder).layoutSoon.setVisibility(View.VISIBLE);
//            ((MyViewHolder) holder).itemView.setClickable(false);
//
//        } else {
        if (holidaysList[position] == EnumHolidays.DAILY || holidaysList[position] == EnumHolidays.CHRISTIAN) {
            ((MyViewHolder) holder).textCategory.setTextSize(11.5f);
        } else  ((MyViewHolder) holder).textCategory.setTextSize(13);

            ((MyViewHolder) holder).layoutSoon.setVisibility(View.GONE);
            ((MyViewHolder) holder).itemView.setClickable(true);

            int colorId = holidaysList[position].color;
            int color = ((MyViewHolder) holder).cardView.getContext().getResources().getColor(colorId);

            ((MyViewHolder) holder).cardView.setCardBackgroundColor(color);
            ((MyViewHolder) holder).textCategory.setText(holidaysList[position].holidayName);
            ((MyViewHolder) holder).iconCategory.setImageResource(holidaysList[position].iconID);
//        }
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            ((Activity)context).getWindow()
//        }
    }

    private void setAnimation(View viewToAnimate, int position) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.fade_in);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    @Override
    public void onViewDetachedFromWindow(final RecyclerView.ViewHolder holder) {
        ((MyViewHolder) holder).clearAnimation();
    }

    @Override
    public int getItemCount() {
        return holidaysList.length;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        MaterialCardView cardView;

        TextView textCategory;
        ImageView iconCategory;

        LinearLayout layoutSoon;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardView);
            textCategory = itemView.findViewById(R.id.textCategory);
            iconCategory = itemView.findViewById(R.id.iconCategory);

            layoutSoon = itemView.findViewById(R.id.layoutSoon);

            itemView.setOnClickListener(v -> {
                Intent intent = new Intent(context, CategoryActivity.class);
                intent.putExtra("categoryID", holidaysList[getAdapterPosition()].id);
                intent.putExtra("color", holidaysList[getAdapterPosition()].color);
                intent.putExtra("iconCategory", holidaysList[getAdapterPosition()].iconID);
                intent.putExtra("titleCategory", holidaysList[getAdapterPosition()].holidayName);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) context, cardView, "MyAnimation");
                    context.startActivity(intent, options.toBundle());
                } else {
                    context.startActivity(intent);
                }
            });
        }

        void clearAnimation() {
            itemView.clearAnimation();
        }
    }
}
