package com.aprz.heartharena;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.aprz.heartharena.activity.CardDetailActivity;
import com.aprz.heartharena.adapter.RarityAdapter;
import com.aprz.heartharena.app.App;
import com.aprz.heartharena.db.DBCopier;
import com.aprz.heartharena.db.DBManager;
import com.aprz.heartharena.utils.PermissionUtil;
import com.aprz.heartharena.voice.RecognizerDialogUtil;
import com.aprz.heartharena.voice.listener.OnGetSpeakRecognizerResultListener;
import com.flyco.tablayout.SlidingTabLayout;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends RxAppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, MaterialSearchView.OnVoiceClickListener, OnGetSpeakRecognizerResultListener, AdapterView.OnItemClickListener {

    private static final int REQUEST_WRITE_STORAGE = 1010;
    private static final int REQUEST_RECORD_AUDIO = 1011;

    RarityAdapter mRarityAdapter;

    @BindView(R.id.search_view)
    MaterialSearchView mSearchView;

    @BindView(R.id.view_pager)
    ViewPager mViewPager;

    @BindView(R.id.sliding_tabs)
    SlidingTabLayout mSlidingTabLayout;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    ActionBarDrawerToggle mDrawerToggle;

    DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        // 太牛逼
        // https://stackoverflow.com/questions/26486730/in-android-app-toolbar-settitle-method-has-no-effect-application-name-is-shown
        setSupportActionBar(mToolbar);


        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.getMenu().getItem(0).setChecked(true);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setTitle(navigationView.getMenu().getItem(0).getTitle());
        } else {
            mToolbar.setTitle(navigationView.getMenu().getItem(0).getTitle());
        }

        PermissionUtil.requestPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE, REQUEST_WRITE_STORAGE);

        if (!firstRun()) {
            configView();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // The result of the popup opened with the requestPermissions() method
        // is in that method, you need to check that your application comes here

        if (requestCode == REQUEST_WRITE_STORAGE) {
            if (grantResults.length == 0) {
                Toast.makeText(this, "没有外部储存空间", Toast.LENGTH_LONG).show();
                finish();
                return;
            }
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "不授予该权限会导致图片加载变慢哦", Toast.LENGTH_LONG).show();
            }

            // 請求第二個權限
            PermissionUtil.requestPermission(this,
                    Manifest.permission.RECORD_AUDIO, REQUEST_RECORD_AUDIO);
        } else if (requestCode == REQUEST_RECORD_AUDIO) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "不授予该权限无法使用语音识别功能哦", Toast.LENGTH_LONG).show();
            }
            // 第一次进入显示提示框
            showDialogFirstRun();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (mSearchView.isSearchOpen()) {
                mSearchView.closeSearch();
            } else {
                super.onBackPressed();
            }
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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        switchFragment(item.getTitle().toString());

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    @Override
    public void onPostCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDrawerLayout.removeDrawerListener(mDrawerToggle);
    }

    private void switchFragment(String profession) {
        mToolbar.setTitle(profession);
        mRarityAdapter.resetAdapter(profession);
//        mViewPager.setAdapter(mRarityAdapter);
    }

    private void configView() {
        initViewPager();
        initSearchView();
        switchFragment(App.getInstance().getString(R.string.druid));
    }

    private boolean firstRun() {
        SharedPreferences sp = getSharedPreferences("settings", MODE_PRIVATE);
        return sp.getBoolean("first_run", true);
    }

    private void showDialogFirstRun() {
        if (!firstRun()) {
            return;
        }

        final MaterialDialog dialog = new MaterialDialog.Builder(this)
                .title(R.string.first_run_tips_title)
                .content(R.string.first_run_tips_content)
                .progress(true, 0)
                .cancelable(false)
                .show();
        DBCopier.copy(this, () -> {
            dialog.dismiss();
            configView();
        });
        SharedPreferences sp = getSharedPreferences("settings", MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putBoolean("first_run", false);
        edit.apply();
    }

    private void initViewPager() {
        mRarityAdapter = new RarityAdapter(getFragmentManager(),
                this, mToolbar.getTitle().toString());
        mViewPager.setAdapter(mRarityAdapter);
        mViewPager.setOffscreenPageLimit(4);
        mSlidingTabLayout.setViewPager(mViewPager);
    }

    private void initSearchView() {
        //初始化SearchBar
        mSearchView.showVoice(true);
        mSearchView.setVoiceSearch(true);
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
        mSearchView.setOnVoiceClickListener(this);
        mSearchView.setOnItemClickListener(this);
    }

    @Override
    public void onVoiceClicked() {
        RecognizerDialogUtil util = new RecognizerDialogUtil();
        util.show(this);
        util.setResultListener(this);
    }

    @Override
    public void onGetResult(String result) {
        mSearchView.setSearchText(result);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String profession = mToolbar.getTitle().toString();
        String cardName = (String) parent.getAdapter().getItem(position);
        CardDetailActivity.launch(this, DBManager.getCard(this, cardName, profession));
    }
}
