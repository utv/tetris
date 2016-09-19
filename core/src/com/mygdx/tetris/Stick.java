package com.mygdx.tetris;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Created by Computer on 9/8/2016.
 */
public class Stick extends Piece {
    public Stick(Viewport viewport) {
        super(viewport);

        for (int i = 0; i < Constants.PIECE_SIZE; i++) {
//            float x = Constants.FIELD_WIDTH * this.blockRatioWidth / 2 - 2 * this.blockRatioWidth;
            float x = (Constants.FIELD_WIDTH * Constants.BLOCK_SIZE / 2) - 2 * Constants.BLOCK_SIZE;
            float y = Constants.FIELD_HEIGHT / 2;
            Vector2 pos = new Vector2(x + i * Constants.BLOCK_SIZE, y);
            this.blocks.add(new Block(pos));
        }
    }

}
