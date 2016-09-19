package com.mygdx.tetris;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Created by Computer on 9/12/2016.
 */
public class Field {
    public static final String TAG = Field.class.getName();
    Viewport viewport;
    Block[][] blocks;

    public Field(Viewport viewport) {
        this.viewport = viewport;
        init();
    }

    public void init() {
        blocks = new Block[Constants.FIELD_HEIGHT][Constants.FIELD_WIDTH];
        for (int row = 0; row < Constants.FIELD_HEIGHT; row++) {
            for (int col = 0; col < Constants.FIELD_WIDTH; col++) {
                blocks[row][col] = null;
            }
        }
    }

    public void update(Piece piece) {
        if (isPieceCollided(piece)) {
            Gdx.app.log(TAG, "piece size = " + piece.blocks.size);
            for (Block block : piece.blocks) {
                int row = MathUtils.ceil(block.pos.y);
                int col = MathUtils.ceil(block.pos.x);
                Gdx.app.log(TAG, "col = " + col + ", row = " + row);
                Vector2 pos = new Vector2(col, row);
                if (this.blocks[row][col] == null) this.blocks[row][col] = new Block(pos);

            }
            piece.isMoving = false;
        }
    }

    private boolean isPieceCollided(Piece piece) {
        for (int row = 0; row < Constants.FIELD_HEIGHT; row++) {
            for (int col = 0; col < Constants.FIELD_WIDTH; col++) {
                if (blocks[row][col] != null) {
                    for (Block block : piece.blocks) {
                        if (block.pos.dst(blocks[row][col].pos) <= Constants.BLOCK_SIZE
                                && block.pos.x == blocks[row][col].pos.x) {
                            Gdx.app.log(TAG, "isPieceCollided:: col = " + col + ", row = " + row);
                            return true;
                        }

                    }
                }
            }
        }

        if (piece.isHitGround()) return true;
        return false;
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
