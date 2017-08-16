package com.aprz.heartharena.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.aprz.heartharena.R;
import com.aprz.heartharena.app.App;
import com.aprz.heartharena.bean.Card;
import com.aprz.heartharena.config.CardConfig;
import com.aprz.heartharena.utils.ScoreTextColorUtil;

/**
 * Created by aprz on 17-8-13.
 * email: lyldalek@gmail.com
 * desc:
 */

public class CardDetailActivity extends BaseToolbarActivity {

    public static void launch (Activity activity, Card card) {
        Intent intent = new Intent(activity, CardDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(CardConfig.KEY_CARD, card);
        intent.putExtras(bundle);
        activity.startActivity(intent);
    }

    Card mCard;

    TextView mCardProfession;
    TextView mCardName;
    TextView mCardRarity;
    TextView mCardScore;
    CardView mScoreCard;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void initIntentArgs(Intent intent) {
        super.initIntentArgs(intent);
        mCard = intent.getParcelableExtra(CardConfig.KEY_CARD);

        Bundle bundle = intent.getExtras();
        bundle.getParcelable(CardConfig.KEY_CARD);
        Log.e("aprz", "888888888888");
    }

    @Override
    protected void finishInflateView() {

    }

    @Override
    protected void configView(FrameLayout frameLayout, Bundle savedInstanceState) {
        mCardProfession = (TextView) frameLayout.findViewById(R.id.profession);
        mCardName = (TextView) frameLayout.findViewById(R.id.name);
        mCardRarity = (TextView) frameLayout.findViewById(R.id.rarity);
        mCardScore = (TextView) frameLayout.findViewById(R.id.score);
        mScoreCard = (CardView) frameLayout.findViewById(R.id.score_card);

        String profession = mCard.getProfession();
        if (mCard.getCommon() == 1) {
            profession = App.getInstance().getString(R.string.neutral);
        }
        mCardProfession.setText(profession);
        mCardName.setText(mCard.getName());
        String[] arr = mCard.getRarity().split(" ");
        mCardRarity.setText(arr[0]);
        mCardScore.setText(mCard.getScore());

        int color = ScoreTextColorUtil.getColor(mCard.getScoreLevel());
        mScoreCard.setBackgroundColor(color);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_card_detail;
    }

    @Override
    public void initToolBar(Toolbar toolbar) {
        toolbar.setTitle("卡牌详情");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

}
