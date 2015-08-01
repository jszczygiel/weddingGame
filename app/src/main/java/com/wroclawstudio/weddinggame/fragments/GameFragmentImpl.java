package com.wroclawstudio.weddinggame.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wroclawstudio.weddinggame.R;
import com.wroclawstudio.weddinggame.engine.GameView;
import com.wroclawstudio.weddinggame.fragments.interfaces.GameFragment;
import com.wroclawstudio.weddinggame.models.envioremnt.WorldModel;
import com.wroclawstudio.weddinggame.presenters.GamePresenterImpl;

public class GameFragmentImpl extends BasePresenterFragment<GamePresenterImpl> implements GameFragment {
    private GameView gameView;
    private View progres;

    public static BasePresenterFragment newInstance() {
        return new GameFragmentImpl();
    }

    @Override
    public GamePresenterImpl initializePresenter() {
        return new GamePresenterImpl();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game, container, false);
        gameView = (GameView) view.findViewById(R.id.fragment_game_view);
        progres = view.findViewById(R.id.fragment_progress);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        getPresenter().onLoad();
    }

    @Override
    public void showProgress() {
        progres.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progres.setVisibility(View.GONE);
    }

    @Override
    public void loadWorld(WorldModel worldModel) {
        gameView.setWorld(worldModel);
    }
}
