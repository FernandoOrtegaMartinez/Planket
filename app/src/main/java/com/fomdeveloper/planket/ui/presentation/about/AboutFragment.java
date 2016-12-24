package com.fomdeveloper.planket.ui.presentation.about;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.fomdeveloper.planket.R;
import com.fomdeveloper.planket.ui.presentation.main.ToolbarInterface;
import com.fomdeveloper.planket.ui.view.widget.HtmlTextView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Fernando on 11/10/2016.
 */

public class AboutFragment extends Fragment{

    public static final String TAG = AboutFragment.class.getSimpleName();

    @Nullable private ToolbarInterface toolbarInterface;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.repository_textview)
    HtmlTextView repositoryTextView;

    public static AboutFragment newInstance() {
        return new AboutFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ToolbarInterface){
            toolbarInterface = (ToolbarInterface) context;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_about,container,false);
        ButterKnife.bind(this,view);
        setupToolbar();
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        repositoryTextView.setHtmlText(getString(R.string.repository));

    }

    private void setupToolbar(){

        setHasOptionsMenu(true);

        if (toolbarInterface != null) {
            toolbarInterface.setToolbar(toolbar);
        }
        final ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar!=null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
        }
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem item=menu.findItem(R.id.action_search);
        item.setVisible(false);
    }

}
