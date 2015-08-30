package com.wroclawstudio.weddinggame.models.envioremnt;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.wroclawstudio.weddinggame.R;
import com.wroclawstudio.weddinggame.utils.ViewUtils;

import java.util.ArrayList;
import java.util.List;

public class EnvironmentBuilder {

    private final int height;
    private final int lenght;
    private final Context context;
    private List<Integer> holes = new ArrayList<>();
    private List<PlaneObject> grass = new ArrayList<>();
    private List<PlaneObject> platform = new ArrayList<>();
    private int castlePosition;
    private ArrayList<PlaneObject> cloud;
    private int enemyPosition;

    public EnvironmentBuilder(Context context, int height, int lenght) {
        this.context = context;
        this.height = height;
        this.lenght = lenght;
    }

    public EnvironmentBuilder setHoles(List<Integer> holes) {
        this.holes = holes;
        return this;
    }

    public EnvironmentBuilder setGrass(List<PlaneObject> grass) {
        this.grass = grass;
        return this;
    }

    public EnvironmentBuilder setPlatform(List<PlaneObject> platform) {
        this.platform = platform;
        return this;
    }

    public EnvironmentBuilder setCastlePosition(int castlePosition) {
        this.castlePosition = castlePosition;
        return this;
    }

    public EnvironmentBuilder setCloud(ArrayList<PlaneObject> cloud) {
        this.cloud = cloud;
        return this;
    }

