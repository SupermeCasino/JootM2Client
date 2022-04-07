package joot.m2.client;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.github.jootnet.mir2.core.actor.Action;
import com.github.jootnet.mir2.core.actor.Direction;
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
	private boolean mouseDown = false;
	private int mouseX;
	private int mouseY;
	private int mouseButton;
	
	/** 当前人物动作 */
	private HumActionInfo currentHumAction;
	/** 当前人物动作是第几帧 */
	private short currentHumActionTick;
	/** 当前人物动作开始时间 */
	private long currentHumActionFrameStartTime;
	
	public JootM2C() {
		//Assets.init("D:\\Program Files (x86)\\盛大网络\\热血传奇");
		Assets.init("D:\\Program Files\\ShengquGames\\Legend of mir");
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

		calcAction();
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
		mouseDown = true;
		mouseX = (int) x;
		mouseY = (int) y;
		mouseButton = button;
		return true;
	}

	/** 鼠标或手指(移动端)在地图上抬起时 */
	private void mapTouchUp (InputEvent event, float x, float y, int pointer, int button) {
		mouseDown = false;
	}
	
	/** 鼠标在地图上移动时 */
	private boolean mapMouseMoved (InputEvent event, float x, float y) {
		return false;
	}

	/** 鼠标或手指(移动端)在地图上按下并移动时 */
	public void mapTouchDragged(InputEvent event, float x, float y, int pointer) {
		mouseX = (int) x;
		mouseY = (int) y;
	}

	// 计算玩家动作
	private void calcAction() {
		// 当前动作
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
		// 下一步的动作
		HumActionInfo nextAction = calcNextAction();
		if (currentHumAction == nextAction) return;
		if (currentHumAction.act != Action.Stand && currentHumActionTick != 1) return; // 这一步还没走完，等一下下

		currentHumAction = nextAction;
		currentHumActionFrameStartTime = System.currentTimeMillis();
		currentHumActionTick = 1;
	}

	// 根据鼠标（手指）动作计算当前人物应该进行的动作
	private HumActionInfo calcNextAction() {
		if (!mouseDown) return HumActionInfos.stand(currentHumAction.dir);
		// 鼠标与屏幕中心x、y轴的距离
        int xx = mouseX - Gdx.graphics.getWidth() / 2;
        int yy = mouseY - Gdx.graphics.getHeight() / 2;
        
        if (Math.abs(xx) < 48 && Math.abs(yy) < 32) return HumActionInfos.stand(currentHumAction.dir);
        
        // 来自网络的虚拟摇杆算法，计算方向
        // 勾股定理求斜边
        double obl = Math.sqrt(Math.pow(xx, 2) + Math.pow(yy, 2));
        // 求弧度
        double rad = yy < 0 ? Math.acos(xx / obl) : (Math.PI * 2- Math.acos(xx / obl));
        // 弧度转角度
        double angle = 180 / Math.PI * rad;

        Direction dir = null;
        if ((angle >= 337.5 && angle <= 360) || angle < 22.5) {
        	dir = Direction.East;
        } else if (angle >= 22.5 && angle < 67.5) {
        	dir = Direction.SouthEast;
        } else if (angle >= 67.5 && angle < 112.5) {
        	dir = Direction.South;
        } else if (angle >= 112.5 && angle < 157.5) {
        	dir = Direction.SouthWest;
        } else if (angle >= 157.5 && angle < 202.5) {
        	dir = Direction.West;
        } else if (angle >= 202.5 && angle < 247.5) {
        	dir = Direction.NorthWest;
        } else if (angle >= 247.5 && angle < 292.5) {
        	dir = Direction.North;
        } else if (angle >= 292.5 && angle < 337.5) {
        	dir = Direction.NorthEast;
        }
        
        if (mouseButton == Buttons.LEFT) {
        	return HumActionInfos.walk(dir);
        } else if (mouseButton == Buttons.RIGHT) {
        	return HumActionInfos.run(dir);
        }
		return HumActionInfos.stand(currentHumAction.dir);
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
		@Override
		public void touchDragged(InputEvent event, float x, float y, int pointer) {
			JootM2C.this.mapTouchDragged(event, x, y, pointer);
		}
	}

}