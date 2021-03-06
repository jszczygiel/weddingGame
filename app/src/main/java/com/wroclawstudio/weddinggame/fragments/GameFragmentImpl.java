package com.wroclawstudio.weddinggame.fragments;

import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.plattysoft.leonids.ParticleSystem;
import com.wroclawstudio.weddinggame.R;
import com.wroclawstudio.weddinggame.engine.GameView;
import com.wroclawstudio.weddinggame.engine.threds.AnimationThread;
import com.wroclawstudio.weddinggame.fragments.interfaces.GameFragment;
import com.wroclawstudio.weddinggame.models.envioremnt.WorldModel;
import com.wroclawstudio.weddinggame.presenters.GamePresenterImpl;

public class GameFragmentImpl extends BasePresenterFragment<GamePresenterImpl> implements GameFragment, AnimationThread.PlayerAction {
    private GameView gameView;
    private View progres;
    private MediaPlayer themePlayer;
    private MediaPlayer marchPlayer;
    private MaterialDialog.Builder feelOffBuilder;
    private boolean isShowing;
    private MaterialDialog.Builder killedBuilder;
    private View wishesContainer;

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
        wishesContainer=view.findViewById(R.id.fragment_wishes_container);
        TextView wishes = (TextView) view.findViewById(R.id.fragment_game_wishes);
        TextView wishesDate = (TextView) view.findViewById(R.id.fragment_game_date);

        Typeface typeFace = Typeface.createFromAsset(getActivity().getApplicationContext().getAssets(), "font.ttf");
        feelOffBuilder = buildDialog(typeFace, R.string.player_died_content);
        killedBuilder = buildDialog(typeFace, R.string.player_killed_content);
        wishes.setTypeface(typeFace);
        wishesDate.setTypeface(typeFaces);
        return view;
    }

    private MaterialDialog.Builder buildDialog(Typeface typeFace, int content) {
        return new MaterialDialog.Builder(getActivity())
                .autoDismiss(false).cancelable(false)
                .title(R.string.player_died_title)
                .backgroundColorRes(R.color.logo_background)
                .titleColorRes(R.color.font_color)
                .contentColorRes(R.color.font_color)
                .dividerColorRes(R.color.black)
                .positiveColorRes(R.color.font_color)
                .content(content)
                .positiveText(R.string.player_positive)
                .typeface(typeFace, typeFace)
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onAny(MaterialDialog dialog) {
                        FragmentActivity activity = getActivity();
                        if (!activity.isFinishing()) {
                            activity.finish();
                        }
                    }
                });
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
    public void playerFeelOff() {
        if (!isShowing) {
            isShowing = true;
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    feelOffBuilder.show();
                }
            });
        }
    }

    @Override
    public void playerReachedEnd() {
        if (!isShowing) {
            showFireworks();
            isShowing = true;
            stopThemeSong();
            startMarchSong();
        }
    }

    private void showFireworks() {
        wishesContainer.post(new Runnable() {
            @Override
            public void run() {
                wishesContainer.animate().alpha(1).translationY(0)
                        .setInterpolator(new DecelerateInterpolator())
                        .setDuration(500).start();
                new ParticleSystem(getActivity(), 50, R.drawable.star_pink, 1000)
                        .setScaleRange(0.7f, 1.3f)
                        .setSpeedRange(0.1f, 0.25f)
                        .setAcceleration(0.0001f, 90)
                        .setRotationSpeedRange(90, 180)
                        .setFadeOut(200, new AccelerateInterpolator())
                        .oneShot(wishesContainer, 100);
                new ParticleSystem(getActivity(), 50, R.drawable.star_white, 1000)
                        .setScaleRange(0.7f, 1.3f)
                        .setSpeedRange(0.1f, 0.25f)
                        .setAcceleration(0.0001f, 90)
                        .setRotationSpeedRange(90, 180)
                        .setFadeOut(200, new AccelerateInterpolator())
                        .oneShot(wishesContainer, 100);
            }
        });

    }

    @Override
    public void playerKilledByEnemy() {
        if (!isShowing) {
            isShowing = true;
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    killedBuilder.show();
                }
            });
        }
    }

    @Override
    public void startThemeSong() {
        if (themePlayer == null) {
            loadTheme();
        }
        if (themePlayer != null) {
            themePlayer.setLooping(true);
            themePlayer.start();
        }
    }

    @Override
    public void stopThemeSong() {
        if (themePlayer != null) {
            themePlayer.stop();
            themePlayer = null;
        }
    }

    @Override
    public void startMarchSong() {
        if (marchPlayer == null) {
            loadMarch();
        }
        if (marchPlayer != null) {
            marchPlayer.setLooping(true);
            marchPlayer.start();
        }
    }

    @Override
    public void stopMarchSong() {
        if (marchPlayer != null) {
            marchPlayer.stop();
            marchPlayer = null;
        }
    }

    public void loadTheme() {
        themePlayer = MediaPlayer.create(getActivity(), R.raw.game_theme);
    }

    public void loadMarch() {
        marchPlayer = MediaPlayer.create(getActivity(), R.raw.wedding_march);
    }
}
