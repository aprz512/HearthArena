package com.aprz.heartharena.activity;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.aprz.heartharena.R;
import com.aprz.heartharena.config.CardConfig;
import com.aprz.heartharena.db.DBManager;
import com.aprz.heartharena.fragment.CardListFragment;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by aprz on 17-8-8.
 * email: lyldalek@gmail.com
 * desc:
 */

public class CardListActivity extends BaseActivity {

    public static void launch(Activity activity, String rarity, String profession) {
        Intent intent = new Intent(activity, CardListActivity.class);
        intent.putExtra(CardConfig.KEY_RARITY, rarity);
        intent.putExtra(CardConfig.KEY_PROFESSION, profession);
        activity.startActivity(intent);
    }

    private String mRarity, mProfession;

    @BindView(R.id.search_view)
    MaterialSearchView mSearchView;

    @Override
    protected void initIntentArgs(Intent intent) {
        super.initIntentArgs(intent);
        mRarity = intent.getStringExtra(CardConfig.KEY_RARITY);
        mProfession = intent.getStringExtra(CardConfig.KEY_PROFESSION);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_card_list;
    }

    @Override
    protected void configView(Bundle savedInstanceState) {
        mSearchView.setVoiceSearch(false);
        mSearchView.setCursorDrawable(R.drawable.custom_cursor);
        mSearchView.setEllipsize(true);
        ArrayList<String> allCardName = DBManager.getAllCardName(this);
        String[] suggestions = new String[allCardName.size()];
        mSearchView.setSuggestions(allCardName.toArray(suggestions));
        mSearchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    @Override
    protected void finishInflateView() {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_content,
                CardListFragment.newInstance(mRarity, mProfession));
        fragmentTransaction.commit();
    }


    @Override
    public void initToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(mProfession + "-" + mRarity);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        mSearchView.setMenuItem(item);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

}
