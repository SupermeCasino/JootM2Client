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

import java.io.Closeable;

import com.github.jootnet.mir2.core.Texture;

/**
 * 图片库通用接口
 * 
 * @author 云中双月
 */
public interface ImageLibrary extends Closeable {

	/**
	 * 获取图片库中图片数量
	 * 
	 * @return 图片数量
	 */
	int count();
	
	/**
	 * 获取图片库中指定索引的图片数据
	 * 
	 * @param index
	 * 		图片索引
	 * @return 对应图片数据
	 */
	Texture tex(int index);
	
	/**
	 * 获取图片库中指定索引的图片信息
	 * 
	 * @param index
	 * 		图片索引
	 * @return 对应图片信息
	 */
	ImageInfo info(int index);
}
