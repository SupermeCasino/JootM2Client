package joot.m2.client.map;

import java.io.File;
import java.util.stream.IntStream;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.github.jootnet.mir2.core.map.MapTileInfo;
import com.github.jootnet.mir2.core.map.Maps;

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
		String mapNo = fileName.split("/")[1];
		String mapPath = Dir;
		if (!mapPath.endsWith(File.separator)) {
			mapPath += File.separator;
		}
		mapPath += mapNo + ".map";
		Maps.get(mapNo, mapPath);
	}

	@Override
	public Map loadSync(AssetManager manager, String fileName, FileHandle file, AssetLoaderParameters<Map> parameter) {
		String mapNo = fileName.split("/")[1];
		com.github.jootnet.mir2.core.map.Map _map = Maps.get(mapNo, null);
		Map ret = new Map(_map.getWidth(), _map.getHeight());
		IntStream.range(0, _map.getWidth()).parallel().forEach(w -> {
			IntStream.range(0, _map.getHeight()).parallel().forEach(h -> {
				MapTileInfo mti = _map.getTiles()[w][h];
				if (mti.isHasBng()) {
					ret.setTileFileName((short)w, (short)h, "Tiles/" + mti.getBngImgIdx());
				}
				if (mti.isHasMid()) {
					ret.setSmTileFileName((short)w, (short)h, "SmTiles/" + mti.getMidImgIdx());
				}
				if (!mti.isHasAni() && mti.isHasObj()) {
					String objFileName = "Objects";
					if (mti.getObjFileIdx() != 0) {
						objFileName += mti.getObjFileIdx();
					}
					objFileName += "/" + mti.getObjImgIdx();
					ret.setObjFileName((short)w, (short)h, objFileName);
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
