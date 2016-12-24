package com.fomdeveloper.planket;

import com.fomdeveloper.planket.data.PaginatedDataManager;
import com.fomdeveloper.planket.data.model.Comment;
import com.fomdeveloper.planket.data.model.Fave;
import com.fomdeveloper.planket.data.model.transportmodel.CommentsContainer;
import com.fomdeveloper.planket.data.model.transportmodel.FavesContainer;
import com.fomdeveloper.planket.data.repository.FlickrRepository;
import com.fomdeveloper.planket.ui.presentation.ego.EgoView;
import com.fomdeveloper.planket.ui.presentation.ego.EgoPresenter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


import rx.Single;
import rx.schedulers.Schedulers;


import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Fernando on 23/07/16.
 */
public class EgoPresenterTest {

    private static final int FIRST_PAGE = PaginatedDataManager.FIRST_PAGE_DEFAULT;
    private static final int ANY_PAGE = 5;
    private static final String ANY_PHOTO_ID = "123";

    @Mock
    private FlickrRepository mockFlickrRepository;
    @Mock
    private PaginatedDataManager mockPaginatedDataManager;
    @Mock
    private EgoView mockView;

    private EgoPresenter egoPresenter; // sut

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
        egoPresenter = new EgoPresenter(mockFlickrRepository, mockPaginatedDataManager, Schedulers.immediate(), Schedulers.immediate());
        egoPresenter.attachView(mockView);
    }

    // Comments

    @Test
    public void loadCommentsShowsProgressOnFirstPage() {
        CommentsContainer commentsContainer = mock(CommentsContainer.class);
        when(mockPaginatedDataManager.isLoadingFirstPage()).thenReturn(true);
        when(mockPaginatedDataManager.getNextPageToLoad()).thenReturn(FIRST_PAGE);
        when(mockFlickrRepository.getComments(ANY_PHOTO_ID,FIRST_PAGE)).thenReturn(Single.just(commentsContainer));

        egoPresenter.loadComments(ANY_PHOTO_ID);

        verify(mockView).showLoading(true);
        verify(mockView,never()).showNoMorePagesToLoad();
    }

    @Test
    public void loadCommentsShowsNoMorePagesToLoad() {
        when(mockPaginatedDataManager.areTotalPagesLoaded()).thenReturn(true);

        egoPresenter.loadComments(ANY_PHOTO_ID);

        verify(mockView).showNoMorePagesToLoad();
        verify(mockPaginatedDataManager,never()).getNextPageToLoad();
    }


    @Test
    public void loadCommentsShowIntoView() {
        CommentsContainer commentsContainer = mock(CommentsContainer.class);
        when(commentsContainer.getComments()).thenReturn(TestDataFactory.makeComments(5));
        when(mockPaginatedDataManager.getNextPageToLoad()).thenReturn(FIRST_PAGE);
        when(mockFlickrRepository.getComments(ANY_PHOTO_ID,FIRST_PAGE)).thenReturn(Single.just(commentsContainer));

        egoPresenter.loadComments(ANY_PHOTO_ID);

        verify(mockFlickrRepository).getComments(eq(ANY_PHOTO_ID), eq(FIRST_PAGE));
        verify(mockView).showLoading(false);
        verify(mockPaginatedDataManager).addPageLoaded();
        verify(mockPaginatedDataManager).setTotalPages(anyInt());
        verify(mockView).showEgo(commentsContainer.getComments());
        verify(mockView, never()).showNoEgo();
    }

    @Test
    public void loadCommentsShowErrorRetryScreen() {
        when(mockPaginatedDataManager.getNextPageToLoad()).thenReturn(FIRST_PAGE);
        when(mockPaginatedDataManager.isLoadingFirstPage()).thenReturn(true);
        when(mockFlickrRepository.getComments(ANY_PHOTO_ID, FIRST_PAGE))
                .thenReturn(Single.<CommentsContainer>error(new RuntimeException()));

        egoPresenter.loadComments(ANY_PHOTO_ID);

        verify(mockFlickrRepository).getComments(eq(ANY_PHOTO_ID),eq(FIRST_PAGE));
        verify(mockView, never()).showEgo(anyListOf(Comment.class));
        verify(mockView, never()).showNoEgo();
        verify(mockView).showLoading(false);
        verify(mockView).showErrorScreen(false);
    }

    @Test
    public void loadCommentsShowErrorLoadingMore() {
        when(mockPaginatedDataManager.getNextPageToLoad()).thenReturn(ANY_PAGE);
        when(mockFlickrRepository.getComments(ANY_PHOTO_ID, ANY_PAGE))
                .thenReturn(Single.<CommentsContainer>error(new RuntimeException()));

        egoPresenter.loadComments(ANY_PHOTO_ID);

        verify(mockFlickrRepository).getComments(eq(ANY_PHOTO_ID),eq(ANY_PAGE));
        verify(mockView, never()).showEgo(anyListOf(Comment.class));
        verify(mockView, never()).showNoEgo();
        verify(mockView).showLoading(false);
        verify(mockView).showErrorScreen(true);
    }

    // Faves

    @Test
    public void loadFavesShowsProgressOnFirstPage() {
        FavesContainer favesContainer = mock(FavesContainer.class);
        when(mockPaginatedDataManager.isLoadingFirstPage()).thenReturn(true);
        when(mockPaginatedDataManager.getNextPageToLoad()).thenReturn(FIRST_PAGE);
        when(mockFlickrRepository.getFaves(ANY_PHOTO_ID,FIRST_PAGE)).thenReturn(Single.just(favesContainer));

        egoPresenter.loadFaves(ANY_PHOTO_ID);

        verify(mockView).showLoading(true);
        verify(mockView,never()).showNoMorePagesToLoad();
    }

    @Test
    public void loadFavesShowsNoMorePagesToLoad() {
        when(mockPaginatedDataManager.areTotalPagesLoaded()).thenReturn(true);

        egoPresenter.loadFaves(ANY_PHOTO_ID);

        verify(mockView).showNoMorePagesToLoad();
        verify(mockPaginatedDataManager,never()).getNextPageToLoad();
    }


    @Test
    public void loadFavesShowIntoView() {
        FavesContainer favesContainer = mock(FavesContainer.class);
        when(favesContainer.getFaves()).thenReturn(TestDataFactory.makeFaves(5));
        when(mockPaginatedDataManager.getNextPageToLoad()).thenReturn(FIRST_PAGE);
        when(mockFlickrRepository.getFaves(ANY_PHOTO_ID,FIRST_PAGE)).thenReturn(Single.just(favesContainer));

        egoPresenter.loadFaves(ANY_PHOTO_ID);

        verify(mockFlickrRepository).getFaves(eq(ANY_PHOTO_ID), eq(FIRST_PAGE));
        verify(mockView).showEgo(favesContainer.getFaves());
        verify(mockView).showLoading(false);
        verify(mockPaginatedDataManager).addPageLoaded();
        verify(mockPaginatedDataManager).setTotalPages(anyInt());
        verify(mockView, never()).showNoEgo();
    }

    @Test
    public void loadFavesShowErrorRetryScreen() {
        when(mockPaginatedDataManager.getNextPageToLoad()).thenReturn(FIRST_PAGE);
        when(mockPaginatedDataManager.isLoadingFirstPage()).thenReturn(true);
        when(mockFlickrRepository.getFaves(ANY_PHOTO_ID, FIRST_PAGE))
                .thenReturn(Single.<FavesContainer>error(new RuntimeException()));

        egoPresenter.loadFaves(ANY_PHOTO_ID);

        verify(mockFlickrRepository).getFaves(eq(ANY_PHOTO_ID),eq(FIRST_PAGE));
        verify(mockView, never()).showEgo(anyListOf(Fave.class));
        verify(mockView, never()).showNoEgo();
        verify(mockView).showErrorScreen(false);
        verify(mockView).showLoading(false);
    }

    @Test
    public void loadFavesShowErrorLoadingMore() {
        when(mockPaginatedDataManager.getNextPageToLoad()).thenReturn(ANY_PAGE);
        when(mockFlickrRepository.getFaves(ANY_PHOTO_ID, ANY_PAGE))
                .thenReturn(Single.<FavesContainer>error(new RuntimeException()));

        egoPresenter.loadFaves(ANY_PHOTO_ID);

        verify(mockFlickrRepository).getFaves(eq(ANY_PHOTO_ID),eq(ANY_PAGE));
        verify(mockView, never()).showEgo(anyListOf(Fave.class));
        verify(mockView, never()).showNoEgo();
        verify(mockView).showLoading(false);
        verify(mockView).showErrorScreen(true);
    }

}
