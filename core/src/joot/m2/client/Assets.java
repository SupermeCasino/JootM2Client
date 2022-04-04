package joot.m2.client;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Texture;

import joot.m2.client.image.W_LLoader;
import joot.m2.client.map.Map;
import joot.m2.client.map.MapLoader;

/** 资源加载工具类 */
public final class Assets {

	/** 用于地图的异步加载对象 */
	private static AssetManager Map = new AssetManager(null, false);
	
	/** 用于大地砖纹理的异步加载对象 */
	private static AssetManager Tiles = new AssetManager(null, false);
	/** 用于小地砖纹理的异步加载对象 */
	private static AssetManager SmTiles = new AssetManager(null, false);
	/** 用于对象层纹理的异步加载对象 */
	private static AssetManager Objects = new AssetManager(null, false);
	/** 用于对象层纹理的异步加载对象 */
	private static AssetManager Objects2 = new AssetManager(null, false);
	/** 用于对象层纹理的异步加载对象 */
	private static AssetManager Objects3 = new AssetManager(null, false);
	/** 用于对象层纹理的异步加载对象 */
	private static AssetManager Objects4 = new AssetManager(null, false);
	/** 用于对象层纹理的异步加载对象 */
	private static AssetManager Objects5 = new AssetManager(null, false);
	/** 用于对象层纹理的异步加载对象 */
	private static AssetManager Objects6 = new AssetManager(null, false);
	/** 用于对象层纹理的异步加载对象 */
	private static AssetManager Objects7 = new AssetManager(null, false);
	/** 用于对象层纹理的异步加载对象 */
	private static AssetManager Objects8 = new AssetManager(null, false);
	/** 用于对象层纹理的异步加载对象 */
	private static AssetManager Objects9 = new AssetManager(null, false);
	/** 用于对象层纹理的异步加载对象 */
	private static AssetManager Objects10 = new AssetManager(null, false);
	/** 用于对象层纹理的异步加载对象 */
	private static AssetManager Objects13 = new AssetManager(null, false);
	/** 用于对象层纹理的异步加载对象 */
	private static AssetManager Objects14 = new AssetManager(null, false);
	/** 用于对象层纹理的异步加载对象 */
	private static AssetManager Objects15 = new AssetManager(null, false);
	
	static {
		FileHandleResolver resolver = new InternalFileHandleResolver();
		Map.setLoader(Map.class, new MapLoader(resolver));
		Tiles.setLoader(Texture.class, new W_LLoader(resolver));
		SmTiles.setLoader(Texture.class, new W_LLoader(resolver));
		Objects.setLoader(Texture.class, new W_LLoader(resolver));
		Objects2.setLoader(Texture.class, new W_LLoader(resolver));
		Objects3.setLoader(Texture.class, new W_LLoader(resolver));
		Objects4.setLoader(Texture.class, new W_LLoader(resolver));
		Objects5.setLoader(Texture.class, new W_LLoader(resolver));
		Objects6.setLoader(Texture.class, new W_LLoader(resolver));
		Objects7.setLoader(Texture.class, new W_LLoader(resolver));
		Objects8.setLoader(Texture.class, new W_LLoader(resolver));
		Objects9.setLoader(Texture.class, new W_LLoader(resolver));
		Objects10.setLoader(Texture.class, new W_LLoader(resolver));
		Objects13.setLoader(Texture.class, new W_LLoader(resolver));
		Objects14.setLoader(Texture.class, new W_LLoader(resolver));
		Objects15.setLoader(Texture.class, new W_LLoader(resolver));
	}
	
	/**
	 * 异步加载某个资源
	 * 
	 * @param fileName 资源名称
	 */
	public static void load(String fileName) {
		AssetManager[] am = new AssetManager[1];
		Class<?>[] type = new Class[1];
		resolve(fileName, am, type);
		am[0].load(fileName, type[0]);
	}
	
	/**
	 * 判断某个资源是否正在加载或已加载完成
	 * 
	 * @param fileName 资源名称
	 * @return 如果资源正在（或已经）加载，则返回true
	 */
	public static boolean contains(String fileName) {
		AssetManager[] am = new AssetManager[1];
		Class<?>[] type = new Class[1];
		resolve(fileName, am, type);
		return am[0].contains(fileName);
	}

