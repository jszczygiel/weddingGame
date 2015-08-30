package com.wroclawstudio.weddinggame.fragments;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.materialdialogs.MaterialDialog;
import com.wroclawstudio.weddinggame.R;
import com.wroclawstudio.weddinggame.engine.GameView;
import com.wroclawstudio.weddinggame.engine.threds.AnimationThread;
import com.wroclawstudio.weddinggame.fragments.interfaces.GameFragment;
import com.wroclawstudio.weddinggame.models.envioremnt.WorldModel;
import com.wroclawstudio.weddinggame.presenters.GamePresenterImpl;

public class GameFragmentImpl extends BasePresenterFragment<GamePresenterImpl> implements GameFragment, AnimationThread.PlayerAction {
    private GameView gameView;
    private View progres;
    private MediaPlayer player;

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
        gameView.setPlayerListener(this);
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

    @Override
    public void showPlayerDiedDialog() {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                new MaterialDialog.Builder(getActivity())
                        .autoDismiss(false).cancelable(false)
                        .title(R.string.player_died_title)
                        .positiveText(android.R.string.ok)
                        .callback(new MaterialDialog.ButtonCallback() {
                            @Override
                            public void onAny(MaterialDialog dialog) {
                                if (!getActivity().isFinishing()) {
                                    getActivity().finish();
                                }
                            }
                        })
                        .show();
            }
        });

    }

    @Override
    public void playerDied() {
        getPresenter().playerDied();
    }

    @Override
    public void startSong() {
        if (player == null) {
            loadTheme();
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
            player=null;
        }
    }

    public void loadTheme() {
        player = MediaPlayer.create(getActivity(), R.raw.game_theme);
    }
    public void loadMarch() {
        player = MediaPlayer.create(getActivity(), R.raw.wedding_march);
    }
}
