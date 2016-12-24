package com.fomdeveloper.planket;

import com.fomdeveloper.planket.data.model.Comment;
import com.fomdeveloper.planket.data.model.Fave;
import com.fomdeveloper.planket.data.model.PhotoItem;
import com.fomdeveloper.planket.data.model.transportmodel.PhotosContainer;
import com.fomdeveloper.planket.data.model.transportmodel.UserShowcase;

import java.util.ArrayList;


/**
 * Created by Fernando on 26/09/16.
 */
class TestDataFactory {

    static ArrayList<PhotoItem> makePhotoItems(int count) {
        ArrayList<PhotoItem> photoItems = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            photoItems.add(new PhotoItem());
        }
        return photoItems;
    }

    static ArrayList<Comment> makeComments(int count) {
        ArrayList<Comment> comments = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            comments.add(new Comment());
        }
        return comments;
    }

    static ArrayList<Fave> makeFaves(int count) {
        ArrayList<Fave> faves = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            faves.add(new Fave());
        }
        return faves;
    }

    static UserShowcase makeUserShowcase(int photosCount, int favesCount) {
        PhotosContainer photosContainer = new PhotosContainer();
        photosContainer.setPhotoItems(makePhotoItems(photosCount));

        PhotosContainer favesContainer = new PhotosContainer();
        favesContainer.setPhotoItems(makePhotoItems(favesCount));

        return new UserShowcase(photosContainer,favesContainer);
    }

}
