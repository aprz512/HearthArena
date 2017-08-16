package com.aprz.heartharena.adapter;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aprz.heartharena.R;
import com.aprz.heartharena.bean.Card;
import com.aprz.heartharena.image.ImageLoader;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;

/**
 * Created by aprz on 17-8-4.
 * 卡牌适配器 使用 recycleView
 *
 * 布局使用卡片效果
 */

public class CardListAdapter extends RecyclerView.Adapter<CardListAdapter.CardViewHolder> {

    private ArrayList<Card> mDatas = new ArrayList<>();

    public CardListAdapter() {
    }

    static class CardViewHolder extends RecyclerView.ViewHolder{

        private SimpleDraweeView image;
        private TextView name, score, scoreLevel;

        CardViewHolder(View itemView) {
            super(itemView);

            image = (SimpleDraweeView) itemView.findViewById(R.id.card_image);
            name = (TextView) itemView.findViewById(R.id.name);
            score = (TextView) itemView.findViewById(R.id.score);
            scoreLevel = (TextView) itemView.findViewById(R.id.score_level);
        }

    }


    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View card = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_card, parent, false);

        return new CardViewHolder(card);
    }

    @Override
    public void onBindViewHolder(CardViewHolder holder, int position) {
        holder.name.setText(mDatas.get(position).getName());
        holder.score.setText(mDatas.get(position).getScore());
        holder.scoreLevel.setText(mDatas.get(position).getScoreLevel());
        ImageLoader.getInstance().load(holder.image, mDatas.get(position).getImage());
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public void swapCursor(Cursor data, boolean isAdd) {
        if (data == null) {
            mDatas.clear();
            notifyDataSetChanged();
            return;
        }

        if (!data.moveToFirst()) {
            mDatas.clear();
            notifyDataSetChanged();
            return;
        }

        if (!isAdd) {
            mDatas.clear();
        }

        do {
            Card card = Card.createWithCursor(data);
            mDatas.add(card);
        } while (data.moveToNext());

        notifyDataSetChanged();
    }

}
