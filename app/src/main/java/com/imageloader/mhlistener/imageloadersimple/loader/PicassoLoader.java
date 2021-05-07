package com.imageloader.mhlistener.imageloadersimple.loader;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.imageloader.mhlistener.imageloaderlib.BitmapCallBack;
import com.imageloader.mhlistener.imageloaderlib.ILoaderStrategy;
import com.imageloader.mhlistener.imageloaderlib.LoaderOptions;
import com.imageloader.mhlistener.imageloadersimple.App;
import com.squareup.picasso.LruCache;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.squareup.picasso.Target;
import com.squareup.picasso.Transformation;

import java.io.File;

/**
 * Picasso(毕加索)图片加载框架（实现统一接口）
 *
 * Created by JohnsonFan on 2017/6/27.
 */

public class PicassoLoader implements ILoaderStrategy {
	private volatile static Picasso sPicassoSingleton;
	/** 磁盘缓存路径。 */
	private final String PICASSO_CACHE = "picasso-cache";
	private static LruCache sLruCache = new LruCache(App.gApp);

	private static Picasso getPicasso() {
		if (sPicassoSingleton == null) {
			synchronized (PicassoLoader.class) {
				if (sPicassoSingleton == null) {
					sPicassoSingleton = new Picasso.Builder(App.gApp).memoryCache(sLruCache).build();
				}
			}
		}
		return sPicassoSingleton;
	}

	@Override
	public void clearMemoryCache() {
		sLruCache.clear();
	}

	@Override
	public void clearDiskCache() {
		File diskFile = new File(App.gApp.getCacheDir(), PICASSO_CACHE);
		if (diskFile.exists()) {
			//这边自行写删除代码
//	        FileUtil.deleteFile(diskFile);
		}
	}

	@Override
	public void loadImage(LoaderOptions options) {
		RequestCreator requestCreator = null;
		// 返回一个新建的RequestCreator对象
		if (options.url != null) {
			requestCreator = getPicasso().load(options.url);
		} else if (options.file != null) {
			requestCreator = getPicasso().load(options.file);
		}else if (options.drawableResId != 0) {
			requestCreator = getPicasso().load(options.drawableResId);
		} else if (options.uri != null){
			requestCreator = getPicasso().load(options.uri);
		}

		if (requestCreator == null) {
//			throw new NullPointerException("requestCreator must not be null");
			throw new NullPointerException("Picasso 的 RequestCreator 必须不能为空");
		}

		//圆角
		if (options.bitmapAngle != 0) {
			requestCreator.transform(new PicassoTransformation(options.bitmapAngle));
		}
		//旋转角度
		if (options.degrees != 0) {
			requestCreator.rotate(options.degrees);
		}
		// 设置宽高
		if (options.targetHeight > 0 && options.targetWidth > 0) {
			requestCreator.resize(options.targetWidth, options.targetHeight);
		}
//		.onlyScaleDown() // 设置缩放
		// 设置裁剪方式
		if (options.isCenterInside) {
			requestCreator.centerInside();
		} else if (options.isCenterCrop) {
			requestCreator.centerCrop();
		}
		// 设置图片(质量)格式
		if (options.config != null) {
			requestCreator.config(options.config);
		}
		// 设置占位(出错)图
		if (options.errorResId != 0) {
			requestCreator.error(options.errorResId);
		}
		// 设置占位(加载)图
		if (options.placeholderResId != 0) {
			requestCreator.placeholder(options.placeholderResId);
		}
		// 设置内存缓存策略
		if (options.skipLocalCache) {
			requestCreator.memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE);
		}
		// 设置网络(磁盘)缓存策略
		if (options.skipNetCache) {
			requestCreator.networkPolicy(NetworkPolicy.NO_CACHE, NetworkPolicy.NO_STORE);
		}

		// 设置图片加载的目标控件
		if (options.targetView instanceof ImageView) {
			requestCreator.into(((ImageView)options.targetView));
		} else if (options.callBack != null){
			requestCreator.into(new PicassoTarget(options.callBack));
		}
	}

	/**
	 * Picasso 加载结果回调。
	 */
	class PicassoTarget implements Target {

		/** (本框架)位图加载回调。 */
		BitmapCallBack callBack;

		protected PicassoTarget(BitmapCallBack callBack) {
			this.callBack = callBack;
		}

		@Override
		public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
			if (this.callBack != null) {
				this.callBack.onBitmapLoaded(bitmap);
			}
		}

		@Override
		public void onBitmapFailed(Exception e, Drawable errorDrawable) {
			if (this.callBack != null) {
				this.callBack.onBitmapFailed(e);
			}
		}

		@Override
		public void onPrepareLoad(Drawable placeHolderDrawable) {

		}
	}

	class PicassoTransformation implements Transformation {
		private float bitmapAngle;

		protected PicassoTransformation(float corner){
			this.bitmapAngle = corner;
		}

		@Override
		public Bitmap transform(Bitmap source) {
			float roundPx = bitmapAngle;//圆角的横向半径和纵向半径
			Bitmap output = Bitmap.createBitmap(source.getWidth(),
					source.getHeight(), Bitmap.Config.ARGB_8888);
			Canvas canvas = new Canvas(output);
			final int color = 0xff424242;
			final Paint paint = new Paint();
			final Rect rect = new Rect(0, 0, source.getWidth(),source.getHeight());
			final RectF rectF = new RectF(rect);
			paint.setAntiAlias(true);
			canvas.drawARGB(0, 0, 0, 0);
			paint.setColor(color);
			canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
			paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
			canvas.drawBitmap(source, rect, rect, paint);
			source.recycle();
			return output;
		}

		@Override
		public String key() {
			return "bitmapAngle()";
		}
	}

}
