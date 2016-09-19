package com.mygdx.tetris;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Computer on 9/8/2016.
 */
public class Constants {
    public static final float WORLD_WIDTH = 15.0f;
    public static final float WORLD_HEIGHT = 20.0f;
    public static final int FIELD_WIDTH = 10;
    public static final int FIELD_HEIGHT = 20;

    public static final Color BG_COLOR = Color.BLACK;
    public static final float BLOCK_SIZE = 1.0f;
    public static final int NUM_BLOCKS = 100;
    public static final int PIECE_SIZE = 4;
    public static final Vector2 BLOCK_ACCELERATION = new Vector2(0, -5.0f);
    public static final Vector2 BLOCK_VELOCITY = new Vector2(0, -5.0f);

}
