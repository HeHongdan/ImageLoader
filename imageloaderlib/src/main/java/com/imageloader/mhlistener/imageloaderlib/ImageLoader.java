package com.imageloader.mhlistener.imageloaderlib;

import android.net.Uri;

import java.io.File;

/**
 * 图片加载类（采取策略方式框架间解耦）
 * 策略或者静态代理模式，开发者只需要关心ImageLoader + LoaderOptions
 * Created by JohnsonFan on 2018/3/8.
 */
public class ImageLoader{

	/** 图片加载框架（统一接口） */
	private static ILoaderStrategy sLoader;
	/** 图片加载类（静态实例） */
	private static volatile ImageLoader sInstance;

	private ImageLoader() {
	}

	/**
	 * 单例模式
	 *
	 * @return 本类实例
	 */
	public static ImageLoader getInstance() {
		if (sInstance == null) {
			synchronized (ImageLoader.class) {
				if (sInstance == null) {
					sInstance = new ImageLoader();
				}
			}
		}
		return sInstance;
	}

	/**
	 * 提供全局替换图片加载框架的接口，若切换其它框架，可以实现一键全局替换
	 *
	 * @param loader	加载框架
	 */
	public void setGlobalImageLoader(ILoaderStrategy loader) {
		sLoader = loader;
	}

	/**
	 * 加载图片
	 *
	 * @param url 资源定位符
	 * @return 加载属性
	 */
	public LoaderOptions load(String url) {
		return new LoaderOptions(url);
	}

	/**
	 * 加载图片
	 *
	 * @param uri 资源标志符
	 * @return 加载属性
	 */
	public LoaderOptions load(Uri uri) {
		return new LoaderOptions(uri);
	}

	/**
	 * 加载图片
	 *
	 * @param file 资源文件
	 * @return 加载属性
	 */
	public LoaderOptions load(File file) {
		return new LoaderOptions(file);
	}

	/**
	 * 加载图片
	 *
	 * @param drawable drawable资源文件
	 * @return 加载属性
	 */
	public LoaderOptions load(int drawable) {
		return new LoaderOptions(drawable);
	}

	/**
	 * 优先使用实时设置的图片loader，其次使用全局设置的图片loader
	 *
	 * @param options	加载属性
	 */
	public void loadOptions(LoaderOptions options) {
		if (options.loader != null) {
			options.loader.loadImage(options);
		} else {
			checkNotNull();
			sLoader.loadImage(options);
		}
	}

	/**
	 * 清理内存缓存
	 */
	public void clearMemoryCache() {
		checkNotNull();
		sLoader.clearMemoryCache();
	}

	/**
	 * 清理磁盘缓存
	 */
	public void clearDiskCache() {
		checkNotNull();
		sLoader.clearDiskCache();
	}

	/**
	 * 校验加载框架为空
	 */
	private void checkNotNull() {
		if (sLoader == null) {
			throw new NullPointerException("你必须先设置图片加载框架！you must be set your imageLoader at first!");
		}
	}
}
