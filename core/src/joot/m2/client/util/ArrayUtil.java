package joot.m2.client.util;

import java.lang.reflect.Array;

/**
 * 数组工具类
 * 
 * @author 林星
 *
 */
public final class ArrayUtil {
	
	/**
	 * 判断数组特定索引处元素是否不为空
	 * 
	 * @param <T> 泛型
	 * @param arr 数组
	 * @param index 索引
	 * @return 如果数组为空、长度不足或给定索引处为null，则返回false
	 */
	public static <T> boolean eleNotNull(T[] arr, int index) {
		if (arr == null) return false;
		if (arr.length <= index) return false;
		return arr[index] != null;
	}

	/**
	 * 将给定数组以最小长度重设大小
	 * <br>
	 * 若给定数组已经足够长，则不会改变原数组
	 * 
	 * @param <T> 泛型
	 * @param origin 原数组
	 * @param minSize 最小长度
	 * @return 符合最小长度的新数组，之前数组对应索引处的元素会保留，其余位置为null
	 */
	@SuppressWarnings("unchecked")
	public static <T> T[] resize(T[] origin, int minSize) {
		if (origin.length >= minSize) return origin;
		var items = origin;
		var newItems = (T[])Array.newInstance(items.getClass().getComponentType(), minSize);
		System.arraycopy(items, 0, newItems, 0, Math.min(origin.length, newItems.length));
		return newItems;
	}
	
	/**
	 * 设置数组特定索引处元素
	 * <br>
	 * 若数组不够长，则先对其进行重设大小
	 * 
	 * @param <T> 泛型
	 * @param arr 原数组
	 * @param index 索引
	 * @param ele 元素
	 * @return 可能重设大小后的新数组
	 * @see #resize(Object[], int)
	 */
	public static <T> T[] setEle(T[] arr, int index, T ele) {
		var items = resize(arr, index + 1);
		items[index] = ele;
		return items;
	}
}
