package com.wroclawstudio.weddinggame.engine;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.util.AttributeSet;
import android.view.TextureView;

import com.wroclawstudio.weddinggame.engine.threds.AnimationThread;
import com.wroclawstudio.weddinggame.models.envioremnt.WorldModel;

public class GameView extends TextureView implements TextureView.SurfaceTextureListener {

    private AnimationThread animationThread;
    private WorldModel worldModel;

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setSurfaceTextureListener(this);
    }


    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int width, int height) {
        if (animationThread == null) {
            animationThread = new AnimationThread(this, width, height);
            animationThread.setWorld(worldModel);
            animationThread.setRunning(true);
            animationThread.start();
        }
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int width, int height) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {

        boolean retry = true;
        if (animationThread != null) {
            animationThread.setRunning(false);
            while (retry) {
                try {
                    animationThread.join();
                    retry = false;
                    animationThread = null;
                } catch (InterruptedException ignored) {
                }
            }
        }
        return true;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {
    }

    public AnimationThread getAnimationThread() {
        return animationThread;
    }

    public void setWorld(WorldModel worldModel) {
        this.worldModel = worldModel;
    }
}
