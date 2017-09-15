package com.dihanov.musiq.ui;

/**
 * Created by Dimitar Dihanov on 15.9.2017 г..
 */

public interface BasePresenter<T> {
    void takeView(T view);

    void leaveView();
}
