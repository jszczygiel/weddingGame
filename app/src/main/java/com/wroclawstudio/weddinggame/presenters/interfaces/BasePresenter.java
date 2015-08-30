package com.wroclawstudio.weddinggame.presenters.interfaces;

import android.content.Context;
import android.support.annotation.CallSuper;

public abstract class BasePresenter<T> {

    T view;
    private Context context;

    @CallSuper
    public void onAttach(T view) {
        this.view = view;
    }

    @CallSuper
    public void onDetach() {
        this.view = null;
    }

    public void onStart() {
    }

    public void onStop(){

    }

    protected boolean isViewAvailable() {
        return view != null;
    }

    protected T getView() {
        return view;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}