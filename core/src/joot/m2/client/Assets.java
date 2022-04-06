package joot.m2.client;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
	
	private static java.util.Map<String, AssetManager> W_L = new HashMap<>();

	
	/**
	 * 指定客户端安装目录初始化资源加载器
	 * 
	 * @param baseDir 客户端安装目录
	 */
	public static void init(String baseDir) {
		FileHandleResolver resolver = new InternalFileHandleResolver();
		Map.setLoader(Map.class, new MapLoader(resolver));

		Path path = Paths.get(baseDir, "map");
		if (Files.exists(path)) {
			MapLoader.Dir = path.toString();
		} else {
			path = Paths.get(baseDir, "Map");
			if (Files.exists(path))
				MapLoader.Dir = path.toString();
		}

		path = Paths.get(baseDir, "data");
		if (Files.exists(path)) {
			W_LLoader.Dir = path.toString();
		} else {
			path = Paths.get(baseDir, "Data");
			if (Files.exists(path))
				W_LLoader.Dir = path.toString();
		}

		List<String> w_lNames = new ArrayList<>();
		for (String w_l : List.of("tiles", "smtiles", "objects"/*, "hum", "weapon"*/)) {
			for (int i = 0; i < 100; ++i) {
				w_lNames.add(w_l + (i == 0 ? "" : i) + ".");
			}
		}
		for (File f : path.toFile().listFiles()) {
			String fn = f.getName().toLowerCase();
			boolean find = false;
			for (String w_lName : w_lNames) {
				if (fn.startsWith(w_lName)) {
					find = true;
					break;
				}
			}
			if (find) {
				String fnNoExt = fn.substring(0, fn.lastIndexOf('.'));
				AssetManager amW_L = new AssetManager(null, false);
				amW_L.setLoader(Texture.class, new W_LLoader(resolver));
				W_L.put(fnNoExt + "/", amW_L);
			}
		}
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
		W_L.values().stream().forEach(am -> am.update());
	}
	
	/**
	 * 执行异步加载任务
	 * <br>
	 * 使用较长时间，尝试加载多个
	 * 
	 * @param millis 最长允许消耗的时间（毫秒）
	 */
	public static void update(final int millis) {
		Map.update(millis);
		W_L.values().stream().forEach(am -> am.update(millis));
	}
	
	private static void resolve(final String fileName, final AssetManager[] am, final Class<?>[] type) {
		if (fileName.startsWith("map/")) {
			am[0] = Assets.Map;
			type[0] = joot.m2.client.map.Map.class;
		} else {
			W_L.entrySet().stream().filter(w_l -> fileName.toLowerCase().startsWith(w_l.getKey()))
					.findFirst()
					.ifPresent(w_l -> {
				am[0] = w_l.getValue();
				type[0] = Texture.class;
			});
		}
	}
}