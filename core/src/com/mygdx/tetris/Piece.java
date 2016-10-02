package com.mygdx.tetris;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
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
    Vector2 centroid;

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
        int rand = new Random().nextInt(3);
        if (rand == 0) {
            square(Constants.squareOrigin);
            //stick(Constants.stickOrigin);
        }
        else if (rand == 1) {
            stick(Constants.stickOrigin);
        }
        else if (rand == 2) {
            tShape(Constants.pieceOrigin);
        }
    }

    /*
     *   T shape
     */
    public void tShape(Vector2 origin) {
        this.type = Constants.PieceType.T_SHAPE;
        this.canRotate = true;
        this.isMoving = true;
        for (int i = 0; i < Constants.PIECE_SIZE - 1; i++) {
            Vector2 pos = new Vector2(origin.x + i * Constants.BLOCK_SIZE, origin.y);
            this.blocks.set(i, new Block(pos));
        }

        Vector2 pos = new Vector2(origin.x + Constants.BLOCK_SIZE, origin.y + Constants.BLOCK_SIZE);
        this.blocks.set(Constants.PIECE_SIZE - 1, new Block(pos));
        this.centroid = this.blocks.get(1).pos;
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
        //this.centroid = this.blocks.get(3).pos;
    }

    /*
     * Line shape
     */
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
        this.centroid = this.blocks.get(2).pos;
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
        int row = getFieldRow(blockPos.y);
        int col = getFieldColumn(blockPos.x);
        blockPos.x = col * Constants.BLOCK_SIZE;
        blockPos.y = row * Constants.BLOCK_SIZE;
        return new Vector2(col, row);
    }

    private int getFieldRow(float y) {
        int remainder  = (int) (y % Constants.FIELD_HEIGHT);
        int row;
        if (y - remainder > 0.5) row = MathUtils.ceil(y);
        else row = MathUtils.floor(y);

        row = Math.max(0, row);
        return Math.min(Constants.FIELD_HEIGHT - 1, row);
    }

    private int getFieldColumn(float x) {
        int remainder  = (int) (x % Constants.FIELD_WIDTH);
        int col;
        if (x - remainder > 0.5) col = MathUtils.ceil(x);
        else col = MathUtils.floor(x);

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
        int num = 0;
        for (Block block : this.blocks) {
            int row = getFieldRow(block.pos.y);
            int col = getFieldColumn(block.pos.x);
            Gdx.app.log(TAG, "x = " + block.pos.x + ", y = " + block.pos.y);
            Gdx.app.log(TAG, "row = " + row + ", col = " + col);
            if (field.blocks[row][col] == null) {
                float x = col * Constants.BLOCK_SIZE;
                float y = row * Constants.BLOCK_SIZE;
                field.blocks[row][col] = new Block(new Vector2(x, y));

            }
            num++;
        }
        Gdx.app.log(TAG, "num = " + num);
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

        // rotates about the centroid.
        for (int i = 0; i < blocks.size; i++) {
            this.blocks.get(i).rotate90(this.centroid, clockwise);
        }
    }

    public void render(ShapeRenderer renderer) {
        for (Block block : blocks) {
            block.render(renderer);
        }
    }

}
