package com.wroclawstudio.weddinggame.fragments.interfaces;

import com.wroclawstudio.weddinggame.models.envioremnt.WorldModel;

public interface GameFragment {

    void showProgress();

    void hideProgress();

    void loadWorld(WorldModel worldModel);
}
