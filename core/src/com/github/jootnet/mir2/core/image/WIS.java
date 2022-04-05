/*
 * Copyright 2017 JOOTNET Project
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * Support: https://github.com/jootnet/mir2.core
 */
package com.github.jootnet.mir2.core.image;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.github.jootnet.mir2.core.BinaryReader;
import com.github.jootnet.mir2.core.SDK;
import com.github.jootnet.mir2.core.Texture;

/**
 * 热血传奇2WIS图片库
 * 
 * @author 云中双月
 */
final class WIS implements ImageLibrary {

	private int imageCount;
	/**
	 * 获取库中图片数量
	 * 
	 * @return 存在于当前WIS库中的图片数量
	 */
	int getImageCount() {
		return imageCount;
	}
    /* 图片数据起始位置 */
    private int[] offsetList;
    /* 图片数据长度 */
    private int[] lengthList;
    private ImageInfo[] imageInfos;
    /**
     * 获取库中图片信息数组
     * 
     * @return 所有存在于当前WIS库中的图片信息数组
     */
	ImageInfo[] getImageInfos() {
		return imageInfos;
	}
	/* WIS文件随机读取对象 */
	private BinaryReader br_wis;
	private boolean loaded;
	/**
	 * 获取库加载状态
	 * 
	 * @return true表示库加载成功 false表示加载失败
	 */
	public boolean isLoaded() {
		return loaded;
	}
	/* 文件指针读取锁 */
    private Object wis_locker = new Object();
	
