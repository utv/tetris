package com.mygdx.tetris;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Created by Computer on 9/12/2016.
 */
public class Field {
    public static final String TAG = Field.class.getName();
    Viewport viewport;
    Block[][] blocks;
    Array<Array<Piece>> pieces;

    public Field(Viewport viewport) {
        this.viewport = viewport;
        init();
    }

    public void init() {
        this.pieces = new Array<Array<Piece>>(Constants.FIELD_HEIGHT);

        blocks = new Block[Constants.FIELD_HEIGHT][Constants.FIELD_WIDTH];
        for (int row = 0; row < Constants.FIELD_HEIGHT; row++) {
            for (int col = 0; col < Constants.FIELD_WIDTH; col++) {
                blocks[row][col] = null;
            }
        }
    }

    public void update(float delta) {
        // checks if adjacent rows are full of blocks from bottom to top, then removes rows.
        //
    }

    public void render(ShapeRenderer renderer) {
        for (int row = 0; row < Constants.FIELD_HEIGHT; row++) {
            for (int col = 0; col < Constants.FIELD_WIDTH; col++) {
                if (blocks[row][col] != null)
                    blocks[row][col].render(renderer);
            }
        }
    }
}
