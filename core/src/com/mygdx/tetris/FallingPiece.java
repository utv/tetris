package com.mygdx.tetris;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Created by Computer on 10/13/2016.
 */

public class FallingPiece extends Piece {
    public FallingPiece(Viewport viewport) {
        super(viewport);
    }

    public void update(float delta, Field field) {
        if (isHitGround()) {
            for (Block block : this.blocks) {
                int row = 0;
                if (block.pos.y > 0.0f) row = MathUtils.ceil(block.pos.y);
                int col = getFieldColumn(block.pos.x);
                Gdx.app.log(TAG, "x = " + block.pos.x + ", col = " + col);
                Gdx.app.log(TAG, "y = " + block.pos.y + ", row = " + row);
                if (field.positions[row][col] == null) {
                    float x = col * Constants.BLOCK_SIZE;
                    float y = row * Constants.BLOCK_SIZE;
                    field.positions[row][col] = new Block(new Vector2(x, y));
                }
            }

            this.generate();
        }
        else if (isOnTopOfAnotherBlock(field)) {
            this.addPieceToField(field);
            this.generate();
        }
        else if (this.isMoving) {
            for (Block block : blocks) {
                block.update(delta);
            }

            // updates centroids
            this.centroid.mulAdd(this.blocks.get(0).velocity, delta);

        }
    }
}
