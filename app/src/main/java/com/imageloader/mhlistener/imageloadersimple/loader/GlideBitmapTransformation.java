package com.imageloader.mhlistener.imageloadersimple.loader;

import android.graphics.Bitmap;
import android.graphics.Matrix;

import androidx.annotation.NonNull;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.load.resource.bitmap.TransformationUtils;
import com.bumptech.glide.util.Preconditions;
import com.bumptech.glide.util.Util;

import java.nio.ByteBuffer;
import java.security.MessageDigest;

/**
 * https://muyangmin.github.io/glide-docs-cn/doc/configuration.html
 * https://blog.csdn.net/xct841990555/article/details/84036355
 *
 * 类描述：圆角、旋转、缩放。
 *
 * @author HeHongdan
 * @date 4/30/21
 * @since v4/30/21
 *
 * A {@link BitmapTransformation} which rounds the corners of a bitmap.
 */
public final class GlideBitmapTransformation extends BitmapTransformation {
  private static final String ID = "com.bumptech.glide.load.resource.bitmap.RoundedCorners";
  private static final byte[] ID_BYTES = ID.getBytes(CHARSET);

  /** 圆角。 */
  private final int roundingRadius;
  /** 旋转。 */
  private float rotateRotationAngle = 0f;


  /**
   * @param roundingRadius the corner radius (in device-specific pixels).
   * @throws IllegalArgumentException if rounding radius is 0 or less.
   */
  public GlideBitmapTransformation(int roundingRadius) {
    Preconditions.checkArgument(roundingRadius > 0, "roundingRadius must be greater than 0.");
    this.roundingRadius = roundingRadius;
  }

  /**
   * 构造方法。
   *
   * @param roundingRadius 圆角大小。
   * @param rotateRotationAngle 旋转角度。
   */
  public GlideBitmapTransformation(int roundingRadius, float rotateRotationAngle) {
    Preconditions.checkArgument(roundingRadius > 0, "roundingRadius must be greater than 0.");
    this.roundingRadius = roundingRadius;
    this.rotateRotationAngle = rotateRotationAngle;
  }

  @Override
  protected Bitmap transform(@NonNull BitmapPool pool, @NonNull Bitmap toTransform, int outWidth, int outHeight) {
    Matrix matrix = new Matrix();
    matrix.postRotate(rotateRotationAngle);
    Bitmap transformBitmap = Bitmap.createBitmap(toTransform, 0, 0, toTransform.getWidth(), toTransform.getHeight(), matrix, true);

    return TransformationUtils.roundedCorners(pool, transformBitmap, roundingRadius);
  }

  @Override
  public boolean equals(Object o) {
    if (o instanceof GlideBitmapTransformation) {
      GlideBitmapTransformation other = (GlideBitmapTransformation) o;
      return roundingRadius == other.roundingRadius;
    }
    return false;
  }

  @Override
  public int hashCode() {
    return Util.hashCode(ID.hashCode(), Util.hashCode(roundingRadius));
  }

  @Override
  public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {
    messageDigest.update(ID_BYTES);

    byte[] radiusData = ByteBuffer.allocate(4).putInt(roundingRadius).array();
    messageDigest.update(radiusData);
  }

}
