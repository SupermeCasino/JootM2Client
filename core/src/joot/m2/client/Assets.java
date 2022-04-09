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
import com.github.jootnet.mir2.core.actor.HumActionInfo;

import joot.m2.client.image.M2Texture;
import joot.m2.client.image.W_LLoader;
import joot.m2.client.map.Map;
import joot.m2.client.map.MapLoader;
import joot.m2.client.util.ArrayUtil;

/** 资源加载工具类 */
public final class Assets {

	/** 用于地图的异步加载对象 */
	private static AssetManager Map = new AssetManager(null, false);
	
	/** 用于纹理的异步加载对象 */
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
		for (String w_l : List.of("tiles", "smtiles", "objects", "hum", "weapon", "humeffect", "weaponeffect")) {
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
				amW_L.setLoader(M2Texture.class, new W_LLoader(resolver));
				W_L.put(fnNoExt + "/", amW_L);
			}
		}
		//load("smtiles/58");
		//load("smtiles/59");
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
		prepareDress();
	}

	
	/** 衣服纹理加载键 */
	private static String[][][] dressNames; // 第一维是文件hum/hum2;第二维是编号1/2;第三维是固定600张纹理图的加载键
	/** 衣服纹理集合 */
	private static M2Texture[][][] dressTextures; // 第一维是文件hum/hum2;第二维是编号1/2;第三维是固定600张纹理图
	/**
	 * 为玩家准备衣服纹理
	 * <br>
	 * 其实就是把某个衣服所需的600张图送入加载队列
	 * 
	 * @param fileIdx 资源文件编号，从0开始
	 * @param dressIdx 衣服编号，从1开始
	 */
	public static void prepareDress(int fileIdx, int dressIdx) {
		if (ArrayUtil.eleNotNull(dressNames, fileIdx) &&
				ArrayUtil.eleNotNull(dressNames[fileIdx], dressIdx - 1)){
			// 已经在申了思密达！
			return;
		}
		
		if (dressNames == null) dressNames = new String[fileIdx + 1][][];
		else if (dressNames.length <= fileIdx) dressNames = ArrayUtil.resize(dressNames, fileIdx + 1);
		
		if (dressNames[fileIdx] == null) dressNames[fileIdx] = new String[dressIdx][];
		else if (dressNames[fileIdx].length < dressIdx) dressNames[fileIdx] = ArrayUtil.resize(dressNames[fileIdx], dressIdx);
		dressNames[fileIdx][dressIdx - 1] = new String[600];
		
		for (int i = 1; i < 601; ++i) {
			String dressName = "hum";
			if (fileIdx != 0)
				dressName += fileIdx;
			dressName += "/";
			dressName += ((dressIdx - 1) * 600 + i - 1);
			dressNames[fileIdx][dressIdx - 1][i - 1] = dressName;
			Assets.load(dressName);
		}
	}
	
	/**
	 * 获取人物衣服贴图
	 * 
	 * @param fileIdx 衣服资源文件索引
	 * @param dressIdx 衣服编号
	 * @param action 人物动作
	 * @param tick 动作是第几帧
	 * @return 已加载的纹理贴图或null
	 */
	public static M2Texture getDress(int fileIdx, int dressIdx, HumActionInfo action, int tick) {
		if (ArrayUtil.eleNotNull(dressTextures, fileIdx) &&
				ArrayUtil.eleNotNull(dressTextures[fileIdx], dressIdx - 1)){
			int texIdx = action.frameIdx + tick - 1;
			return dressTextures[fileIdx][dressIdx - 1][texIdx];
		}
		return null;
	}
	
	private static void prepareDress() {
		if (dressNames == null) return;
		if (dressTextures == null) {
			dressTextures = new M2Texture[dressNames.length][][];
		} else dressTextures = ArrayUtil.resize(dressTextures, dressNames.length);
		for (int fileIdx = 0; fileIdx < dressNames.length; ++fileIdx) {
			String[][] dressNames_s = dressNames[fileIdx];
			if (dressNames_s == null) continue;
			if (dressTextures[fileIdx] == null) {
				dressTextures[fileIdx] = new M2Texture[dressNames_s.length][];
			} else dressTextures[fileIdx] = ArrayUtil.resize(dressTextures[fileIdx], dressNames_s.length);
			for (int dressIdx = 0; dressIdx < dressNames_s.length; ++dressIdx) {
				String[] dressNames_ss = dressNames_s[dressIdx];
				if (dressNames_ss == null) continue;
				if (dressTextures[fileIdx][dressIdx] == null) {
					dressTextures[fileIdx][dressIdx] = new M2Texture[600];
				}
				for (int i = 1; i < 601; ++i) {
					if (dressNames_ss[i - 1] != null && Assets.isLoaded(dressNames_ss[i - 1])) {
						dressTextures[fileIdx][dressIdx][i - 1] = Assets.get(dressNames_ss[i - 1]);
						dressNames_ss[i - 1] = null;
					}
				}
			}
		}
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
				type[0] = M2Texture.class;
			});
		}
	}
}