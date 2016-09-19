package com.mygdx.tetris;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Created by Computer on 9/8/2016.
 */
public class Block {
    Vector2 pos;
    Vector2 velocity;


    public Block(Vector2 pos) {
        this.pos = pos;
        this.velocity = Constants.BLOCK_VELOCITY;

    }

    public void update(float delta) {
        this.pos.mulAdd(this.velocity, delta);
    }

    private void rotate(Vector2 center, float degree) {
        float x = this.pos.x;
        float y = this.pos.y;
        this.pos.x = center.x + (x - center.x) * MathUtils.cosDeg(degree) - (y - center.y) * MathUtils.sinDeg(degree);
        this.pos.y = center.y + (y - center.y) * MathUtils.cosDeg(degree) + (x - center.x) * MathUtils.sinDeg(degree);
    }

    // Rotates block around a point.
    public void rotate90(Vector2 center, boolean clockwise) {
        if (clockwise)
            rotate(center, -90);
        else
            rotate(center, 90);
    }

    public void render(ShapeRenderer renderer) {
        renderer.setColor(Color.GOLD);
        renderer.set(ShapeRenderer.ShapeType.Filled);
        renderer.rect(pos.x, pos.y, Constants.BLOCK_SIZE, Constants.BLOCK_SIZE);
    }
}
