package com.example.demo.view;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.example.demo.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Xfermode;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory.Options;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

public class DrawPic extends ImageView {

	int clr_bg, clr_fg;
	Paint mPaint;
	Path path;
	Bitmap cacheBitmap;
	Canvas cacheCanvas;
	Bitmap imagebitmap;

	ShapeDrawable sharedrawable;
	Context mContext;
	
	public DrawPic(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		mContext=context;
		
		clr_bg = Color.WHITE;
		clr_fg = Color.CYAN;

		mPaint = new Paint();
		mPaint.setDither(true);
		mPaint.setAntiAlias(true);
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setStrokeWidth(3);
		mPaint.setColor(clr_fg);
//		mPaint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
		path = new Path();

		imagebitmap=BitmapFactory.decodeResource(context.getResources(), R.drawable.applog);
		
		cacheBitmap = Bitmap.createBitmap(480, 480, Config.ARGB_8888);

		cacheCanvas = new Canvas(cacheBitmap);
//		cacheCanvas.drawBitmap(cacheBitmap, null, null);
//		image.setBounds(0, 0, 180, 180);
//		image.draw(cacheCanvas);


	}

	public DrawPic(Context context) {
		super(context);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.drawBitmap(cacheBitmap, 0, 0,null);
		canvas.drawPath(path, mPaint);
	}

	float curX;
	float curY;
	boolean isMoving = false;

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		float x = event.getX();
		float y = event.getY();

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			curX = x;
			curY = y;
			path.moveTo(curX, curY);
			isMoving = true;
			break;
		case MotionEvent.ACTION_MOVE:
			path.quadTo(curX, curY, x, y);
			curX = x;
			curY = y;

			isMoving = true;

			break;
		case MotionEvent.ACTION_UP:
			cacheCanvas.drawPath(path, mPaint);
			path.reset();
			isMoving = false;
			break;

		default:
			break;
		}
		invalidate();
		return true;
	}
	
	public void ImageShowURIPic(Uri uri){
		// 创建bitmapfactory的option
					BitmapFactory.Options option = new Options();
					// 获得view的尺寸
					int width_iv = getWidth();
					int height_iv = getHeight();
					// 设置获取位图的尺寸
					option.inJustDecodeBounds = true;
					Bitmap bitmap = null;
					try {
						bitmap = BitmapFactory.decodeStream(mContext.getContentResolver().openInputStream(uri), null, option);
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
						bitmap = BitmapFactory.decodeStream(mContext.getContentResolver().openInputStream(uri), null, option);
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}
					// 创建位图的bitmap
//					Bitmap alteredBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());
					cacheBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());
					// 设置画笔
//					mPaint = new Paint();
//					paint.setStrokeWidth(3);
//					paint.setColor(Color.BLUE);
					// 设置位图
					cacheCanvas= new Canvas(cacheBitmap);
					Matrix matrix = new Matrix();
					cacheCanvas.drawBitmap(bitmap, matrix, mPaint);
					// 给图像设置bitmap
//					this.setImageBitmap(cacheBitmap);
					invalidate();
	}
	
	public void saveToFile(String filename) throws FileNotFoundException{
		File file=new File(filename);
		if (file.exists()) {
			throw new RuntimeException("文件:"+filename+"已经存在");
		}
		FileOutputStream fos=new FileOutputStream(file);
		cacheBitmap.compress(CompressFormat.PNG, 50, fos);
		try {
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
