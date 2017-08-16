package com.aprz.heartharena.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.aprz.heartharena.R;
import com.aprz.heartharena.activity.CardListActivity;
import com.aprz.heartharena.adapter.CardRarityAdapter;
import com.aprz.heartharena.app.App;
import com.aprz.heartharena.bean.Card;
import com.aprz.heartharena.config.CardConfig;
import com.aprz.heartharena.db.DBManager;

import java.util.ArrayList;
import java.util.concurrent.Executors;

/**
 * Created by aprz on 17-8-4.
 *
 * 卡牌稀有度界面
 * 一个四个级别 每个都是一个 card
 */

public class ProfessionFragment extends BaseFragment {

    private CardRarityAdapter mCommonAdapter, mRareAdapter, mEpicAdapter, mLegendAdapter;
    private MyCard mCommonCard = new MyCard();
    private MyCard mRareCard = new MyCard();
    private MyCard mEpicCard = new MyCard();
    private MyCard mLegendCard = new MyCard();

    private String mProfession;

    public static ProfessionFragment newInstance(String profession) {
        ProfessionFragment fragment = new ProfessionFragment();
        Bundle bundle = new Bundle();
        bundle.putString("profession", profession);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mProfession = getArguments().getString(CardConfig.KEY_PROFESSION);
    }

    @Override
    public int getContentLayoutResId() {
        return R.layout.fragment_profession;
    }

    @Override
    public void initNetWorkErrorView(View netWorkErrorView, Bundle savedInstanceState) {

    }

    @Override
    public void initContentView(View contentView, Bundle savedInstanceState) {
        initCard(contentView, R.id.cardView_common, mCommonCard);
        initCard(contentView, R.id.cardView_rare, mRareCard);
        initCard(contentView, R.id.cardView_epic, mEpicCard);
        initCard(contentView, R.id.cardView_legend, mLegendCard);

        mCommonCard.cardRarity.setText(App.getInstance().getString(R.string.common));
        mRareCard.cardRarity.setText(App.getInstance().getString(R.string.rare));
        mEpicCard.cardRarity.setText(App.getInstance().getString(R.string.epic));
        mLegendCard.cardRarity.setText(App.getInstance().getString(R.string.legend));

        mCommonCard.more.setOnClickListener(new OnMoreClickListenerImpl(R.id.cardView_common));
        mRareCard.more.setOnClickListener(new OnMoreClickListenerImpl(R.id.cardView_rare));
        mEpicCard.more.setOnClickListener(new OnMoreClickListenerImpl(R.id.cardView_epic));
        mLegendCard.more.setOnClickListener(new OnMoreClickListenerImpl(R.id.cardView_legend));

        mCommonAdapter = new CardRarityAdapter();
        mRareAdapter = new CardRarityAdapter();
        mEpicAdapter = new CardRarityAdapter();
        mLegendAdapter = new CardRarityAdapter();

        mCommonCard.cardList.setLayoutManager(
                new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        mRareCard.cardList.setLayoutManager(
                new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        mEpicCard.cardList.setLayoutManager(
                new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        mLegendCard.cardList.setLayoutManager(
                new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        mCommonCard.cardList.setAdapter(mCommonAdapter);
        mRareCard.cardList.setAdapter(mRareAdapter);
        mEpicCard.cardList.setAdapter(mEpicAdapter);
        mLegendCard.cardList.setAdapter(mLegendAdapter);
    }

    @Override
    protected void startLoadData() {
        loadCardData();
    }

    private void initCard(View root, int cardId, MyCard myCard) {
        View card = root.findViewById(cardId);
        myCard.cardRarity = (TextView) card.findViewById(R.id.card_rarity_text);
        myCard.more = (TextView) card.findViewById(R.id.more);
        myCard.cardList = (RecyclerView) card.findViewById(R.id.cards);
    }

    private void loadCardData() {
        showLoadingView();
        Executors.newSingleThreadExecutor().execute(() -> {
            final ArrayList<Card> commonData = DBManager.getCards(getActivity(), mProfession,
                    CardConfig.COMMON, CardConfig.HORIZONTAL_RARITY_COUNT);
            final ArrayList<Card> rareData = DBManager.getCards(getActivity(), mProfession,
                    CardConfig.RARE, CardConfig.HORIZONTAL_RARITY_COUNT);
            final ArrayList<Card> epicData = DBManager.getCards(getActivity(), mProfession,
                    CardConfig.EPIC, CardConfig.HORIZONTAL_RARITY_COUNT);
            final ArrayList<Card> legendData = DBManager.getCards(getActivity(), mProfession,
                    CardConfig.LEGEND, CardConfig.HORIZONTAL_RARITY_COUNT);

            new Handler(Looper.getMainLooper()).postAtFrontOfQueue(() -> {
                mCommonAdapter.initData(commonData);
                mRareAdapter.initData(rareData);
                mEpicAdapter.initData(epicData);
                mLegendAdapter.initData(legendData);
                showContentView();
            });
        });
    }

    private class OnMoreClickListenerImpl implements View.OnClickListener {

        private int cardId;

        OnMoreClickListenerImpl(int cardId) {
            this.cardId = cardId;
        }

        @Override
        public void onClick(View v) {
            switch (cardId) {
                case R.id.cardView_common:
                    CardListActivity.launch(getActivity(), CardConfig.COMMON, mProfession);
                    break;
                case R.id.cardView_rare:
                    CardListActivity.launch(getActivity(), CardConfig.RARE, mProfession);
                    break;
                case R.id.cardView_epic:
                    CardListActivity.launch(getActivity(), CardConfig.EPIC, mProfession);
                    break;
                case R.id.cardView_legend:
                    CardListActivity.launch(getActivity(), CardConfig.LEGEND, mProfession);
                    break;
            }
        }
    }

    private static class MyCard {
        TextView cardRarity;
        TextView more;
        RecyclerView cardList;
    }

}
