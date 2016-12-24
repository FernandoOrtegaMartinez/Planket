package com.fomdeveloper.planket.data;

import android.content.Context;

import com.fomdeveloper.planket.data.model.UserFavePhotoId;


import java.util.HashSet;
import java.util.Set;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

/**
 * Created by Fernando on 08/10/2016.
 */

public class PlanketDatabase {

    private final String USER_ID_FIELD_NAME = "userId";
    private final String PHOTO_ID_FIELD_NAME = "photoId";

    private Context context;
    private RealmConfiguration realmConfiguration;

    public PlanketDatabase(Context context) {
        this.context = context;
        setup();
    }

    private Realm getRealmInstance() {
        return Realm.getDefaultInstance();
    }

    private void setup() {
        if (realmConfiguration == null) {
            Realm.init(context);
            realmConfiguration = new RealmConfiguration.Builder().build();
            Realm.setDefaultConfiguration(realmConfiguration);
        } else {
            throw new IllegalStateException("PlanketDatabase already configured");
        }
    }


    public void deleteUserFav(String userId, final String photoId){
        Realm realm = getRealmInstance();
        final RealmResults<UserFavePhotoId> userFavePhotoIdsResults =
                realm.where(UserFavePhotoId.class)
                .equalTo(USER_ID_FIELD_NAME,userId)
                .equalTo(PHOTO_ID_FIELD_NAME, photoId)
                .findAll();

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                userFavePhotoIdsResults.deleteAllFromRealm();
            }
        });
    }

    public void addUserFav(final String userId, final Set<String> userFavPhotos) {
        Realm realm = getRealmInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                UserFavePhotoId userFavePhotoId;
                for (String photoId : userFavPhotos){
                    if(existInRealm(realm, userId, photoId)) {
                        userFavePhotoId = realm.createObject(UserFavePhotoId.class);
                        userFavePhotoId.userId = userId;
                        userFavePhotoId.photoId = photoId;
                    }
                }
            }
        });
    }

    public void addUserFav(final String userId, final String photoId) {
        Realm realm = getRealmInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                if(existInRealm(realm, userId, photoId)) {
                    UserFavePhotoId userFavePhotoId = realm.createObject(UserFavePhotoId.class);
                    userFavePhotoId.userId = userId;
                    userFavePhotoId.photoId = photoId;
                }
            }
        });
    }

    public Set<String> loadAllUserFaves(String userId) {
        RealmResults<UserFavePhotoId>  userFavePhotoIdsResults = getRealmInstance()
                .where(UserFavePhotoId.class)
                .equalTo(USER_ID_FIELD_NAME, userId)
                .findAll();

        Set<String> userFavPhotos = new HashSet();
        for (UserFavePhotoId userFavePhotoId : userFavePhotoIdsResults){
            userFavPhotos.add(userFavePhotoId.photoId);
        }
        return userFavPhotos;
    }

    public void close() {
        getRealmInstance().close();
    }

    private boolean existInRealm(Realm realm, String userId, String photoId) {
        return realm.where(UserFavePhotoId.class)
                .equalTo(USER_ID_FIELD_NAME, userId)
                .equalTo(PHOTO_ID_FIELD_NAME, photoId)
                .findFirst() == null;
    }

}