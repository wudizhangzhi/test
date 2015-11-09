package com.example.demo.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Bitmap.Config;
import android.util.AttributeSet;
import android.view.MotionEvent;
import uk.co.senab.photoview.PhotoView;

public class DrawPhotoView extends PhotoView {

	Paint mPaint;// 画笔
	Path path;// 路径
	Bitmap cacheBitmap;// 缓存位图
	Canvas cacheCanvas;// 缓存画布

	private int clr_bg, clr_fg;

	public DrawPhotoView(Context context, AttributeSet attr) {
		super(context, attr);

		clr_fg = Color.CYAN;
		// 初始化画笔
		mPaint = new Paint();
		// Dither（图像的抖动处理，当每个颜色值以低于8位表示时，对应图像做抖动处理可以实现在可显示颜色总数比较低（比如256色）时还保持较好的显示效果：
		mPaint.setDither(true);
		mPaint.setAntiAlias(true);// 抗锯齿
		mPaint.setStyle(Paint.Style.STROKE);// 画轮廓
		mPaint.setStrokeWidth(3);// 线条宽度
		mPaint.setColor(clr_fg);

		path = new Path();
		// 创建位图、画布，绘底色
		cacheBitmap = Bitmap.createBitmap(480, 800, Config.ARGB_8888);
		cacheCanvas = new Canvas(cacheBitmap);
	}

	public DrawPhotoView(Context context) {
		super(context);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		// 因为手指抬起后会重置path,所以需要先绘制上次保存的状态
		canvas.drawBitmap(cacheBitmap, 0, 0, null);
		// 绘制路径
		canvas.drawPath(path, mPaint);
	}

	private float curX, curY;
	public boolean isMoving;
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		float x = event.getX();
		float y = event.getY();

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			curX = x;
			curY = y;
			path.moveTo(x, y);
			isMoving = true;
			break;
		case MotionEvent.ACTION_MOVE:
			// if (!isMoving) {
			// break;
			// }
			path.quadTo(curX, curY, x, y);
			// 效果一样的方法
			// path.lineTo(x, y);
			curX = x;
			curY = y;
			
			break;
		case MotionEvent.ACTION_UP:
			//保存最后的状态
			cacheCanvas.drawPath(path, mPaint);
			//重置画笔
			path.reset();
			isMoving=false;
			break;
		default:
			break;
		}
		invalidate();
		return true;
	}
}
