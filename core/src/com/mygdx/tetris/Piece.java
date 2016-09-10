package com.mygdx.tetris;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Created by Computer on 9/8/2016.
 */
public class Piece {
    public static final String TAG = Piece.class.getName();
    Array<Block> blocks;
    Viewport viewport;
    boolean isMoving;
    boolean canRotate;

    public Piece(Viewport viewport) {
        this.viewport = viewport;
        this.blocks = new Array<Block>(Constants.PIECE_SIZE);
        this.isMoving = true;
        this.canRotate = true;
    }

    public void update(float delta) {
        if (isMoving) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.A)) {
                this.rotate(false);
            }

            if (Gdx.input.isKeyJustPressed(Input.Keys.S)) {
                this.rotate(true);
            }

            for (Block block : blocks) {
                block.update(delta);
            }
        }
    }

    public boolean checkCollision(Piece anotherPiece) {
        // hits died piece or not
        for (Block anotherBlock : anotherPiece.blocks) {
            for (int i = 0; i < this.blocks.size; i++) {
                if (this.blocks.get(i).pos.y - anotherBlock.pos.y <= Constants.BLOCK_SIZE)
                    return true;
            }
        }

        return isHitGround();
    }

    public boolean isHitGround() {
        for (Block block : blocks) {
            if (block.pos.y <= 0) return true;
        }
        return false;
    }

    private void rotate(boolean clockwise) {
        if (!this.canRotate) return;

        // rotates about first block of a piece itself.
        Vector2 centroid = this.blocks.get(0).pos;
        for (int i = 0; i < blocks.size; i++) {
            if (i > 0) {
                this.blocks.get(i).rotate90(centroid, clockwise);
            }
        }
    }

    public void render(ShapeRenderer renderer) {
        for (Block block : blocks) {
            block.render(renderer);
        }
    }
}
