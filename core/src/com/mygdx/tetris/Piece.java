package com.mygdx.tetris;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.Random;

/**
 * Created by Computer on 9/8/2016.
 */
public class Piece {
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

    }

    public void generate() {
        int rand = new Random().nextInt(2);
        if (rand == 0) {
            square(Constants.squareOrigin);
        }
        else if (rand == 1) {
            stick(Constants.stickOrigin);
        }
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


    public void update(float delta, Field field) {
        if (isHitGround() || isOnTopOfAnotherBlock(field)) {
            this.addPieceToField(field);

            //this.isMoving = false;
            this.generate();
        }
        else if (this.isMoving) {
            for (Block block : blocks) {
                block.update(delta);
            }

        }
    }

    private Vector2 getFieldPosition(Vector2 blockPos) {
        int row = MathUtils.ceil(blockPos.y);
        int col = MathUtils.ceil(blockPos.x);
        row = Math.max(0, row);
        col = Math.max(0, col);
        row = Math.min(Constants.FIELD_HEIGHT - 1, row);
        col = Math.min(Constants.FIELD_WIDTH - 1, col);
        return new Vector2(col, row);
    }

    private int getFieldRow(float y) {
        int row = MathUtils.ceil(y);
        row = Math.max(0, row);
        return Math.min(Constants.FIELD_HEIGHT - 1, row);
    }

    private int getFieldColumn(float x) {
        int col = MathUtils.ceil(x);
        col = Math.max(0, col);
        return Math.min(Constants.FIELD_WIDTH - 1, col);
    }

    private boolean isOnField(Array<Vector2> positions) {
        for (Vector2 pos: positions) {
            int col = MathUtils.ceil(pos.x);
            if (col < 0) return false;
            if (col > (Constants.FIELD_WIDTH - 1) * Constants.BLOCK_SIZE) return false;
        }
        return true;
    }

    public boolean isHitGround() {
        for (Block block: this.blocks) {
            if (block.pos.y <= 0) return true;
        }
        return false;
    }

    public boolean isOverlap(Array<Vector2> positions, Field field) {
        for (int row = 0; row < Constants.FIELD_HEIGHT; row++) {
            for (int col = 0; col < Constants.FIELD_WIDTH; col++) {
                if (field.blocks[row][col] != null) {
                    for (int i = 0; i < Constants.PIECE_SIZE; i++) {
                        Vector2 pos = positions.get(i);
                        if (getFieldColumn(pos.x) == col && getFieldRow(pos.y) == row) {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    private boolean isOnTopOfAnotherBlock(Field field) {
        for (int row = 0; row < Constants.FIELD_HEIGHT; row++) {
            for (int col = 0; col < Constants.FIELD_WIDTH; col++) {
                if (field.blocks[row][col] != null) {
                    for (Block block : this.blocks) {
                        int blockRow = getFieldRow(block.pos.y);
                        int blockCol = getFieldColumn(block.pos.x);
                        if (blockRow == row + 1 && blockCol == col) {
                            Gdx.app.log(TAG, "isOnTopOfAnotherBlock:: row = " + row + ", col = " + col);
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public boolean isValidMove(Vector2 direction, Field field) {
        Array<Vector2> positions = new Array<Vector2>(Constants.PIECE_SIZE);
        for (int i = 0; i < Constants.PIECE_SIZE; i++) {
            Vector2 block = this.blocks.get(i).pos;
            Vector2 pos = new Vector2(block.x + direction.x, block.y + direction.y);
            positions.add(pos);
        }

        if (isOverlap(positions, field) || !isOnField(positions))
            return false;

        return true;
    }

    private void addPieceToField(Field field) {
        for (Block block : this.blocks) {
            int row = getFieldRow(block.pos.y);
            int col = getFieldColumn(block.pos.x);

            if (field.blocks[row][col] == null)
                field.blocks[row][col] = new Block(new Vector2(block.pos.x, block.pos.y));
        }
    }

    public void moveLeft(Field field) {
        Vector2 direction = new Vector2(Constants.BLOCK_SIZE * -1.0f, 0);
        if (!isValidMove(direction, field)) {
            return;
        }

        // updates new positions for a piece.
        for (Block block : blocks) {
            block.pos.x -= Constants.BLOCK_SIZE;
        }

    }

    public void moveRight(Field field) {
        Vector2 direction = new Vector2(Constants.BLOCK_SIZE * 1.0f, 0);
        if (!isValidMove(direction, field))
            return;

        for (Block block : blocks) {
            block.pos.x += Constants.BLOCK_SIZE;
        }
    }

    public void rotate(boolean clockwise) {
        if (!this.canRotate) return;

        // rotates about first block of a piece itself.
        Vector2 centroid = this.blocks.get(0).pos;
        for (int i = 0; i < blocks.size; i++) {
            if (i > 0) {
                this.blocks.get(i).rotate90(centroid, clockwise);
            }
        }
        Vector2 pos = getFieldPosition(this.blocks.get(0).pos);
        Gdx.app.log(TAG, "field pos x =" + pos.x + ", y = " + pos.y);
    }

    public void render(ShapeRenderer renderer) {
        for (Block block : blocks) {
            block.render(renderer);
        }
    }

}
