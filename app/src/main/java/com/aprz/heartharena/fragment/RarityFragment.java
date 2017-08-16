package com.aprz.heartharena.fragment;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.aprz.heartharena.R;
import com.aprz.heartharena.adapter.CardItemDecoration;
import com.aprz.heartharena.adapter.CardListAdapter;
import com.aprz.heartharena.bean.CardTable;
import com.aprz.heartharena.config.CardConfig;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;


/**
 * Created by aprz on 17-8-16.
 * email: lyldalek@gmail.com
 * desc:
 */

public class RarityFragment extends BaseFragment implements
        LoaderManager.LoaderCallbacks<Cursor>,
        SwipyRefreshLayout.OnRefreshListener{

    private CardListAdapter mAdapter;
    private RecyclerView mRecyclerView;

    SwipyRefreshLayout mRefreshLayout;

    private int mCurrentPage = 0;
    private final int COUNT = 10;

    public static RarityFragment newInstance(String rarity, String profession) {
        RarityFragment fragment = new RarityFragment();
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
        baseUri = baseUri.buildUpon()
                .appendQueryParameter("limit", String.valueOf(mCurrentPage * COUNT))
                .appendQueryParameter("offset", String.valueOf(COUNT))
                .build();
        String selection = CardTable.PROFESSION + " = ? "
                + "AND rarity LIKE ?" ;

        String rarity = args.getString(CardConfig.KEY_RARITY);
        String profession = args.getString(CardConfig.KEY_PROFESSION);

        return new CursorLoader(getActivity(), baseUri,
                CARD_SUMMARY_PROJECTION, selection,
                new String[]{profession, "%" + rarity + "%"},
                CardTable.DEFAULT_SORT_ORDER);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        showContentView();
        mRefreshLayout.setRefreshing(false);
        if (mCurrentPage == 0) {
            mAdapter.swapCursor(data, false);
        } else {
            mAdapter.swapCursor(data, true);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null, false);
    }

    @Override
    public void initNetWorkErrorView(View netWorkErrorView, Bundle savedInstanceState) {

    }

    @Override
    public void initContentView(View contentView, Bundle savedInstanceState) {
        mRecyclerView = (RecyclerView) contentView.findViewById(R.id.rv_card_list);
        mRecyclerView.addItemDecoration(new CardItemDecoration());
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(manager);
        mAdapter = new CardListAdapter();
        mRecyclerView.setAdapter(mAdapter);
        mRefreshLayout = (SwipyRefreshLayout) contentView.findViewById(R.id.swipy_refreshlayout);
        mRefreshLayout.setColorSchemeColors(ActivityCompat.getColor(getActivity(), R.color.colorAccent));
        mRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    protected void startLoadData() {
        Loader<Object> loader = getLoaderManager().getLoader(0);
        if (loader != null) {
            getLoaderManager().restartLoader(0, getArguments(), this);
            mRefreshLayout.setRefreshing(true);
        } else {
            getLoaderManager().initLoader(0, getArguments(), this);
            showLoadingView();
        }
    }


    @Override
    public void onRefresh(SwipyRefreshLayoutDirection direction) {
        if (direction == SwipyRefreshLayoutDirection.TOP) {
            mCurrentPage = 0;
        } else if (direction == SwipyRefreshLayoutDirection.BOTTOM) {
            mCurrentPage++;
        }
        startLoadData();
    }
}


