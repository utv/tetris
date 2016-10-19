package com.mygdx.tetris;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

/**
 * Created by Computer on 9/8/2016.
 */
public class TetrisScreen extends InputAdapter implements Screen {
    public static final String TAG = TetrisScreen.class.getName();
    public static final Color BG_COLOR = Color.BLACK;

    ExtendViewport tetrisViewport;
    ScreenViewport hudViewport;
    Constants.State state;
    Field field;
    FallingPiece fallingPiece;
    ShapeRenderer renderer;

    @Override
    public void show() {
        tetrisViewport = new ExtendViewport(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT);
        renderer = new ShapeRenderer();
        renderer.setAutoShapeType(true);
        field = new Field(this.tetrisViewport);
        fallingPiece = new FallingPiece(this.tetrisViewport);
        state = Constants.State.PIECE_MOVING;
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void render(float delta) {
        // processInput
        processInput(delta, state, fallingPiece, field);
        // update
        update(delta, state, fallingPiece, field);

        // render
        tetrisViewport.apply(true);
        Gdx.gl.glClearColor(BG_COLOR.r, BG_COLOR.g, BG_COLOR.b, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        renderer.setProjectionMatrix(tetrisViewport.getCamera().combined);
        renderer.begin();
        fallingPiece.render(renderer);
        field.render(renderer);
        renderer.end();
    }

    private void update(float delta, Constants.State state, FallingPiece fallingPiece, Field field) {
        field.update(delta);
        fallingPiece.update(delta, field);
    }

    private void processInput(float delta, Constants.State state, FallingPiece piece, Field field) {

        if (piece.isMoving){
            if (Gdx.input.isKeyJustPressed(Input.Keys.A)) {
                piece.rotate(false, field);
                return;
            }

            if (Gdx.input.isKeyJustPressed(Input.Keys.S)) {
                piece.rotate(true, field);
                return;
            }

            if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
                piece.moveLeft(field);
                return;
            }

            if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
                piece.moveRight(field);
                return;
            }

            if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
                piece.attach();
                return;
            }
        }
    }

    @Override
    public void resize(int width, int height) {
        tetrisViewport.update(width, height, true);
        field.init();
        fallingPiece.init();
        fallingPiece.generate();
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
