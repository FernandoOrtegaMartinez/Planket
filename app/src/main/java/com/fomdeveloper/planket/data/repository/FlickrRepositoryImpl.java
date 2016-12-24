package com.fomdeveloper.planket.data.repository;

import android.content.Context;
import android.support.annotation.NonNull;

import com.fomdeveloper.planket.data.PlanketDatabase;
import com.fomdeveloper.planket.data.api.FlickrService;
import com.fomdeveloper.planket.data.model.FlickrUser;
import com.fomdeveloper.planket.data.model.PhotoItem;
import com.fomdeveloper.planket.data.model.transportmodel.CommentsContainer;
import com.fomdeveloper.planket.data.model.transportmodel.CommentsResponse;
import com.fomdeveloper.planket.data.model.transportmodel.FavesContainer;
import com.fomdeveloper.planket.data.model.transportmodel.FavesResponse;
import com.fomdeveloper.planket.data.model.transportmodel.PhotosContainer;
import com.fomdeveloper.planket.data.model.transportmodel.SearchPhotosResponse;
import com.fomdeveloper.planket.data.model.transportmodel.StatusResponse;
import com.fomdeveloper.planket.data.model.transportmodel.UserInfoResponse;
import com.fomdeveloper.planket.data.model.transportmodel.UserShowcase;
import com.fomdeveloper.planket.data.prefs.PlanketBoxPreferences;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import rx.Observable;
import rx.Single;
import rx.functions.Func1;
import rx.functions.Func2;

/**
 * Created by Fernando on 31/05/16.
 */
public class FlickrRepositoryImpl implements FlickrRepository {

    private FlickrService flickrService;
    private Context context;
    private PlanketDatabase planketDatabase;
    private PlanketBoxPreferences planketBoxPreferences;
    private Set<String> userFavPhotos = new HashSet();

    public FlickrRepositoryImpl(FlickrService flickrService, Context context, PlanketDatabase planketDatabase, PlanketBoxPreferences planketBoxPreferences) {
        this.flickrService = flickrService;
        this.context = context;
        this.planketDatabase = planketDatabase;
        this.planketBoxPreferences = planketBoxPreferences;
    }


    @Override
    public Single<PhotosContainer> getInterestingness(int page) {
        return flickrService.searchInterestingness(page).map(new Func1<SearchPhotosResponse, PhotosContainer>() {
            @Override
            public PhotosContainer call(SearchPhotosResponse searchPhotosResponse) {
                return searchPhotosResponse.getPhotosContainer();
            }
        });
    }

    @Override
    public Single<PhotosContainer> getPhotosWithTag(@NonNull String tag, int page) {
        return flickrService.searchPhotosWithTag(tag, page).map(new Func1<SearchPhotosResponse, PhotosContainer>() {
            @Override
            public PhotosContainer call(SearchPhotosResponse searchPhotosResponse) {
                return searchPhotosResponse.getPhotosContainer();
            }
        });
    }

    @Override
    public Single<PhotosContainer> getPhotosWithText(@NonNull String text, int page) {
        return flickrService.searchPhotosWithText(text, page).map(new Func1<SearchPhotosResponse, PhotosContainer>() {
            @Override
            public PhotosContainer call(SearchPhotosResponse searchPhotosResponse) {
                return searchPhotosResponse.getPhotosContainer();
            }
        });
    }

    @Override
    public Single<PhotosContainer> getPhotosForUser(@NonNull String userId, int page) {
        return flickrService.searchPhotosForUser(userId, page).map(new Func1<SearchPhotosResponse, PhotosContainer>() {
            @Override
            public PhotosContainer call(SearchPhotosResponse searchPhotosResponse) {
                return searchPhotosResponse.getPhotosContainer();
            }
        });
    }

    @Override
    public Single<PhotosContainer> getFavesForUser(@NonNull final String userId, int page) {
        return flickrService.searchFavesForUser(userId, page).map(new Func1<SearchPhotosResponse, PhotosContainer>() {
            @Override
            public PhotosContainer call(SearchPhotosResponse searchPhotosResponse) {
                return searchPhotosResponse.getPhotosContainer();
            }
        });
    }

    @Override
    public Single<UserShowcase> getShowcases(@NonNull final String userId, final int count) {

        Single<PhotosContainer> userPhotosSingle = getUserPhotosForShowcase(userId,count);
        Single<PhotosContainer> userFavesSingle = getUserFavesForShowcase(userId,count);

        return Single.zip(userPhotosSingle, userFavesSingle, new Func2<PhotosContainer, PhotosContainer, UserShowcase>() {
            @Override
            public UserShowcase call(PhotosContainer photosContainer, PhotosContainer favesContainer) {
                return new UserShowcase(photosContainer, favesContainer);
            }
        });

    }

