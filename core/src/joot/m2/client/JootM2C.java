package joot.m2.client;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.github.jootnet.mir2.core.actor.Action;
import com.github.jootnet.mir2.core.actor.HumActionInfo;
import com.github.jootnet.mir2.core.actor.HumActionInfos;

import joot.m2.client.map.MapActor;

public class JootM2C extends ApplicationAdapter {
	private Stage stage;
	
	/** 地图绘制 */
	private MapActor map = new MapActor();
	private short mapX;
	private short mapY;
	/** 地图事件 */
	private InputListener inputListenerInMap = new InputListenerInMap();
	
	/** 当前人物动作 */
	private HumActionInfo currentHumAction;
	/** 当前人物动作是第几帧 */
	private short currentHumActionTick;
	/** 当前人物动作开始时间 */
	private long currentHumActionFrameStartTime;
	
	public JootM2C() {
		Assets.init("D:\\Program Files (x86)\\盛大网络\\热血传奇");
	}

	@Override
	public void create () {
		stage = new Stage(new ScreenViewport());
		map.addListener(inputListenerInMap);
		
		currentHumAction = HumActionInfos.StandSouth;
		currentHumActionFrameStartTime = System.currentTimeMillis();
		currentHumActionTick = 1;
		mapX = 300;
		mapY = 300;
		map.enter("0").move(mapX, mapY);
		
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
		
		if (System.currentTimeMillis() - currentHumActionFrameStartTime > currentHumAction.duration) {
			currentHumActionFrameStartTime = System.currentTimeMillis();
			if (++currentHumActionTick > currentHumAction.frameCount) {
				currentHumActionTick = 1;
				
				if (currentHumAction.act == Action.Walk || currentHumAction.act == Action.Run) {
					// 走完或跑完了一步，地图中心坐标更新
					int step = 1;
					if (currentHumAction.act == Action.Run) step++;
					
					switch (currentHumAction.dir) {
					case North:
						mapY -= step;
						break;
					case NorthEast:
						mapY -= step;
						mapX += step;
						break;
					case East:
						mapX += step;
						break;
					case SouthEast:
						mapY += step;
						mapX += step;
						break;
					case South:
						mapY += step;
						break;
					case SouthWest:
						mapY += step;
						mapX -= step;
						break;
					case West:
						mapX -= step;
						break;
					case NorthWest:
						mapY -= step;
						mapX -= step;
						break;
					
					default:
						break;
					}
					
					map.move(mapX, mapY);
				}
			}
		}
		
		map.setHumAction(currentHumAction, currentHumActionTick);
		stage.act(delta);
		stage.draw();
	}

	@Override
	public void dispose () {
		stage.dispose();
	}
	
	/** 鼠标或手指(移动端)在地图上按下时 */
	private boolean mapTouchDown (InputEvent event, float x, float y, int pointer, int button) {
		System.out.println("down");
		return true;
	}

	/** 鼠标或手指(移动端)在地图上抬起时 */
	private void mapTouchUp (InputEvent event, float x, float y, int pointer, int button) {
		System.out.println("up");
	}
	
	/** 鼠标在地图上移动时 */
	private boolean mapMouseMoved (InputEvent event, float x, float y) {
		return false;
	}
	
	private class InputListenerInMap extends InputListener {
		@Override
		public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
			return JootM2C.this.mapTouchDown(event, x, y, pointer, button);
		}
		@Override
		public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
			JootM2C.this.mapTouchUp(event, x, y, pointer, button);
		}
		@Override
		public boolean mouseMoved(InputEvent event, float x, float y) {
			return JootM2C.this.mapMouseMoved(event, x, y);
		}
	}

}
