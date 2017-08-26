package com.hdy.goldhe.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

/**
 * Created by hdy on 2017/5/23.
 */

public class Fragmentadapter extends FragmentPagerAdapter {
    private Fragment[] fragments;
    private FragmentManager fragmentManager;
    public Fragmentadapter(FragmentManager fm, Fragment[] fragment) {
        super(fm);
        this.fragments=fragment;
        this.fragmentManager=fm;
    }
    @Override

    public Fragment getItem(int position) {
        return fragments[position];
    }

    @Override

    public int getCount() {
        return fragments.length;
    }
    @Override
    public CharSequence getPageTitle(int position) {
        return super.getPageTitle(position);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment)super.instantiateItem(container,position);
        fragmentManager.beginTransaction().show(fragment).commit();
        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        //                super.destroyItem(container, position, object);
        Fragment fragment =fragments[position];
        fragmentManager.beginTransaction().hide(fragment).commit();
    }

}