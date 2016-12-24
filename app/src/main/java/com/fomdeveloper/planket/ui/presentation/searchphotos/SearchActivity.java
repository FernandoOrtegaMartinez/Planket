package com.fomdeveloper.planket.ui.presentation.searchphotos;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.fomdeveloper.planket.R;
import com.fomdeveloper.planket.ui.presentation.main.ToolbarInterface;

/**
 * Created by Fernando on 30/07/16.
 */
public class SearchActivity extends AppCompatActivity implements ToolbarInterface {

    public static final String EXTRA_SEACH_ID = "extra.SEACH_ID";
    public static final String EXTRA_SEARCH_TYPE = "extra.SEARCH_TYPE";
    public static final String EXTRA_TITLE_TYPE = "extra.TITLE_TYPE";

    public static void start(Context context,String searchId, SearchType searchType) {
        Intent starter = new Intent(context, SearchActivity.class);
        starter.putExtra(EXTRA_SEACH_ID,searchId);
        starter.putExtra(EXTRA_SEARCH_TYPE,searchType.name());
        context.startActivity(starter);
    }

    public static void start(Context context,String searchId, String toolbarTitle, SearchType searchType) {
        Intent starter = new Intent(context, SearchActivity.class);
        starter.putExtra(EXTRA_SEACH_ID,searchId);
        starter.putExtra(EXTRA_SEARCH_TYPE,searchType.name());
        starter.putExtra(EXTRA_TITLE_TYPE,toolbarTitle);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_search);
        loadSearchFragment();

    }

    private void loadSearchFragment() {
        FragmentManager fm = getSupportFragmentManager();
        if (fm.findFragmentByTag(SearchFragment.TAG) == null) {
            SearchFragment searchFragment =SearchFragment.newInstance( getIntent().getStringExtra(EXTRA_SEACH_ID), getIntent().getStringExtra(EXTRA_TITLE_TYPE), getIntent().getStringExtra(EXTRA_SEARCH_TYPE) );
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.container, searchFragment, SearchFragment.TAG);
            ft.commit();
        }
    }

    @Override
    public void setToolbar(Toolbar toolbar) {
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home){
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