    private Single<PhotosContainer> getUserPhotosForShowcase(@NonNull String userId, int count) {
        return flickrService.getUserPhotosForShowcase(userId, count).map(new Func1<SearchPhotosResponse, PhotosContainer>() {
            @Override
            public PhotosContainer call(SearchPhotosResponse searchPhotosResponse) {
                return searchPhotosResponse.getPhotosContainer();
            }
        });
    }

    private Single<PhotosContainer> getUserFavesForShowcase(@NonNull final String userId, int count) {
        return flickrService.getUserFavesForShowcase(userId, count).map(new Func1<SearchPhotosResponse, PhotosContainer>() {
            @Override
            public PhotosContainer call(SearchPhotosResponse searchPhotosResponse) {
                return searchPhotosResponse.getPhotosContainer();
            }
        });
    }

    @Override
    public Single<FavesContainer> getFaves(@NonNull String photoId, int page) {
        return flickrService.getFaves(photoId, page).map(new Func1<FavesResponse, FavesContainer>() {
            @Override
            public FavesContainer call(FavesResponse favesResponse) {
                return favesResponse.getFavesContainer();
            }
        });
    }

    @Override
    public Single<CommentsContainer> getComments(@NonNull String photoId, int page) {
        return flickrService.getComments(photoId, page).map(new Func1<CommentsResponse, CommentsContainer>() {
            @Override
            public CommentsContainer call(CommentsResponse commentsResponse) {
                return commentsResponse.getCommentsContainer();
            }
        });
    }

    @Override
    public Single<FlickrUser> getUserInfo(@NonNull String userId) {
        return flickrService.getUserInfo(userId).map(new Func1<UserInfoResponse, FlickrUser>() {
            @Override
            public FlickrUser call(UserInfoResponse userInfoResponse) {
                return userInfoResponse.getFlickrUser();
            }
        });
    }

    @Override
    public Single<StatusResponse> addPhotoFavorite(@NonNull final String photoId) {
        return flickrService.addPhotoFavorite(photoId);
    }

    @Override
    public Single<StatusResponse> removePhotoFavorite(@NonNull final String photoId) {
        return flickrService.removePhotoFavorite(photoId);
    }

    @Override
    public Observable<SearchPhotosResponse> getAllFaves(@NonNull final String userId) {
        return Observable.range(1,Integer.MAX_VALUE)
                .concatMap(new Func1<Integer, Observable<SearchPhotosResponse>>() {
                    @Override
                    public Observable<SearchPhotosResponse> call(Integer page) {
                        return flickrService.getAllFaves(userId, page);
                    }
                })
                .takeUntil(new Func1<SearchPhotosResponse, Boolean>() {
                    @Override
                    public Boolean call(SearchPhotosResponse searchPhotosResponse) {
                        return searchPhotosResponse.getPhotosContainer().getPage() >= searchPhotosResponse.getPhotosContainer().getTotalPages();
                    }
                }).reduce(new Func2<SearchPhotosResponse, SearchPhotosResponse, SearchPhotosResponse>() {
                    @Override
                    public SearchPhotosResponse call(SearchPhotosResponse searchPhotosResponse, SearchPhotosResponse searchPhotosResponse2) {
                        searchPhotosResponse.getPhotosContainer().getPhotoItems().addAll(searchPhotosResponse2.getPhotosContainer().getPhotoItems());
                        return searchPhotosResponse;
                    }
                });

    }

    @Override
    public boolean isPhotoFavorite(@NonNull String photoId) {
        if (!planketBoxPreferences.isUserLoggedIn()){
            return false;
        }
        return userFavPhotos.contains(photoId);
    }

    @Override
    public void saveUserFavPhotos(String userId, ArrayList<PhotoItem> photoItems){
        if (planketBoxPreferences.isUserLoggedIn() && planketBoxPreferences.getUserId().equals(userId)) {
            for (PhotoItem photoItem : photoItems) {
                userFavPhotos.add(photoItem.getPhotoId());
            }
            planketDatabase.addUserFav(userId,userFavPhotos);
        }
    }

    @Override
    public void saveUserFavPhoto(String photoId){
        if (!planketBoxPreferences.isUserLoggedIn()) return;
        userFavPhotos.add(photoId);
        String userId = planketBoxPreferences.getUserId();
        planketDatabase.addUserFav(userId,photoId);
    }

    @Override
    public void deleteUserFavPhoto(String photoId){
        if (!planketBoxPreferences.isUserLoggedIn()) return;
        userFavPhotos.remove(photoId);
        String userId = planketBoxPreferences.getUserId();
        planketDatabase.deleteUserFav(userId,photoId);
    }

    @Override
    public void loadUserFaves(){
        if (!planketBoxPreferences.isUserLoggedIn()) return;
        String userId = planketBoxPreferences.getUserId();
        userFavPhotos = planketDatabase.loadAllUserFaves(userId);
    }

}
