package com.imageloader.mhlistener.imageloaderlib;

import android.graphics.Bitmap;

/**
 * 位图加载回调。
 *
 * Created by JohnsonFan on 2018/3/7.
 */

public interface BitmapCallBack {

	/**
	 * 加载完成（回调）。
	 *
	 * @param bitmap 位图。
	 */
	void onBitmapLoaded(Bitmap bitmap);

	/**
	 * 加载失败（回调）。
	 *
	 * @param e 异常。
	 */
	void onBitmapFailed(Exception e);



	/**
	 * 空回调。
	 */
	public static class EmptyCallback implements BitmapCallBack {

		/**
		 * 加载完成（回调）。
		 *
		 * @param bitmap 位图。
		 */
		@Override
		public void onBitmapLoaded(Bitmap bitmap) {

		}

		/**
		 * 加载失败（回调）。
		 *
		 * @param e 异常。
		 */
		@Override
		public void onBitmapFailed(Exception e) {

		}
	}
}
