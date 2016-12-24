package com.fomdeveloper.planket;

import com.fomdeveloper.planket.data.model.transportmodel.StatusResponse;
import com.fomdeveloper.planket.data.repository.FlickrRepository;
import com.fomdeveloper.planket.ui.presentation.photodetail.PhotoDetailPresenter;
import com.fomdeveloper.planket.ui.presentation.photodetail.PhotoDetailView;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import rx.Single;
import rx.schedulers.Schedulers;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Fernando on 22/10/2016.
 */

public class PhotoDetailPresenterTest {

    private static final String A_PHOTO_ID = "photo_id";

    @Mock
    private FlickrRepository mockFlickrRepository;
    @Mock
    private PhotoDetailView mockView;

    private PhotoDetailPresenter photoDetailPresenter; //sut

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
        photoDetailPresenter = new PhotoDetailPresenter(mockFlickrRepository, Schedulers.immediate(), Schedulers.immediate());
        photoDetailPresenter.attachView(mockView);
    }

    @Test
    public void setPhotoFavoriteShowsSuccessful1() {
        StatusResponse mockStatusResponse = mock(StatusResponse.class);
        when(mockStatusResponse.getStatus()).thenReturn(StatusResponse.STATE_OK);
        when(mockFlickrRepository.addPhotoFavorite(A_PHOTO_ID)).thenReturn(Single.just(mockStatusResponse));

        photoDetailPresenter.setPhotoFavorite(A_PHOTO_ID);

        verify(mockFlickrRepository).saveUserFavPhoto(A_PHOTO_ID);
        verify(mockView).showPhotoFavoriteSuccessful();
        verify(mockView,never()).showPhotoFavoriteError(anyString());
    }

    @Test
    public void setPhotoFavoriteShowsSuccessful2() {
        StatusResponse mockStatusResponse = mock(StatusResponse.class);
        when(mockStatusResponse.getStatus()).thenReturn("STATE_ERROR");
        when(mockStatusResponse.getCode()).thenReturn(StatusResponse.ERROR_CODE_ALREADY_FAV);

        when(mockFlickrRepository.addPhotoFavorite(A_PHOTO_ID)).thenReturn(Single.just(mockStatusResponse));

        photoDetailPresenter.setPhotoFavorite(A_PHOTO_ID);

        verify(mockFlickrRepository).saveUserFavPhoto(A_PHOTO_ID);
        verify(mockView).showPhotoFavoriteSuccessful();
        verify(mockView,never()).showPhotoFavoriteError(anyString());
    }

    @Test
    public void setPhotoFavoriteShowsErrorPermissions() {
        StatusResponse mockStatusResponse = mock(StatusResponse.class);
        when(mockStatusResponse.getStatus()).thenReturn("STATE_ERROR");
        when(mockStatusResponse.getCode()).thenReturn(StatusResponse.ERROR_CODE_NO_PERMISSIONS);

        when(mockFlickrRepository.addPhotoFavorite(A_PHOTO_ID)).thenReturn(Single.just(mockStatusResponse));

        photoDetailPresenter.setPhotoFavorite(A_PHOTO_ID);

        verify(mockView,never()).showPhotoFavoriteSuccessful();
        verify(mockView).showPhotoFavoriteError("Please log in to Flickr first.");
    }

    @Test
    public void setPhotoFavoriteShowsUnknownError() {
        StatusResponse mockStatusResponse = mock(StatusResponse.class);
        when(mockStatusResponse.getStatus()).thenReturn("STATE_ERROR");
        when(mockStatusResponse.getCode()).thenReturn(-1);

        when(mockFlickrRepository.addPhotoFavorite(A_PHOTO_ID)).thenReturn(Single.just(mockStatusResponse));

        photoDetailPresenter.setPhotoFavorite(A_PHOTO_ID);

        verify(mockView,never()).showPhotoFavoriteSuccessful();
        verify(mockView).showPhotoFavoriteError("There was an unknown error.");
    }

    @Test
    public void setPhotoFavoriteShowsError() {
        StatusResponse mockStatusResponse = mock(StatusResponse.class);
        when(mockStatusResponse.getStatus()).thenReturn("STATE_ERROR");
        when(mockStatusResponse.getCode()).thenReturn(-1);

        when(mockFlickrRepository.addPhotoFavorite(A_PHOTO_ID)).thenReturn(Single.<StatusResponse>error(new RuntimeException()));

        photoDetailPresenter.setPhotoFavorite(A_PHOTO_ID);

        verify(mockView,never()).showPhotoFavoriteSuccessful();
        verify(mockView).showPhotoFavoriteError(anyString());
    }

    @Test
    public void removePhotoFavoriteShowsSuccessful1() {
        StatusResponse mockStatusResponse = mock(StatusResponse.class);
        when(mockStatusResponse.getStatus()).thenReturn(StatusResponse.STATE_OK);
        when(mockFlickrRepository.removePhotoFavorite(A_PHOTO_ID)).thenReturn(Single.just(mockStatusResponse));

        photoDetailPresenter.removePhotoFavorite(A_PHOTO_ID);

        verify(mockFlickrRepository).deleteUserFavPhoto(A_PHOTO_ID);
        verify(mockView).showRemoveFavoriteSuccessful();
        verify(mockView,never()).showRemoveFavoriteError(anyString());
    }

    @Test
    public void removePhotoFavoriteShowsSuccessful2() {
        StatusResponse mockStatusResponse = mock(StatusResponse.class);
        when(mockStatusResponse.getStatus()).thenReturn("STATE_ERROR");
        when(mockStatusResponse.getCode()).thenReturn(StatusResponse.ERROR_CODE_PHOTO_NOT_FAV);

        when(mockFlickrRepository.removePhotoFavorite(A_PHOTO_ID)).thenReturn(Single.just(mockStatusResponse));

        photoDetailPresenter.removePhotoFavorite(A_PHOTO_ID);

        verify(mockFlickrRepository).deleteUserFavPhoto(A_PHOTO_ID);
        verify(mockView).showRemoveFavoriteSuccessful();
        verify(mockView,never()).showRemoveFavoriteError(anyString());
    }

    @Test
    public void removePhotoFavoriteShowsErrorPermissions() {
        StatusResponse mockStatusResponse = mock(StatusResponse.class);
        when(mockStatusResponse.getStatus()).thenReturn("STATE_ERROR");
        when(mockStatusResponse.getCode()).thenReturn(StatusResponse.ERROR_CODE_NO_PERMISSIONS);

        when(mockFlickrRepository.removePhotoFavorite(A_PHOTO_ID)).thenReturn(Single.just(mockStatusResponse));

        photoDetailPresenter.removePhotoFavorite(A_PHOTO_ID);

        verify(mockView,never()).showRemoveFavoriteSuccessful();
        verify(mockView).showRemoveFavoriteError("Please log in to Flickr first.");
    }

    @Test
    public void removePhotoFavoriteShowsUnknownError() {
        StatusResponse mockStatusResponse = mock(StatusResponse.class);
        when(mockStatusResponse.getStatus()).thenReturn("STATE_ERROR");
        when(mockStatusResponse.getCode()).thenReturn(-1);

        when(mockFlickrRepository.removePhotoFavorite(A_PHOTO_ID)).thenReturn(Single.just(mockStatusResponse));

        photoDetailPresenter.removePhotoFavorite(A_PHOTO_ID);

        verify(mockView,never()).showRemoveFavoriteSuccessful();
        verify(mockView).showRemoveFavoriteError("There was an unknown error.");
    }

    @Test
    public void removePhotoFavoriteShowsError() {
        StatusResponse mockStatusResponse = mock(StatusResponse.class);
        when(mockStatusResponse.getStatus()).thenReturn("STATE_ERROR");
        when(mockStatusResponse.getCode()).thenReturn(-1);

        when(mockFlickrRepository.removePhotoFavorite(A_PHOTO_ID)).thenReturn(Single.<StatusResponse>error(new RuntimeException()));

        photoDetailPresenter.removePhotoFavorite(A_PHOTO_ID);

        verify(mockView,never()).showRemoveFavoriteSuccessful();
        verify(mockView).showRemoveFavoriteError(anyString());
    }

}
