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
    public static final String TAG = Block.class.getName();
    Vector2 pos;
    Vector2 velocity;

    public Block(Block block) {
        this.pos = new Vector2(block.pos.x, block.pos.y);
        this.velocity = new Vector2(Constants.BLOCK_VELOCITY.x, Constants.BLOCK_VELOCITY.y);
    }

    public Block(Vector2 pos) {
        this.pos = new Vector2(pos.x, pos.y);
        this.velocity = new Vector2(Constants.BLOCK_VELOCITY.x, Constants.BLOCK_VELOCITY.y);
    }

    public void update(float delta) {
        this.pos.mulAdd(this.velocity, delta);
    }

    private void rotate(Vector2 center, float degree) {
        float x = this.pos.x + Constants.BLOCK_SIZE / 2;
        float y = this.pos.y + Constants.BLOCK_SIZE / 2;
        this.pos.x = center.x + (x - center.x) * MathUtils.cosDeg(degree) - (y - center.y) * MathUtils.sinDeg(degree);
        this.pos.y = center.y + (y - center.y) * MathUtils.cosDeg(degree) + (x - center.x) * MathUtils.sinDeg(degree);
        this.pos.x -= Constants.BLOCK_SIZE / 2;
        this.pos.y -= Constants.BLOCK_SIZE / 2;
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