    WIS(String wisPath) {
    	File f_wis = new File(SDK.repairFileName(wisPath));
		if(!f_wis.exists()) return;
		if(!f_wis.isFile()) return;
		if(!f_wis.canRead()) return;
    	try {
    		br_wis = new BinaryReader(f_wis);
    		// 从文件末尾开始读取图片数据描述信息
    		// 一组描述信息包括12个字节(3个int值)，依次为图片数据起始位置(相对于文件)、图片数据大小(包括基本信息)、保留
    		// 使用两个List保存offsetList和lengthList
    		List<Integer> offsets = new ArrayList<Integer>();
    		List<Integer> lengths = new ArrayList<Integer>();
    		int readPosition = (int) (br_wis.length() - 12);
    		int currentOffset = 0;
    		int currentLength = 0;
    		do {
    			br_wis.seek(readPosition);
    			readPosition -= 12;
    			
    			currentOffset = br_wis.readIntLE();
    			currentLength = br_wis.readIntLE();
    			
    			offsets.add(currentOffset);
    			lengths.add(currentLength);
    		} while(currentOffset > 512);
    		Collections.reverse(offsets);
    		Collections.reverse(lengths);
    		offsetList = new int[offsets.size()];
    		for(int i = 0; i < offsetList.length; ++i)
    			offsetList[i] = offsets.get(i);
    		lengthList = new int[lengths.size()];
    		for(int i = 0; i < lengthList.length; ++i)
    			lengthList[i] = lengths.get(i);
    		imageCount = offsetList.length;
    		// 读取图片信息
    		imageInfos = new ImageInfo[imageCount];
    		for(int i = 0; i < imageCount; ++i) {
				int offset = offsetList[i];
				if(offset + 12 > br_wis.length()) {
					// 数据出错，直接赋值为空图片
					imageInfos[i] = ImageInfo.EMPTY;
            		continue;
				}
    			ImageInfo ii = new ImageInfo();
    			br_wis.seek(offsetList[i] + 4);
    			ii.setWidth((short)br_wis.readUnsignedShortLE());
				ii.setHeight((short)br_wis.readUnsignedShortLE());
				ii.setOffsetX(br_wis.readShortLE());
				ii.setOffsetY(br_wis.readShortLE());
                imageInfos[i] = ii;
    		}
            loaded = true;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
    }
        
    /**
	 * 解压数据
	 * @param packed 压缩的数据
	 * @param unpackLength 解压后数据大小
	 */
	private static byte[] unpack(byte[] packed, int unpackLength) {
		int srcLength = packed.length; // 压缩后数据大小
		byte[] result = new byte[unpackLength]; // 解压后数据
		int srcIndex = 0; // 当前解压的字节索引
		int dstIndex = 0; // 解压过程还原出的字节索引
		// 解压过程为逐字节进行(字节应转为1-256)
		// 如果当前字节非0则表示将以下一个字节数据填充当前字节个字节位置
		// 如果当前字节为0且下一个字节不为0则表示从下下个字节开始到下一个字节长度都没有压缩，直接复制到目标数组
		// 如果当前字节为0且下一个字节也为0则可能是脏数据，不予处理
		// XX YY 表示以YY填充XX个字节
		// 00 XX YY ZZ ... 表示从YY开始XX个字节是未被压缩的，直接复制出来即可
		while(srcLength > 0 && unpackLength > 0) {
			int length = packed[srcIndex++]; // 取出第一个标志位
			int value = packed[srcIndex++]; // 取出第二个标志位
			srcLength -= 2;
			/*if(value == 0 && length == 0) {
				// 脏数据
				continue;
			} else */if(length != 0) {
				// 需要解压缩
				unpackLength -= length;
				for(int i = 0; i < length; ++i) {
					result[dstIndex++] = (byte) value;
				}
			} else if(value != 0) {
				srcLength -= value;
				unpackLength -= value;
				System.arraycopy(packed, srcIndex, result, dstIndex, value);
				dstIndex += value;
				srcIndex += value;
			}
		}
		return result;
	}
    
    /**
     * 关闭WIS对象，释放其引用的文件流以及内存占用
     */
	public final void close() throws IOException {
		synchronized (wis_locker) {
			offsetList = null;
			lengthList = null;
            imageInfos = null;
            loaded = false;
			if (br_wis != null)
            {
				br_wis.close();
            }
		}
	}

	public final Texture tex(int index) {
		if(!loaded) return Texture.EMPTY;
		if(index < 0) return Texture.EMPTY;
		if(index >= imageCount) return Texture.EMPTY;
		if(imageInfos[index] == ImageInfo.EMPTY) return Texture.EMPTY;
    	try{
    		ImageInfo ii = imageInfos[index];
    		int offset = offsetList[index];
    		int length = lengthList[index];
    		/*if(length < 14) {
    			// 如果是空白图片
    			return Texture.EMPTY;
    		}*/
    		byte[] imageBytes = new byte[ii.getWidth() * ii.getHeight()];
    		byte[] packed = null;
    		byte encry = 0;
    		synchronized (wis_locker) {
        		// 是否压缩(RLE)
        		br_wis.seek(offset);
        		encry = br_wis.readByte();
        		br_wis.skipBytes(11);
        		if(encry == 1) {
        			// 压缩了
        			packed = new byte[length - 12];
        			br_wis.read(packed);
        		} else {
        			// 没压缩
        			br_wis.read(imageBytes);
        		}
			}
    		if(encry == 1)
    			imageBytes = unpack(packed, imageBytes.length);
    		byte[] sRGBA = new byte[ii.getWidth() * ii.getHeight() * 4];
    		int index1 = 0;
    		for(int h = 0; h < ii.getHeight(); ++h)
    			for(int w = 0; w < ii.getWidth(); ++w) {
    				byte[] pallete = SDK.palletes[imageBytes[index1++] & 0xff];
					int _idx = (w + h * ii.getWidth()) * 4;
					sRGBA[_idx] = pallete[1];
					sRGBA[_idx + 1] = pallete[2];
					sRGBA[_idx + 2] = pallete[3];
					sRGBA[_idx + 3] = pallete[0];
    			}
	    	return new Texture(sRGBA, ii.getWidth(), ii.getHeight());
    	} catch(Exception ex) {
    		ex.printStackTrace();
    		return Texture.EMPTY;
    	}
    }

	public final ImageInfo info(int index) {
		if(!loaded) return ImageInfo.EMPTY;
		if(index < 0) return ImageInfo.EMPTY;
		if(index >= imageCount) return ImageInfo.EMPTY;
		return imageInfos[index];
	}

	public int count() {
		return imageCount;
	}

}
