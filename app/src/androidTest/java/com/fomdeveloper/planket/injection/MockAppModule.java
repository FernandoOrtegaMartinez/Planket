package com.fomdeveloper.planket.injection;


import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;

import com.fomdeveloper.planket.BuildConfig;
import com.fomdeveloper.planket.NetworkManager;
import com.fomdeveloper.planket.bus.RxEventBus;
import com.fomdeveloper.planket.data.PlanketDatabase;
import com.fomdeveloper.planket.data.PaginatedDataManager;
import com.fomdeveloper.planket.data.api.FlickrOauthService;
import com.fomdeveloper.planket.data.api.FlickrService;
import com.fomdeveloper.planket.data.api.oauth.OAuthManager;
import com.fomdeveloper.planket.data.api.oauth.OAuthManagerImpl;
import com.fomdeveloper.planket.data.api.oauth.OAuthToken;
import com.fomdeveloper.planket.data.prefs.PlanketBoxPreferences;
import com.fomdeveloper.planket.data.prefs.UserHelper;
import com.fomdeveloper.planket.data.repository.FlickrRepository;
import com.fomdeveloper.planket.ui.presentation.base.oauth.OauthPresenter;
import com.fomdeveloper.planket.ui.presentation.ego.EgoPresenter;
import com.fomdeveloper.planket.ui.presentation.main.MainPresenter;
import com.fomdeveloper.planket.ui.presentation.photodetail.PhotoDetailPresenter;
import com.fomdeveloper.planket.ui.presentation.profile.ProfilePresenter;
import com.fomdeveloper.planket.ui.presentation.searchphotos.SearchPresenter;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.mockito.Mockito;

import java.io.IOException;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import se.akerfeldt.okhttp.signpost.OkHttpOAuthConsumer;
import se.akerfeldt.okhttp.signpost.SigningInterceptor;

/**
 * Created by Fernando on 24/12/2016.
 */

@Module
public class MockAppModule {


    /************* MOCKS *************/

    @Provides @Singleton
    public UserHelper provideUserHelper(){
        return Mockito.mock(PlanketBoxPreferences.class);
    }

    @Provides @Singleton
    public FlickrRepository provideFlickrRepository(){
        return Mockito.mock(FlickrRepository.class);
    }

    @Provides @Singleton
    public NetworkManager provideNetworkManager(){
        return Mockito.mock(NetworkManager.class);
    }

    /**************************/

    private Application application;

    public MockAppModule(Application application) {
        this.application = application;
    }

    @Provides  @Singleton
    public Context provideContext(){
        return this.application;
    }

    @Provides  @Singleton
    public Gson provideGson(){
        return new Gson();
    }

    @Provides @Singleton
    public ConnectivityManager provideConnectivityManager(){
        return (ConnectivityManager) application.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    @Provides @Singleton
    public PlanketBoxPreferences providePlanketPreferences(Context context, Gson gson){
        return new PlanketBoxPreferences(context,gson);
    }

    @Provides @Singleton
    public PlanketDatabase providePlanketDatabase(Context context){
        return new PlanketDatabase(context);
    }

    @Provides @Singleton @Named("main_thread")
    public Scheduler provideMainScheduler(){
        return AndroidSchedulers.mainThread();
    }

    @Provides @Singleton @Named("io_thread")
    public Scheduler provideIOScheduler(){
        return Schedulers.io();
    }

    @Provides @Singleton
    public RxEventBus provideRxBus(){
        return new RxEventBus();
    }

    @Provides @Singleton
    public Picasso providePicasso(Context context){
        return Picasso.with(context);
    }

    @Provides @Named("non_oauth") @Singleton
    public OkHttpClient provideOkHttpClient(OkHttpOAuthConsumer okHttpOAuthConsumer, PlanketBoxPreferences planketBoxPreferences){

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel( BuildConfig.DEBUG? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);

        Interceptor paramInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                HttpUrl url = request.url().newBuilder()
                        .addQueryParameter(FlickrService.PARAM_API_KEY, BuildConfig.FLICKR_API_KEY)
                        .addQueryParameter(FlickrService.PARAM_FORMAT,"json")
                        .addQueryParameter(FlickrService.PARAM_JSONCALLBACK,"1")
                        .build();

                request = request.newBuilder().url(url).build();

                return chain.proceed(request);
            }
        };

        OkHttpClient.Builder okHttpClientBuilder =  new OkHttpClient.Builder()
                .addInterceptor(paramInterceptor)
                .addInterceptor(loggingInterceptor)
                .addInterceptor(new SigningInterceptor(okHttpOAuthConsumer));

