package com.example.tab_layout;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {

    private ViewPager2 viewPager;

    private PagerAdapter pagerAdapter;

    private frag1_class frag1;

    private frag2_class frag2;

    private frag3_class frag3;

    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        createFragment();

        createViewpager();

        settingTabLayout();

    }



    private void createFragment() {

        frag1 = new frag1_class();

        frag2 = new frag2_class();

        frag3 = new frag3_class();

    }

    private void createViewpager(){

        viewPager = (ViewPager2)findViewById(R.id.viewpager_control);

        pagerAdapter = new PagerAdapter(getSupportFragmentManager(), getLifecycle());

        pagerAdapter.addFragment(frag1);

        pagerAdapter.addFragment(frag2);

        pagerAdapter.addFragment(frag3);

        viewPager.setAdapter(pagerAdapter);

        viewPager.setUserInputEnabled(false); //이 줄을 주석처리 하면 슬라이딩으로 탭을 바꿀 수 있다

    }

    private void settingTabLayout(){

        tabLayout = (TabLayout)findViewById(R.id.tablayout_control);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override

            public void onTabSelected(TabLayout.Tab tab) {

                int pos = tab.getPosition();

                switch(pos){

                    case 0:

                        viewPager.setCurrentItem(0);

                        break;

                    case 1:

                        viewPager.setCurrentItem(1);

                        break;

                    case 2:

                        viewPager.setCurrentItem(2);

                        break;

                }

            }

            @Override

            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override

            public void onTabReselected(TabLayout.Tab tab) {

            }

        });

    }

}