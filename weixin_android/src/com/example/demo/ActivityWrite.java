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
	// ��д����
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
		case R.id.btn_clear:// ���
			drawView.clear();
			break;
		case R.id.btn_save:// ����
			SaveTask task = new SaveTask();
			task.execute();
			break;
		case R.id.btn_choose:// ѡȡͼƬ
			Intent intent = new Intent(Intent.ACTION_PICK, Media.EXTERNAL_CONTENT_URI);
			startActivityForResult(intent, 0);
			break;
		default:
			break;
		}
	}

	/**
	 * ������첽�߳�
	 * 
	 * @author Administrator
	 *
	 */
	class SaveTask extends AsyncTask<Void, Void, String> {

		@Override
		protected String doInBackground(Void... params) {
			String post = "";
			// �ж�sd���Ƿ���ڿ���
			String state = Environment.getExternalStorageState();
			if (!state.equals(Environment.MEDIA_MOUNTED)) {
				return "sd��δ׼����";
			}
			// ����ʱ�������ļ���
			Calendar calendar = Calendar.getInstance();

			Calendar c = Calendar.getInstance();
			String name = "" + c.get(Calendar.YEAR) + c.get(Calendar.MONTH) + c.get(Calendar.DAY_OF_MONTH)
					+ c.get(Calendar.HOUR_OF_DAY) + c.get(Calendar.MINUTE) + c.get(Calendar.SECOND) + ".png";
			Log.i("�ļ���", name);
			// �����ļ�
			File file = new File(Environment.getExternalStorageDirectory(), name);
			try {
//				drawView.saveToFile(file.getAbsolutePath());
//				photoView.saveToFile(file.getAbsolutePath());
			} catch (Exception e) {
				e.printStackTrace();
				return "����ʧ��";
			}
			return "����ɹ���" + name;
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
			// ����bitmapfactory��option
			BitmapFactory.Options option = new Options();
			// ���view�ĳߴ�
			int width_iv = photoView.getWidth();
			int height_iv = photoView.getHeight();
			// ���û�ȡλͼ�ĳߴ�
			option.inJustDecodeBounds = true;
			Bitmap bitmap = null;
			try {
				bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri), null, option);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			// ��ȡ���ű���
			int wRatio = (int) Math.ceil(option.outHeight / ((float) width_iv));
			int hRatio = (int) Math.ceil(option.outWidth / (float) height_iv);
			if (wRatio > 1 || hRatio > 1) {
				if (wRatio > hRatio) {
					option.inSampleSize = wRatio;
				} else {
					option.inSampleSize = hRatio;
				}
			}

			// ����ͼ����������Ľ���
			option.inJustDecodeBounds = false;
			try {
				bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri), null, option);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			// ����λͼ��bitmap
			Bitmap alteredBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());
			// ���û���
			paint = new Paint();
			paint.setStrokeWidth(3);
			paint.setColor(Color.BLUE);
			// ����λͼ
			canvas = new Canvas(alteredBitmap);
			Matrix matrix = new Matrix();
			canvas.drawBitmap(bitmap, matrix, paint);
			// ��ͼ������bitmap
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
