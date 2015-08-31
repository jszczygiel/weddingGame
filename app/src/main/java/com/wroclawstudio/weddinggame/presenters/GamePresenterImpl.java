package com.wroclawstudio.weddinggame.presenters;

import android.os.AsyncTask;

import com.wroclawstudio.weddinggame.R;
import com.wroclawstudio.weddinggame.fragments.interfaces.GameFragment;
import com.wroclawstudio.weddinggame.models.characters.BaseCharacterObject;
import com.wroclawstudio.weddinggame.models.characters.MushroomCharacterObject;
import com.wroclawstudio.weddinggame.models.envioremnt.BaseGameObject;
import com.wroclawstudio.weddinggame.models.envioremnt.EnvironmentBuilder;
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
                int worldSize = 125;
                List<BaseGameObject[]> environment = new EnvironmentBuilder(getContext(), 2, worldSize)
                        .setHoles(new ArrayList<>(Arrays.asList(new Integer[]{
                                20, 21, 22,
                                30, 31,
                                45, 46, 47, 48, 49, 50, 51, 52,
                                69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79
                        })))
                        .setGrass(new ArrayList<EnvironmentBuilder.PlaneObject>() {
                            {
                                add(new EnvironmentBuilder.PlaneObject(10, 12, 2));
                                add(new EnvironmentBuilder.PlaneObject(15, 17, 2));
                                add(new EnvironmentBuilder.PlaneObject(38, 39, 2));
                                add(new EnvironmentBuilder.PlaneObject(56, 60, 2));
                                add(new EnvironmentBuilder.PlaneObject(85, 88, 2));
                                add(new EnvironmentBuilder.PlaneObject(90, 93, 2));
                                add(new EnvironmentBuilder.PlaneObject(116, 118, 2));
                            }
                        })
                        .setPlatform(new ArrayList<EnvironmentBuilder.PlaneObject>() {
                            {
                                add(new EnvironmentBuilder.PlaneObject(5, 6, 5));
                                add(new EnvironmentBuilder.PlaneObject(25, 29, 5));
                                add(new EnvironmentBuilder.PlaneObject(44, 47, 5));
                                add(new EnvironmentBuilder.PlaneObject(65, 68, 2));
                                add(new EnvironmentBuilder.PlaneObject(66, 68, 3));
                                add(new EnvironmentBuilder.PlaneObject(67, 68, 4));
                                add(new EnvironmentBuilder.PlaneObject(68, 68, 5));
                                add(new EnvironmentBuilder.PlaneObject(71, 72, 5));
                                add(new EnvironmentBuilder.PlaneObject(76, 77, 6));
                            }
                        })
                        .setCloud(new ArrayList<EnvironmentBuilder.PlaneObject>() {
                            {
                                add(new EnvironmentBuilder.PlaneObject(6, 8, 7, 8));
                                add(new EnvironmentBuilder.PlaneObject(18, 21, 8, 9));
                                add(new EnvironmentBuilder.PlaneObject(31, 33, 7, 8));
                                add(new EnvironmentBuilder.PlaneObject(47, 49, 8, 9));
                                add(new EnvironmentBuilder.PlaneObject(64, 66, 7, 8));
                                add(new EnvironmentBuilder.PlaneObject(87, 90, 7, 8));
                                add(new EnvironmentBuilder.PlaneObject(100, 103, 7, 8));
                            }
                        })
                        .setText(new ArrayList<EnvironmentBuilder.PlaneObject>() {
                            {
                                add(new EnvironmentBuilder.PlaneObject(4,9,"Tester Grzybciu, wielki chwat,"));
                                add(new EnvironmentBuilder.PlaneObject(4,8,"Postanowil ruszyc w swiat"));
                                add(new EnvironmentBuilder.PlaneObject(33,9,"Ruszyl szybko, sil nie szczedzi,"));
                                add(new EnvironmentBuilder.PlaneObject(33,8,"Na zlamanie karku pedzi"));
                                add(new EnvironmentBuilder.PlaneObject(62,9,"Otchlan bugow moi mili"));
                                add(new EnvironmentBuilder.PlaneObject(62,8,"Moze wciagnac w kazdej chwili"));
                                add(new EnvironmentBuilder.PlaneObject(84,9,"Andy potwor mnozy taski "));
                                add(new EnvironmentBuilder.PlaneObject(84,8,"Nie okaze swojej laski"));
                                add(new EnvironmentBuilder.PlaneObject(110,9,"Lecz w nagrode, (dumna mina)"));
                                add(new EnvironmentBuilder.PlaneObject(110,8,"Tu juz czeka Karolina!"));
                            }
                        })
                        .setEnemy(97)
                        .setCastlePosition(worldSize)
                        .build();

                int color = getContext().getResources().getColor(R.color.background_color);
                BaseCharacterObject playerCharacter = new MushroomCharacterObject(
                        ViewUtils.getDrawable(getContext(), R.drawable.shroom_left),
                        ViewUtils.getDrawable(getContext(), R.drawable.shroom_right),
                        ViewUtils.getDrawable(getContext(), R.drawable.shroom_mid));
                return new WorldModel(environment, worldSize, color, playerCharacter);
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

    @Override
    public void onResume() {
        getView().startThemeSong();
    }

    @Override
    public void onPause() {
        getView().stopThemeSong();
        getView().stopMarchSong();
    }
}

