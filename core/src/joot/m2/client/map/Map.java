package joot.m2.client.map;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/** 地图信息描述对象  */
public final class Map {

	/** 地图宽度 */
	private short width;
	/** 地图高度 */
	private short height;
	
	/** 地图块大地砖所在文件及索引 */
	private String[][] tilesFileName;
	/** 地图块小地砖所在文件及索引 */
	private String[][] smTilesFileName;
	/** 地图对象层图片所在文件及索引 */
	private String[][] objsFileName;
	
	/** 地图块大地砖纹理 */
	private Texture[][] tilesTexture;
	/** 地图块小地砖纹理 */
	private Texture[][] smTilesTexture;
	/** 地图对象层图片纹理 */
	private Texture[][] objsTexture;
	
	/** 带顺序的纹理补充 */
	private List<TextureRegion> objTextureRegions = new ArrayList<>();
	/** 纹理补充的xy坐标 */
	private java.util.Map<TextureRegion, short[]> objTextureRegions2XY = new HashMap<>();
	
	/**
	 * 使用地图宽高初始化地图对象
	 * 
	 * @param width 地图宽（横向块数量）
	 * @param height 地图高（纵向块数量）
	 */
	public Map(short width, short height) {
		this.width = width;
		this.height = height;
		tilesFileName = new String[width][height];
		smTilesFileName = new String[width][height];
		objsFileName = new String[width][height];
		tilesTexture = new Texture[width][height];
		smTilesTexture = new Texture[width][height];
		objsTexture = new Texture[width][height];
	}
	
	/** 获取地图宽度 */
	public short getWidth() {
		return width;
	}
	/** 获取地图高度 */
	public short getHeight() {
		return height;
	}
	
	/**
	 * 设置特定地图块的大地砖资源文件名
	 * 
	 * @param x 地图横坐标
	 * @param y 地图纵坐标
	 * @param fileName 资源文件名，用于AssetManager的load
	 */
	public void setTileFileName(short x, short y, String fileName) {
		tilesFileName[x][y] = fileName;
	}
	/**
	 * 获取特定地图块的大地砖资源文件名
	 * 
	 * @param x 地图横坐标
	 * @param y 地图纵坐标
	 * @return 资源文件名，用于AssetManager的load
	 */
	public String getTileFileName(short x, short y) {
		return tilesFileName[x][y];
	}
	
	/**
	 * 设置特定地图块的小地砖资源文件名
	 * 
	 * @param x 地图横坐标
	 * @param y 地图纵坐标
	 * @param fileName 资源文件名，用于AssetManager的load
	 */
	public void setSmTileFileName(short x, short y, String fileName) {
		smTilesFileName[x][y] = fileName;
	}
	/**
	 * 获取特定地图块的小地砖资源文件名
	 * 
	 * @param x 地图横坐标
	 * @param y 地图纵坐标
	 * @return 资源文件名，用于AssetManager的load
	 */
	public String getSmTileFileName(short x, short y) {
		return smTilesFileName[x][y];
	}
	
	/**
	 * 设置特定地图块的对象层资源文件名
	 * 
	 * @param x 地图横坐标
	 * @param y 地图纵坐标
	 * @param fileName 资源文件名，用于AssetManager的load
	 */
	public void setObjFileName(short x, short y, String fileName) {
		objsFileName[x][y] = fileName;
	}
	/**
	 * 获取特定地图块的对象层资源文件名
	 * 
	 * @param x 地图横坐标
	 * @param y 地图纵坐标
	 * @return 资源文件名，用于AssetManager的load
	 */
	public String getObjFileName(short x, short y) {
		return objsFileName[x][y];
	}
	
	/**
	 * 设置特定地图块的大地砖纹理
	 * 
	 * @param x 地图横坐标
	 * @param y 地图纵坐标
	 * @param tex 大地砖纹理
	 */
	public void setTileTexture(short x, short y, Texture tex) {
		tilesTexture[x][y] = tex;
	}
	/**
	 * 获取特定地图块的大地砖纹理
	 * 
	 * @param x 地图横坐标
	 * @param y 地图纵坐标
	 * @return 大地砖纹理
	 */
	public Texture getTileTexture(short x, short y) {
		return tilesTexture[x][y];
	}
	
	/**
	 * 设置特定地图块的小地砖纹理
	 * 
	 * @param x 地图横坐标
	 * @param y 地图纵坐标
	 * @param tex 小地砖纹理
	 */
	public void setSmTileTexture(short x, short y, Texture tex) {
		smTilesTexture[x][y] = tex;
	}
	/**
	 * 获取特定地图块的小地砖纹理
	 * 
	 * @param x 地图横坐标
	 * @param y 地图纵坐标
	 * @return 小地砖纹理
	 */
	public Texture getSmTileTexture(short x, short y) {
		return smTilesTexture[x][y];
	}
	
	/**
	 * 设置特定地图块的对象层纹理
	 * 
	 * @param x 地图横坐标
	 * @param y 地图纵坐标
	 * @param tex 对象层纹理
	 */
	public void setObjTexture(short x, short y, Texture tex) {
		objsTexture[x][y] = tex;
	}
	/**
	 * 获取特定地图块的对象层纹理
	 * 
	 * @param x 地图横坐标
	 * @param y 地图纵坐标
	 * @return 对象层纹理
	 */
	public Texture getObjTexture(short x, short y) {
		return objsTexture[x][y];
	}
	
	/**
	 * 添加特定地图块的对象层补充纹理
	 * 
	 * @param x 地图横坐标
	 * @param y 地图纵坐标
	 * @param tex 对象层补充纹理
	 */
	public void addObjTextureRegion(short x, short y, TextureRegion tex) {
		objTextureRegions2XY.put(tex, new short[] {x, y});
		objTextureRegions.add(tex);
		objTextureRegions.sort((tr1, tr2) -> {
			return objTextureRegions2XY.get(tr1)[1] - objTextureRegions2XY.get(tr2)[1];
		});
	}
	
	/**
	 * 获取地图所有对象补充纹理
	 * 
	 * @return 地图所有对象补充纹理
	 */
	public List<TextureRegion> getObjsTextureRegion() {
		return objTextureRegions;
	}
	
	/**
	 * 获取地图块补充层纹理所在块坐标
	 * 
	 * @param tex 补充层纹理
	 * @return x,y分为在第0，1个元素
	 */
	public short[] getObjTextureRegion(TextureRegion tex) {
		return objTextureRegions2XY.get(tex);
	}
}
