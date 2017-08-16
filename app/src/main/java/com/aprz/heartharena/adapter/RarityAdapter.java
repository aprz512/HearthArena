package com.aprz.heartharena.adapter;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.support.v13.app.FragmentStatePagerAdapter;

import com.aprz.heartharena.R;
import com.aprz.heartharena.fragment.RarityFragment;

import java.util.Arrays;
import java.util.List;

/**
 * Created by aprz on 17-8-10.
 * email: lyldalek@gmail.com
 * desc:
 */

public class RarityAdapter extends FragmentStatePagerAdapter {

    private final String[] TITLES;
    private Fragment[] mFragments;

    private String mProfession;

    public RarityAdapter(FragmentManager fm, Context context, String profession) {
        super(fm);
        TITLES = context.getResources().getStringArray(R.array.rarities);
        mFragments = new Fragment[TITLES.length];
        this.mProfession = profession;
    }

    @Override
    public Fragment getItem(int position) {

        if (mFragments[position] != null) {
            return mFragments[position];
        }

        switch (position) {
            case 0:
            case 1:
            case 2:
            case 3:
                mFragments[position] = RarityFragment.newInstance(TITLES[position], mProfession);
                break;
            default:
                break;
        }

        return mFragments[position];
    }

    @Override
    public int getCount() {
        return TITLES.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return TITLES[position];
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    public void resetAdapter(String profession) {
        this.mProfession = profession;
        for (int i=0; i< TITLES.length; i++) {
            mFragments[i] = null;
        }
        notifyDataSetChanged();
    }

    public int getItemPosition(String profession) {
        List<String> professions = Arrays.asList(TITLES);
        int index = professions.indexOf(profession);
        if (index < 0) {
            index = 0;
        }
        return index;
    }
}
