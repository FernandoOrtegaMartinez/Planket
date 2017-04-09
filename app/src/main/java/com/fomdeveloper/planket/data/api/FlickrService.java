package com.fomdeveloper.planket.data.api;

import com.fomdeveloper.planket.data.model.transportmodel.CommentsResponse;
import com.fomdeveloper.planket.data.model.transportmodel.FavesResponse;
import com.fomdeveloper.planket.data.model.transportmodel.SearchPhotosResponse;
import com.fomdeveloper.planket.data.model.transportmodel.StatusResponse;
import com.fomdeveloper.planket.data.model.transportmodel.UserInfoResponse;

import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;
import rx.Single;

/**
 * Created by Fernando on 09/05/16.
 */
public interface FlickrService {

    String ENDPOINT = "https://api.flickr.com/services/rest/";
    String PARAM_API_KEY = "api_key";
    String PARAM_FORMAT = "format";
    String PARAM_JSONCALLBACK = "nojsoncallback";
    String EXTRAS_URL = "url_s,url_z,url_h,tags,count_faves,count_comments,icon_server,owner_name,date_upload,geo,description";


    @GET("?method=flickr.interestingness.getList&per_page=100&extras="+EXTRAS_URL)
    Single<SearchPhotosResponse> searchInterestingness(@Query("page") int page);

    @GET("?method=flickr.photos.search&per_page=100&extras="+EXTRAS_URL)
    Single<SearchPhotosResponse> searchPhotosWithTag(@Query("tags") String tag, @Query("page") int page);

    @GET("?method=flickr.photos.search&per_page=100&extras="+EXTRAS_URL)
    Single<SearchPhotosResponse> searchPhotosWithText(@Query("text") String text, @Query("page") int page);

    @GET("?method=flickr.people.getPhotos&per_page=100&extras="+EXTRAS_URL)
    Single<SearchPhotosResponse> searchPhotosForUser(@Query("user_id") String userId, @Query("page") int page);

    @GET("?method=flickr.favorites.getList&per_page=100&extras="+EXTRAS_URL)
    Single<SearchPhotosResponse> searchFavesForUser(@Query("user_id") String userId, @Query("page") int page);

    @GET("?method=flickr.people.getPhotos&extras="+EXTRAS_URL)
    Single<SearchPhotosResponse> getUserPhotosForShowcase(@Query("user_id") String userId, @Query("per_page") int count);

    @GET("?method=flickr.favorites.getList&extras="+EXTRAS_URL)
    Single<SearchPhotosResponse> getUserFavesForShowcase(@Query("user_id") String userId, @Query("per_page") int count);

    @GET("?method=flickr.photos.getFavorites&per_page=50")
    Single<FavesResponse> getFaves(@Query("photo_id") String photoId, @Query("page") int page);

    @GET("?method=flickr.photos.comments.getList&per_page=50")
    Single<CommentsResponse> getComments(@Query("photo_id") String photoId, @Query("page") int page);

    @GET("?method=flickr.people.getInfo")
    Single<UserInfoResponse> getUserInfo(@Query("user_id") String userId);

    @GET("?method=flickr.favorites.getList&per_page=500")
    Observable<SearchPhotosResponse> getAllFaves(@Query("user_id") String userId, @Query("page") int page);

    @POST("?method=flickr.favorites.add")
    Single<StatusResponse> addPhotoFavorite(@Query("photo_id") String photoId);

    @POST("?method=flickr.favorites.remove")
    Single<StatusResponse> removePhotoFavorite(@Query("photo_id") String photoId);


}
