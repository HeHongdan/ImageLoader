package com.imageloader.mhlistener.imageloadersimple.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

/**
 * 类描述：。
 *
 * @author HeHongdan
 * @date 5/6/21
 * @since v5/6/21
 *
 *
 * <pre>
 *     author: Blankj
 *     blog  : http://blankj.com
 *     time  : 2016/08/12
 *     desc  : utils about image
 * </pre>
 */
public final class ImageUtils {


	/**
	 * Drawable to bitmap.
	 *
	 * @param drawable The drawable.
	 * @return bitmap
	 */
	public static Bitmap drawable2Bitmap(final Drawable drawable) {
		if (drawable instanceof BitmapDrawable) {
			BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
			if (bitmapDrawable.getBitmap() != null) {
				return bitmapDrawable.getBitmap();
			}
		}
		Bitmap bitmap;
		if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
			bitmap = Bitmap.createBitmap(1, 1,
					drawable.getOpacity() != PixelFormat.OPAQUE
							? Bitmap.Config.ARGB_8888
							: Bitmap.Config.RGB_565);
		} else {
			bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
					drawable.getIntrinsicHeight(),
					drawable.getOpacity() != PixelFormat.OPAQUE
							? Bitmap.Config.ARGB_8888
							: Bitmap.Config.RGB_565);
		}
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
		drawable.draw(canvas);
		return bitmap;
	}

}

