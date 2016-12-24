package com.fomdeveloper.planket.ui.presentation.main;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fomdeveloper.planket.PlanketApplication;
import com.fomdeveloper.planket.NetworkManager;
import com.fomdeveloper.planket.R;
import com.fomdeveloper.planket.data.PlanketDatabase;
import com.fomdeveloper.planket.data.prefs.UserHelper;
import com.fomdeveloper.planket.ui.presentation.about.AboutFragment;
import com.fomdeveloper.planket.ui.presentation.login.FlickrLoginActivity;
import com.fomdeveloper.planket.ui.presentation.profile.ProfileActivity;
import com.fomdeveloper.planket.ui.presentation.searchphotos.SearchFragment;
import com.fomdeveloper.planket.ui.presentation.searchphotos.SearchActivity;
import com.fomdeveloper.planket.ui.presentation.searchphotos.SearchType;
import com.fomdeveloper.planket.ui.utils.DialogFactory;
import com.fomdeveloper.planket.ui.view.CircleTransformation;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import butterknife.BindDimen;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity implements MainView, ToolbarInterface, SearchView.OnQueryTextListener {

    @Inject MainPresenter presenter;
    @Inject UserHelper userHelper;
    @Inject Picasso picasso;
    @Inject NetworkManager networkManager;
    @Inject
    PlanketDatabase planketDatabase;

    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.navigation_view)
    NavigationView navigationView;
    @BindDimen(R.dimen.profile_pic_size_big)
    int profilePicSize;

    private MenuItem searchItem;

//    NavigationView header views
    private View headerView;
    private View noLoggedInLayout;
    private View loggedInLayout;
    private TextView userNameTextView;
    private ImageView profilePic;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((PlanketApplication)getApplication()).getAppComponent().inject(this);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        presenter.attachView(this);

        getHeaderViews();
        setUpNavDrawer();

        if (userHelper.isUserLoggedIn()){
            presenter.loadUserFavesFromDB();
        }

        showInterestingnessPage();
    }

    @Override
    protected void onDestroy() {
        planketDatabase.close();
        presenter.detachView();
        super.onDestroy();
    }

    private void setUpNavDrawer() {

        navigationView.setCheckedItem(R.id.action_interestingness);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setChecked(true);
                switch (menuItem.getItemId()) {

                    case R.id.action_interestingness: {
                        showInterestingnessPage();
                        break;
                    }
                    case R.id.action_about: {
                        showAboutPage();
                        break;
                    }
                    case R.id.action_login:
                        flickrUserAction();
                        menuItem.setChecked(false);
                        break;
                    default:break;

                }
                drawerLayout.closeDrawers();
                return true;
            }


        });

        updateUserViews();
    }


    @Override
    public void setToolbar(Toolbar toolbar) {
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
    }

    private void showInterestingnessPage(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.findFragmentByTag(SearchFragment.TAG) == null) {
            FragmentTransaction ft = fragmentManager.beginTransaction();
            SearchFragment searchFragment = SearchFragment.newInstance(null,SearchType.INTERESTINGNESS);
            ft.replace(R.id.main_container, searchFragment, SearchFragment.TAG);
            ft.commit();
        }
    }

    private void showAboutPage(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.findFragmentByTag(AboutFragment.TAG) == null) {
            FragmentTransaction ft = fragmentManager.beginTransaction();
            AboutFragment aboutFragment = AboutFragment.newInstance();
            ft.replace(R.id.main_container, aboutFragment, AboutFragment.TAG);
            ft.commit();
        }
    }

    private void flickrUserAction(){
        if (userHelper.isUserLoggedIn()) {
            userHelper.logoutUser();
            updateUserViews();
            Toast.makeText(this, R.string.logout_success_message,Toast.LENGTH_LONG).show();
        } else {
            FlickrLoginActivity.start(MainActivity.this);
        }
    }

    @Override
    public void updateUserProfilePic(String UserProfilePicUrl) {
        picasso.load(UserProfilePicUrl)
                .resize(profilePicSize, profilePicSize)
                .centerCrop()
                .transform(new CircleTransformation())
                .placeholder(R.drawable.profile)
                .into(profilePic);
    }

    @Override
    public void updateUserViews(){
        if (navigationView!=null) {
            MenuItem loginItem = navigationView.getMenu().findItem(R.id.action_login);
            if (loginItem != null) {
                if (userHelper.isUserLoggedIn()) {
                    loginItem.setTitle(R.string.drawer_action_logout);
                    noLoggedInLayout.setVisibility(View.GONE);
                    loggedInLayout.setVisibility(View.VISIBLE);
                    userNameTextView.setText(userHelper.getUserName());
                    headerView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ProfileActivity.start(MainActivity.this,userHelper.getUserId());
                            drawerLayout.closeDrawers();
                        }
                    });
                    if (userHelper.getUserProfilePicUrl()!=null){
                        updateUserProfilePic(userHelper.getUserProfilePicUrl());
                    }
                } else {
                    loginItem.setTitle(R.string.drawer_action_login);
                    loggedInLayout.setVisibility(View.GONE);
                    noLoggedInLayout.setVisibility(View.VISIBLE);
                    headerView.setOnClickListener(null);
                }
            }
        }
    }

    private void getHeaderViews(){
        headerView = navigationView.getHeaderView(0);
        noLoggedInLayout = headerView.findViewById(R.id.no_logged_in_layout);
        loggedInLayout = headerView.findViewById(R.id.logged_in_layout);
        userNameTextView = (TextView) headerView.findViewById(R.id.user_name);
        profilePic = (ImageView) headerView.findViewById(R.id.profile_pic);
        noLoggedInLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FlickrLoginActivity.start(MainActivity.this);
                drawerLayout.closeDrawers();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);

        searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setQueryHint(getString(R.string.search_hint));
        TextView et = (TextView) searchView.findViewById(R.id.search_src_text);
        final int MAX_LENGTH = 50;
        et.setFilters(new InputFilter[]{new InputFilter.LengthFilter(MAX_LENGTH)});
        searchView.setOnQueryTextListener(this);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        searchItem.collapseActionView();
        if (networkManager.isConnectedToInternet()) {
            SearchActivity.start(MainActivity.this, query, SearchType.TEXT);
        }else{
            DialogFactory.createOkDialog(this,getString(R.string.oh_no_title), getString(R.string.no_internet)).show();
        }
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }
}