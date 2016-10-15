package com.mygdx.tetris;

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
    Block[][] positions;
    Array<Block> blocks;

    public Field(Viewport viewport) {
        this.viewport = viewport;
        init();
    }

    public void init() {
        this.blocks = new Array<Block>(Constants.FIELD_HEIGHT * Constants.FIELD_WIDTH);

        positions = new Block[Constants.FIELD_HEIGHT][Constants.FIELD_WIDTH];
        for (int row = 0; row < Constants.FIELD_HEIGHT; row++) {
            for (int col = 0; col < Constants.FIELD_WIDTH; col++) {
                positions[row][col] = null;
            }
        }
    }

    public void update(float delta) {
        // checks if adjacent rows are full of positions from bottom to top, then removes rows.

        int cnt = 0;
        Vector2 velocity = Constants.BLOCK_VELOCITY;
        Constants.State state = null;

        for (int row = 0; row < Constants.FIELD_HEIGHT; row++) {
            // counts positions in a row
            for (int col = 0; col < Constants.FIELD_WIDTH; col++) {
                if (positions[row][col] != null) cnt++;
                else break;
            }
            // resets row
            if (cnt == Constants.FIELD_WIDTH) {
                for (int col = 0; col < Constants.FIELD_WIDTH; col++) {
                    this.positions[row][col] = null;
                }

                state = Constants.State.ROW_DELETE;
            }
            cnt = 0;
        }

        if (state == Constants.State.ROW_DELETE) {
            for (int row = 0; row < Constants.FIELD_HEIGHT; row++) {
                for (int col = 0; col < Constants.FIELD_WIDTH; col++) {
                    if (this.positions[row][col] != null) {
                        this.positions[row][col].velocity = new Vector2(velocity.x, velocity.y);
                        this.blocks.add(this.positions[row][col]);
                    }
                }
            }
        }


    }

    public void render(ShapeRenderer renderer) {
        for (int row = 0; row < Constants.FIELD_HEIGHT; row++) {
            for (int col = 0; col < Constants.FIELD_WIDTH; col++) {
                if (positions[row][col] != null)
                    positions[row][col].render(renderer);
            }
        }
    }
}
