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

    public List<BaseGameObject[]> build() {
        List<BaseGameObject[]> list = new ArrayList<>();
        Drawable groundDrawable = ViewUtils.getDrawable(context, R.drawable.ground);
        Drawable startGrassDrawable = ViewUtils.getDrawable(context, R.drawable.grass_left);
        Drawable endGrassDrawable = ViewUtils.getDrawable(context, R.drawable.grass_right);
        Drawable middleGrassDrawable = ViewUtils.getDrawable(context, R.drawable.grass_mid);
        Drawable platformDrawable = ViewUtils.getDrawable(context, R.drawable.brick);

        for (int index = 0; index < lenght; index++) {
            List<BaseGameObject> column = new ArrayList<>();
            if (!holes.contains(index)) {
                for (int i = 0; i < height; i++) {
                    column.add(new GroundObject(groundDrawable, i));
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

                position = platform.indexOf(new EnvironmentInteger(index));
                if (position != -1) {
                    PlaneObject object = platform.get(position);
                    column.add(new PlatformObject(platformDrawable, object.startY));
                }

            }
            list.add(column.toArray(new BaseGameObject[column.size()]));
        }

        return list;
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
