package joot.m2.client.map;

import java.util.HashMap;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Group;

import joot.m2.client.Assets;
import static joot.m2.client.Drawing.*;

/**
 * 地图绘制对象
 */
public final class MapActor extends Group {
	
	private java.util.Map<String, Map> maps = new HashMap<>();
	private String mapNo_;
	private Map map_;
	private int roleX_;
	private int roleY_;
	
	/**
	 * 进入地图
	 * 
	 * @param mapNo 地图编号
	 * @return 当前对象
	 */
	public MapActor enter(String mapNo) {
		if (mapNo_ != null && mapNo_.equals(mapNo)) return this;
		mapNo_ = mapNo;
		map_ = null;
		return this;
	}
	
	/**
	 * 设置地图展示中心坐标
	 * <br>
	 * 即是当前玩家游戏坐标
	 * 
	 * @param roleX 游戏坐标x
	 * @param roleY 游戏坐标y
	 */
	public void move(int roleX, int roleY) {
		roleX_ = roleX;
		roleY_ = roleY;
	}
	
	@Override
	public void act(float delta) {
		if (map_ == null) {
			if (mapNo_ != null) {
				if (maps.containsKey(mapNo_)) {
					map_ = maps.get(mapNo_);
				} else {
					if (!Assets.contains("Map/" + mapNo_)) {
						Assets.load("Map/" + mapNo_);
					} else if (Assets.isLoaded("Map/" + mapNo_)) {
						map_ = Assets.get("Map/" + mapNo_);
						maps.put(mapNo_, map_);
					} else {
						Assets.update(); // 加载一次，针对地图
					}
				}
			}
		} else {
			RectI pixel = new RectI();
			RectI game = new RectI();
			calcCache(pixel, game);
			for (int w = 0; w < game.width; ++w) {
				for (int h = 0; h < game.height; ++h) {
					String tileFileName = map_.getTileFileName((short) (game.x - 1 + w), (short) (game.y - 1 + h));
					String smTileFileName = map_.getSmTileFileName((short) (game.x - 1 + w), (short) (game.y - 1 + h));
					String objFileName = map_.getObjFileName((short) (game.x - 1 + w), (short) (game.y - 1 + h));
					if (tileFileName != null) {
						if (Assets.contains(tileFileName)) {
							if (Assets.isLoaded(tileFileName)) {
								map_.setTileFileName((short) (game.x - 1 + w), (short) (game.y - 1 + h), null);
								map_.setTileTexture((short) (game.x - 1 + w), (short) (game.y - 1 + h), Assets.get(tileFileName));
							}
						} else {
							Assets.load(tileFileName);
						}
					}
					if (smTileFileName != null) {
						if (Assets.contains(smTileFileName)) {
							if (Assets.isLoaded(smTileFileName)) {
								map_.setSmTileFileName((short) (game.x - 1 + w), (short) (game.y - 1 + h), null);
								map_.setSmTileTexture((short) (game.x - 1 + w), (short) (game.y - 1 + h), Assets.get(smTileFileName));
							}
						} else {
							Assets.load(smTileFileName);
						}
					}
					if (objFileName != null) {
						if (Assets.contains(objFileName)) {
							if (Assets.isLoaded(objFileName)) {
								map_.setObjFileName((short) (game.x - 1 + w), (short) (game.y - 1 + h), null);
								Texture objTex = Assets.get(objFileName);
								if (objTex.getHeight() <= 32) {
									// 如果是单个地图块大小obj图像，则直接赋值
									map_.setObjTexture((short) (game.x - 1 + w), (short) (game.y - 1 + h), objTex);
								} else {
									// 这里是我的一个灵感，我将原来的m2中纵向横跨多个地图块的obj图像以地图块高度拆分到多个
									// 即向上分，比如我们在（1，10）处有一个48*160的obj图像，则把这幅图分到（1，10）=>（1，6）
									// 这样有好处，绘制的时候不需要多画（原来必须向下多画一些才能保证视区元素完整），更好做半透明（当人物在树下房檐下时更好针对某个地图块做半透明）
									//	唯一可能出现的麻烦是多层问题。比如原来（1，9）处也有一个超出32高度的obj
									int upCellCount = (int) Math.ceil(objTex.getHeight() / 32.f);
									for (int upIdx = 0; upIdx < upCellCount; ++upIdx) {
										TextureRegion region = new TextureRegion(objTex, 0, 
												Math.max(0, objTex.getHeight() - (upIdx + 1) * 32),
												objTex.getWidth(),
												Math.min(32, objTex.getHeight() - upIdx * 32));
										map_.addObjTextureRegion((short) (game.x - 1 + w), (short) (game.y - 1 - upIdx + h), region);
									}
								}
							}
						} else {
							Assets.load(objFileName);
						}
					}
				}
			}
			Assets.update(40); // 加载多次，针对纹理
		}
		super.act(delta);
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		if (map_ != null) {
			RectI pixel = new RectI();
			RectI game = new RectI();
			calcDisplay(pixel, game);
			int sx = pixel.x;
			int sy = pixel.y;
			
			sx = pixel.x;
			for (int w = 0; w < game.width; ++w) {
				sy = pixel.y;
				for (int h = 0; h < game.height; ++h) {
					Texture tile = map_.getTileTexture((short) (game.x + w - 1), (short) (game.y + h - 1));
					// 绘制大地砖
					if (tile != null) {
						transDraw(batch, (int) getWidth(), (int) getHeight(), tile, sx, sy);
					}
					sy += 32;
				}
				sx += 48;
			}
			
			sx = pixel.x;
			for (int w = 0; w < game.width; ++w) {
				sy = pixel.y;
				for (int h = 0; h < game.height; ++h) {
					Texture smTile = map_.getSmTileTexture((short) (game.x + w - 1), (short) (game.y + h - 1));
					// 绘制小地砖
					if (smTile != null) {
						transDraw(batch, (int) getWidth(), (int) getHeight(), smTile, sx, sy);
					}
					sy += 32;
				}
				sx += 48;
			}
			
			sx = pixel.x;
			for (int w = 0; w < game.width; ++w) {
				sy = pixel.y;
				for (int h = 0; h < game.height; ++h) {
					Texture obj = map_.getObjTexture((short) (game.x + w - 1), (short) (game.y + h - 1));
					// 绘制对象层
					if (obj != null) {
						transDraw(batch, (int) getWidth(), (int) getHeight(), obj, sx, sy);
					}
					sy += 32;
				}
				sx += 48;
			}
			
			// 绘制纹理补充
			for (TextureRegion objRegion : map_.getObjsTextureRegion()) {
				short[] xy = map_.getObjTextureRegion(objRegion);
				if (xy[0] < game.x - 1 || xy[0] > game.x + game.width - 2) continue;
				if (xy[1] < game.y - 1 || xy[1] > game.y + game.height - 2) continue;
				sx = pixel.x + (xy[0] - game.x + 1) * 48;
				sy = pixel.y + (xy[1] - game.y + 1) * 32;
				transDraw(batch, (int) getWidth(), (int) getHeight(), objRegion, sx, sy
						+ 32 - objRegion.getRegionHeight()); // 32 - height是最上面的那个拆分块的空白高度
			}
		}
		super.draw(batch, parentAlpha);
	}
	
