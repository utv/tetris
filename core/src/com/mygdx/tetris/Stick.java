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
            float x = viewport.getWorldWidth() / 2 - 2 * Constants.BLOCK_SIZE;
            Vector2 pos = new Vector2(x + i * Constants.BLOCK_SIZE, viewport.getWorldHeight() / 2);
            this.blocks.add(new Block(pos));
        }
    }

}
