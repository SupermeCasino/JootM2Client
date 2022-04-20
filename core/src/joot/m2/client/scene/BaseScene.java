package joot.m2.client.scene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;

import joot.m2.client.util.AssetUtil;

/**
 * 场景基类
 * 
 * @author linxing
 *
 */
abstract class BaseScene implements Screen {
	// 舞台
	protected Stage stage;

	@Override
	public void show() {
		if (stage != null)
			Gdx.input.setInputProcessor(stage);
	}

	@Override
	public void render(float delta) {
		AssetUtil.update(40);
		
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		if (stage != null) {
			stage.act(delta);
			stage.draw();
		}
	}

	@Override
	public void resize(int width, int height) {
		if (stage != null)
			stage.getViewport().update(width, height, true);
	}

	@Override
	public void pause() { }

	@Override
	public void resume() { }

	@Override
	public void hide() { }

	@Override
	public void dispose() { }

}
