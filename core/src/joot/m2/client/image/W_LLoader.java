package joot.m2.client.image;

import java.nio.file.Paths;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.utils.Array;
import com.github.jootnet.mir2.core.image.ImageInfo;
import com.github.jootnet.mir2.core.image.ImageLibraries;

/** 纹理异步加载对象 */
public final class W_LLoader extends AsynchronousAssetLoader<M2Texture, AssetLoaderParameters<M2Texture>> {

	/** *.wil/*.wzl/*.wis文件所在文件夹 */
	public static String Dir = null;
	private com.github.jootnet.mir2.core.Texture texture_;
	private ImageInfo ii_;
	
	public W_LLoader(FileHandleResolver resolver) {
		super(resolver);
	}

	@Override
	public void loadAsync(AssetManager manager, String fileName, FileHandle file,
			AssetLoaderParameters<M2Texture> parameter) {
		String[] lib_idx = fileName.split("/");
		texture_ = ImageLibraries.get(lib_idx[0], Paths.get(Dir, lib_idx[0]).toString()).tex(Integer.parseInt(lib_idx[1]));
		ii_ = ImageLibraries.get(lib_idx[0], Paths.get(Dir, lib_idx[0]).toString()).info(Integer.parseInt(lib_idx[1]));
	}

	@Override
	public M2Texture loadSync(AssetManager manager, String fileName, FileHandle file,
			AssetLoaderParameters<M2Texture> parameter) {
		Pixmap pm = new Pixmap(texture_.getWidth(), texture_.getHeight(), Pixmap.Format.RGBA8888);
		pm.getPixels().put(texture_.getRGBAs());
		pm.getPixels().flip();
		texture_ = null;
		return new M2Texture(pm, ii_.getOffsetX(), ii_.getOffsetY());
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file,
			AssetLoaderParameters<M2Texture> parameter) {
		return null;
	}
}
