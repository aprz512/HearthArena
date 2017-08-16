package com.aprz.heartharena.fragment;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.aprz.heartharena.R;
import com.aprz.heartharena.adapter.CardItemDecoration;
import com.aprz.heartharena.adapter.CardListAdapter;
import com.aprz.heartharena.bean.CardTable;
import com.aprz.heartharena.config.CardConfig;

/**
 * Created by aprz on 17-8-4.
 * 卡牌列表 界面
 */

public class CardListFragment extends BaseFragment implements
        LoaderManager.LoaderCallbacks<Cursor> {

    private CardListAdapter mAdapter;

    public static CardListFragment newInstance(String rarity, String profession) {
        CardListFragment fragment = new CardListFragment();
        Bundle bundle = new Bundle();
        bundle.putString(CardConfig.KEY_RARITY, rarity);
        bundle.putString(CardConfig.KEY_PROFESSION, profession);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getContentLayoutResId() {
        return R.layout.fragment_content_list;
    }

    static final String[] CARD_SUMMARY_PROJECTION = new String[] {
            CardTable.ID,
            CardTable.NAME,
            CardTable.IMAGE,
            CardTable.SCORE,
            CardTable.SCORE_LEVEL,
            CardTable.RARITY,
            CardTable.PROFESSION,
            CardTable.NEW_CARD,
            CardTable.COMMON,
            CardTable.NEW_CARD,
            CardTable.SCORE_INT,
    };

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri baseUri = CardTable.CONTENT_URI;
        String selection = CardTable.PROFESSION + " = ?" + "AND rarity LIKE ?";

        String rarity = args.getString(CardConfig.KEY_RARITY);
        String profession = args.getString(CardConfig.KEY_PROFESSION);

        return new CursorLoader(getActivity(), baseUri,
                CARD_SUMMARY_PROJECTION, selection, new String[]{profession, "%" + rarity + "%"}, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
//        mAdapter.swapCursor(data);
        showContentView();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
//        mAdapter.swapCursor(null);
    }

    @Override
    public void initNetWorkErrorView(View netWorkErrorView, Bundle savedInstanceState) {

    }

    @Override
    public void initContentView(View contentView, Bundle savedInstanceState) {
        RecyclerView cardList = (RecyclerView) contentView.findViewById(R.id.rv_card_list);
        cardList.addItemDecoration(new CardItemDecoration());
        cardList.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new CardListAdapter();
        cardList.setAdapter(mAdapter);
    }

    @Override
    protected void finishInflateView(View view, Bundle savedInstanceState) {
        super.finishInflateView(view, savedInstanceState);
        // 强制出发懒加载事件
        // 如果这样写了  就不好在 ViewPager 里面重用了
        onVisible();
    }

    @Override
    protected void startLoadData() {
        getLoaderManager().initLoader(0, getArguments(), this);
        showLoadingView();
    }
}
