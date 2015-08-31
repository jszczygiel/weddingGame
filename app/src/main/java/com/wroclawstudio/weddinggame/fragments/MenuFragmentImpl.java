package com.wroclawstudio.weddinggame.fragments;

import android.content.Intent;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import com.wroclawstudio.weddinggame.R;
import com.wroclawstudio.weddinggame.activities.GameActivity;
import com.wroclawstudio.weddinggame.fragments.interfaces.MenuFragment;
import com.wroclawstudio.weddinggame.presenters.MenuPresenterImpl;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MenuFragmentImpl extends BasePresenterFragment<MenuPresenterImpl> implements MenuFragment {
    MediaPlayer player;

    @Bind({R.id.fragment_menu_start, R.id.fragment_menu_about, R.id.fragment_menu_logo})
    List<TextView> views;
    private View container;

    @Override
    public MenuPresenterImpl initializePresenter() {
        return new MenuPresenterImpl();
    }

    public static BasePresenterFragment newInstance() {
        return new MenuFragmentImpl();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu, container, false);
        ButterKnife.bind(this, view);
        Typeface typeFace = Typeface.createFromAsset(getActivity().getApplicationContext().getAssets(), "font.ttf");

        for (TextView item : views) {
            item.setTypeface(typeFace);
        }
        return view;
    }


    @OnClick(R.id.fragment_menu_start)
    void start() {
        startActivity(new Intent(getActivity(), GameActivity.class));
    }

    @OnClick(R.id.fragment_menu_about)
    void about() {

    }

    @Override
    public void startSong() {
        if (player == null) {
            loadSong();
        }
        if (player != null) {
            player.setLooping(true);
            player.start();
        }
    }

    @Override
    public void stopSong() {
        if (player != null) {
            player.stop();
            player = null;
        }
    }

    public void loadSong() {
        player = MediaPlayer.create(getActivity(), R.raw.proclaimers_theme);
    }
}
