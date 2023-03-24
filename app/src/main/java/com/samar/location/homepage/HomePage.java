package com.samar.location.homepage;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;


import com.google.android.material.tabs.TabLayout;
import com.samar.location.R;

public class HomePage extends AppCompatActivity {

    TabLayout home_tabs;
    ViewPager2 home_viewpager;
    HomeTabs_Adpater homeTabs_adpater;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_home_page);

        home_tabs = findViewById(R.id.my_tablayout);
        home_viewpager=findViewById(R.id.my_viewpager);

        //Tabs are created
        home_tabs.addTab(home_tabs.newTab().setText("LIST"));
        home_tabs.addTab(home_tabs.newTab().setText("MAP"));

        //now we need to create adpater class to provide fragmnet view object
       homeTabs_adpater = new HomeTabs_Adpater(getSupportFragmentManager(),getLifecycle());


       //set adapter in viewpage to get the object of fragment view
        home_viewpager.setAdapter(homeTabs_adpater);

        //click on tabs to show tab

        home_tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                home_viewpager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        //slide horixontal to show tab
        home_viewpager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                home_tabs.selectTab(home_tabs.getTabAt(position));
            }
        });


        //------------------------------------------------//Bottom Naviagation Bar -------------------------------------------------------------------------------------------



    }
}