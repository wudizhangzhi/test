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

	Paint mPaint;// ����
	Path path;// ·��
	Bitmap cacheBitmap;// ����λͼ
	Canvas cacheCanvas;// ���滭��

	private int clr_bg, clr_fg;

	public DrawPhotoView(Context context, AttributeSet attr) {
		super(context, attr);

		clr_fg = Color.CYAN;
		// ��ʼ������
		mPaint = new Paint();
		// Dither��ͼ��Ķ���������ÿ����ɫֵ�Ե���8λ��ʾʱ����Ӧͼ���������������ʵ���ڿ���ʾ��ɫ�����Ƚϵͣ�����256ɫ��ʱ�����ֽϺõ���ʾЧ����
		mPaint.setDither(true);
		mPaint.setAntiAlias(true);// �����
		mPaint.setStyle(Paint.Style.STROKE);// ������
		mPaint.setStrokeWidth(3);// �������
		mPaint.setColor(clr_fg);

		path = new Path();
		// ����λͼ�����������ɫ
		cacheBitmap = Bitmap.createBitmap(480, 800, Config.ARGB_8888);
		cacheCanvas = new Canvas(cacheBitmap);
	}

	public DrawPhotoView(Context context) {
		super(context);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		// ��Ϊ��ָ̧��������path,������Ҫ�Ȼ����ϴα����״̬
		canvas.drawBitmap(cacheBitmap, 0, 0, null);
		// ����·��
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
			// Ч��һ���ķ���
			// path.lineTo(x, y);
			curX = x;
			curY = y;
			
			break;
		case MotionEvent.ACTION_UP:
			//��������״̬
			cacheCanvas.drawPath(path, mPaint);
			//���û���
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
