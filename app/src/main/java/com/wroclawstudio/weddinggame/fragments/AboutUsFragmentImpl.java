package com.wroclawstudio.weddinggame.fragments;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wroclawstudio.weddinggame.R;
import com.wroclawstudio.weddinggame.fragments.interfaces.AboutUsFragment;
import com.wroclawstudio.weddinggame.presenters.AboutUsPresenterImpl;
import com.wroclawstudio.weddinggame.presenters.interfaces.BasePresenter;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class AboutUsFragmentImpl extends BasePresenterFragment<AboutUsPresenterImpl> implements AboutUsFragment {

    @Bind({R.id.fragment_about_title, R.id.fragment_about_left, R.id.fragment_about_right})
    List<TextView> views;

    @Override
    public AboutUsPresenterImpl initializePresenter() {
        return new AboutUsPresenterImpl();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about_us, container, false);
        ButterKnife.bind(this, view);
        Typeface typeFace = Typeface.createFromAsset(getActivity().getApplicationContext().getAssets(), "font.ttf");

        for (TextView item : views) {
            item.setTypeface(typeFace);
        }
        return view;
    }
}
