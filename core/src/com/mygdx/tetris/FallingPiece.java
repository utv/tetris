package com.mygdx.tetris;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Created by Computer on 10/13/2016.
 */

public class FallingPiece extends Piece {
    public FallingPiece(Viewport viewport) {
        super(viewport);
    }

    public void moveLeft(Field field) {
        // new block blocks
        Array<Block> newBlocks = new Array<Block>(Constants.PIECE_SIZE);
        for (int i = 0; i < Constants.PIECE_SIZE; i++) {
            newBlocks.add(new Block(this.blocks.get(i).pos));
            Block added = newBlocks.peek();
            added.pos.x -= Constants.BLOCK_SIZE;
        }

        if (isValidMove(newBlocks, field)) {
            this.blocks = newBlocks;
            this.centroid.x -= Constants.BLOCK_SIZE;
        }

    }

    public void moveRight(Field field) {
        // new block blocks
        Array<Block> newBlocks = new Array<Block>(Constants.PIECE_SIZE);
        for (int i = 0; i < Constants.PIECE_SIZE; i++) {
            newBlocks.add(new Block(this.blocks.get(i).pos));
            Block added = newBlocks.peek();
            added.pos.x += Constants.BLOCK_SIZE;
        }

        if (isValidMove(newBlocks, field)) {
            this.blocks = newBlocks;
            this.centroid.x += Constants.BLOCK_SIZE;
        }

    }

    /*
 * Rotates if either against the wall or overlaps with other blocks
 * No wall kicks
 */
    public void rotate(boolean clockwise, Field field) {
        if (!this.canRotate) return;

        Array<Block> newBlocks = new Array<Block>(Constants.PIECE_SIZE);
        for (int i = 0; i < Constants.PIECE_SIZE; i++) {
            newBlocks.add(new Block(this.blocks.get(i).pos));
        }

        for (int i = 0; i < Constants.PIECE_SIZE; i++) {
            newBlocks.get(i).rotate90(this.centroid, clockwise);
        }

        if (isValidMove(newBlocks, field)) {
            this.blocks = newBlocks;
        }

    }


    public void update(float delta, Field field) {
        if (isHitGround()) {
            for (Block block : this.blocks) {
                int row = 0;
                if (block.pos.y > 0.0f) row = getFieldRow(block.pos.y);
                int col = getFieldColumn(block.pos.x);
                Gdx.app.log(TAG, "piece x = " + block.pos.x + ", col = " + col);
                Gdx.app.log(TAG, "piece y = " + block.pos.y + ", row = " + row);
                if (field.blocks[row][col] == null) {
                    float x = col * Constants.BLOCK_SIZE;
                    float y = row * Constants.BLOCK_SIZE;
                    field.blocks[row][col] = new Block(new Vector2(x, y));
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
            if (this.centroid != this.blocks.get(Constants.PIECE_SIZE - 1).pos)
                this.centroid.mulAdd(Constants.BLOCK_VELOCITY, delta);
        }
    }
}
