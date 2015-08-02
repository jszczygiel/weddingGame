package com.wroclawstudio.weddinggame.presenters;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;

import com.wroclawstudio.weddinggame.R;
import com.wroclawstudio.weddinggame.fragments.interfaces.GameFragment;
import com.wroclawstudio.weddinggame.models.characters.BaseCharacterObject;
import com.wroclawstudio.weddinggame.models.characters.MushroomCharacterObject;
import com.wroclawstudio.weddinggame.models.envioremnt.BaseGameObject;
import com.wroclawstudio.weddinggame.models.envioremnt.GroundObject;
import com.wroclawstudio.weddinggame.models.envioremnt.WorldModel;
import com.wroclawstudio.weddinggame.presenters.interfaces.BasePresenter;
import com.wroclawstudio.weddinggame.utils.ViewUtils;

import java.util.ArrayList;
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
                Drawable drawable = ViewUtils.getDrawable(getContext(), R.drawable.ground);
                List<BaseGameObject[]> environment = new ArrayList<>();
                for (int i = 0; i < 128; i++) {
                    BaseGameObject[] column;
                    if (i != 10 && i != 11 && i != 89 && i != 90) {
                        column = new BaseGameObject[]{new GroundObject(drawable, 0), new GroundObject(drawable, 1)};
                    } else {
                        column = new BaseGameObject[]{new GroundObject(drawable, 0), new GroundObject(drawable, 1), new GroundObject(drawable, 2)};
                    }
                    environment.add(column);
                }

                int color = getContext().getResources().getColor(R.color.background_color);
                BaseCharacterObject playerCharacter = new MushroomCharacterObject(ViewUtils.getDrawable(getContext(), R.drawable.mushroom_1), ViewUtils.getDrawable(getContext(), R.drawable.mushroom_2), ViewUtils.getDrawable(getContext(), R.drawable.mushroowm_standing));
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
}
