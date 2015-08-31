package com.wroclawstudio.weddinggame.engine.threds;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;

import com.wroclawstudio.weddinggame.engine.GameView;
import com.wroclawstudio.weddinggame.models.characters.BaseCharacterObject;
import com.wroclawstudio.weddinggame.models.envioremnt.BaseGameObject;
import com.wroclawstudio.weddinggame.models.envioremnt.MessageObject;
import com.wroclawstudio.weddinggame.models.envioremnt.WorldModel;

import java.util.List;

public class AnimationThread extends Thread {
    public static final int FRAMES = 60;
    public static final int FRAME_LENGTH = 1000 / FRAMES;
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
    private PlayerAction listener;
    private Paint paint;

    public void setListener(PlayerAction listener) {
        this.listener = listener;
    }

    public interface PlayerAction {

        void playerFeelOff();

        void playerReachedEnd();

        void playerKilledByEnemy();
    }

    public AnimationThread(GameView holder, int width, int height, Paint paint) {
        this.holder = holder;
        synchronized (holder) {
            canvasWidth = width;
            canvasHeight = height;
        }
        int divider = 20;
        screenBlocksWidth = canvasWidth / divider;
        endBlockX = blocksOnScreen = divider;
        maxColumn = canvasHeight / screenBlocksWidth;
        pixelStep = screenBlocksWidth / 12;

        this.paint = paint;
    }

    public void setRunning(boolean isRunning) {
        running = isRunning;
        synchronized (lock) {
            lock.notifyAll();
        }
    }

    public boolean isRunning() {
        return running;
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
                sleepFor = (int) (FRAME_LENGTH - frameTime);

                if (sleepFor > 0) {
                    try {
                        Thread.sleep(sleepFor);
                    } catch (InterruptedException ignored) {
                    }
                }
                while (sleepFor < 0) {
                    sleepFor += FRAME_LENGTH;
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
                    if (endBlockX < world.getEnvironment().size()) {
                        column = world.getEnvironment().get(currentX);
                        for (BaseGameObject aColumn : column) {
                            if (!(aColumn instanceof MessageObject)) {
                                drawEnvironment(currentX, aColumn.getY(), aColumn, canvas);
                            }
                        }
                    }
                }
                for (int currentX = startBlockX - 10; currentX < endBlockX + 1; currentX++) {
                    if (currentX > 0) {
                        column = world.getEnvironment().get(currentX);
                        for (BaseGameObject aColumn : column) {
                            if (aColumn instanceof MessageObject) {
                                drawText(currentX, aColumn.getY(), (MessageObject) aColumn, canvas);
                            }
                        }
                    }
                }

                drawPlayer(world.getPlayerCharacter(), canvas);

            }
        }
    }

    private void drawText(int currentX, int currentY, MessageObject object, Canvas canvas) {
        if (currentY > maxColumn) {
            currentY = maxColumn;
        }
        int left = currentX * screenBlocksWidth - pixelOffset;
        int right = (currentX + 1) * screenBlocksWidth - pixelOffset;
        int bottom = canvasHeight - screenBlocksWidth * currentY;
        int top = canvasHeight - screenBlocksWidth * (currentY + 1);
        canvas.drawText(object.getText(), left, top, paint);
    }

    private void drawText(List<BaseGameObject[]> text, Canvas canvas) {

    }

    private void drawPlayer(BaseCharacterObject playerCharacter, Canvas canvas) {
        Rect playerRect = playerCharacter.getBounds();
        playerRect.offset(0, 2 * pixelStep);

        int yPixelChange = calculatePlayerYChange(playerRect, world);
        playerRect.offset(0, -yPixelChange);

        if (listener != null && playerRect.centerY() > canvasHeight) {
            listener.playerFeelOff();
        }

        playerCharacter.getCurrentDrawable().setBounds(playerRect);
        playerCharacter.getCurrentDrawable().draw(canvas);
    }

    private int calculatePlayerYChange(Rect playerRect, WorldModel environment) {
        if (startBlockX + PLAYER_BLOCK_OFFSET < environment.getWorldSize()) {
            for (int index = 0; index < 2; index++) {
                BaseGameObject[] tempColumn = environment.getEnvironment().get(startBlockX + PLAYER_BLOCK_OFFSET + index);

                for (int i = tempColumn.length - 1; i >= 0; i--) {
                    if (!tempColumn[i].isSolid()) {
                        continue;
                    }
                    Rect objectRect = tempColumn[i].getBounds();
                    if (Rect.intersects(playerRect, objectRect)) {
                        if (objectRect.bottom > playerRect.bottom) {
                            world.getPlayerCharacter().setCanJump(true);
                            return playerRect.bottom - objectRect.top;
                        } else {
                            return -(objectRect.bottom - playerRect.top);
                        }
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
        if (startBlockX + PLAYER_BLOCK_OFFSET < environment.getWorldSize() - 1) {
            BaseGameObject[] tempColumn = environment.getEnvironment().get(startBlockX + PLAYER_BLOCK_OFFSET + 1);

            for (int i = tempColumn.length - 1; i >= 0; i--) {
                if (!tempColumn[i].isSolid()) {
                    continue;
                }
                Rect objectRect = tempColumn[i].getBounds();
                objectRect.offset(-pixelStep, 0);
                if (Rect.intersects(playerRect, objectRect)) {
                    objectRect.offset(pixelStep, 0);
                    if (tempColumn[i].isLeathal()) {
                        if (listener != null) {
                            listener.playerKilledByEnemy();
                        }
                    }
                    return pixelStep;
                }
                objectRect.offset(pixelStep, 0);

            }
            return 0;
        }
        if (listener != null) {
            listener.playerReachedEnd();
        }
        return pixelStep;
    }

    public boolean addJump() {
        Rect playerRect = world.getPlayerCharacter().getBounds();
        playerRect.offset(0, -4 * pixelStep);

        int yPixelChange = calculatePlayerYChange(playerRect, world);
        playerRect.offset(0, -yPixelChange);
        return 4 * pixelStep + yPixelChange == 0;
    }

    public void leftWalkAnimation() {
        world.getPlayerCharacter().setCurrentDrawable(BaseCharacterObject.LEFT_STEP);
    }

    public void rightWalkAnimation() {
        world.getPlayerCharacter().setCurrentDrawable(BaseCharacterObject.RIGHT_STEP);

    }

    public void standingAnimation() {
        world.getPlayerCharacter().setCurrentDrawable(BaseCharacterObject.STANDING);
    }

}