        if (planketBoxPreferences.getAccessToken()!=null){
            OAuthToken oAuthToken = planketBoxPreferences.getAccessToken();
            okHttpOAuthConsumer.setTokenWithSecret(oAuthToken.getToken(),oAuthToken.getTokenSecret());
        }

        return okHttpClientBuilder.build();

    }

    @Provides @Singleton
    public OkHttpOAuthConsumer provideOkHttpOAuthConsumer(){
        return new OkHttpOAuthConsumer(BuildConfig.FLICKR_API_KEY, BuildConfig.FLICKR_CONSUMER_SECRET);
    }

    @Provides @Named("oauth") @Singleton
    public OkHttpClient provideOauthOkHttpClient(OkHttpOAuthConsumer okHttpOAuthConsumer){

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel( BuildConfig.DEBUG? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);

        return new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .addInterceptor(new SigningInterceptor(okHttpOAuthConsumer))
                .build();

    }

    @Provides @Named("non_oauth") @Singleton
    public Retrofit provideRetrofit(@Named("non_oauth") OkHttpClient okHttpClient){
        return new Retrofit.Builder()
                .baseUrl( FlickrService.ENDPOINT )
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(okHttpClient)
                .build();
    }

    @Provides @Named("oauth") @Singleton
    public Retrofit provideOauthRetrofit(@Named("oauth") OkHttpClient okHttpClient){
        return new Retrofit.Builder()
                .baseUrl( FlickrOauthService.ENDPOINT )
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(okHttpClient)
                .build();
    }

    @Provides @Singleton
    public FlickrService provideFlickrService(@Named("non_oauth") Retrofit retrofit){
        return retrofit.create(FlickrService.class);
    }

    @Provides @Singleton
    public FlickrOauthService provideFlickrOauthService(@Named("oauth") Retrofit retrofit){
        return retrofit.create(FlickrOauthService.class);
    }

    @Provides @Singleton
    public OAuthManager provideOAuthManager(FlickrOauthService flickrOauthService,OkHttpOAuthConsumer okHttpOAuthConsumer, PlanketBoxPreferences planketBoxPreferences, Context context){
        return new OAuthManagerImpl(flickrOauthService,okHttpOAuthConsumer, planketBoxPreferences, context);
    }

    @Provides
    public PaginatedDataManager providePaginatedManager(){
        return new PaginatedDataManager();
    }

    @Provides
    public MainPresenter provideMainPresenter(FlickrRepository flickrRepository, PlanketBoxPreferences planketBoxPreferences, RxEventBus rxEventBus, @Named("main_thread") Scheduler mainScheduler, @Named("io_thread") Scheduler ioScheduler){
        return new MainPresenter(flickrRepository, planketBoxPreferences, rxEventBus, mainScheduler, ioScheduler);
    }

    @Provides
    public OauthPresenter provideFlickrLoginPresenter(OAuthManager oAuthManager, @Named("main_thread") Scheduler mainScheduler, @Named("io_thread") Scheduler ioScheduler){
        return new OauthPresenter(oAuthManager, mainScheduler, ioScheduler);
    }

    @Provides
    public SearchPresenter provideSearchPresenter(FlickrRepository flickrRepository, PaginatedDataManager paginatedDataManager, @Named("main_thread") Scheduler mainScheduler, @Named("io_thread") Scheduler ioScheduler){
        return new SearchPresenter(flickrRepository, paginatedDataManager, mainScheduler, ioScheduler);
    }

    @Provides
    public PhotoDetailPresenter providePhotoDetailPresenter(FlickrRepository flickrRepository, @Named("main_thread") Scheduler mainScheduler, @Named("io_thread") Scheduler ioScheduler){
        return new PhotoDetailPresenter(flickrRepository, mainScheduler, ioScheduler);
    }

    @Provides
    public EgoPresenter provideEgoPresenter(FlickrRepository flickrRepository, PaginatedDataManager paginatedDataManager, @Named("main_thread") Scheduler mainScheduler, @Named("io_thread") Scheduler ioScheduler){
        return new EgoPresenter(flickrRepository, paginatedDataManager, mainScheduler, ioScheduler);
    }

    @Provides
    public ProfilePresenter provideProfilePresenter(FlickrRepository flickrRepository, @Named("main_thread") Scheduler mainScheduler, @Named("io_thread") Scheduler ioScheduler){
        return new ProfilePresenter(flickrRepository, mainScheduler, ioScheduler);
    }

}
