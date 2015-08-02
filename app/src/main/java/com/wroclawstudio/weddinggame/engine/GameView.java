package com.wroclawstudio.weddinggame.engine;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.TextureView;

import com.wroclawstudio.weddinggame.engine.threds.AnimationThread;
import com.wroclawstudio.weddinggame.models.envioremnt.WorldModel;

public class GameView extends TextureView implements TextureView.SurfaceTextureListener {

    private AnimationThread animationThread;
    private WorldModel worldModel;
    private ScrollThread scrollThread;
    private JumpThread jumpThread;

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setSurfaceTextureListener(this);
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int width, int height) {
        this.halfWidth = width / 2;
        if (animationThread == null) {
            animationThread = new AnimationThread(this, width, height);
            animationThread.setWorld(worldModel);
            animationThread.setRunning(true);
            animationThread.start();
        }
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int width, int height) {
        if (animationThread != null) {
            animationThread.setSurfaceSize(width, height);
        }
    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {

        boolean retry = true;
        if (animationThread != null) {
            animationThread.setSuspend(true);
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

    private int halfWidth;
    private long startClickTime;
    private static final int MAX_CLICK_DURATION = 200;
    private int scrollFingerId;

    @Override
    public boolean onTouchEvent(MotionEvent event) {


        int action = event.getAction() & MotionEvent.ACTION_MASK;
        int pointerIndex = (event.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK) >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
        int fingerId = event.getPointerId(pointerIndex);

        switch (action) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                if (event.getX(pointerIndex) > halfWidth) {
                    if (scrollThread == null) {
                        scrollFingerId = fingerId;
                        scrollThread = new ScrollThread();
                        scrollThread.start();
                    }
                } else {
                    startClickTime = System.currentTimeMillis();
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_CANCEL:
                if (scrollFingerId == fingerId) {
                    stopScrolling();
                }
                if (event.getX(pointerIndex) < halfWidth) {

                    long clickDuration = System.currentTimeMillis() - startClickTime;
                    if (clickDuration < MAX_CLICK_DURATION) {
                        if (jumpThread == null) {
                            jumpThread = new JumpThread();
                            jumpThread.start();
                        }
                    }
                }
                break;
        }

        return true;
    }

    private void stopScrolling() {
        if (scrollThread != null) {
            scrollThread.interrupt();
            scrollThread = null;
        }
    }


    class ScrollThread extends Thread {
        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(AnimationThread.FRAME_LENGTH);
                    animationThread.incrementOffset();
                } catch (InterruptedException e) {
                    return;
                }
            }
        }
    }

    class JumpThread extends Thread {
        long jumpStart;

        @Override
        public void run() {
            if (worldModel.getPlayerCharacter().canJump()) {
                jumpStart = System.currentTimeMillis();
                worldModel.getPlayerCharacter().setCanJump(false);
                for (int frame = 0; frame < AnimationThread.FRAMES / 2 + 1; frame++) {
                    try {
                        if (animationThread.addJump()) {
                            break;
                        }
                        Thread.sleep(AnimationThread.FRAME_LENGTH);
                    } catch (InterruptedException e) {
                        return;
                    }
                }
            }
            jumpThread = null;
        }
    }
}