	/**
	 * 判断某个资源是否已加载完毕
	 * 
	 * @param fileName 资源名称
	 * @return 加载完毕则返回true
	 */
	public static boolean isLoaded(String fileName) {
		AssetManager[] am = new AssetManager[1];
		Class<?>[] type = new Class[1];
		resolve(fileName, am, type);
		return am[0].isLoaded(fileName);
	}
	
	/**
	 * 获取已加载资源
	 * 
	 * @param <T> 资源类型
	 * @param fileName 资源名称
	 * @return 对应资源
	 */
	public static <T> T get(String fileName) {
		AssetManager[] am = new AssetManager[1];
		Class<?>[] type = new Class[1];
		resolve(fileName, am, type);
		return am[0].get(fileName);
	}
	
	/** 执行异步加载任务（使用较短时间，只加载任务队列中第一个） */
	public static void update() {
		Map.update();
		Tiles.update();
		SmTiles.update();
		Objects.update();
		Objects2.update();
		Objects3.update();
		Objects4.update();
		Objects5.update();
		Objects6.update();
		Objects7.update();
		Objects8.update();
		Objects9.update();
		Objects10.update();
		Objects13.update();
		Objects14.update();
		Objects15.update();
	}
	
	/**
	 * 执行异步加载任务
	 * <br>
	 * 使用较长时间，尝试加载多个
	 * 
	 * @param millis 最长允许消耗的时间（毫秒）
	 */
	public static void update(int millis) {
		Map.update(millis);
		Tiles.update(millis);
		SmTiles.update(millis);
		Objects.update(millis);
		Objects2.update(millis);
		Objects3.update(millis);
		Objects4.update(millis);
		Objects5.update(millis);
		Objects6.update(millis);
		Objects7.update(millis);
		Objects8.update(millis);
		Objects9.update(millis);
		Objects10.update(millis);
		Objects13.update(millis);
		Objects14.update(millis);
		Objects15.update(millis);
	}
	
	private static void resolve(String fileName, AssetManager[] am, Class<?>[] type) {
		if (fileName.startsWith("Map/")) {
			am[0] = Assets.Map;
			type[0] = joot.m2.client.map.Map.class;
		} else if (fileName.startsWith("Tiles/")) {
			am[0] = Assets.Tiles;
			type[0] = Texture.class;
		} else if (fileName.startsWith("SmTiles/")) {
			am[0] = Assets.SmTiles;
			type[0] = Texture.class;
		} else if (fileName.startsWith("Objects/")) {
			am[0] = Assets.Objects;
			type[0] = Texture.class;
		} else if (fileName.startsWith("Objects2/")) {
			am[0] = Assets.Objects2;
			type[0] = Texture.class;
		} else if (fileName.startsWith("Objects3/")) {
			am[0] = Assets.Objects3;
			type[0] = Texture.class;
		} else if (fileName.startsWith("Objects4/")) {
			am[0] = Assets.Objects4;
			type[0] = Texture.class;
		} else if (fileName.startsWith("Objects5/")) {
			am[0] = Assets.Objects5;
			type[0] = Texture.class;
		} else if (fileName.startsWith("Objects6/")) {
			am[0] = Assets.Objects6;
			type[0] = Texture.class;
		} else if (fileName.startsWith("Objects7/")) {
			am[0] = Assets.Objects7;
			type[0] = Texture.class;
		} else if (fileName.startsWith("Objects8/")) {
			am[0] = Assets.Objects8;
			type[0] = Texture.class;
		} else if (fileName.startsWith("Objects9/")) {
			am[0] = Assets.Objects9;
			type[0] = Texture.class;
		} else if (fileName.startsWith("Objects10/")) {
			am[0] = Assets.Objects10;
			type[0] = Texture.class;
		} else if (fileName.startsWith("Objects13/")) {
			am[0] = Assets.Objects13;
			type[0] = Texture.class;
		} else if (fileName.startsWith("Objects14/")) {
			am[0] = Assets.Objects14;
			type[0] = Texture.class;
		} else if (fileName.startsWith("Objects15/")) {
			am[0] = Assets.Objects15;
			type[0] = Texture.class;
		}
	}
}
