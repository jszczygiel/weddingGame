package com.wroclawstudio.weddinggame.presenters;

import com.wroclawstudio.weddinggame.fragments.interfaces.MenuFragment;
import com.wroclawstudio.weddinggame.presenters.interfaces.BasePresenter;

public class MenuPresenterImpl extends BasePresenter<MenuFragment> {

    @Override
    public void onStart() {
        getView().startSong();
    }

    @Override
    public void onStop(){
        getView().stopSong();
    }
}
