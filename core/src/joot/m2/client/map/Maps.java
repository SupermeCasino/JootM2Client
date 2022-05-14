package joot.m2.client.map;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.stream.IntStream;

/** 地图异步加载对象 */
public final class Maps {

	/** *.map文件的存储路径 */
	public static String Dir = null;
	/** 微端基址 */
	public static String wdBaseUrl = null;

	/** 地图缓存 */
	private static java.util.Map<String, Map> maps = new HashMap<>();
	
	/**
	 * 指定客户端安装目录初始化资源加载器
	 * 
	 * @param baseDir 客户端安装目录
	 * @param wdBaseUrl 微端基址
	 */
	public static void init(String baseDir, String wdBaseUrl) {
		Maps.wdBaseUrl = wdBaseUrl;

		var path = Paths.get(baseDir, "Map");
		if (!Files.exists(path)) {
			path = Paths.get(baseDir, "map");
		}
		Dir = path.toString();
	}

	/**
	 * 通过编号获取地图对象
	 * 
	 * @param mapNo 地图编号
	 * @return 地图对象或null
	 */
	public static Map get(String mapNo) {
		if (maps.containsKey(mapNo))
			return maps.get(mapNo);
		var m2map = com.github.jootnet.m2.core.map.Maps.get(Paths.get(Dir, mapNo + ".map").toString(), wdBaseUrl);
		if (m2map == null) {
			return null;
		}
		var map = new Map(m2map.getWidth(), m2map.getHeight());
		IntStream.range(0, m2map.getWidth()).parallel().forEach(_w -> {
			IntStream.range(0, m2map.getHeight()).parallel().forEach(_h -> {
				var mti = m2map.getTiles()[_w][_h];
				var w = _w + 1; // 转换为游戏坐标
				var h = _h + 1;
				map.canFly[w][h] = mti.isCanFly();
				map.canWalk[w][h] = mti.isCanWalk();
				if (mti.isHasBng()) {
					var tileFileName = "tiles";
					if (mti.getBngFileIdx() != 0) {
						tileFileName += mti.getBngFileIdx();
					}
					tileFileName += "/" + mti.getBngImgIdx();
					map.tilesFileName[w][h] = tileFileName;
				}
				if (mti.isHasMid()) {
					var smTileFileName = "smtiles";
					if (mti.getMidFileIdx() != 0) {
						smTileFileName += mti.getMidFileIdx();
					}
					smTileFileName += "/" + mti.getMidImgIdx();
					map.smTilesFileName[w][h] = smTileFileName;
				}
				if (!mti.isHasAni() && mti.isHasObj()) {
					var objFileName = "objects";
					if (mti.getObjFileIdx() != 0) {
						objFileName += mti.getObjFileIdx();
					}
					objFileName += "/" + mti.getObjImgIdx();
					map.objsFileName[w][h] = objFileName;
				}
			});
		});
		maps.put(mapNo, map);
		return map;
	}

}
