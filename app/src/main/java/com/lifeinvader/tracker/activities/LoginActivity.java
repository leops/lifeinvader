package com.lifeinvader.tracker.activities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.lifeinvader.tracker.R;
import com.lifeinvader.tracker.fragments.LoginFragment;
import com.lifeinvader.tracker.fragments.RegisterFragment;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TabsAdapter adapter = new TabsAdapter(getSupportFragmentManager());

        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(adapter);

        TabLayout tabBar = (TabLayout) findViewById(R.id.tabs);
        tabBar.setupWithViewPager(pager);
    }

    public static class TabsAdapter extends FragmentPagerAdapter {
        public TabsAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch(position) {
                case 0:
                    return "Login";
                default:
                    return "Register";
            }
        }

        @Override
        public Fragment getItem(int position) {
            switch(position) {
                case 0:
                    return new LoginFragment();
                default:
                    return new RegisterFragment();
            }
        }
    }
}

