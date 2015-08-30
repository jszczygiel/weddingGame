package com.wroclawstudio.weddinggame.presenters;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;

import com.afollestad.materialdialogs.MaterialDialog;
import com.wroclawstudio.weddinggame.R;
import com.wroclawstudio.weddinggame.fragments.interfaces.GameFragment;
import com.wroclawstudio.weddinggame.models.characters.BaseCharacterObject;
import com.wroclawstudio.weddinggame.models.characters.MushroomCharacterObject;
import com.wroclawstudio.weddinggame.models.envioremnt.BaseGameObject;
import com.wroclawstudio.weddinggame.models.envioremnt.EnvironmentBuilder;
import com.wroclawstudio.weddinggame.models.envioremnt.GroundObject;
import com.wroclawstudio.weddinggame.models.envioremnt.WorldModel;
import com.wroclawstudio.weddinggame.presenters.interfaces.BasePresenter;
import com.wroclawstudio.weddinggame.utils.ViewUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GamePresenterImpl extends BasePresenter<GameFragment> {


    public void onLoad() {
        getView().showProgress();
        loadWorld();

    }

    private void loadWorld() {
        new AsyncTask<Void, Void, WorldModel>() {

            @Override
            protected WorldModel doInBackground(Void... params) {
                List<BaseGameObject[]> environment = new EnvironmentBuilder(getContext(), 2, 120)
                        .setHoles(new ArrayList<>(Arrays.asList(new Integer[]{10, 11, 20, 21, 30, 31})))
                        .setGrass(new ArrayList<EnvironmentBuilder.PlaneObject>() {
                            {
                                add(new EnvironmentBuilder.PlaneObject(15, 17, 2));
                            }
                        })
                        .setPlatform(new ArrayList<EnvironmentBuilder.PlaneObject>() {
                            {
                                add(new EnvironmentBuilder.PlaneObject(5, 6, 5));
                                add(new EnvironmentBuilder.PlaneObject(25, 29, 5));
                                add(new EnvironmentBuilder.PlaneObject(44, 45, 5));
                            }
                        })
                        .build();

                int color = getContext().getResources().getColor(R.color.background_color);
                BaseCharacterObject playerCharacter = new MushroomCharacterObject(
                        ViewUtils.getDrawable(getContext(), R.drawable.shroom_left),
                        ViewUtils.getDrawable(getContext(), R.drawable.shroom_right),
                        ViewUtils.getDrawable(getContext(), R.drawable.shroom_mid));
                return new WorldModel(environment, color, playerCharacter);
            }

            @Override
            protected void onPostExecute(WorldModel world) {
                if (getView() != null) {
                    getView().hideProgress();
                    getView().loadWorld(world);
                }
            }
        }.execute();
    }

    public void playerDied() {
        getView().showPlayerDiedDialog();

    }

    @Override
    public void onStart() {
        getView().startSong();
    }

    @Override
    public void onStop() {
        getView().stopSong();
    }
}

