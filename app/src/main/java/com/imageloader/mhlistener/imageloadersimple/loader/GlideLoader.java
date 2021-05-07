package com.imageloader.mhlistener.imageloadersimple.loader;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.cache.ExternalCacheDiskCacheFactory;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.imageloader.mhlistener.imageloaderlib.ILoaderStrategy;
import com.imageloader.mhlistener.imageloaderlib.LoaderOptions;
import com.imageloader.mhlistener.imageloadersimple.App;

import java.io.File;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import jp.wasabeef.glide.transformations.gpu.PixelationFilterTransformation;

/**
 * 类描述：Glide 图片加载框架（实现统一接口）
 *
 * @author hehongdan
 * @version v2019/3/15
 * @date 2019/3/15
 */
public class GlideLoader implements ILoaderStrategy {

    /** 磁盘缓存路径。 */
    private final String GLIDE_CACHE = "picasso-cache";




    @SuppressLint("CheckResult")
    @Override
    public void loadImage(final @NonNull LoaderOptions options) {
        RequestOptions requestOptions = new RequestOptions();
//        if (requestOptions == null) {
//            throw new NullPointerException("Glide 的 RequestOptions 必须不能为空");
//        }
        // 设置宽高
        if (options.targetHeight > 0 && options.targetWidth > 0) {
            requestOptions.override(options.targetWidth, options.targetHeight);
        } else {
            requestOptions.override(Target.SIZE_ORIGINAL);
        }
        // 设置裁剪方式
        if (options.isCenterInside) {
            requestOptions.centerInside();
        } else if (options.isCenterCrop) {
            requestOptions.centerCrop();
        }
        // 设置图片(质量)格式
        if (options.config != null) {
            /**
             * Bitmap.Config.
             *	ALPHA_8,
             * 	RGB_565,
             *	ARGB_4444,
             *	ARGB_8888,
             *	RGBA_F16,
             *	HARDWARE;
             */
            if (options.config == Bitmap.Config.ARGB_8888) {
                requestOptions.format(DecodeFormat.PREFER_ARGB_8888);
            } else {
                requestOptions.format(DecodeFormat.PREFER_RGB_565);
            }
        }
        // 设置占位(出错)图
        if (options.errorResId != 0) {
            requestOptions.error(options.errorResId);
        }
        // 设置占位(加载)图
        if (options.placeholderResId != 0) {
            requestOptions.placeholder(options.placeholderResId);
        }
        // 设置内存、网络(磁盘)缓存策略
        requestOptions.skipMemoryCache(options.skipLocalCache);
        if (options.skipNetCache) {
            //skipMemoryCache ： true 关闭内存缓存，false 打开内存缓存
            //diskCacheStrategy： DiskCacheStrategy.NONE 关闭磁盘缓存

            // DiskCacheStrategy.NONE： 表示不缓存任何内容。
            //DiskCacheStrategy.DATA： 表示只缓存原始图片。
            //DiskCacheStrategy.RESOURCE： 表示只缓存转换过后的图片。
            //DiskCacheStrategy.ALL ： 表示既缓存原始图片，也缓存转换过后的图片。
            //DiskCacheStrategy.AUTOMATIC： 表示让Glide根据图片资源智能地选择使用哪一种缓存策略（默认选项）。
            requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);
        } else {
            requestOptions.diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);
        }

        //进(出)场动画
