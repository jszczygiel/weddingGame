package com.wroclawstudio.weddinggame.presenters;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;

import com.wroclawstudio.weddinggame.R;
import com.wroclawstudio.weddinggame.fragments.interfaces.GameFragment;
import com.wroclawstudio.weddinggame.models.envioremnt.BaseGameObject;
import com.wroclawstudio.weddinggame.models.envioremnt.GroundObject;
import com.wroclawstudio.weddinggame.models.envioremnt.WorldModel;
import com.wroclawstudio.weddinggame.presenters.interfaces.BasePresenter;

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
                Drawable drawable = getContext().getDrawable(R.drawable.ground);
                List<BaseGameObject[]> environment = new ArrayList<>();
                for (int i = 0; i < 128; i++) {
                    BaseGameObject[] column = new BaseGameObject[]{new GroundObject(drawable, 0), new GroundObject(drawable, 1)};
                    environment.add(column);
                }

                int color = getContext().getResources().getColor(R.color.background_color);
                return new WorldModel(environment, color);
            }

            @Override
            protected void onPostExecute(WorldModel world) {
                getView().hideProgress();
                getView().loadWorld(world);
            }
        }.execute();
    }
}
