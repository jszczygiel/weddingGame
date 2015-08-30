package com.wroclawstudio.weddinggame.activities;

import com.wroclawstudio.weddinggame.fragments.BasePresenterFragment;
import com.wroclawstudio.weddinggame.fragments.MenuFragmentImpl;

public class MenuActivity extends BaseActivity {
    @Override
    public BasePresenterFragment newInstance() {
        return MenuFragmentImpl.newInstance();
    }
}