//			requestCreator.transform();

        //圆角
        if (options.bitmapAngle != 0) {
            if (true) {
                requestOptions = requestOptions.transform(new RoundedCorners((int) options.bitmapAngle));
            } else {
                //Crop
                //CropTransformation
                //CropCircleTransformation
                //CropCircleWithBorderTransformation
                //CropSquareTransformation
                //RoundedCornersTransformation
                //Color
                //ColorFilterTransformation
                //GrayscaleTransformation
                //Blur
                //BlurTransformation
                //Mask
                //MaskTransformation

                //GPU Filter (use GPUImage)
                //ToonFilterTransformation
                //SepiaFilterTransformation
                //ContrastFilterTransformation
                //InvertFilterTransformation
                //PixelationFilterTransformation
                //SketchFilterTransformation
                //SwirlFilterTransformation
                //BrightnessFilterTransformation
                //KuwaharaFilterTransformation
                //VignetteFilterTransformation
                requestOptions = requestOptions.transforms(new PixelationFilterTransformation(), new RoundedCornersTransformation((int) options.bitmapAngle,0));
            }
        }




        // 设置图片加载的目标控件
        if (options.targetView instanceof ImageView) {
            RequestBuilder<Drawable> builder = null;
//            RequestBuilder<GifDrawable> builder = null;
//            RequestBuilder<File> builder = null;
//            RequestBuilder<Bitmap> builder = null;

            RequestManager manager = Glide.with(options.targetView.getContext());
            if (options.url != null) {
                builder = manager.load(options.url);
            } else if (options.file != null) {
                builder = manager.load(options.file);
            } else if (options.drawableResId != 0) {
                builder = manager.load(options.drawableResId);
            } else if (options.uri != null) {
                builder = manager.load(options.uri);
            }

            ImageView imageView = (ImageView) options.targetView;
            if (builder != null) {
                builder.apply(requestOptions).into(imageView);
            } else {
                Glide.with(imageView.getContext()).load(options.errorResId).into(imageView);
            }
        } else if (options.callBack != null) {
            makeBitmap(options, requestOptions);
        }

    }

    private void makeBitmap(final LoaderOptions options, final RequestOptions requestOptions) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (options.url != null) {
                        //阻塞执行
                        FutureTarget<File> target = Glide.with(App.gApp)//FIXME
                                .load(options.url)
                                .apply(requestOptions)//FIXME
                                .downloadOnly(options.targetWidth, options.targetHeight);
//                                .downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL);
                        final File imageFile = target.get();

                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                if (imageFile != null) {
                                    options.callBack.onBitmapLoaded(BitmapFactory.decodeFile(imageFile.getAbsolutePath()));
                                } else {
                                    throw new NullPointerException("图片文件为空。");
                                }
                            }
                        });

                    } else {
                        throw new IllegalArgumentException("图片的 Url 不能为空。");
                    }
                } catch (Exception e) {
                    e.printStackTrace();

                    options.callBack.onBitmapFailed(e);
                }
            }
        }).start();
    }


    @Override
    public void clearMemoryCache() {
        clearImageMemoryCache(App.gApp);
    }

    @Override
    public void clearDiskCache() {
        if (true) {
            clearImageDiskCache(App.gApp);
        } else {
            File diskFile = new File(App.gApp.getCacheDir(), GLIDE_CACHE);
            if (diskFile.exists()) {
                //这边自行写删除代码
//	        FileUtil.deleteFile(diskFile);
            }
        }
    }


    /**
     * 清除图片内存缓存
     */
    public void clearImageMemoryCache(Context context) {
        try {
            if (Looper.myLooper() == Looper.getMainLooper()) { //只能在主线程执行
                Glide.get(context).clearMemory();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 清除图片磁盘缓存
     */
    public void clearImageDiskCache(final Context context) {
        try {
            if (Looper.myLooper() == Looper.getMainLooper()) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.get(context).clearDiskCache();
                    }
                }).start();
            } else {
                Glide.get(context).clearDiskCache();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 清除图片所有缓存
     */
    public void clearImageAllCache(Context context) {
        clearImageDiskCache(context);
        clearImageMemoryCache(context);
        String ImageExternalCatchDir = context.getExternalCacheDir() + ExternalCacheDiskCacheFactory.DEFAULT_DISK_CACHE_DIR;
        deleteFolderFile(ImageExternalCatchDir, true);
    }


    /**
     * 删除指定目录下的文件，这里用于缓存的删除
     *
     * @param filePath       filePath
     * @param deleteThisPath deleteThisPath
     */
    private void deleteFolderFile(String filePath, boolean deleteThisPath) {
        if (!TextUtils.isEmpty(filePath)) {
            try {
                File file = new File(filePath);
                if (file.isDirectory()) {
                    File files[] = file.listFiles();
                    for (File file1 : files) {
                        deleteFolderFile(file1.getAbsolutePath(), true);
                    }
                }
                if (deleteThisPath) {
                    if (!file.isDirectory()) {
                        file.delete();
                    } else {
                        if (file.listFiles().length == 0) {
                            file.delete();
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}
