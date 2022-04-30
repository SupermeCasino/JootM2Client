package joot.m2.client.map;

import java.nio.file.Paths;
import java.util.stream.IntStream;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.github.jootnet.m2.core.map.Maps;

/** 地图异步加载对象 */
public final class MapLoader extends AsynchronousAssetLoader<Map, AssetLoaderParameters<Map>> {

	/** *.map文件的存储路径 */
	public static String Dir = null;
	
	public MapLoader(FileHandleResolver resolver) {
		super(resolver);
	}

	@Override
	public void loadAsync(AssetManager manager, String fileName, FileHandle file,
			AssetLoaderParameters<Map> parameter) {
		var mapNo = fileName.split("/")[1];
		Maps.get(mapNo, Paths.get(Dir, mapNo + ".map").toString());
	}

	@Override
	public Map loadSync(AssetManager manager, String fileName, FileHandle file, AssetLoaderParameters<Map> parameter) {
		var mapNo = fileName.split("/")[1];
		var _map = Maps.get(mapNo, null);
		var ret = new Map(_map.getWidth(), _map.getHeight());
		IntStream.range(0, _map.getWidth()).parallel().forEach(_w -> {
			IntStream.range(0, _map.getHeight()).parallel().forEach(_h -> {
				var mti = _map.getTiles()[_w][_h];
				var w = _w + 1; // 转换为游戏坐标
				var h = _h + 1;
				ret.canFly[w][h] = mti.isCanFly();
				ret.canWalk[w][h] = mti.isCanWalk();
				if (mti.isHasBng()) {
					var tileFileName = "tiles";
					if (mti.getBngFileIdx() != 0) {
						tileFileName += mti.getBngFileIdx();
					}
					tileFileName += "/" + mti.getBngImgIdx();
					ret.tilesFileName[w][h] = tileFileName;
				}
				if (mti.isHasMid()) {
					var smTileFileName = "smtiles";
					if (mti.getMidFileIdx() != 0) {
						smTileFileName += mti.getMidFileIdx();
					}
					smTileFileName += "/" + mti.getMidImgIdx();
					ret.smTilesFileName[w][h] = smTileFileName;
				}
				if (!mti.isHasAni() && mti.isHasObj()) {
					var objFileName = "objects";
					if (mti.getObjFileIdx() != 0) {
						objFileName += mti.getObjFileIdx();
					}
					objFileName += "/" + mti.getObjImgIdx();
					ret.objsFileName[w][h] = objFileName;
				}
			});
		});
		Maps.remove(mapNo); // 从底层缓存删除，这样可以节省内存
		return ret;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file,
			AssetLoaderParameters<Map> parameter) {
		return null;
	}

}
