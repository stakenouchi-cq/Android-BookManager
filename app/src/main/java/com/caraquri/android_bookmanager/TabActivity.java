package com.caraquri.android_bookmanager;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

public class TabActivity extends AppCompatActivity {
    // タブスライド用のパーツ
    private ViewPager pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);

        pager = (ViewPager) findViewById(R.id.viewpager);
        pager.setAdapter(new TabFragmentPagerAdapter(getSupportFragmentManager(), TabActivity.this));
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(pager);
    }
}
