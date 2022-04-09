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

/**
 * 热血传奇2图片信息
 * 
 * @author 云中双月
 */
public final class ImageInfo {

	public static final ImageInfo EMPTY = new ImageInfo();
	
	ImageInfo() { }
	
	private int width;
	private int height;
	private short offsetX;
	private short offsetY;
	private byte colorBit = 8;
	boolean wzlCompressed = true;
	
	/**
	 * 获取图片色深<br>
	 * 可取值为
	 * <ul>
	 * <li>1:黑白两种颜色</li>
	 * <li>8:256种颜色</li>
	 * <li>16:65536种颜色</li>
	 * <li>24:16777215种颜色</li>
	 * <li>32:4294967295种颜色</li>
	 * </ul>
	 * 
	 * @return 图片色深度
	 */
	public byte getColorBit() {
		return colorBit;
	}
	void setColorBit(byte colorBit) {
		this.colorBit = colorBit;
	}
	
	/**
	 * 获取图片宽度
	 * 
	 * @return 图片宽度,单位为像素
	 */
	public int getWidth() {
		return width;
	}
	void setWidth(int width) {
		this.width = width;
	}

	/**
	 * 获取图片高度
	 * 
	 * @return 图片高度,单位为像素
	 */
	public int getHeight() {
		return height;
	}
	void setHeight(int height) {
		this.height = height;
	}

	/**
	 * 获取图片横向偏移量
	 * 
	 * @return 图片横向偏移量,单位为像素
	 */
	public short getOffsetX() {
		return offsetX;
	}
	void setOffsetX(short offsetX) {
		this.offsetX = offsetX;
	}

	/**
	 * 获取图片纵向偏移量
	 * 
	 * @return 图片纵向偏移量,单位为像素
	 */
	public short getOffsetY() {
		return offsetY;
	}
	void setOffsetY(short offsetY) {
		this.offsetY = offsetY;
	}
}
