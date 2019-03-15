package com.imageloader.mhlistener.imageloaderlib;

/**
 * 图片加载框架统一接口（为了轻松替换Glide、Fresco、Picasso...）
 *
 * Created by JohnsonFan on 2017/6/9.
 */
public interface ILoaderStrategy {

	/**
	 * 加载图片
	 *
	 * @param options 加载属性
	 */
	void loadImage(LoaderOptions options);

	/**
	 * 清理内存缓存
	 */
	void clearMemoryCache();

	/**
	 * 清理磁盘缓存
	 */
	void clearDiskCache();

}