    public List<BaseGameObject[]> build() {
        List<BaseGameObject[]> list = new ArrayList<>();
        Drawable groundDrawable = ViewUtils.getDrawable(context, R.drawable.ground);
        Drawable startGrassDrawable = ViewUtils.getDrawable(context, R.drawable.grass_left);
        Drawable endGrassDrawable = ViewUtils.getDrawable(context, R.drawable.grass_right);
        Drawable middleGrassDrawable = ViewUtils.getDrawable(context, R.drawable.grass_mid);
        Drawable platformDrawable = ViewUtils.getDrawable(context, R.drawable.brick);

        Drawable castleBrickDrawale = ViewUtils.getDrawable(context, R.drawable.castle_brick);
        Drawable castleRoofSkyDrawable = ViewUtils.getDrawable(context, R.drawable.castle_brick_roof_sky);
        Drawable castleRoofDrawable = ViewUtils.getDrawable(context, R.drawable.castle_brick_roof);
        Drawable castleDoorBottom = ViewUtils.getDrawable(context, R.drawable.door_bottom);
        Drawable castleWindowRightDrawable = ViewUtils.getDrawable(context, R.drawable.castle_window_right);
        Drawable castleWindowLeftDrawable = ViewUtils.getDrawable(context, R.drawable.castle_window_left);
        Drawable castleDoorTop = ViewUtils.getDrawable(context, R.drawable.door_top);

        Drawable cloudStartBottom = ViewUtils.getDrawable(context, R.drawable.cloud_l_bot);
        Drawable cloudStartTop = ViewUtils.getDrawable(context, R.drawable.cloud_l_top);
        Drawable cloudEndBottom = ViewUtils.getDrawable(context, R.drawable.cloud_r_bot);
        Drawable cloudEndTop = ViewUtils.getDrawable(context, R.drawable.cloud_r_top);
        Drawable cloudMiddleBottom = ViewUtils.getDrawable(context, R.drawable.cloud_m_bot);
        Drawable cloudMiddleTop = ViewUtils.getDrawable(context, R.drawable.cloud_m_top);

        Drawable enemyTopLeft = ViewUtils.getDrawable(context, R.drawable.enemy_t_l);
        Drawable enemyTopRight = ViewUtils.getDrawable(context, R.drawable.enemy_t_r);
        Drawable enemyBottomLeft = ViewUtils.getDrawable(context, R.drawable.enemy_b_l);
        Drawable enemyBottomRight = ViewUtils.getDrawable(context, R.drawable.enemy_b_r);

        Drawable princessTop = ViewUtils.getDrawable(context, R.drawable.princess_top);
        Drawable princessBottom = ViewUtils.getDrawable(context, R.drawable.princess_bot);

        for (int index = 0; index < lenght + 60; index++) {
            List<BaseGameObject> column = new ArrayList<>();
            if (!holes.contains(index)) {
                for (int i = 0; i < height; i++) {
                    column.add(new GroundObject(groundDrawable, i));
                }
            }

            for (int i = 0; i < platform.size(); i++) {
                PlaneObject object = platform.get(i);
                if (new EnvironmentInteger(index).equals(object)) {
                    column.add(new PlatformObject(platformDrawable, object.startY));
                }
            }

            for (int i = 0; i < cloud.size(); i++) {
                PlaneObject object = cloud.get(i);
                if (new EnvironmentInteger(index).equals(object)) {
                    if (object.startX == index) {
                        column.add(new CloudObject(cloudStartBottom, object.startY));
                        column.add(new CloudObject(cloudStartTop, object.endY));
                    } else if (object.endX == index) {
                        column.add(new CloudObject(cloudEndBottom, object.startY));
                        column.add(new CloudObject(cloudEndTop, object.endY));
                    } else {
                        column.add(new CloudObject(cloudMiddleBottom, object.startY));
                        column.add(new CloudObject(cloudMiddleTop, object.endY));
                    }
                }
            }

            if (enemyPosition - 1 == index) {
                column.add(new EnemyObject(enemyBottomLeft, height));
                column.add(new EnemyObject(enemyTopLeft, height + 1));
            }
            if (enemyPosition == index) {
                column.add(new EnemyObject(enemyBottomRight, height));
                column.add(new EnemyObject(enemyTopRight, height + 1));
            }

            if (castlePosition - 3 < index && castlePosition + 3 > index) {
                int baseLevel = height;
                int level = 3;
                int secondLevel = 2;
                int firstLevel = baseLevel + level;
                boolean inner = castlePosition - 2 < index && castlePosition + 2 > index;
                int maxForPosition = (inner ? secondLevel : 0) + firstLevel;
                for (int currentCastleLevel = height; currentCastleLevel < maxForPosition; currentCastleLevel++) {
                    if (castlePosition == index &&
                            (currentCastleLevel == height || currentCastleLevel == height + 1)) {
                        if (currentCastleLevel == height) {
                            //bottom door
                            column.add(new CastleObject(castleDoorBottom, currentCastleLevel));
                        } else {
                            // top door
                            column.add(new CastleObject(castleDoorTop, currentCastleLevel));
                        }
                    } else if (inner && currentCastleLevel == firstLevel - 1) {
                        column.add(new CastleObject(castleRoofDrawable, currentCastleLevel));
                    } else if (currentCastleLevel == maxForPosition - 1) {
                        // sky roof
                        column.add(new CastleObject(castleRoofSkyDrawable, currentCastleLevel));
                    } else if ((index == castlePosition + 1 || index == castlePosition - 1)
                            && currentCastleLevel == firstLevel) {
                        if (index == castlePosition + 1) {
                            column.add(new CastleObject(castleWindowRightDrawable, currentCastleLevel));
                        } else {
                            column.add(new CastleObject(castleWindowLeftDrawable, currentCastleLevel));
                        }
                    } else {
                        column.add(new CastleObject(castleBrickDrawale, currentCastleLevel));
                    }
                }

            }

            if (castlePosition + 1 == index) {
                column.add(new PlatformObject(princessBottom, height));
                column.add(new PlatformObject(princessTop, height + 1));
            }

            int position = grass.indexOf(new EnvironmentInteger(index));
            if (position != -1) {
                PlaneObject object = grass.get(position);
                if (object.startX == index) {
                    column.add(new GrassObject(startGrassDrawable, object.startY));
                } else if (object.endX == index) {
                    column.add(new GrassObject(endGrassDrawable, object.startY));
                } else {
                    column.add(new GrassObject(middleGrassDrawable, object.startY));
                }
            }
            list.add(column.toArray(new BaseGameObject[column.size()]));
        }

        return list;
    }

    public EnvironmentBuilder setEnemy(int enemyPosition) {
        this.enemyPosition = enemyPosition;
        return this;
    }

    public static class PlaneObject {
        final int startX;
        final int endX;
        final int startY;
        final int endY;

        public PlaneObject(int startX, int endX, int startY, int endY) {
            this.startX = startX;
            this.endX = endX;
            this.startY = startY;
            this.endY = endY;
        }

        public PlaneObject(int startX, int endX, int y) {
            this.startX = startX;
            this.endX = endX;
            this.startY = y;
            this.endY = y;
        }

        @Override
        public boolean equals(Object x) {
            if (x instanceof Integer) {
                int xValue = (Integer) x;
                return xValue >= startX && xValue <= endX;
            }
            return false;
        }
    }

    public static class EnvironmentInteger {

        private final int value;

        public EnvironmentInteger(int value) {
            this.value = value;
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof PlaneObject) {
                return value >= ((PlaneObject) o).startX && value <= ((PlaneObject) o).endX;
            }
            return false;
        }
    }
}
