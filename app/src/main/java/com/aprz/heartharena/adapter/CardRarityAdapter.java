package com.aprz.heartharena.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aprz.heartharena.R;
import com.aprz.heartharena.bean.Card;
import com.aprz.heartharena.image.ImageLoader;
import com.aprz.heartharena.utils.ScoreTextColorUtil;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;

/**
 * Created by aprz on 17-8-4.
 * 卡牌适配器 使用 recycleView
 *
 * 布局使用卡片效果
 */

public class CardRarityAdapter extends RecyclerView.Adapter<CardRarityAdapter.CardViewHolder> {

    private ArrayList<Card> mDatas = new ArrayList<>();

    public CardRarityAdapter() {
    }

    static class CardViewHolder extends RecyclerView.ViewHolder{

        private SimpleDraweeView image;
        private TextView name, score, scoreLevel, profession;

        CardViewHolder(View itemView) {
            super(itemView);

            image = (SimpleDraweeView) itemView.findViewById(R.id.card_image);
            name = (TextView) itemView.findViewById(R.id.card_name);
            score = (TextView) itemView.findViewById(R.id.card_score);
//            scoreLevel = (TextView) itemView.findViewById(R.id.card_score_level);
//            profession = (TextView) itemView.findViewById(R.id.card_profession);
        }


    }


    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View card = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_card_rarity, parent, false);

        return new CardViewHolder(card);
    }

    @Override
    public void onBindViewHolder(CardViewHolder holder, int position) {
        Card card = mDatas.get(position);
        holder.name.setText(card.getName());
        holder.score.setText(card.getScore());
//        holder.scoreLevel.setText(mDatas.get(position).getScoreLevel());
//        holder.profession.setText(mDatas.get(position).getProfession());
//        int color = ;
//        Log.e("color", card.getScoreLevel());
//        Log.e("color", "---" + color);
        holder.score.setTextColor(ScoreTextColorUtil.getColor(card.getScoreLevel()));


        ImageLoader.getInstance().load(holder.image, card.getImage());
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public void initData(ArrayList<Card> datas) {
        mDatas.clear();
        mDatas.addAll(datas);
        notifyDataSetChanged();
    }

}
