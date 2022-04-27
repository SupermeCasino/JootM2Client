package joot.m2.client.util;

import java.io.File;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.github.jootnet.mir2.core.actor.Action;
import com.github.jootnet.mir2.core.actor.HumActionInfo;

import joot.m2.client.image.M2Texture;
import joot.m2.client.image.W_LLoader;
import joot.m2.client.map.Map;
import joot.m2.client.map.MapLoader;

/** 资源加载工具类 */
public final class AssetUtil {

	/** 用于地图的异步加载对象 */
	private static AssetManager Map = new AssetManager(null, false);
	/** 用于纹理的异步加载对象 */
	private static java.util.Map<String, AssetManager> W_L = new HashMap<>();
	
	private static FileHandleResolver resolver = new InternalFileHandleResolver();
	
	/**
	 * 指定客户端安装目录初始化资源加载器
	 * 
	 * @param baseDir 客户端安装目录
	 */
	public static void init(String baseDir) {
		Map.setLoader(Map.class, new MapLoader(resolver));

		var path = Paths.get(baseDir, "map");
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
	}
	
	/**
	 * 停止
	 */
	public static void shutdown() {
		Map.dispose();
		W_L.values().stream().forEach(AssetManager::dispose);
	}
	
	/**
	 * 获取已加载资源
	 * 
	 * @param <T> 资源类型
	 * @param fileName 资源名称
	 * @return 对应资源，未加载完成或不存在返回null
	 */
	@SuppressWarnings("unchecked")
	public static <T> T get(String fileName) {
		var am = new AssetManager[1];
		var type = new Class[1];
		resolve(fileName, am, type);
		if (!am[0].contains(fileName)) {
			am[0].load(fileName, type[0]);
			return null;
		}
		if (!am[0].isLoaded(fileName)) {
			return null;
		}
		return am[0].get(fileName);
	}
	
	public static interface AssetConsumer<T> {
		public void recv(T[] ret);
	}
	
	@SuppressWarnings("unchecked")
	public static <T> void get(AssetConsumer<T> assetConsumer, String... fileNames) {
		var am = new AssetManager[1];
		var type = new Class[1];
		resolve(fileNames[0], am, type);
		T rets[] = (T[]) Array.newInstance(type[0], fileNames.length);
		for (int i = 0; i < fileNames.length; ++i) {
			T ret = null;
			while((ret = get(fileNames[i])) == null) update();
			rets[i] = ret;
		}
		assetConsumer.recv(rets);
	}
	
	/** 执行异步加载任务（使用较短时间，只加载任务队列中第一个） */
	public static void update() {
		Map.update();
		W_L.values().stream().forEach(AssetManager::update);
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

	/** 衣服纹理集合 */
	private static M2Texture[][][] dressTextures; // 第一维是文件hum/hum2;第二维是编号1/2;第三维是固定600张纹理图
	
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
		if (action.act == Action.Stand && tick > 4) tick -= 4;
		int texIdx = action.frameIdx + tick - 1;
		if (dressTextures == null) {
			dressTextures = new M2Texture[fileIdx + 1][][];
		} else if (dressTextures.length <= fileIdx) dressTextures = ArrayUtil.resize(dressTextures, fileIdx + 1);
		if (dressTextures[fileIdx] == null) dressTextures[fileIdx] = new M2Texture[dressIdx][];
		else if (dressTextures[fileIdx].length < dressIdx) dressTextures[fileIdx] = ArrayUtil.resize(dressTextures[fileIdx], dressIdx);
		if (dressTextures[fileIdx][dressIdx - 1] == null) {
			dressTextures[fileIdx][dressIdx - 1] = new M2Texture[600];
		}
		if (dressTextures[fileIdx][dressIdx - 1][texIdx] == null) {
			var dressName = "hum";
			if (fileIdx != 0)
				dressName += fileIdx;
			dressName += "/";
			dressName += ((dressIdx - 1) * 600 + texIdx);
			dressTextures[fileIdx][dressIdx - 1][texIdx] = get(dressName);
		}
		return dressTextures[fileIdx][dressIdx - 1][texIdx];
	}
	
	/** 武器纹理集合 */
	private static M2Texture[][][] weaponTextures; // 第一维是文件weapon/weapon2;第二维是编号1/2;第三维是固定600张纹理图
	
