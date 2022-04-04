package joot.m2.client;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import joot.m2.client.map.MapActor;

public class JootM2C extends ApplicationAdapter {
	private Stage stage;
	private MapActor map = new MapActor();
	
	public JootM2C() {
		Assets.init("D:\\Program Files (x86)\\盛大网络\\热血传奇");
	}

	@Override
	public void create () {
		stage = new Stage(new ScreenViewport());
		map.enter("0").move(300, 300);
		stage.addActor(map);
		Gdx.input.setInputProcessor(stage);
	}

	@Override
	public void resize (int width, int height) {
		boolean canResize = true;
		if (width < 800) {
			width = 800;
			canResize = false;
		}
		if (height < 600) {
			height = 600;
			canResize = false;
		}
		if (!canResize)
			Gdx.graphics.setWindowedMode(width, height);
		// See below for what true means.
		stage.getViewport().update(width, height, true);
		map.setBounds(0, 0, width, height);
	}

	@Override
	public void render () {
		float delta = Gdx.graphics.getDeltaTime();
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.act(delta);
		stage.draw();
	}

	@Override
	public void dispose () {
		stage.dispose();
	}

}
