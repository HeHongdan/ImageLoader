package com.imageloader.mhlistener.imageloadersimple;

import android.app.Application;

import com.imageloader.mhlistener.imageloaderlib.ImageLoader;
import com.imageloader.mhlistener.imageloadersimple.loader.GlideLoader;
import com.imageloader.mhlistener.imageloadersimple.loader.PicassoLoader;

/**
 * Created by JohnsonFan on 2017/12/9.
 */

public class App extends Application {

	public static App gApp;

	@Override
	public void onCreate() {
		super.onCreate();
		gApp = this;

		//初始化图片库
		if (false) {
			ImageLoader.getInstance().setGlobalImageLoader(new PicassoLoader());
		} else {
			ImageLoader.getInstance().setGlobalImageLoader(new GlideLoader());
		}
	}
}
