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
package com.github.jootnet.mir2.core;

/**
 * 热血传奇图片数据<br>
 * 使用三字节sRGB方式存放色彩数据<br>
 * 图片不支持透明色，背景为黑色<br>
 * 使用双缓冲加速图像处理
 * 
 * @author 云中双月
 */
public final class Texture implements Cloneable {

	private static int EMPTY_COLOR_INDEX = 0;
	/**
	 * 空图片
	 */
	public static final Texture EMPTY = new Texture(new byte[] { SDK.palletes[EMPTY_COLOR_INDEX][1],
			SDK.palletes[EMPTY_COLOR_INDEX][2], SDK.palletes[EMPTY_COLOR_INDEX][3], SDK.palletes[EMPTY_COLOR_INDEX][0] }, 1, 1);

	private byte[] pixels;
	private int width;
	private int height;

	/**
	 * 获取图片宽度
	 * 
	 * @return 图片宽度(像素)
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * 获取图片高度
	 * 
	 * @return 图片高度(像素)
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * 获取图片色彩数据<br>
	 * 每一个像素点以R G B A四个byte的分量存储<br>
	 * 即返回的数据长度为图片宽度*图片高度*3大小<br>
	 * 从图片左上角到右下角
	 * 
	 * @return 图片全部颜色数据
	 */
	public byte[] getRGBAs() {
		return pixels;
	}

	/**
	 * 获取图片特定点色彩数据
	 * 
	 * @param x
	 *            横坐标(像素)
	 * @param y
	 *            纵坐标(像素)
	 * @return 特定点色彩数据，三个字节依次表示RGB分量
	 */
	public byte[] getRGBA(int x, int y) {
		if (x > width - 1 || y > height - 1)
			return new byte[] { 0, 0, 0 };
		int _idx = (x + y * width) * 3;
		byte[] ret = new byte[3];
		ret[0] = pixels[_idx];
		ret[1] = pixels[_idx + 1];
		ret[2] = pixels[_idx + 2];
		ret[3] = pixels[_idx + 3];
		return ret;
	}

	/**
	 * 从RGB字节数组创建图片数据
	 *
	 * @param sRGB
	 *            图片色彩数据数据<br>
	 *            每个像素占用四个字节进行存储，从图片左上角到右下角，必须是RGBA顺序
	 * @param width
	 *            图片宽度
	 * @param height
	 *            图片高度
	 *
	 * @throws IllegalArgumentException
	 *             传入的像素数据长度不符合要求
	 */
	public Texture(byte[] sRGB, int width, int height) throws IllegalArgumentException {
		if (sRGB != null && width > 0 && height > 0 && sRGB.length != (width * height * 4))
			throw new IllegalArgumentException("sRGB length not match width * height * 4 !!!");
		this.pixels = sRGB;
		this.width = width;
		this.height = height;
	}

	/**
	 * 判断当前图片是否为空
	 * 
	 * @return true表示当前图片为空，不可用于任何处理/绘制/序列化
	 */
	public final boolean empty() {
		return this == EMPTY || pixels == null || pixels.length == 0 || width <= 1 || height <= 1;
	}

	/**
	 * 增加一个函数用于释放内存。不调用的话有内存泄漏
	 */
	public void release() {
		this.pixels = EMPTY.pixels;
		this.width = EMPTY.width;
		this.height = EMPTY.height;
	}
}
