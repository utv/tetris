package com.mygdx.tetris;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
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
    Array<Block> fallingBlocks;
    Piece remainingBlocks;

    public Field(Viewport viewport) {
        this.viewport = viewport;
        init();
    }

    public void init() {
        this.fallingBlocks = new Array<Block>(Constants.FIELD_HEIGHT * Constants.FIELD_WIDTH);
        this.remainingBlocks = new Piece(this.viewport);

        blocks = new Block[Constants.FIELD_HEIGHT][Constants.FIELD_WIDTH];
        for (int row = 0; row < Constants.FIELD_HEIGHT; row++) {
            for (int col = 0; col < Constants.FIELD_WIDTH; col++) {
                blocks[row][col] = null;
            }
        }
    }

    public int update(float delta) {
        // deletes filled rows
        int lastDeletedRow = -1;
        for (int row = 0; row < Constants.FIELD_HEIGHT; row++) {
            int numBlockInRow = 0;
            for (int col = 0; col < Constants.FIELD_WIDTH; col++) {
                if (this.blocks[row][col] != null) {
                    numBlockInRow++;
                }
            }

            // exits loop with some rows deleted
            if (numBlockInRow < Constants.FIELD_WIDTH && lastDeletedRow >= 0) {
                break;
            }

            // deletes a row
            if (numBlockInRow == Constants.FIELD_WIDTH) {
                for (int col = 0; col < Constants.FIELD_WIDTH; col++) {
                    this.blocks[row][col] = null;
                }
                lastDeletedRow = row;
            }
        }

        return lastDeletedRow;
    }

    public void render(ShapeRenderer renderer) {

        for (Block falling : fallingBlocks) {
            if (falling != null) {
                falling.velocity = new Vector2(Constants.BLOCK_VELOCITY).add(0.0f, 2.0f);
                falling.render(renderer);
            }
        }

        for (int row = 0; row < Constants.FIELD_HEIGHT; row++) {
            for (int col = 0; col < Constants.FIELD_WIDTH; col++) {
                if (blocks[row][col] != null)
                    blocks[row][col].render(renderer);
            }
        }
    }
}
