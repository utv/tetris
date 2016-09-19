package com.mygdx.tetris;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Created by amata on 9/10/16.
 */
public class Square extends Piece {
    public Square(Viewport viewport) {
        super(viewport);
        this.canRotate = false;
        create();
    }

    public void create() {
        for (int i = 0; i < Constants.PIECE_SIZE; i++) {
//            float x = Constants.FIELD_WIDTH * this.blockRatioWidth / 2 - 2 * this.blockRatioWidth;
            float x = (Constants.FIELD_WIDTH * Constants.BLOCK_SIZE / 2) - 2 * Constants.BLOCK_SIZE;
            float y = Constants.FIELD_HEIGHT;
            Vector2 pos = new Vector2(x + (i % 2) * Constants.BLOCK_SIZE, y + (i / 2) * Constants.BLOCK_SIZE);
            this.blocks.add(new Block(pos));
        }
    }
}
