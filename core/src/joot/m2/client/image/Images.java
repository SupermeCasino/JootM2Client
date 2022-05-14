package joot.m2.client.image;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Semaphore;

import com.badlogic.gdx.graphics.Pixmap;
import com.github.jootnet.m2.core.actor.Action;
import com.github.jootnet.m2.core.actor.ChrBasicInfo;
import com.github.jootnet.m2.core.actor.HumActionInfo;
import com.github.jootnet.m2.core.image.Texture;
import com.github.jootnet.m2.core.image.WZL;

public final class Images {

	/** *.wzl文件的存储路径 */
	public static String Dir = null;
	/** 微端基址 */
	public static String wdBaseUrl = null;

	/** 用于纹理的异步加载对象 */
	private static Map<String, WZL> WZLs = new HashMap<>();
	/** 已加载的纹理 */
	private static Map<String, M2Texture> textures = new ConcurrentHashMap<>();
	private static M2Texture EMPTY;
	private static M2Texture[] EMPTY_ARRAY = new M2Texture[0];
	/** 待加入显存的纹理 */
	private static Queue<Map.Entry<String, Texture>> pendingTextures = new ConcurrentLinkedQueue<>();
	/** 最多允许等待加入显存的任务数 */
	private static Semaphore pendingTextureSemaphore = new Semaphore(600);
	
	/**
	 * 指定客户端安装目录初始化资源加载器
	 * 
	 * @param baseDir 客户端安装目录
	 * @param wdBaseUrl 微端基址
	 */
	public static void init(String baseDir, String wdBaseUrl) {
		var path = Paths.get(baseDir, "Data");
		if (!Files.exists(path)) {
			path = Paths.get(baseDir, "data");
		}
		Dir = path.toString();
		Images.wdBaseUrl = wdBaseUrl;
	}
	
	/**
	 * 停止纹理后台加载
	 */
	public static void shutdown() {
		WZLs.values().forEach(WZL::cancelLoad);
		pendingTextureSemaphore.release(WZLs.size());
	}
	
	/**
	 * 获取已加载资源
	 * 
	 * @param fileNames 资源名称
	 * @return 除非所需资源都已加载完毕，否则返回null
	 */
	public static M2Texture[] get(String... fileNames) {
		var texs = new ArrayList<M2Texture>();
		for (var fileName : fileNames) {
			var tex = textures.get(fileName);
			if (tex == null) {
				load(fileName);
			} else {
				texs.add(tex);
			}
		}
		if (texs.size() != fileNames.length) return null;
		return texs.toArray(EMPTY_ARRAY);
	}
	
	/**
	 * 加载纹理到显存
	 * 
	 * @param milli 最多允许工作的毫秒数
	 */
	public static void update(int milli) {
		var sMilli = System.currentTimeMillis();
		var fn2tex = (Map.Entry<String, Texture>) null;
		while (System.currentTimeMillis() - sMilli < milli && (fn2tex = pendingTextures.poll()) != null) {
			Texture tex = fn2tex.getValue();
			if (tex.isEmpty) {
				if (EMPTY == null) {
					var pm = new Pixmap(tex.width, tex.height, Pixmap.Format.RGBA8888);
					pm.getPixels().put(tex.pixels);
					pm.getPixels().flip();
					EMPTY = new M2Texture(pm, (short) tex.offsetX, (short) tex.offsetY);
				}
				textures.put(fn2tex.getKey(), EMPTY);
			} else {
				var pm = new Pixmap(tex.width, tex.height, Pixmap.Format.RGBA8888);
				pm.getPixels().put(tex.pixels);
				pm.getPixels().flip();
				textures.put(fn2tex.getKey(), new M2Texture(pm, (short) tex.offsetX, (short) tex.offsetY));
			}
			pendingTextureSemaphore.release();
		}
	}
	
	/**
	 * 获取人物衣服贴图
	 * 
	 * @param hum 人物
	 * @return 已加载的纹理贴图或null
	 */
	public static M2Texture getDress(ChrBasicInfo hum) {
		int fileIdx = hum.humFileIdx;
		int dressIdx = hum.humIdx;
		HumActionInfo action = hum.action;
		int tick = hum.actionTick;
		if (action.act == Action.Stand && tick > 4) tick -= 4;
		int texIdx = action.frameIdx + tick - 1;
		var dressName = "hum";
		if (fileIdx != 0)
			dressName += fileIdx;
		dressName += "/";
		dressName += ((dressIdx - 1) * 600 + texIdx);
		var tex = textures.get(dressName);
		if (tex == null) {
			load(dressName);
			return null;
		} else {
			return tex;
		}
	}
	
	/**
	 * 获取人物武器贴图
	 * 
	 * @param hum 人物
	 * @return 已加载的纹理贴图或null
	 */
	public static M2Texture getWeapon(ChrBasicInfo hum) {
		int fileIdx = hum.weaponFileIdx;
		int weaponIdx = hum.weaponIdx;
		if (weaponIdx == 0) return null;
		HumActionInfo action = hum.action;
		int tick = hum.actionTick;
		if (action.act == Action.Stand && tick > 4) tick -= 4;
		int texIdx = action.frameIdx + tick - 1;
		var weaponName = "weapon";
		if (fileIdx != 0)
			weaponName += fileIdx;
		weaponName += "/";
		weaponName += ((weaponIdx - 1) * 600 + texIdx);
		var tex = textures.get(weaponName);
		if (tex == null) {
			load(weaponName);
			return null;
		} else {
			return tex;
		}
	}
	
	/**
	 * 获取人物衣服特效贴图
	 * 
	 * @param hum 人物
	 * @return 已加载的纹理贴图或null
	 */
	public static M2Texture getHumEffect(ChrBasicInfo hum) {
		int fileIdx = hum.humEffectFileIdx;
		int humeffectIdx = hum.humEffectIdx;
		if (humeffectIdx == 0) return null;
		HumActionInfo action = hum.action;
		int tick = hum.actionTick;
		int texIdx = action.frameIdx + tick - 1;
		var humeffectName = "humeffect";
		if (fileIdx != 0)
			humeffectName += fileIdx;
		humeffectName += "/";
		humeffectName += ((humeffectIdx - 1) * 600 + texIdx);
		var tex = textures.get(humeffectName);
		if (tex == null) {
			load(humeffectName);
			return null;
		} else {
			return tex;
		}
	}
	
	private static void load(String fileName) {
		var lib_idx = fileName.split("/");
		if (WZLs.containsKey(lib_idx[0])) {
			WZLs.get(lib_idx[0]).load(Integer.parseInt(lib_idx[1]));
		} else {
			var wzl = new WZL(Paths.get(Dir, lib_idx[0] + ".wzx").toString(), wdBaseUrl, 60 * 1000, 1024 * 1024); // 每次处理1M数据，自动加载间隔1分钟
			wzl.onTextureLoaded((fno, no, tex) -> {
				try {
					pendingTextureSemaphore.acquire();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				final Texture m2tex = tex;
				pendingTextures.add(new Map.Entry<String, Texture>() {
					@SuppressWarnings("unused")
					private Texture tex_;

					@Override
					public String getKey() {
						return fno + "/" + no;
					}

					@Override
					public Texture getValue() {
						return m2tex;
					}

					@Override
					public Texture setValue(Texture value) {
						var otex = tex;
						tex_ = value;
						return otex;
					}
					
				});
			})
			.onAllTextureLoaded((fn) -> {
				WZLs.remove(lib_idx[0]);
			});
			wzl.load(Integer.parseInt(lib_idx[1]));
			WZLs.put(lib_idx[0], wzl);
		}
	}
}
