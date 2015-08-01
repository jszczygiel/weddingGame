package com.wroclawstudio.weddinggame.engine.threds;

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.wroclawstudio.weddinggame.engine.GameView;
import com.wroclawstudio.weddinggame.models.envioremnt.BaseGameObject;
import com.wroclawstudio.weddinggame.models.envioremnt.WorldModel;

public class AnimationThread extends Thread {
    private final int FRAMES = 60;
    private final Object lock = new Object();

    private final GameView holder;
    // lifecycle
    private boolean running;
    private boolean suspend;
    // frame drawing logic
    private long timeEnd;
    private long timeStart;
    private long frameTime;
    private int sleepFor;

    // physical dimensions
    private int canvasWidth;
    private int canvasHeight;

    // game dimensions
    int startX = 0;
    int endX = 0;
    int maxColumn=0;
    private int screenBlocksWidth;


    // world objects
    private WorldModel world;

    public AnimationThread(GameView holder, int width, int height) {
        this.holder = holder;
        synchronized (holder) {
            canvasWidth = width;
            canvasHeight = height;
        }

        for (int divider = 20; divider >= 12; divider--) {
            if (canvasWidth % divider == 0) {
                screenBlocksWidth = canvasWidth / divider;
                endX = divider;
                maxColumn=canvasHeight/screenBlocksWidth;
                break;
            }
        }

        Log.i("WEDDING", "initilized with: screenBlocksWidth:" + screenBlocksWidth);

    }

    public void setRunning(boolean isRunning) {
        running = isRunning;
        synchronized (lock) {
            lock.notifyAll();
        }
    }

    public void setSuspend(boolean suspend) {
        this.suspend = suspend;
        synchronized (lock) {
            lock.notifyAll();
        }
    }


    public void setSurfaceSize(int width, int height) {
        synchronized (holder) {
            canvasWidth = width;
            canvasHeight = height;
        }
    }

    @Override
    public void run() {
        while (running) {
            while (!suspend) {

                timeStart = System.currentTimeMillis();
                final Canvas canvas = holder.lockCanvas();

                try {
                    synchronized (holder) {
                        draw(canvas);
                    }
                } finally {
                    if (canvas != null) {
                        holder.unlockCanvasAndPost(canvas);
                    }
                }
                timeEnd = System.currentTimeMillis();
                frameTime = timeEnd - timeStart;
                sleepFor = (int) ((1000 / FRAMES) - frameTime);

                if (sleepFor > 0) {
                    try {
                        Thread.sleep(sleepFor);
                    } catch (InterruptedException ignored) {
                    }
                }
                while (sleepFor < 0) {
                    sleepFor += (1000 / FRAMES);
                }
            }
            synchronized (lock) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
        }

    }
    BaseGameObject[] column;
    private void draw(Canvas canvas) {
        if (canvas != null) {

            // world drawing
            if (world != null) {
                world.getBackgroundColor().draw(canvas);
                for (int currentX = startX; currentX < endX + 1; currentX++) {
                    column = world.getEnvironment().get(currentX);
                    for(int row=0;row<column.length;row++){
                        drawDrawable(currentX, column[row].getY(), column[row].getDrawable(), canvas);
                    }
                }
            }
        }
    }

    private void drawDrawable(int currentX, int currentY, Drawable drawable, Canvas canvas) {
        int left = (currentX - startX) * screenBlocksWidth;
        int right = (currentX+1 - startX) * screenBlocksWidth;
        int bottom=canvasHeight-screenBlocksWidth*currentY;
        int top=canvasHeight-screenBlocksWidth*(currentY+1);
        drawable.setBounds(left, top, right, bottom);
        drawable.draw(canvas);
    }

    public void setWorld(WorldModel world) {
        this.world = world;
        world.getBackgroundColor().setBounds(0,0,canvasWidth,canvasHeight);
    }
}
