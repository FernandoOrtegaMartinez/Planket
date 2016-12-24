package com.fomdeveloper.planket;

import com.fomdeveloper.planket.data.PaginatedDataManager;
import com.fomdeveloper.planket.data.model.PhotoItem;
import com.fomdeveloper.planket.data.model.transportmodel.PhotosContainer;
import com.fomdeveloper.planket.data.repository.FlickrRepository;
import com.fomdeveloper.planket.ui.presentation.searchphotos.SearchView;
import com.fomdeveloper.planket.ui.presentation.searchphotos.SearchPresenter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;

import rx.Single;
import rx.schedulers.Schedulers;

import static org.mockito.Matchers.anyListOf;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Fernando on 03/08/16.
 */
public class SearchPresenterTest {

    private static final int FIRST_PAGE = PaginatedDataManager.FIRST_PAGE_DEFAULT;
    private static final int ANY_PAGE = 5;
    
    @Mock
    private FlickrRepository mockFlickrRepository;
    @Mock
    private PaginatedDataManager mockPaginatedDataManager;
    @Mock
    private SearchView mockView;

    private SearchPresenter searchPresenter; //sut

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
        searchPresenter = new SearchPresenter(mockFlickrRepository,mockPaginatedDataManager, Schedulers.immediate(), Schedulers.immediate());
        searchPresenter.attachView(mockView);
    }


    @Test
    public void loadInterestingnessShowsProgressOnFirstPage() {
        PhotosContainer photosContainer = mock(PhotosContainer.class);
        when(mockPaginatedDataManager.isLoadingFirstPage()).thenReturn(true);
        when(mockPaginatedDataManager.getNextPageToLoad()).thenReturn(FIRST_PAGE);
        when(mockFlickrRepository.getInterestingness(FIRST_PAGE)).thenReturn(Single.just(photosContainer));

        searchPresenter.loadInterestingness();

        verify(mockView).showLoading(true);
        verify(mockView,never()).showNoMorePagesToLoad();
    }

    @Test
    public void loadInterestingnessShowsNoMorePagesToLoad() {
        when(mockPaginatedDataManager.areTotalPagesLoaded()).thenReturn(true);

        searchPresenter.loadInterestingness();

        verify(mockView).showNoMorePagesToLoad();
        verify(mockPaginatedDataManager,never()).getNextPageToLoad();
    }

    @Test
    public void loadInterestingnessShowIntoView(){
        PhotosContainer photosContainer = mock(PhotosContainer.class);
        when(photosContainer.getPhotoItems()).thenReturn(TestDataFactory.makePhotoItems(5));
        when(mockPaginatedDataManager.getNextPageToLoad()).thenReturn(FIRST_PAGE);
        when(mockFlickrRepository.getInterestingness(FIRST_PAGE)).thenReturn(Single.just(photosContainer));

        searchPresenter.loadInterestingness();

        verify(mockFlickrRepository).getInterestingness(eq(FIRST_PAGE));
        verify(mockView).showPhotoItems(photosContainer.getPhotoItems());
        verify(mockView).showLoading(false);
        verify(mockPaginatedDataManager).addPageLoaded();
    }

    @Test
    public void loadInterestingnessShowNoItems(){
        PhotosContainer photosContainer = mock(PhotosContainer.class);
        when(photosContainer.getPhotoItems()).thenReturn(new ArrayList<PhotoItem>());
        when(mockPaginatedDataManager.getNextPageToLoad()).thenReturn(FIRST_PAGE);
        when(mockFlickrRepository.getInterestingness(FIRST_PAGE)).thenReturn(Single.just(photosContainer));

        searchPresenter.loadInterestingness();

        verify(mockFlickrRepository).getInterestingness(eq(FIRST_PAGE));
        verify(mockView).showNoItems(true);
        verify(mockView).showLoading(false);
        verify(mockPaginatedDataManager).addPageLoaded();
    }

    @Test
    public void loadInterestingnessShowErrorRetryScreen() {
        when(mockPaginatedDataManager.getNextPageToLoad()).thenReturn(FIRST_PAGE);
        when(mockPaginatedDataManager.isLoadingFirstPage()).thenReturn(true);
        when(mockFlickrRepository.getInterestingness(FIRST_PAGE))
                .thenReturn(Single.<PhotosContainer>error(new RuntimeException()));

        searchPresenter.loadInterestingness();

        verify(mockFlickrRepository).getInterestingness(eq(FIRST_PAGE));
        verify(mockView, never()).showNoItems(false);
        verify(mockView, never()).showPhotoItems(anyListOf(PhotoItem.class));
        verify(mockView).showSearchError(false);
        verify(mockView).showLoading(false);
    }

    @Test
    public void loadInterestingnessShowErrorLoadingMore() {
        when(mockPaginatedDataManager.getNextPageToLoad()).thenReturn(ANY_PAGE);
        when(mockPaginatedDataManager.isLoadingFirstPage()).thenReturn(true);
        when(mockFlickrRepository.getInterestingness(ANY_PAGE))
                .thenReturn(Single.<PhotosContainer>error(new RuntimeException()));

        searchPresenter.loadInterestingness();

        verify(mockFlickrRepository).getInterestingness(eq(ANY_PAGE));
        verify(mockView).showSearchError(false);
        verify(mockView).showLoading(true);
    }

}
