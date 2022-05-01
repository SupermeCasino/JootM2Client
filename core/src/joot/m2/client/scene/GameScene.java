package joot.m2.client.scene;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.github.jootnet.m2.core.actor.Action;
import com.github.jootnet.m2.core.actor.ChrBasicInfo;
import com.github.jootnet.m2.core.actor.Direction;
import com.github.jootnet.m2.core.actor.HumActionInfo;
import com.github.jootnet.m2.core.actor.HumActionInfos;
import com.github.jootnet.m2.core.net.MessageType;
import com.github.jootnet.m2.core.net.messages.HumActionChange;

import joot.m2.client.App;
import joot.m2.client.ui.MapActor;
import joot.m2.client.ui.StatusBar;
import joot.m2.client.util.NetworkUtil;

/**
 * 游戏场景
 * 
 * @author linxing
 *
 */
public final class GameScene extends BaseScene {
	
	/** 地图绘制 */
	private MapActor mapActor;
	/** 下方状态栏 */
	private StatusBar statusBar;

	/** 人物 */
	public Map<String, ChrBasicInfo> hums; // 其他人
	
	
	@Override
	public void show() {
        stage = new Stage();
		
		mapActor = new MapActor();
		mapActor.setFillParent(true);
		mapActor.addListener(new InputListenerInMap());
		stage.addActor(mapActor);

		statusBar = new StatusBar();
		stage.addActor(statusBar);
		
		super.show();

		Gdx.graphics.setTitle("将唐传奇" + "-" + App.Chr.name);
		hums = new HashMap<>();
		mapActor.enter(App.Chr.mapNo).add(App.Chr);
	}
	
	@Override
	public void resize(int width, int height) {
		var sizeTooLower = false;
		if (width < 800) {
			sizeTooLower = true;
		}
		if (height < 600) {
			sizeTooLower = true;
		}

		if (!sizeTooLower) {
			statusBar.setWidth(width);
			stage.getViewport().update(width, height, true);
		}
	}
	
	@Override
	public void render(float delta) {

		// 处理服务器消息
		doServerMessages();

		// 当前角色特殊处理
		calcMeAction();
		// 其他玩家
		if (hums != null) {
			hums.values().parallelStream().forEach(hum -> hum.act(App.SmoothMoving));
		}
		
		// 地图的视角和绘制偏移以当前角色为准
		mapActor.setCenter(App.Chr.x, App.Chr.y).setShiftX(App.Chr.shiftX).setShiftY(App.Chr.shiftY);
		
		super.render(delta);
	}

    /** 地图事件 */
    private boolean mouseDownFlag = false;
    private int mouseX;
    private int mouseY;
    private int mouseDownButton;
	/** 鼠标或手指(移动端)在地图上按下时 */
	private boolean mapTouchDown (InputEvent event, float x, float y, int pointer, int button) {
		mouseDownFlag = true;
		mouseX = (int) x;
		mouseY = (int) y;
		mouseDownButton = button;
		stage.setKeyboardFocus(mapActor);
		return true;
	}

	/** 鼠标或手指(移动端)在地图上抬起时 */
	private void mapTouchUp (InputEvent event, float x, float y, int pointer, int button) {
		mouseDownFlag = false;
	}
	
	/** 鼠标在地图上移动时 */
	private boolean mapMouseMoved (InputEvent event, float x, float y) {
		return false;
	}

	/** 鼠标或手指(移动端)在地图上按下并移动时 */
	private void mapTouchDragged(InputEvent event, float x, float y, int pointer) {
		mouseX = (int) x;
		mouseY = (int) y;
	}
	
	/** 键盘按下事件 */
	private boolean mapKeyUp(InputEvent event, int keycode) {
		if (keycode == Keys.ENTER) {
			statusBar.focusInput();
		}
		return true;
	}

	// 计算当前玩家动作
	private void calcMeAction() {

		var isLastFrame = App.Chr.actionTick == App.Chr.action.frameCount; // 上一帧的人物动作是否为最后一帧

		App.Chr.act(App.SmoothMoving); // 尝试将人物动作向前推进一帧

		isLastFrame &= App.Chr.actionTick == 1; // 还需成功推进一帧才表示前面确认是最后一帧

		var humAction = App.Chr.action; // 人物按计划当前帧应该做的动作（可能是未完成的动作（走跑），也可能是站立（前一个走跑动作已完成））

		var nextAction = calcMeNextAction(humAction); // 根据鼠标动作计算出来的人物当前应该变更的动作
		
		if (humAction.equals(nextAction)) return; // 这里一般是鼠标未按下或点击自己
		if (humAction.act != Action.Stand && !isLastFrame) return; // 这一步还没走完，等一下下（如果当前是站立，则直接可以开始新动作）

		App.Chr.setAction(nextAction);
		NetworkUtil.sendHumActionChange(App.Chr); // 向服务器报告当前角色状态更改
	}

