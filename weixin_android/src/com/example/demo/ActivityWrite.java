package com.example.demo;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Calendar;

import com.example.demo.view.DrawPhotoView;
import com.example.demo.view.DrawPic;
import com.example.demo.view.DrawView;

import android.R.integer;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore.Images.Media;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import uk.co.senab.photoview.PhotoView;

public class ActivityWrite extends Activity implements OnClickListener, OnTouchListener {
	// 手写界面
	DrawView drawView;
	Button btn_save, btn_clear, btn_choose;
//	DrawPhotoView photoView ;
	PhotoView photoView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_write);

		drawView = (DrawView) findViewById(R.id.drawview);
		btn_save = (Button) findViewById(R.id.btn_save);
		btn_clear = (Button) findViewById(R.id.btn_clear);
		btn_choose = (Button) findViewById(R.id.btn_choose);
		photoView = (DrawPhotoView) findViewById(R.id.drawpic);
//		photoView.setImageResource(R.drawable.ic_launcher);
//		photoView.setBackground(background);
		
		btn_clear.setOnClickListener(this);
		btn_save.setOnClickListener(this);
		btn_choose.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_clear:// 清空
			drawView.clear();
			break;
		case R.id.btn_save:// 保存
			SaveTask task = new SaveTask();
			task.execute();
			break;
		case R.id.btn_choose:// 选取图片
			Intent intent = new Intent(Intent.ACTION_PICK, Media.EXTERNAL_CONTENT_URI);
			startActivityForResult(intent, 0);
			break;
		default:
			break;
		}
	}

	/**
	 * 保存的异步线程
	 * 
	 * @author Administrator
	 *
	 */
	class SaveTask extends AsyncTask<Void, Void, String> {

		@Override
		protected String doInBackground(Void... params) {
			String post = "";
			// 判断sd卡是否存在可用
			String state = Environment.getExternalStorageState();
			if (!state.equals(Environment.MEDIA_MOUNTED)) {
				return "sd卡未准备好";
			}
			// 利用时间生成文件名
			Calendar calendar = Calendar.getInstance();

			Calendar c = Calendar.getInstance();
			String name = "" + c.get(Calendar.YEAR) + c.get(Calendar.MONTH) + c.get(Calendar.DAY_OF_MONTH)
					+ c.get(Calendar.HOUR_OF_DAY) + c.get(Calendar.MINUTE) + c.get(Calendar.SECOND) + ".png";
			Log.i("文件名", name);
			// 保存文件
			File file = new File(Environment.getExternalStorageDirectory(), name);
			try {
//				drawView.saveToFile(file.getAbsolutePath());
//				photoView.saveToFile(file.getAbsolutePath());
			} catch (Exception e) {
				e.printStackTrace();
				return "保存失败";
			}
			return "保存成功：" + name;
		}

		@Override
		protected void onPostExecute(String text) {
			super.onPostExecute(text);
			Toast.makeText(ActivityWrite.this, text, Toast.LENGTH_SHORT).show();
		}
	}
	
	
	Canvas canvas;
	Paint paint;
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			Uri uri = data.getData();
			// 创建bitmapfactory的option
			BitmapFactory.Options option = new Options();
			// 获得view的尺寸
			int width_iv = photoView.getWidth();
			int height_iv = photoView.getHeight();
			// 设置获取位图的尺寸
			option.inJustDecodeBounds = true;
			Bitmap bitmap = null;
			try {
				bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri), null, option);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			// 获取缩放比例
			int wRatio = (int) Math.ceil(option.outHeight / ((float) width_iv));
			int hRatio = (int) Math.ceil(option.outWidth / (float) height_iv);
			if (wRatio > 1 || hRatio > 1) {
				if (wRatio > hRatio) {
					option.inSampleSize = wRatio;
				} else {
					option.inSampleSize = hRatio;
				}
			}

			// 对于图像进行真正的解码
			option.inJustDecodeBounds = false;
			try {
				bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri), null, option);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			// 创建位图的bitmap
			Bitmap alteredBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());
			// 设置画笔
			paint = new Paint();
			paint.setStrokeWidth(3);
			paint.setColor(Color.BLUE);
			// 设置位图
			canvas = new Canvas(alteredBitmap);
			Matrix matrix = new Matrix();
			canvas.drawBitmap(bitmap, matrix, paint);
			// 给图像设置bitmap
//			imageview.setImageBitmap(alteredBitmap);
			photoView.setImageBitmap(alteredBitmap);
			
//			imageview.ImageShowURIPic(uri);
		}
	}

	float curX;
	float curY;

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		float x = event.getX();
		float y = event.getY();
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			curX=x;
			curY=y;
			break;
		case MotionEvent.ACTION_MOVE:
			canvas.drawLine(curX, curY, x, y, paint);
			curX=x;
			curY=y;
			break;
		case MotionEvent.ACTION_UP:
			curX=x;
			curY=y;
			break;

		default:
			break;
		}
		photoView.invalidate();
		return true;
//		return super.onTouchEvent(event);
	}
	
	
}