	/**
	 * 通过地图中心位置和画布大小计算绘制区域和地图展示区域
	 * <br>
	 * 会向外延申一些，用作缓存
	 * 
	 * @param pixel 画布需要绘制内容的区域
	 * @param game 地图需要绘制到画布的区域
	 */
	private void calcCache(RectI pixel, RectI game) {
		// 画布横向可容纳的一半地图块数量（为了视觉效果更好，先减去一个用于绝对居中的块）
		int maxHalfCellH = (int) Math.ceil(((int) getWidth() - 48) / 2 / 48);
		// 画布纵向可容纳的一半地图块数量（为了视觉效果更好，先减去一个用于绝对居中的块）
		int maxHalfCellV = (int) Math.ceil(((int) getHeight() - 32) / 2 / 32);
		
		// 从画布中间往左最多能延申的块数量（-10是冗余缓存量）
		int leftMinCell = roleX_ - maxHalfCellH - 10;
		for(;leftMinCell < 1;++leftMinCell);
		// 从画布中间往上最多能延申的块数量（-10是冗余缓存量）
		int topMinCell = roleY_ - maxHalfCellV - 10;
		for(;topMinCell < 1;++topMinCell);
		// 从画布中间往右最多能延申的块数量（-10是冗余缓存量）
		int rightMaxCell = roleX_ + maxHalfCellH + 10;
		for(;rightMaxCell > map_.getWidth();--rightMaxCell);
		// 从画布中间往下最多能延申的块数量（-50是冗余缓存量）
		int bottomMaxCell = roleY_ + maxHalfCellV + 50;
		for(;bottomMaxCell > map_.getHeight();--bottomMaxCell);
		
		game.x = leftMinCell;
		game.y = topMinCell;
		game.width = rightMaxCell - leftMinCell + 1;
		game.height = bottomMaxCell - topMinCell + 1;
		
		pixel.x = ((int) getWidth() - 48) / 2 - (roleX_ - leftMinCell) * 48; 
		pixel.y = ((int) getHeight() - 32) / 2 - (roleY_ - topMinCell) * 32;
		pixel.width = (rightMaxCell - leftMinCell + 1) * 48;
		pixel.height = (bottomMaxCell - topMinCell + 1) * 32;
	}
	
