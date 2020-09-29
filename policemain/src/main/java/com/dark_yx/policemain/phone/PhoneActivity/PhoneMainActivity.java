package com.dark_yx.policemain.phone.PhoneActivity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.dark_yx.policemain.phone.PhoneFragment.FragmentA;
import com.dark_yx.policemain.phone.PhoneFragment.FragmentB;
import com.dark_yx.policemain.phone.PhoneFragment.FragmentC;
import com.dark_yx.policemain.R;
import com.dark_yx.policemain.util.WhiteListUtil;

import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

public class PhoneMainActivity extends FragmentActivity implements RadioGroup.OnCheckedChangeListener {
    private ViewPager viewPager;
    private List<Fragment> fragments;
    private FragmentPagerAdapter adapter;
    private int item = 0;
    private RadioGroup radioGroup;
    private FragmentA fragmentA;
    private FragmentB fragmentB;
    private FragmentC fragmentC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        setContentView(R.layout.activity_phone_main);
        inteView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        new WhiteListUtil(this, true).getData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void inteView() {
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        radioGroup = (RadioGroup) findViewById(R.id.radio_group);
        fragments = new ArrayList<>();
        fragmentA = new FragmentA();
        fragmentB = new FragmentB();
        fragmentC = new FragmentC();
        fragments.add(fragmentA);
        fragments.add(fragmentB);
        fragments.add(fragmentC);
        adapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }
        };
        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int number) {
                ((RadioButton) radioGroup.getChildAt(number)).setChecked(true);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
        radioGroup.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        item = 0;
        switch (i) {
            case R.id.ra_pal:
                item = 1;
                break;
            case R.id.missedphone:
                item = 2;
                break;
        }
        viewPager.setCurrentItem(item);
    }

}
