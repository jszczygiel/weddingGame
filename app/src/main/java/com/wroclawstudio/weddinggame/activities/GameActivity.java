package com.wroclawstudio.weddinggame.activities;


import android.os.Bundle;

import com.wroclawstudio.weddinggame.fragments.BasePresenterFragment;
import com.wroclawstudio.weddinggame.fragments.GameFragmentImpl;

public class GameActivity extends BaseActivity {


    @Override
    public BasePresenterFragment newInstance() {
        return GameFragmentImpl.newInstance();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().hide();
    }
}
