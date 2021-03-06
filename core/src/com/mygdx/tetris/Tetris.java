package com.mygdx.tetris;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Tetris extends Game {

	@Override
	public void create () {
        showTetrisScreen();
	}

    public void showTetrisScreen() {
        setScreen(new TetrisScreen());
    }

}
