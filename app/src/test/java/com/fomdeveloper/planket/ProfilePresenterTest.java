package com.fomdeveloper.planket;

import com.fomdeveloper.planket.data.model.FlickrUser;
import com.fomdeveloper.planket.data.model.PhotoItem;
import com.fomdeveloper.planket.data.model.transportmodel.UserShowcase;
import com.fomdeveloper.planket.data.repository.FlickrRepository;
import com.fomdeveloper.planket.ui.presentation.profile.ProfilePresenter;
import com.fomdeveloper.planket.ui.presentation.profile.ProfileView;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import rx.Single;
import rx.schedulers.Schedulers;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Fernando on 22/10/2016.
 */

public class ProfilePresenterTest {

    private  static final String AN_USER_ID = "user_id";

    @Mock
    private FlickrRepository mockFlickrRepository;
    @Mock
    private ProfileView mockView;

    private ProfilePresenter profilePresenter; //sut

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
        profilePresenter = new ProfilePresenter(mockFlickrRepository, Schedulers.immediate(), Schedulers.immediate());
        profilePresenter.attachView(mockView);
    }

    @Test
    public void loadUserInfoShowsUserDetails() {
        FlickrUser mockFlickrUser = mock(FlickrUser.class);
        when(mockFlickrRepository.getUserInfo(AN_USER_ID)).thenReturn(Single.just(mockFlickrUser));

        profilePresenter.getUserInfo("user_id");

        verify(mockView).showUserDetails(mockFlickrUser);
    }

    @Test
    public void loadUserInfoNotShowUserDetailsWhenNull() {
        FlickrUser mockFlickrUser = null;
        when(mockFlickrRepository.getUserInfo(AN_USER_ID)).thenReturn(Single.just(mockFlickrUser));

        profilePresenter.getUserInfo(AN_USER_ID);

        verify(mockView,never()).showUserDetails(mockFlickrUser);
    }

    @Test
    public void loadUserInfoNotShowUserDetailsWhenError() {

        when(mockFlickrRepository.getUserInfo(AN_USER_ID)).thenReturn(Single.<FlickrUser>error(new RuntimeException()));

        profilePresenter.getUserInfo(AN_USER_ID);

        verify(mockView,never()).showUserDetails(any(FlickrUser.class));
    }

    @Test
    public void loadShowcasesShowsShowcases() {
        when(mockFlickrRepository.getShowcases(AN_USER_ID, 8)).thenReturn(Single.just(TestDataFactory.makeUserShowcase(8,8)));

        profilePresenter.getShowcases(AN_USER_ID,8);

        verify(mockView).showUserPhotos(anyListOf(PhotoItem.class),anyInt());
        verify(mockView).showUserFaves(anyListOf(PhotoItem.class),anyInt());
        verify(mockView,never()).showNoItems();
        verify(mockView).showLoading(false);
    }

    @Test
    public void loadShowcasesShowsOnlyPhotos() {
        when(mockFlickrRepository.getShowcases(AN_USER_ID, 8)).thenReturn(Single.just(TestDataFactory.makeUserShowcase(8,0)));

        profilePresenter.getShowcases(AN_USER_ID,8);

        verify(mockView).showUserPhotos(anyListOf(PhotoItem.class),anyInt());
        verify(mockView,never()).showUserFaves(anyListOf(PhotoItem.class),anyInt());
        verify(mockView,never()).showNoItems();
        verify(mockView).showLoading(false);
    }

    @Test
    public void loadShowcasesShowsOnlyFaves() {
        when(mockFlickrRepository.getShowcases(AN_USER_ID, 8)).thenReturn(Single.just(TestDataFactory.makeUserShowcase(0,8)));

        profilePresenter.getShowcases(AN_USER_ID,8);

        verify(mockView,never()).showUserPhotos(anyListOf(PhotoItem.class),anyInt());
        verify(mockView).showUserFaves(anyListOf(PhotoItem.class),anyInt());
        verify(mockView,never()).showNoItems();
        verify(mockView).showLoading(false);
    }

    @Test
    public void loadShowcasesShowsNoItems() {
        when(mockFlickrRepository.getShowcases(AN_USER_ID, 8)).thenReturn(Single.just(TestDataFactory.makeUserShowcase(0,0)));

        profilePresenter.getShowcases(AN_USER_ID,8);

        verify(mockView,never()).showUserPhotos(anyListOf(PhotoItem.class),anyInt());
        verify(mockView,never()).showUserFaves(anyListOf(PhotoItem.class),anyInt());
        verify(mockView).showNoItems();
        verify(mockView).showLoading(false);
    }

    @Test
    public void loadShowcasesShowsError() {
        when(mockFlickrRepository.getShowcases(AN_USER_ID, 8)).thenReturn(Single.<UserShowcase>error(new RuntimeException()));

        profilePresenter.getShowcases(AN_USER_ID,8);

        verify(mockView,never()).showUserPhotos(anyListOf(PhotoItem.class),anyInt());
        verify(mockView,never()).showUserFaves(anyListOf(PhotoItem.class),anyInt());
        verify(mockView,never()).showNoItems();
        verify(mockView).showLoading(false);
        verify(mockView).showErrorScreen();

    }

}