	/**
	 * 通过地图中心位置和画布大小计算绘制区域和地图展示区域
	 * <br>
	 * 将不会向外延申一些
	 * 
	 * @param pixel 画布需要绘制内容的区域
	 * @param game 地图需要绘制到画布的区域
	 */
	private void calcDisplay(RectI pixel, RectI game) {
		// 为了绝对居中，都是先减去一个绝对居中的块
		
		int gameLeft = roleX_ - 1; // 往左移动一格
		int pixelLeft = (int) getWidth() / 2 - 24 - 48; // 可视区域左侧还剩的像素（往左移动一格后）
		for (;gameLeft > 1 && pixelLeft >= 0; --gameLeft, pixelLeft -= 48);
		game.x = gameLeft;
		pixel.x = pixelLeft;
		
		int gameRight = roleX_ + 1;
		int pixelRight = (int) getWidth() / 2 + 24 + 48;
		for (;gameRight <= map_.getWidth() && pixelRight <= (int) getWidth(); ++gameRight, pixelRight += 48);
		game.width = gameRight - gameLeft + 1; // 加上中间的块
		pixel.width = pixelRight - pixelLeft; // 由于算的是边界，不需要加中间块

		if (game.x % 2 == 0) {
			// 保证第一列不是双数，不然黑边
			if (game.x > 1) {
				game.x -= 1;
				game.width += 1;
				pixel.x -= 48;
				pixel.width += 48;
			}
		}
		
		int gameTop = roleY_ - 1;
		int pixelTop = (int) getHeight() / 2 - 16 - 32;
		for (;gameTop > 1 && pixelTop >= 0; --gameTop, pixelTop -= 32);
		game.y = gameTop;
		pixel.y = pixelTop;
		
		int gameBottom = roleY_ + 1;
		int pixelBottom = (int) getHeight() / 2 + 16 + 32;
		for (;gameBottom <= map_.getHeight() && pixelBottom <= (int) getHeight(); ++gameBottom, pixelBottom += 32);
		game.height = gameBottom - gameTop + 1;
		pixel.height = pixelBottom - pixelTop;
	}
}
