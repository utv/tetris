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
 *
 * Wall kicks: https://tetris.wiki/SRS#Wall_Kicks
 */
public class Piece {
    public static final String TAG = Piece.class.getName();
    Array<Block> blocks;
    Viewport viewport;
    boolean isMoving;
    boolean canRotate;
    Constants.PieceType type;
    Vector2 centroid;
    int centroidBlockPos;

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
        int rand = new Random().nextInt(5);
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
        else if (rand == 3) {
            jShape(Constants.pieceOrigin);
        }
        else if (rand == 4) {
            sShape(Constants.pieceOrigin);
        }
    }

    /*
     *  S shape
     */
    public void sShape(Vector2 origin) {
        this.type = Constants.PieceType.S_SHAPE;
        this.canRotate = true;
        this.isMoving = true;

        Vector2 pos = new Vector2(origin.x, origin.y);
        this.blocks.set(0, new Block(pos));
        for (int i = 0; i < Constants.PIECE_SIZE / 2; i++) {
            pos = new Vector2(origin.x + i * Constants.BLOCK_SIZE, origin.y);
            this.blocks.set(i, new Block(pos));
        }
        for (int i = Constants.PIECE_SIZE / 2; i < Constants.PIECE_SIZE; i++) {
            pos = new Vector2(origin.x + (i - 1) * Constants.BLOCK_SIZE, origin.y + Constants.BLOCK_SIZE);
            this.blocks.set(i, new Block(pos));
        }
        this.centroid =
                new Vector2(this.blocks.get(1).pos.x + Constants.BLOCK_SIZE / 2, this.blocks.get(1).pos.y + Constants.BLOCK_SIZE / 2);

    }

    /*
     *  J shape
     */
    public void jShape(Vector2 origin) {
        this.type = Constants.PieceType.J_SHAPE;
        this.canRotate = true;
        this.isMoving = true;

        Vector2 pos = new Vector2(origin.x, origin.y);
        this.blocks.set(0, new Block(pos));
        for (int i = 1; i < Constants.PIECE_SIZE; i++) {
            pos = new Vector2(origin.x + (i - 1) * Constants.BLOCK_SIZE, origin.y - Constants.BLOCK_SIZE);
            this.blocks.set(i, new Block(pos));
        }
        this.centroid =
            new Vector2(this.blocks.get(1).pos.x + Constants.BLOCK_SIZE / 2, this.blocks.get(1).pos.y + Constants.BLOCK_SIZE / 2);
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
        this.centroid =
            new Vector2(this.blocks.get(1).pos.x + Constants.BLOCK_SIZE / 2, this.blocks.get(1).pos.y + Constants.BLOCK_SIZE / 2);

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
        this.centroid = new Vector2(this.blocks.get(2).pos.x, this.blocks.get(2).pos.y);
    }

    public void update(float delta, Field field) {
        if (isHitGround()) {
            for (Block block : this.blocks) {
                int row = 0;
                if (block.pos.y > 0.0f) row = MathUtils.ceil(block.pos.y);
                int col = getFieldColumn(block.pos.x);
                Gdx.app.log(TAG, "x = " + block.pos.x + ", col = " + col);
                Gdx.app.log(TAG, "y = " + block.pos.y + ", row = " + row);
                if (field.blocks[row][col] == null) {
                    float x = col * Constants.BLOCK_SIZE;
                    float y = row * Constants.BLOCK_SIZE;
                    field.blocks[row][col] = new Block(new Vector2(x, y));
                }
            }

            //this.generate();
        }
        else if (isOnTopOfAnotherBlock(field)) {
            this.addPieceToField(field);
            //this.generate();
        }
        else if (this.isMoving) {
            for (Block block : blocks) {
                block.update(delta);
            }

            // updates centroids
            this.centroid.mulAdd(this.blocks.get(0).velocity, delta);

        }
    }

    private Vector2 getFieldPosition(Vector2 blockPos) {
        int row = getFieldRow(blockPos.y);
        int col = getFieldColumn(blockPos.x);
        blockPos.x = col * Constants.BLOCK_SIZE;
        blockPos.y = row * Constants.BLOCK_SIZE;
        return new Vector2(col, row);
    }

    protected int getFieldRow(float y) {
        int remainder  = (int) (y % Constants.FIELD_HEIGHT);
        int row;
        if (y - remainder > 0.5) row = MathUtils.ceil(y);
        else row = MathUtils.floor(y);

        row = Math.max(0, row);
        return Math.min(Constants.FIELD_HEIGHT - 1, row);
    }

    protected int getFieldColumn(float x) {
        int remainder  = (int) (x % Constants.FIELD_WIDTH);
        int col;
        if (x - remainder > 0.5) col = MathUtils.ceil(x);
        else col = MathUtils.floor(x);

        col = Math.max(0, col);
        return Math.min(Constants.FIELD_WIDTH - 1, col);
    }

    private boolean isOnField(Array<Block> newBlocks) {
        for (Block block: newBlocks) {
            if (block.pos.x < 0) return false;
            if (block.pos.x > (Constants.FIELD_WIDTH - 1) * Constants.BLOCK_SIZE) return false;
        }
        return true;
    }

    public boolean isHitGround() {
        for (Block block: this.blocks) {
            if (block.pos.y <= 0) return true;
        }
        return false;
    }

    public boolean isOverlap(Array<Block> newBlocks, Field field) {
        for (int row = 0; row < Constants.FIELD_HEIGHT; row++) {
            for (int col = 0; col < Constants.FIELD_WIDTH; col++) {
                if (field.blocks[row][col] != null) {
                    for (int i = 0; i < Constants.PIECE_SIZE; i++) {
                        Vector2 pos = newBlocks.get(i).pos;
                        if (getFieldColumn(pos.x) == col && getFieldRow(pos.y) == row) {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    protected boolean isOnTopOfAnotherBlock(Field field) {
        for (Block block : this.blocks) {
            int blockRow = getFieldRow(block.pos.y);
            int blockCol = getFieldColumn(block.pos.x);
            if (blockRow - 1 >= 0 && field.blocks[blockRow - 1][blockCol] != null) {
                Gdx.app.log(TAG, "Yes::isOnTopOfAnotherBlock:: row = " + blockRow  + ", col = " + blockCol);
                return true;
            }
        }
        return false;
    }

    public boolean isValidMove(Array<Block> blocks, Field field) {
        if (isOverlap(blocks, field) || !isOnField(blocks))
            return false;

        return true;
    }

    protected void addPieceToField(Field field) {
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
        }

    }

    public void moveLeft(Field field) {
        // new block positions
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
        // new block positions
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

    public void attach() {
        for (Block block : this.blocks) {
            block.velocity.y *= 10.5f;
        }
    }

    public void render(ShapeRenderer renderer) {
        for (Block block : blocks) {
            block.render(renderer);
        }
    }

}
