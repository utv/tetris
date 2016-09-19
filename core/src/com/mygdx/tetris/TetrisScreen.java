package com.mygdx.tetris;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.util.Random;

/**
 * Created by Computer on 9/8/2016.
 */
public class TetrisScreen extends InputAdapter implements Screen {
    public static final String TAG = TetrisScreen.class.getName();
    public static final Color BG_COLOR = Color.BLACK;

    ExtendViewport tetrisViewport;
    ScreenViewport hudViewport;
    Field field;
    Piece piece;
    Constants.State state;
    ShapeRenderer renderer;

    @Override
    public void show() {
        tetrisViewport = new ExtendViewport(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT);
        renderer = new ShapeRenderer();
        renderer.setAutoShapeType(true);
        field = new Field(this.tetrisViewport);
        state = Constants.State.PIECE_MOVING;
        Gdx.input.setInputProcessor(this);
    }

    private Piece generatePiece() {
        int rand = new Random().nextInt(2);
        if (rand == 0) return new Square(this.tetrisViewport);
        if (rand == 1) return new Stick(this.tetrisViewport);
        return new Stick(this.tetrisViewport);
    }

    @Override
    public void render(float delta) {
        // processInput
        if (!piece.isMoving) piece = generatePiece();
        processInput(delta, piece);
        // update
        piece.update(delta);
        field.update(piece);

        // render
        tetrisViewport.apply(true);
        Gdx.gl.glClearColor(BG_COLOR.r, BG_COLOR.g, BG_COLOR.b, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        renderer.setProjectionMatrix(tetrisViewport.getCamera().combined);
        renderer.begin();
        if (piece.isMoving) piece.render(renderer);
        field.render(renderer);
        renderer.end();
    }

    private void processInput(float delta, Piece piece) {

    }

    @Override
    public void resize(int width, int height) {
        tetrisViewport.update(width, height, true);
        field.init();
        piece = generatePiece();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        renderer.dispose();
    }

    @Override
    public void dispose() {
        renderer.dispose();
    }
}
