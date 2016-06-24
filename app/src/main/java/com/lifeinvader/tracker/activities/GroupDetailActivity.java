package com.lifeinvader.tracker.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.lifeinvader.tracker.R;
import com.lifeinvader.tracker.fragments.GroupDetailFragment;

/**
 * Affiche la liste des membres d'un groupe
 */
public class GroupDetailActivity extends AppCompatActivity {
    private String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_group_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        assert fab != null;

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GroupDetailActivity.this, MapsActivity.class);
                intent.putExtra(GroupDetailFragment.ARG_ITEM, key);

                startActivity(intent);
            }
        });

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if (savedInstanceState == null) {
            key = getIntent().getStringExtra(GroupDetailFragment.ARG_ITEM);

            Bundle arguments = new Bundle();
            arguments.putString(
                GroupDetailFragment.ARG_ITEM,
                key
            );

            GroupDetailFragment fragment = new GroupDetailFragment();
            fragment.setArguments(arguments);

            getSupportFragmentManager().beginTransaction()
                .add(R.id.group_users_container, fragment)
                .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpTo(this, new Intent(this, GroupListActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
