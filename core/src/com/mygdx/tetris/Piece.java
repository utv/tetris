package com.mygdx.tetris;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g3d.particles.influencers.ColorInfluencer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.Random;

/**
 * Created by Computer on 9/8/2016.
 */
public class Piece implements Cloneable {
    public static final String TAG = Piece.class.getName();
    Array<Block> blocks;
    Viewport viewport;
    boolean isMoving;
    boolean canRotate;
    Constants.PieceType type;

    public Piece(Viewport viewport) {
        this.viewport = viewport;
        this.isMoving = true;
        this.canRotate = true;
        init();
    }

    public void init() {
        this.blocks = new Array<Block>(Constants.PIECE_SIZE);
        for (int i = 0; i < Constants.PIECE_SIZE; i++) {
            this.blocks.add(new Block(new Vector2(0, 0)));
        }
        generate();
    }

    public void generate() {
        int rand = new Random().nextInt(2);
        if (rand == 0) {
            square(Constants.squareOrigin);
        }
        else if (rand == 1) {
            stick(Constants.stickOrigin);
        }
        else square(Constants.squareOrigin);
    }

    public void square(Vector2 origin) {
        this.type = Constants.PieceType.SQUARE;
        this.canRotate = false;
        this.isMoving = true;
        for (int i = 0; i < Constants.PIECE_SIZE; i++) {
            /*float x = (Constants.FIELD_WIDTH * Constants.BLOCK_SIZE / 2) - 2 * Constants.BLOCK_SIZE;
            float y = Constants.FIELD_HEIGHT;*/
            Vector2 pos = new Vector2(origin.x + (i % 2) * Constants.BLOCK_SIZE, origin.y + (i / 2) * Constants.BLOCK_SIZE);
            this.blocks.set(i, new Block(pos));
        }
    }

    public void stick(Vector2 origin) {
        this.type = Constants.PieceType.STICK;
        this.canRotate = true;
        this.isMoving = true;
        for (int i = 0; i < Constants.PIECE_SIZE; i++) {
            /*float x = (Constants.FIELD_WIDTH * Constants.BLOCK_SIZE / 2) - 2 * Constants.BLOCK_SIZE;
            float y = Constants.FIELD_HEIGHT / 2;*/
            Vector2 pos = new Vector2(origin.x + i * Constants.BLOCK_SIZE, origin.y);
            this.blocks.set(i, new Block(pos));
        }
    }


    public void update(float delta) {
        // process input
        if (isMoving) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.A)) {
                this.rotate(false);
            }

            if (Gdx.input.isKeyJustPressed(Input.Keys.S)) {
                this.rotate(true);
            }

            if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
                moveLeft();
                if (!isOnField()) {
                    // resets positions
                    for (int i = 0; i < Constants.PIECE_SIZE; i++) {
                        this.blocks.get(i).pos.x += Constants.BLOCK_SIZE;
                    }
                }
            }

            if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
                moveRight();
                if (!isOnField()) {
                    // resets positions
                    for (int i = 0; i < Constants.PIECE_SIZE; i++) {
                        this.blocks.get(i).pos.x -= Constants.BLOCK_SIZE;
                    }
                }
            }

            for (Block block : blocks) {
                block.update(delta);
            }
        }
    }

    private void moveLeft() {
        for (Block block : blocks) {
            block.pos.x -= Constants.BLOCK_SIZE;
        }
    }

    private void moveRight() {
        for (Block block : blocks) {
            block.pos.x += Constants.BLOCK_SIZE;
        }
    }

    private boolean isOnField() {
        for (Block block : blocks) {
            if (block.pos.x < 0) return false;
            if (block.pos.x > (Constants.FIELD_WIDTH - 1) * Constants.BLOCK_SIZE) return false;
        }
        return true;
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
