package com.example.demo;

import android.util.Log;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.demo.view.DrawView;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXImageObject;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXMusicObject;
import com.tencent.mm.sdk.modelmsg.WXTextObject;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import Decoder.BASE64Encoder;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import net.sf.json.JSON;
import uk.co.senab.photoview.PhotoView;

public class MainActivity extends Activity implements OnClickListener {
	Button btn_sendToWx, btn_launch, btn_register, btn_sendimage, btn_sendmusic, btn_sendpage, btn_token, btn_sendall,
			btn_sendimagemsg;

	private static final String APP_ID = "wx9fa2a68da1eca278";
	private IWXAPI api;
	// 测试用公共账号
	private static final String WX_APP_ID = "wxeebd32309ff9a187";
	private static final String WX_APP_SECRET = "21c1a896b231f114738f7b04b95c1644";
	private String Token;
	// 我的公共账号
//	 private static final String WX_APP_ID = "wxbacc90fe16af9648";
//	 private static final String WX_APP_SECRET ="a732faee5044729a316c4505e2c52b80";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// 获取wx实例
		api = WXAPIFactory.createWXAPI(this, APP_ID, false);

		initView();
	}

	private void initView() {
		btn_sendToWx = (Button) findViewById(R.id.btn_sendToWx);
		btn_launch = (Button) findViewById(R.id.btn_launch);
		btn_register = (Button) findViewById(R.id.btn_register);
		btn_sendimage = (Button) findViewById(R.id.btn_sendimage);
		btn_sendmusic = (Button) findViewById(R.id.btn_sendmusic);
		btn_sendpage = (Button) findViewById(R.id.btn_sendpage);
		btn_token = (Button) findViewById(R.id.btn_token);
		btn_sendall = (Button) findViewById(R.id.btn_sendall);
		btn_sendimagemsg = (Button) findViewById(R.id.btn_sendimagemsg);

		btn_sendToWx.setOnClickListener(this);
		btn_launch.setOnClickListener(this);
		btn_register.setOnClickListener(this);
		btn_sendimage.setOnClickListener(this);
		btn_sendmusic.setOnClickListener(this);
		btn_sendpage.setOnClickListener(this);
		btn_token.setOnClickListener(this);
		btn_sendall.setOnClickListener(this);
		btn_sendimagemsg.setOnClickListener(this);
	}

	/**
	 * 转化文件为base64字符串
	 * 
	 * @param path
	 * @return
	 * @throws Exception
	 */
	public static String encodeBase64File(File file) throws Exception {
		FileInputStream inputFile = new FileInputStream(file);
		byte[] buffer = new byte[(int) file.length()];
		inputFile.read(buffer);
		inputFile.close();
		return new BASE64Encoder().encode(buffer);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_register:
			Toast.makeText(this, "注册结果：" + api.registerApp(APP_ID), Toast.LENGTH_SHORT).show();
			break;
		case R.id.btn_sendToWx:// 发送文本
			// 初始化一个WXTextObject对象
			WXTextObject textObj = new WXTextObject();
			textObj.text = "测试文本";

			// 用WXTextObject对象初始化一个WXMediaMessage对象
			WXMediaMessage msg = new WXMediaMessage();
			msg.mediaObject = textObj;
			// 发送文本类型的消息时，title字段不起作用
			// msg.title = "Will be ignored";
			msg.description = "测试文本";

			// 构造一个Req
			SendMessageToWX.Req req = new SendMessageToWX.Req();
			req.transaction = buildTransaction("text"); // transaction字段用于唯一标识一个请求
			req.message = msg;
			// SendMessageToWX.Req.WXSceneTimeline;分享到好友圈
			req.scene = SendMessageToWX.Req.WXSceneSession;// 发送给好友

			// 调用api接口发送数据到微信
			api.sendReq(req);
			break;
		case R.id.btn_launch:// 登录微信
			api.openWXApp();
			break;
		case R.id.btn_sendimage:// 发送图片
			Toast.makeText(MainActivity.this, "发送图片", Toast.LENGTH_SHORT).show();
			Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
			WXImageObject imageobj = new WXImageObject(bitmap);
			Bitmap thumbBitmap = Bitmap.createScaledBitmap(bitmap, 150, 150, true);

			WXMediaMessage imageMsg = new WXMediaMessage();
			imageMsg.mediaObject = imageobj;
			imageMsg.thumbData = BmpToByteArray(thumbBitmap, true);

			SendMessageToWX.Req reqImage = new SendMessageToWX.Req();
			reqImage.message = imageMsg;
			reqImage.transaction = buildTransaction("img");
			reqImage.scene = SendMessageToWX.Req.WXSceneSession;// 发送给好友

			api.sendReq(reqImage);
			break;
		case R.id.btn_sendmusic:// 发送音乐
			WXMusicObject musicObj = new WXMusicObject();
			musicObj.musicUrl = "http://staff2.ustc.edu.cn/~wdw/softdown/index.asp/0042515_05.ANDY.mp3";

			WXMediaMessage musicMsg = new WXMediaMessage();
			musicMsg.mediaObject = musicObj;
			musicMsg.title = "揉揉是傻叉！揉揉是傻叉！揉揉是傻叉！";
			musicMsg.description = "揉揉绝壁是傻叉！揉揉绝壁是傻叉！揉揉绝壁是傻叉！";
			musicMsg.thumbData = BmpToByteArray(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher),
					true);

			SendMessageToWX.Req musicReq = new SendMessageToWX.Req();
			musicReq.message = musicMsg;
			musicReq.transaction = buildTransaction("music");
			musicReq.scene = SendMessageToWX.Req.WXSceneSession;

			api.sendReq(musicReq);
			break;
		case R.id.btn_sendpage:// 发送网页
			WXWebpageObject webObj = new WXWebpageObject();
			webObj.webpageUrl = "http://user.qzone.qq.com/1079229086?ADUIN=554330595&ADSESSION=1442364949&ADTAG=CLIENT.QQ.5419_FriendInfo_PersonalInfo.0&ADPUBNO=26494&ptlang=2052";

			WXMediaMessage webMsg = new WXMediaMessage(webObj);
			webMsg.title = "揉揉是逗比.com";
			webMsg.description = "welcome to 揉揉是逗比.com";
			webMsg.thumbData = BmpToByteArray(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher),
					true);

			SendMessageToWX.Req reqWeb = new SendMessageToWX.Req();
			reqWeb.message = webMsg;
			reqWeb.transaction = buildTransaction("webpage");
			reqWeb.scene = SendMessageToWX.Req.WXSceneSession;

			api.sendReq(reqWeb);
			break;
		case R.id.btn_token:
			Thread r = new WXThread();
			r.start();
			break;
		case R.id.btn_sendall:
			Thread r2 = new WXsendTextThread();
			r2.start();
			break;
		case R.id.btn_sendimagemsg:
			WXsendImageMsg r3 = new WXsendImageMsg();
			r3.start();
			break;
		default:
			break;
		}
	}

	private byte[] BmpToByteArray(Bitmap thumbBitmap, boolean NeedRecycle) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		thumbBitmap.compress(CompressFormat.PNG, 100, baos);
		byte[] result = baos.toByteArray();
		if (NeedRecycle) {
			thumbBitmap.recycle();
		}
		try {
			baos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	private String buildTransaction(final String type) {
		return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
	}

	/**
	 * 获取凭证
	 * 
	 * @author Administrator
	 *
	 */
	class WXThread extends Thread {
		@Override
		public void run() {
			super.run();
			HttpClient httpclient = new DefaultHttpClient();
			String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + WX_APP_ID
					+ "&secret=" + WX_APP_SECRET;
			HttpGet httpGet = new HttpGet(url);

			try {
				HttpResponse response = httpclient.execute(httpGet);
				if (response.getStatusLine().getStatusCode() == 200) {
					HttpEntity entity = response.getEntity();
					InputStream is = entity.getContent();
					BufferedReader reader = new BufferedReader(new InputStreamReader(is));
					JSONObject json = new JSONObject(reader.readLine());
					Token = json.getString("access_token");
					Log.i("token", Token);
				}
			} catch (ClientProtocolException e2) {
				e2.printStackTrace();
			} catch (IOException e2) {
				e2.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	class WXsendTextThread extends Thread {
		@Override
		public void run() {
			super.run();
			try {
				HttpClient httpclient = new DefaultHttpClient();
				String url = "https://api.weixin.qq.com/cgi-bin/message/mass/sendall?access_token=" + Token;
				HttpPost httpPost = new HttpPost(url);
				// HttpParams params=new BasicHttpParams();
				// params.setParameter("encoding", "utf-8");
				// 消息
				JSONObject jsonObj = new JSONObject();

				JSONObject json1 = new JSONObject();
				json1.put("is_to_all", true);

				// 消息内容
				String content = "群发文本内容";
				JSONObject json2 = new JSONObject();
				json2.put("content", content);

				jsonObj.put("filter", json1);
				jsonObj.put("text", json2);
				jsonObj.put("msgtype", "text");

				// 需要设置编码为utf-8
				StringEntity jsonentity = new StringEntity(jsonObj.toString(), "utf-8");

				httpPost.setEntity(jsonentity);

				HttpResponse response = httpclient.execute(httpPost);
				if (response.getStatusLine().getStatusCode() == 200) {
					HttpEntity entity = response.getEntity();
					InputStream is = entity.getContent();
					BufferedReader reader = new BufferedReader(new InputStreamReader(is));
					JSONObject json = new JSONObject(reader.readLine());
					Log.i("返回", json.toString());
				}
			} catch (ClientProtocolException e2) {
				e2.printStackTrace();
			} catch (IOException e2) {
				e2.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	class WXsendImageMsg extends Thread {
		@Override
		public void run() {
			super.run();
			try {
				//上传图片
				String fileurl = "";
				HttpClient httpclient = new DefaultHttpClient();
				String url = "https://api.weixin.qq.com/cgi-bin/media/upload?access_token=" + Token+"&type=thumb";
				HttpPost httpPost = new HttpPost(url);
				// HttpParams params=new BasicHttpParams();
				// params.setParameter("encoding", "utf-8");
				File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "big.jpg");
				FileBody f1 = new FileBody(file);
				MultipartEntity reqEntity = new MultipartEntity();
				reqEntity.addPart("file", f1);
				reqEntity.addPart("filename", new StringBody("big.jpg"));
				
				httpPost.setEntity(reqEntity);

				HttpResponse response = httpclient.execute(httpPost);
				if (response.getStatusLine().getStatusCode() == 200) {
					HttpEntity entity = response.getEntity();
					InputStream is = entity.getContent();
					BufferedReader reader = new BufferedReader(new InputStreamReader(is));
					JSONObject json = new JSONObject(reader.readLine());
//					String[] s = json.toString().split("\\\\/");
//					Log.i("返回json",json.toString());
//					fileurl = s[s.length - 2];
//					Log.i("返回json", s[s.length - 2]);
					fileurl=json.getString("thumb_media_id");
				}
				
				Log.i("返回文件地址", fileurl);
				// 创建图文json
				String mediaUrl="";
				JSONObject j1 = new JSONObject();
				j1.put("thumb_media_id", fileurl);
				j1.put("title", "tuwen title");
				j1.put("content_source_url", "www.qq.com");
				j1.put("content", "tuwen content");
				j1.put("show_cover_pic", "1");
				
				JSONArray array = new JSONArray();
				array.put(j1);
				array.put(j1);

				JSONObject obj = new JSONObject();
				obj.put("articles", array);
				Log.i("发送的json", obj.toString());
				// 发送图文信息素材
				HttpPost httpPost2 = new HttpPost(
						"https://api.weixin.qq.com/cgi-bin/media/uploadnews?access_token=" + Token);
				httpPost2.setEntity(new StringEntity(obj.toString()));
				HttpResponse response2 = httpclient.execute(httpPost2);
				if (response2.getStatusLine().getStatusCode() == 200) {
					HttpEntity entity2 = response2.getEntity();
					InputStream is2 = entity2.getContent();
					BufferedReader reader2 = new BufferedReader(new InputStreamReader(is2));
					JSONObject json2 = new JSONObject(reader2.readLine());
					// 获取url
					Log.i("返回", json2.toString());
					mediaUrl=json2.getString("media_id");
				}
				
				//发送图文消息
				
				JSONObject json1_1=new JSONObject();
				json1_1.put("is_to_all", "true");
				
				JSONObject json2_1=new JSONObject();
				json2_1.put("media_id", mediaUrl);
				
				JSONObject jsonAll=new JSONObject();
				jsonAll.put("msgtype", "mpnews");
				jsonAll.put("filter", json1_1);
				jsonAll.put("mpnews", json2_1);
				Log.i("发送图文消息", jsonAll.toString());
				
				String sendUrl="https://api.weixin.qq.com/cgi-bin/message/mass/sendall?access_token="+Token;
				HttpPost httpPost3=new HttpPost(sendUrl);
				httpPost3.setEntity(new StringEntity(jsonAll.toString()));
				HttpResponse res=httpclient.execute(httpPost3);
				if (res.getStatusLine().getStatusCode()==200) {
					HttpEntity e=res.getEntity();
					InputStream is=e.getContent();
					BufferedReader r=new BufferedReader(new InputStreamReader(is));
					Log.i("群发图文消息后返回", r.readLine());
				}
			} catch (ClientProtocolException e2) {
				e2.printStackTrace();
			} catch (IOException e2) {
				e2.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			}

			
		}
	}
}