	// 根据鼠标（手指）动作计算当前人物应该进行的动作
	private HumActionInfo calcMeNextAction(HumActionInfo action) {
		if (!mouseDownFlag) return action;
		// 鼠标与屏幕中心x、y轴的距离
		var mapCenter = mapActor.humXY2MapXY(App.Chr.x, App.Chr.y);
        var disX = mouseX - (mapCenter[0] + App.Chr.shiftX + 24); // 脚踩地图块中心
        var disY = mouseY - (mapCenter[1] + App.Chr.shiftY + 16);
        
        if (Math.abs(disX) < 24 && Math.abs(disY) < 16) return action;
        
        // 来自网络的虚拟摇杆算法，计算方向
        // 勾股定理求斜边
        var obl = Math.sqrt(Math.pow(disX, 2) + Math.pow(disY, 2));
        // 求弧度
        var rad = disY < 0 ? Math.acos(disX / obl) : (Math.PI * 2- Math.acos(disX / obl));
        // 弧度转角度
        var angle = 180 / Math.PI * rad;

        var moveStep = 0;
        if (mouseDownButton == Buttons.LEFT) {
        	moveStep = 1;
        } else if (mouseDownButton == Buttons.RIGHT) {
        	moveStep = 2;
        }
        
        Direction dir = null;
        var canWalk = false;
        if (angle >= 337.5 || angle < 22.5) {
        	dir = Direction.East;
            canWalk = mapActor.isCanWalk(App.Chr.x + 1, App.Chr.y); // 目标位置（一个身位）是否可达
            if(moveStep == 2 && !mapActor.isCanWalk(App.Chr.x + 2, App.Chr.y)) // 若想要跑动，且目标不可达，则尝试改为走到最近的一个身位
                --moveStep;
        } else if (angle >= 22.5 && angle < 67.5) {
        	dir = Direction.SouthEast;
            canWalk = mapActor.isCanWalk(App.Chr.x + 1, App.Chr.y + 1);
            if(moveStep == 2 && !mapActor.isCanWalk(App.Chr.x + 2, App.Chr.y + 2))
                --moveStep;
        } else if (angle >= 67.5 && angle < 112.5) {
        	dir = Direction.South;
            canWalk = mapActor.isCanWalk(App.Chr.x, App.Chr.y + 1);
            if(moveStep == 2 && !mapActor.isCanWalk(App.Chr.x, App.Chr.y + 2))
                --moveStep;
        } else if (angle >= 112.5 && angle < 157.5) {
        	dir = Direction.SouthWest;
            canWalk = mapActor.isCanWalk(App.Chr.x - 1, App.Chr.y + 1);
            if(moveStep == 2 && !mapActor.isCanWalk(App.Chr.x - 2, App.Chr.y + 2))
                --moveStep;
        } else if (angle >= 157.5 && angle < 202.5) {
        	dir = Direction.West;
            canWalk = mapActor.isCanWalk(App.Chr.x - 1, App.Chr.y);
            if(moveStep == 2 && !mapActor.isCanWalk(App.Chr.x - 2, App.Chr.y))
                --moveStep;
        } else if (angle >= 202.5 && angle < 247.5) {
        	dir = Direction.NorthWest;
            canWalk = mapActor.isCanWalk(App.Chr.x - 1, App.Chr.y - 1);
            if(moveStep == 2 && !mapActor.isCanWalk(App.Chr.x - 2, App.Chr.y - 2))
                --moveStep;
        } else if (angle >= 247.5 && angle < 292.5) {
        	dir = Direction.North;
        	canWalk = mapActor.isCanWalk(App.Chr.x, App.Chr.y - 1);
            if(moveStep == 2 && !mapActor.isCanWalk(App.Chr.x, App.Chr.y - 2))
                --moveStep;
        } else if (angle >= 292.5 && angle < 337.5) {
        	dir = Direction.NorthEast;
        	 canWalk = mapActor.isCanWalk(App.Chr.x + 1, App.Chr.y - 1);
             if(moveStep == 2 && !mapActor.isCanWalk(App.Chr.x + 2, App.Chr.y - 2))
                 --moveStep;
        }
        
        if (canWalk) {
	        if (moveStep == 1) {
	        	return HumActionInfos.walk(dir);
	        } else if (moveStep == 2) {
	        	return HumActionInfos.run(dir);
	        }
        }
		return HumActionInfos.stand(dir);
	}
	
	private class InputListenerInMap extends InputListener {
		@Override
		public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
			return GameScene.this.mapTouchDown(event, x, y, pointer, button);
		}
		@Override
		public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
			GameScene.this.mapTouchUp(event, x, y, pointer, button);
		}
		@Override
		public boolean mouseMoved(InputEvent event, float x, float y) {
			return GameScene.this.mapMouseMoved(event, x, y);
		}
		@Override
		public void touchDragged(InputEvent event, float x, float y, int pointer) {
			GameScene.this.mapTouchDragged(event, x, y, pointer);
		}
		@Override
		public boolean keyUp(InputEvent event, int keycode) {
			return GameScene.this.mapKeyUp(event, keycode);
		}
	}

	// 处理服务器消息
	private void doServerMessages() {
		NetworkUtil.recv(msg -> {
			if (msg.type() == MessageType.HUM_ACTION_CHANGE) {
				var action = (HumActionChange) msg;
				if (action.name.equals(App.Chr.name)) {
					// 当前玩家动作改变消息，可以视为服务器确认
					App.Chr.nextX = action.nextX;
					App.Chr.nextY = action.nextY;
				} else {
					// 其他玩家消息，直接修改其状态
					if (hums.containsKey(action.name)) {
						ChrBasicInfo hum = hums.get(action.name);
						hum.setAction(action.action).setPosition(action.x, action.y);
						hum.nextX = action.nextX;
						hum.nextY = action.nextY;
					}
				}
				return true;
			} else if (msg.type() == MessageType.ENTER_RESP) {
				
				return true;
			}
			return false;
		});
	}
	
}