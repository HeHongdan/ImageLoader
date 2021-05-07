package com.imageloader.mhlistener.imageloadersimple;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.imageloader.mhlistener.imageloaderlib.BitmapCallBack;
import com.imageloader.mhlistener.imageloaderlib.ImageLoader;
import com.imageloader.mhlistener.imageloaderlib.LoaderOptions;

public class MainActivity extends AppCompatActivity {

	ImageView imageView;
	Button bt_reLoad;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		imageView = findViewById(R.id.imageview);
		bt_reLoad = findViewById(R.id.bt_reLoad);
		bt_reLoad.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				loadImage(imageView);
			}
		});
		loadImage(imageView);
	}

	/**
	 * 加载图片，使用方式。
	 *
	 * @param imageView 显示图片组件。
	 */
	private void loadImage(final ImageView imageView) {
		String url = "https://ww2.sinaimg.cn/large/7a8aed7bgw1eutsd0pgiwj20go0p0djn.jpg";
		LoaderOptions rotate = ImageLoader.getInstance()
				.load(url)
				.angle(80)
				.resize(400, 600)
				.centerCrop()
				.config(Bitmap.Config.RGB_565)
				.placeholder(R.mipmap.ic_launcher)
				.error(R.mipmap.test)
				.skipLocalCache(true)
				.skipNetCache(true)
				.rotate(0);

		if (true) {
			rotate.into(imageView);
		} else {
			rotate.bitmap(new BitmapCallBack() {
				@Override
				public void onBitmapLoaded(Bitmap bitmap) {
					Log.d("【HHD】", "加载成功= " + bitmap);
					imageView.setImageBitmap(bitmap);
				}

				@Override
				public void onBitmapFailed(Exception e) {
					Log.d("【HHD】", "加载失败= " + e);
				}
			});
		}

	}

}
