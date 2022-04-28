package joot.m2.client.actor;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import com.github.jootnet.mir2.core.actor.Action;
import com.github.jootnet.mir2.core.actor.HumActionInfo;
import com.github.jootnet.mir2.core.actor.HumActionInfos;

import joot.m2.client.App;
import joot.m2.client.image.M2Texture;
import joot.m2.client.util.AssetUtil;

/**
 * 玩家对象
 * 
 * @author 林星
 *
 */
public final class Hum {
	
	private String name;
	private int x;
	private int y;
	private int nextX;
	private int nextY;
	private int shiftX; // 地图绘制时横向偏移量，人物向右时为正，走动时第一帧为8，第二帧为16（动作花费6帧时）
	private int shiftY;
	
	private int humFileIdx;
	private int humIdx;
	private int humEffectFileIdx;
	private int humEffectIdx;
	private int weaponFileIdx;
	private int weaponIdx;
	/*private int weaponEffectFileIdx;
	private int weaponEffectIdx;*/
	
	private HumActionInfo action;
	private short actionTick;
	private long actionFrameStartTime;
	private long actionStartTime;
	
	/**
	 * 通过玩家名称创建角色对象
	 * 
	 * @param name 玩家名称
	 */
	public Hum(String name) {
		this.name = name;
		this.humIdx = 1; // 默认是穿着裤衩光着膀子的秃子
	}
	
	/**
	 * 获取人物名称
	 * 
	 * @return 玩家名称
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * 人物移动到地图坐标
	 * 
	 * @param x 身处地图横坐标
	 * @param y 身处地图纵坐标
	 * @return 当前对象
	 */
	public Hum setPosition(int x, int y) {
		if (x != this.x)
			propertyChangeSupport.firePropertyChange("x", this.x, x);
		if (y != this.y)
			propertyChangeSupport.firePropertyChange("y", this.y, y);
		this.x = x;
		this.y = y;
		this.nextX = x;
		this.nextY = y;
		return this;
	}
	
	/**
	 * 更新玩家动作
	 * 
	 * @param action 玩家当前动作
	 * @return 当前对象
	 */
	public Hum setAction(HumActionInfo action) {
		this.action = action;
		actionTick = 1;
		actionFrameStartTime = System.currentTimeMillis();
		actionStartTime = System.currentTimeMillis();
		return this;
	}
	
	/**
	 * 获取当前人物的动作
	 * 
	 * @return 人物当前动作
	 */
	public HumActionInfo getAction() {
		return action;
	}
	
	/**
	 * 获取当前人物动作的帧号
	 * 
	 * @return 动作帧号
	 */
	public short getActionTick() {
		return actionTick;
	}
	
	/**
	 * 获取人物身处横坐标
	 * 
	 * @return 地图横坐标
	 */
	public int getX() {
		return x;
	}

	/**
	 * 获取人物身处纵坐标
	 * 
	 * @return 地图纵坐标
	 */
	public int getY() {
		return y;
	}

	/**
	 * 设置人物身处横坐标
	 * 
	 * @param x 地图横坐标
	 */
	public void setX(int x) {
		this.x = x;
	}

	/**
	 * 设置人物身处纵坐标
	 * 
	 * @param x 地图纵坐标
	 */
	public void setY(int y) {
		this.y = y;
	}

	/**
	 * 获取角色当前动作完成之后应该到达的位置
	 * 
	 * @return 目标位置横坐标
	 */
	public int getNextX() {
		return nextX;
	}

	/**
	 * 获取角色当前动作完成之后应该到达的位置
	 * 
	 * @return 目标位置纵坐标
	 */
	public int getNextY() {
		return nextY;
	}

	/**
	 * 设置角色当前动作完成之后应该到达的位置
	 * 
	 * @param nextX 目标位置横坐标
	 */
	public void setNextX(int nextX) {
		this.nextX = nextX;
	}

	/**
	 * 设置角色当前动作完成之后应该到达的位置
	 * 
	 * @param nextY 目标位置纵坐标
	 */
	public void setNextY(int nextY) {
		this.nextY = nextY;
	}
	
	/**
	 * 穿衣服
	 * 
	 * @param fileIdx 资源文件编号，从0开始
	 * @param dressIdx 资源文件中的衣服编号（600一组），从1开始
	 * @return 当前对象
	 */
	public Hum setDress(int fileIdx, int dressIdx) {
		this.humFileIdx = fileIdx;
		this.humIdx = dressIdx;
		return this;
	}
	
	/**
	 * 获取人物当前应该绘制的衣服贴图
	 * 
	 * @return 纹理对象
	 */
	public M2Texture getCurrentDressTexture() {
		return AssetUtil.getDress(humFileIdx, humIdx, action, actionTick);
	}
	
	/**
	 * 长翅膀
	 * 
	 * @param fileIdx 资源文件编号，从0开始
	 * @param dressIdx 资源文件中的衣服特效编号（600一组），从1开始
	 * @return 当前对象
	 */
	public Hum setWing(int fileIdx, int dressIdx) {
		this.humEffectFileIdx = fileIdx;
		this.humEffectIdx = dressIdx;
		return this;
	}
	
	/**
	 * 获取人物当前应该绘制的翅膀贴图
	 * 
	 * @return 纹理对象
	 */
	public M2Texture getCurrentWingTexture() {
		return AssetUtil.getHumEffect(humEffectFileIdx, humEffectIdx, action, actionTick);
	}
	
	/**
	 * 拿武器
	 * 
	 * @param fileIdx 资源文件编号，从0开始
	 * @param humIdx 资源文件中的武器编号（600一组），从1开始
	 * @return 当前对象
	 */
	public Hum setWeapon(int fileIdx, int equipIdx) {
		weaponFileIdx = fileIdx;
		weaponIdx = equipIdx;
		return this;
	}

