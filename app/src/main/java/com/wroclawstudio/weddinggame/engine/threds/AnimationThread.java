package com.wroclawstudio.weddinggame.engine.threds;

import android.graphics.Canvas;
import android.graphics.Rect;

import com.wroclawstudio.weddinggame.engine.GameView;
import com.wroclawstudio.weddinggame.models.characters.BaseCharacterObject;
import com.wroclawstudio.weddinggame.models.envioremnt.BaseGameObject;
import com.wroclawstudio.weddinggame.models.envioremnt.WorldModel;
import com.wroclawstudio.weddinggame.utils.EngineUtils;

public class AnimationThread extends Thread {
    public static final int FRAMES = 60;
    public static final int FRAME_LENGHT = 1000 / FRAMES;
    private static final int PLAYER_BLOCK_OFFSET = 2;
    private final Object lock = new Object();

    private final GameView holder;
    // lifecycle
    private boolean running;
    private boolean suspend;

    // physical dimensions
    private int canvasWidth;
    private int canvasHeight;

    // game dimensions
    int startBlockX = 0;
    int endBlockX = 0;
    int maxColumn = 0;
    int pixelOffset = 0;
    int pixelStep;

    int screenBlocksWidth;
    int blocksOnScreen;

    // world objects
    WorldModel world;

    public AnimationThread(GameView holder, int width, int height) {
        this.holder = holder;
        synchronized (holder) {
            canvasWidth = width;
            canvasHeight = height;
        }

        for (int divider = 20; divider >= 8; divider--) {
            if (canvasWidth % divider == 0) {
                screenBlocksWidth = canvasWidth / divider;
                endBlockX = blocksOnScreen = divider;
                maxColumn = canvasHeight / screenBlocksWidth;
                pixelStep = screenBlocksWidth / 12;
                break;
            }
        }


    }

    public int getPixelStep() {
        return pixelStep;
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
        long timeStart;
        long timeEnd;
        long frameTime;
        int sleepFor;
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
                sleepFor = (int) (FRAME_LENGHT - frameTime);

                if (sleepFor > 0) {
                    try {
                        Thread.sleep(sleepFor);
                    } catch (InterruptedException ignored) {
                    }
                }
                while (sleepFor < 0) {
                    sleepFor += FRAME_LENGHT;
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
                for (int currentX = startBlockX; currentX < endBlockX + 1; currentX++) {
                    if (endBlockX < world.getWorldSize()) {
                        column = world.getEnvironment().get(currentX);
                        for (int row = 0; row < column.length; row++) {
                            drawEnvironment(currentX, column[row].getY(), column[row], canvas);
                        }
                    }
                }
                drawPlayer(world.getPlayerCharacter(), canvas);

            }
        }
    }

    private void drawPlayer(BaseCharacterObject playerCharacter, Canvas canvas) {
        Rect playerRect = playerCharacter.getBounds();
        playerRect.offset(0, pixelStep);

        int yPixelChange = calculatePlayerYChange(playerRect, world);
        playerRect.offset(0, -yPixelChange);

        playerCharacter.getCurrentDrawable().setBounds(playerRect);
        playerCharacter.getCurrentDrawable().draw(canvas);
    }

    private int calculatePlayerYChange(Rect playerRect, WorldModel environment) {
        if (startBlockX + PLAYER_BLOCK_OFFSET < environment.getWorldSize()) {
            for (int index = 0; index < 2; index++) {
                BaseGameObject[] tempColumn = environment.getEnvironment().get(startBlockX + PLAYER_BLOCK_OFFSET + index);

                for (int i = tempColumn.length - 1; i >= 0; i--) {
                    if(!tempColumn[i].isSolid()){
                        continue;
                    }
                    Rect objectRect = tempColumn[i].getBounds();
                    if (Rect.intersects(playerRect, objectRect)) {
                            return playerRect.bottom - objectRect.top;
                    }
                }
            }
        }
        return 0;
    }

    private void drawEnvironment(int currentX, int currentY, BaseGameObject gameObject, Canvas canvas) {
        if (currentY > maxColumn) {
            currentY = maxColumn;
        }
        int left = currentX * screenBlocksWidth - pixelOffset;
        int right = (currentX + 1) * screenBlocksWidth - pixelOffset;
        int bottom = canvasHeight - screenBlocksWidth * currentY;
        int top = canvasHeight - screenBlocksWidth * (currentY + 1);
        gameObject.setBounds(left, top, right, bottom);
        gameObject.getDrawable().draw(canvas);
    }

    public void setWorld(WorldModel world) {
        this.world = world;
        world.getBackgroundColor().setBounds(0, 0, canvasWidth, canvasHeight);
        world.getPlayerCharacter().setBounds(PLAYER_BLOCK_OFFSET * screenBlocksWidth, canvasHeight - screenBlocksWidth * 4,
                (PLAYER_BLOCK_OFFSET + 1) * screenBlocksWidth, canvasHeight - screenBlocksWidth * 3);

    }

    public void incrementOffset() {
        Rect playerRect = world.getPlayerCharacter().getBounds();

        pixelOffset += pixelStep - calculatePlayerXChange(playerRect, world, pixelStep);
        startBlockX = pixelOffset / screenBlocksWidth;
        endBlockX = startBlockX + blocksOnScreen;
    }

    private int calculatePlayerXChange(Rect playerRect, WorldModel environment, int pixelStep) {
        if (startBlockX + PLAYER_BLOCK_OFFSET < environment.getWorldSize()) {
            BaseGameObject[] tempColumn = environment.getEnvironment().get(startBlockX + PLAYER_BLOCK_OFFSET + 1);

            for (int i = tempColumn.length - 1; i >= 0; i--) {
                if(!tempColumn[i].isSolid()){
                    continue;
                }
                Rect objectRect = tempColumn[i].getBounds();
                objectRect.offset(-pixelStep, 0);
                if (Rect.intersects(playerRect, objectRect)) {
                    objectRect.offset(pixelStep, 0);
                    return pixelStep;
                }
                objectRect.offset(pixelStep, 0);

            }
        }
        return 0;
    }


}
