package com.fogok.explt;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.fogok.explt.core.Handler;

public class Main extends ApplicationAdapter {

    private SpriteBatch batch;
    private Handler handler;


	@Override
	public void create () {
        //initNatives
		batch = new SpriteBatch();
        ///

        //initCore
        handler = new Handler();
        ///
	}

	@Override
	public void render () {
        //natives
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
        ///

        handler.handle(batch);

        //natives
		batch.end();
        ///
	}

    @Override
    public void dispose() {
        batch.dispose();
        handler.dispose();
    }
}
