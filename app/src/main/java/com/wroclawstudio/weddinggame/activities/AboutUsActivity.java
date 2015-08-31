package com.wroclawstudio.weddinggame.activities;

import com.wroclawstudio.weddinggame.fragments.AboutUsFragmentImpl;
import com.wroclawstudio.weddinggame.fragments.BasePresenterFragment;

public class AboutUsActivity extends BaseActivity {
    @Override
    public BasePresenterFragment newInstance() {
        return new AboutUsFragmentImpl();
    }
}