	/**
	 * 获取人物当前应该绘制的武器贴图
	 * 
	 * @return 纹理对象
	 */
	public M2Texture getCurrentWeaponTexture() {
		if (weaponIdx < 1) return null;
		return AssetUtil.getWeapon(weaponFileIdx, weaponIdx, action, actionTick);
	}
	
	/**
	 * 人物动作渐进
	 * <br>
	 * 计算人物动作是否该前进一帧
	 * 
	 * @return 当前对象
	 */
	public Hum act() {
		// 计算当前动作
		boolean actionDone = false;
		var delta = System.currentTimeMillis() - actionStartTime;
		if (!App.SmoothMoving) {
			if (System.currentTimeMillis() - actionFrameStartTime > action.duration) {
				actionFrameStartTime = System.currentTimeMillis();
				if (++actionTick > action.frameCount) {
					actionDone = true;
				}
			}
		} else {
			if (delta > action.duration * action.frameCount) {
				actionDone = true;
			} else {
				actionTick = (short) Math.min(delta / action.duration + 1, action.frameCount);
			}
		}

		if (actionDone) {
			if (action.act == Action.Walk || action.act == Action.Run) {
				// 走完或跑完了一步，地图中心坐标更新
				var step = 1;
				if (action.act == Action.Run) step++;
	
				var nx = x;
				var ny = y;
				switch (action.dir) {
					case North:
						ny -= step;
						break;
					case NorthEast:
						ny -= step;
						nx += step;
						break;
					case East:
						nx += step;
						break;
					case SouthEast:
						ny += step;
						nx += step;
						break;
					case South:
						ny += step;
						break;
					case SouthWest:
						ny += step;
						nx -= step;
						break;
					case West:
						nx -= step;
						break;
					case NorthWest:
						ny -= step;
						nx -= step;
						break;
	
					default:
						break;
				}
				if (nextX == nx && nextY == ny) { // 允许移动
					setPosition(nx, ny);
				}
			}
			setAction(HumActionInfos.stand(action.dir));
		}

		if (!App.SmoothMoving) {
			shift();
		} else {
			shiftSmooth((int) delta);
		}
		
		return this;
	}
	
	private void shift() {
		// 计算人物偏移
		shiftX = 0;
		shiftY = 0;
		if (action.act == Action.Walk || action.act == Action.Run) {
			var step = action.act == Action.Run ? 2 : 1;

			switch (action.dir) {
			case North:
				shiftY = -32 * actionTick / action.frameCount * step; // 向上偏移
				break;
			case NorthEast:
				shiftY = -32 * actionTick / action.frameCount * step;
				shiftX = 48 * actionTick / action.frameCount * step; // 向右偏移
				break;
			case East:
				shiftX = 48 * actionTick / action.frameCount * step;
				break;
			case SouthEast:
				shiftY = 32 * actionTick / action.frameCount * step;
				shiftX = 48 * actionTick / action.frameCount * step;
				break;
			case South:
				shiftY = 32 * actionTick / action.frameCount * step;
				break;
			case SouthWest:
				shiftY = 32 * actionTick / action.frameCount * step;
				shiftX = -48 * actionTick / action.frameCount * step;
				break;
			case West:
				shiftX = -48 * actionTick / action.frameCount * step;
				break;
			case NorthWest:
				shiftY = -32 * actionTick / action.frameCount * step;
				shiftX = -48 * actionTick / action.frameCount * step;
				break;
			
			default:
				break;
			}
		}
	}
	
	private void shiftSmooth(int delta) {
		// 计算人物偏移（平滑）
		shiftX = 0;
		shiftY = 0;
		if (action.act == Action.Walk || action.act == Action.Run) {
			var step = action.act == Action.Run ? 2 : 1;

			switch (action.dir) {
			case North:
				shiftY = -32 * delta * step / action.frameCount / action.duration; // 向上偏移
				break;
			case NorthEast:
				shiftY = -32 * delta * step / action.frameCount / action.duration;
				shiftX = 48 * delta * step / action.frameCount / action.duration; // 向右偏移
				break;
			case East:
				shiftX = 48 * delta * step / action.frameCount / action.duration;
				break;
			case SouthEast:
				shiftY = 32 * delta * step / action.frameCount / action.duration;
				shiftX = 48 * delta * step / action.frameCount / action.duration;
				break;
			case South:
				shiftY = 32 * delta * step / action.frameCount / action.duration;
				break;
			case SouthWest:
				shiftY = 32 * delta * step / action.frameCount / action.duration;
				shiftX = -48 * delta * step / action.frameCount / action.duration;
				break;
			case West:
				shiftX = -48 * delta * step / action.frameCount / action.duration;
				break;
			case NorthWest:
				shiftY = -32 * delta * step / action.frameCount / action.duration;
				shiftX = -48 * delta * step / action.frameCount / action.duration;
				break;
			
			default:
				break;
			}
		}
	}

	/**
	 * 获取人物绘制偏移
	 * <br>
	 * 移动时相对于地图
	 * 
	 * @return 横向偏移量
	 */
	public int getShiftX() {
		return shiftX;
	}

	/**
	 * 获取人物绘制偏移
	 * <br>
	 * 移动时相对于地图
	 * 
	 * @return 横向纵移量
	 */
	public int getShiftY() {
		return shiftY;
	}

	private final PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
	
	public void addPropertyChangeListener(PropertyChangeListener listener) { 
		propertyChangeSupport.addPropertyChangeListener(listener); 
	}
    
	public void removePropertyChangeListener(PropertyChangeListener listener) { 
		propertyChangeSupport.removePropertyChangeListener(listener); 
	}
}
