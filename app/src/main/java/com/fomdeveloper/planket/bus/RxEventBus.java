package com.fomdeveloper.planket.bus;


import rx.Observable;
import rx.functions.Func1;
import rx.subjects.PublishSubject;

/**
 * Created by Fernando on 23/09/16.
 */
public class RxEventBus {

    private final PublishSubject<Object> busSubject;

    public RxEventBus() {
        busSubject = PublishSubject.create();
    }

    public void post(Object event) {
        busSubject.onNext(event);
    }

    public Observable<Object> observable() {
        return busSubject;
    }

    public <T> Observable<T> filteredObservable(final Class<T> eventClass) {
        return busSubject.filter(new Func1<Object, Boolean>() {
            @Override
            public Boolean call(Object event) {
                return eventClass.isInstance(event);
            }
        }).map(new Func1<Object, T>() {
            //Safe to cast because of the previous filter
            @SuppressWarnings("unchecked")
            @Override
            public T call(Object event) {
                return (T) event;
            }
        });
    }
}