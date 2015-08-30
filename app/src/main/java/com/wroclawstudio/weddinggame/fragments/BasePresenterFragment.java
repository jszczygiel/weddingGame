package com.wroclawstudio.weddinggame.fragments;


import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.v4.app.Fragment;

import com.wroclawstudio.weddinggame.presenters.interfaces.BasePresenter;

public abstract class BasePresenterFragment<T extends BasePresenter> extends Fragment {


    /**
     * instance of presenter
     */
    private T presenter;

    /**
     * @return provides new instance of presenter
     */
    public abstract T initializePresenter();

    /**
     * This function can be overridden to setup presenter. It is being called in onCreate after
     * initializing presenter
     *
     * @param presenter presenter to setup
     */
    @CallSuper
    public void setUpPresenter(T presenter) {
        presenter.setContext(getActivity());
    }


    public T getPresenter() {
        return presenter;
    }

    private void setPresenter() {
        this.presenter = initializePresenter();
    }

    @Override
    @CallSuper
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setPresenter();
        setUpPresenter(presenter);
        getPresenter().onAttach(this);
    }

    @Override
    @CallSuper
    public void onResume() {
        super.onResume();
        getPresenter().onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        getPresenter().onPause();
    }

    @Override
    @CallSuper
    public void onDestroy() {
        super.onDestroy();
        getPresenter().onDetach();
        presenter = null;
    }
}