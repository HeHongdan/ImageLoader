package com.imageloader.mhlistener.imageloaderlib;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.View;

import androidx.annotation.DrawableRes;

import java.io.File;

/**
 * 图片属性（Builder设计模式：可以继续继承并拓展）
 * 该类为图片加载框架的通用属性封装，不能耦合任何一方的框架
 *
 * Created by JohnsonFan on 2017/7/13.
 */
public class LoaderOptions {
	/** 占位图 加载中 */
	public int placeholderResId;
	/** 占位图 加载中 */
	public Drawable placeholder;
	/** 占位图 加载出错 */
	public int errorResId;
	/** 是否居中填满 超过裁剪 */
	public boolean isCenterCrop;
	/** 是否完整居中 按比例 */
	public boolean isCenterInside;
	/** 是否缓存到本地 */
	public boolean skipLocalCache;
	/** 是否缓存没有也不请求网络 */
	public boolean skipNetCache;
	/** 位图压缩质量 */
	public Bitmap.Config config = Bitmap.Config.RGB_565;
	/** 目标（显示）宽度 */
	public int targetWidth;
	/** 目标（显示）高度 */
	public int targetHeight;
	/** 圆角角度 */
	public float bitmapAngle;
	/** 旋转角度（注意:picasso针对三星等本地图片，默认旋转回0度，即正常位置。此时不需要自己rotate） */
	public float degrees;
	/** 展示图片组件 */
	public View targetView;
	public BitmapCallBack callBack;
	/** 资源定位符 */
	public String url;
	/** 资源标志符 */
	public Uri uri;
	/** 资源文件 */
	public File file;
	/** drawable资源文件 */
	public int drawableResId;
	/** 图片加载库（可实时切换） */
	public ILoaderStrategy loader;

	/**
	 * 加载图片属性
	 *
	 * @param url	资源定位符
	 */
	public LoaderOptions(String url) {
		this.url = url;
	}

	/**
	 * 加载图片属性
	 *
	 * @param uri 资源标志符
	 */
	public LoaderOptions(Uri uri) {
		this.uri = uri;
	}

	/**
	 * 加载图片属性
	 *
	 * @param file	资源文件
	 */
	public LoaderOptions(File file) {
		this.file = file;
	}

	/**
	 * 加载图片属性
	 *
	 * @param drawableResId drawable资源文件
	 */
	public LoaderOptions(int drawableResId) {
		this.drawableResId = drawableResId;
	}

	/**
	 * 展示图片组件
	 *
	 * @param targetView	组件
	 */
	public void into(View targetView) {
		this.targetView = targetView;
		ImageLoader.getInstance().loadOptions(this);
	}

	public void bitmap(BitmapCallBack callBack) {
		this.callBack = callBack;
		ImageLoader.getInstance().loadOptions(this);
	}

	public LoaderOptions loader(ILoaderStrategy imageLoader) {
		this.loader = imageLoader;
		return this;
	}

	/**
	 * 占位图 加载中
	 *
	 * @param placeholderResId	占位图
	 * @return	加载属性
	 */
	public LoaderOptions placeholder(@DrawableRes int placeholderResId) {
		this.placeholderResId = placeholderResId;
		return this;
	}

	public LoaderOptions placeholder(Drawable placeholder) {
		this.placeholder = placeholder;
		return this;
	}

	/**
	 * 占位图 加载出错
	 *
	 * @param errorResId	占位图
	 * @return	加载属性
	 */
	public LoaderOptions error(@DrawableRes int errorResId) {
		this.errorResId = errorResId;
		return this;
	}

	/**
	 * 是否居中填满（超过裁剪）
	 *
	 * @return 加载属性
	 */
	public LoaderOptions centerCrop() {
		isCenterCrop = true;
		return this;
	}

	public LoaderOptions centerInside() {
		isCenterInside = true;
		return this;
	}

	/**
	 * 位图压缩质量
	 *
	 * @param config	压缩质量
	 * @return	加载属性
	 */
	public LoaderOptions config(Bitmap.Config config) {
		this.config = config;
		return this;
	}

	/**
	 * 目标（显示）宽度/高度
	 *
	 * @param targetWidth 目标宽度
	 * @param targetHeight	目标高度
	 * @return	加载属性
	 */
	public LoaderOptions resize(int targetWidth, int targetHeight) {
		this.targetWidth = targetWidth;
		this.targetHeight = targetHeight;
		return this;
	}

	/**
	 * 圆角
	 * @param bitmapAngle   度数
	 * @return	加载属性
	 */
	public LoaderOptions angle(float bitmapAngle) {
		this.bitmapAngle = bitmapAngle;
		return this;
	}

	/**
	 * 是否缓存到本地
	 *
	 * @param skipLocalCache	是否缓存
	 * @return 加载属性
	 */
	public LoaderOptions skipLocalCache(boolean skipLocalCache) {
		this.skipLocalCache = skipLocalCache;
		return this;
	}

	public LoaderOptions skipNetCache(boolean skipNetCache) {
		this.skipNetCache = skipNetCache;
		return this;
	}

	/**
	 * 旋转
	 *
	 * @param degrees 角度
	 * @return	加载属性
	 */
	public LoaderOptions rotate(float degrees) {
		this.degrees = degrees;
		return this;
	}

}


