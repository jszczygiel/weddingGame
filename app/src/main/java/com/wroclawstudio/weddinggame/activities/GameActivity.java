package com.wroclawstudio.weddinggame.activities;


import com.wroclawstudio.weddinggame.fragments.BasePresenterFragment;
import com.wroclawstudio.weddinggame.fragments.GameFragmentImpl;

public class GameActivity extends BaseActivity {


    @Override
    public BasePresenterFragment newInstance() {
        return GameFragmentImpl.newInstance();
    }
}
