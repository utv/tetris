package com.mygdx.tetris;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.DelayedRemovalArray;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Created by Computer on 9/8/2016.
 */
public class Pieces {
    public static final String TAG = Pieces.class.getName();
    DelayedRemovalArray<Piece> pieceList;
    Viewport viewport;

    public Pieces(Viewport viewport) {
        this.viewport = viewport;
        init();
    }

    public void init() {
        pieceList = new DelayedRemovalArray<Piece>(Constants.NUM_BLOCKS);
        if (pieceList.size == 0) pieceList.add(new Stick(viewport));
    }

    private void generate() {

    }

    public void update(float delta) {
        Piece latest = pieceList.get(pieceList.size - 1);
        // field is not empty
        if (latest.isMoving && pieceList.size > 1) {
            for (int i = 0; i < pieceList.size - 1; i++) {
                if (latest.checkCollision(pieceList.get(i))) {
                    latest.isMoving = false;
                }
            }
        }
        // only one piece with empty field
        if (latest.isMoving && pieceList.size == 1) {
            if (latest.isHitGround()) latest.isMoving = false;
        }
        // creates new moving piece
        else if (!latest.isMoving){
            pieceList.add(new Stick(viewport));
        }

        for (Piece piece : pieceList) {
            piece.update(delta);
        }
    }

    public void render(ShapeRenderer renderer) {
        for (Piece piece : pieceList) {
            piece.render(renderer);
        }
    }
}