	/**
	 * 获取人物武器贴图
	 * 
	 * @param fileIdx 武器资源文件索引
	 * @param weaponIdx 武器编号
	 * @param action 人物动作
	 * @param tick 动作是第几帧
	 * @return 已加载的纹理贴图或null
	 */
	public static M2Texture getWeapon(int fileIdx, int weaponIdx, HumActionInfo action, int tick) {
		if (action.act == Action.Stand && tick > 4) tick -= 4;
		int texIdx = action.frameIdx + tick - 1;
		if (weaponTextures == null) {
			weaponTextures = new M2Texture[fileIdx + 1][][];
		} else if (weaponTextures.length <= fileIdx) weaponTextures = ArrayUtil.resize(weaponTextures, fileIdx + 1);
		if (weaponTextures[fileIdx] == null) weaponTextures[fileIdx] = new M2Texture[weaponIdx][];
		else if (weaponTextures[fileIdx].length < weaponIdx) weaponTextures[fileIdx] = ArrayUtil.resize(weaponTextures[fileIdx], weaponIdx);
		if (weaponTextures[fileIdx][weaponIdx - 1] == null) {
			weaponTextures[fileIdx][weaponIdx - 1] = new M2Texture[600];
		}
		if (weaponTextures[fileIdx][weaponIdx - 1][texIdx] == null) {
			var weaponName = "weapon";
			if (fileIdx != 0)
				weaponName += fileIdx;
			weaponName += "/";
			weaponName += ((weaponIdx - 1) * 600 + texIdx);
			weaponTextures[fileIdx][weaponIdx - 1][texIdx] = get(weaponName);
		}
		return weaponTextures[fileIdx][weaponIdx - 1][texIdx];
	}
	
	/** 衣服特效纹理集合 */
	private static M2Texture[][][] humEffectTextures; // 第一维是文件humeffect/humeffect2;第二维是编号1/2;第三维是固定600张纹理图
	
	/**
	 * 获取人物衣服特效贴图
	 * 
	 * @param fileIdx 衣服特效资源文件索引
	 * @param humeffectIdx 衣服特效编号
	 * @param action 人物动作
	 * @param tick 动作是第几帧
	 * @return 已加载的纹理贴图或null
	 */
	public static M2Texture getHumEffect(int fileIdx, int humeffectIdx, HumActionInfo action, int tick) {
		int texIdx = action.frameIdx + tick - 1;
		if (humEffectTextures == null) {
			humEffectTextures = new M2Texture[fileIdx + 1][][];
		} else if (humEffectTextures.length <= fileIdx) humEffectTextures = ArrayUtil.resize(humEffectTextures, fileIdx + 1);
		if (humEffectTextures[fileIdx] == null) humEffectTextures[fileIdx] = new M2Texture[humeffectIdx][];
		else if (humEffectTextures[fileIdx].length < humeffectIdx) humEffectTextures[fileIdx] = ArrayUtil.resize(humEffectTextures[fileIdx], humeffectIdx);
		if (humEffectTextures[fileIdx][humeffectIdx - 1] == null) {
			humEffectTextures[fileIdx][humeffectIdx - 1] = new M2Texture[600];
		}
		if (humEffectTextures[fileIdx][humeffectIdx - 1][texIdx] == null) {
			var humeffectName = "humeffect";
			if (fileIdx != 0)
				humeffectName += fileIdx;
			humeffectName += "/";
			humeffectName += ((humeffectIdx - 1) * 600 + texIdx);
			humEffectTextures[fileIdx][humeffectIdx - 1][texIdx] = get(humeffectName);
		}
		return humEffectTextures[fileIdx][humeffectIdx - 1][texIdx];
	}
	
	private static void resolve(final String fileName, final AssetManager[] am, final Class<?>[] type) {
		final var w_lName = fileName.toLowerCase().split("/")[0];
		if (w_lName.equals("map")) {
			am[0] = AssetUtil.Map;
			type[0] = joot.m2.client.map.Map.class;
		} else {
			W_L.entrySet().stream().filter(w_l -> w_lName.equals(w_l.getKey()))
					.findFirst()
					.ifPresent(w_l -> {
				am[0] = w_l.getValue();
				type[0] = M2Texture.class;
			});
			if (am[0] == null) {
				for (var f : new File(W_LLoader.Dir).listFiles()) {
					var fn = f.getName().toLowerCase();
					try {
						var fnNoExt = fn.substring(0, fn.lastIndexOf('.'));
						if (fnNoExt.equals(w_lName)) {
							var amW_L = new AssetManager(null, false);
							amW_L.setLoader(M2Texture.class, new W_LLoader(resolver));
							W_L.put(w_lName, amW_L);
							am[0] = amW_L;
							type[0] = M2Texture.class;
							break;
						}
					} catch (Exception ex) { }
				}
			}
		}
	}
}