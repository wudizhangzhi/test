package com.example.jiesi;

import android.support.v7.app.ActionBarActivity;
import android.text.style.UpdateAppearance;
import android.util.Log;

import java.io.File;
import java.lang.reflect.Method;
import java.util.Timer;
import java.util.TimerTask;

import com.example.jiesi.DialogSet.OnPositiveClickListener;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;
import cn.jpush.android.api.JPushInterface;

public class MainActivity extends ActionBarActivity {
	// 网页加载超时时间
	private final int TIMEOUT = 8000;

	WebView webView;
	ProgressBar progressbar;
	SharedPreferences sp;
	// 测试用
	private static String URL = "www.baidu.com";
	// private static String URL = "rg.ga.cheersdata.com";
	View dialog;
	AutoCompleteTextView edt_ip;

	Timer timer;// 加载超时检查

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// 获取数据
		sp = this.getSharedPreferences("URL", MODE_PRIVATE);
		URL = sp.getString("url_path", "www.baidu.com");

		// 初始化推送
		// JPushInterface.setDebugMode(true);
		// JPushInterface.init(this);
		// getSupportActionBar().setDisplayShowCustomEnabled(true);
		// getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME |
		// ActionBar.DISPLAY_SHOW_TITLE);
		// getSupportActionBar().setDisplayShowHomeEnabled(true);

		progressbar = (ProgressBar) findViewById(R.id.progressBar);
		webView = (WebView) findViewById(R.id.webview);

		WebSettings webSettings = webView.getSettings();
		webSettings.setUseWideViewPort(true);// 设置此属性，调整至适合大小
		// 设置支持缩放
		webSettings.setSupportZoom(true);
		webSettings.setBuiltInZoomControls(true);
		webSettings.setLoadWithOverviewMode(true);
		webSettings.setJavaScriptEnabled(true);// 支持js脚本

		// 判断页面加载进度
		webView.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				if (newProgress == 100) {
					progressbar.setVisibility(View.GONE);
				} else {
					progressbar.setVisibility(View.VISIBLE);
				}
			}

		});

		webView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// 返回值是true的时候控制WebView去打开，为false调用系统浏览器或第三方浏览器
				view.loadUrl(url);
				return true;
			}

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				if (!isNetworkAvailable()) {
					webView.stopLoading();
					Toast.makeText(MainActivity.this, "手机未连接网络", Toast.LENGTH_LONG).show();
					super.onPageStarted(view, url, favicon);
				}
				timer = new Timer();
				TimerTask task = new TimerTask() {

					@Override
					public void run() {
						Message msg = handler.obtainMessage();
						msg.what = 1;
						handler.sendMessage(msg);
						timer.cancel();
						timer.purge();
					}
				};
				super.onPageStarted(view, url, favicon);
				timer.schedule(task, TIMEOUT, 1);
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				timer.cancel();
				timer.purge();
				super.onPageFinished(view, url);
			}
			
			@Override
			public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
				if (isNetworkAvailable()) {
					Message msg = handler.obtainMessage();
					msg.what = 2;
					msg.obj=description;
					handler.sendMessage(msg);
					timer.cancel();
					timer.purge();
				}
				super.onReceivedError(view, errorCode, description, failingUrl);
			}
		});

		// webView.loadUrl(URL);
		LoadUrl(webView, URL);

	}

	/**
	 * 处理webview的加载
	 */
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 1) {
				Toast.makeText(MainActivity.this, "网络连接超时，请检查手机网络", Toast.LENGTH_SHORT).show();
				webView.stopLoading();
			}else if (msg.what==2) {
				Toast.makeText(MainActivity.this, "请输入正确的地址:"+msg.obj, Toast.LENGTH_SHORT).show();
				webView.stopLoading();
				//加载自定义页面
//				webView.loadUrl("file:///android_asset/404.html");
			}
		}
	};

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
			webView.goBack();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		setIconEnable(menu, true);

		MenuInflater inflater = new MenuInflater(this);
		inflater.inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_settings:
			Toast.makeText(this, "设置", Toast.LENGTH_SHORT).show();
			final DialogSet dialogset = new DialogSet(this, strToStringSet(sp.getString("history", "")));
			dialogset.setOnPositiveClickListener(new OnPositiveClickListener() {

				@Override
				public void onClick(String ip) {
					// 保存功能
					// 保存到默认地址
					URL = ip;
					Editor editor = sp.edit();
					editor.putString("url_path", URL);
					editor.commit();
					// webView.loadUrl(URL);
					LoadUrl(webView, URL);
					// 保存更新到历史地址
					// 判断历史数量，如果达到3个则替换一个
					updateHistory(sp.getString("history", ""), URL);
					Log.i("url", URL);
					dialogset.dismiss();
				}
			});
			dialogset.show();
			break;
		case R.id.action_refresh:
			webView.reload();
			Toast.makeText(this, "刷新", Toast.LENGTH_SHORT).show();
			break;
		case R.id.action_clear:// 清空缓存
			CookieSyncManager.createInstance(this);
			CookieManager cookieManager = CookieManager.getInstance();
			cookieManager.setAcceptCookie(true);
			cookieManager.removeAllCookie();
			// 移除
			// clearCacheFolder(this.getCacheDir(),
			// System.currentTimeMillis());
			webView.clearCache(true);
			webView.reload();
			// webView.loadDataWithBaseURL(null, "","text/html", "utf-8",URL);
			// 清除url历史
			Editor editor = sp.edit();
			editor.clear();
			editor.commit();
			Toast.makeText(this, "清除缓存", Toast.LENGTH_SHORT).show();
			break;
		case R.id.action_out:// 退出程序
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("退出");
			builder.setMessage("您确认要退出吗？");
			builder.setNegativeButton("取消", null);
			builder.setCancelable(true);
			builder.setPositiveButton("确认", new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					System.exit(0);
				}
			});
			builder.create().show();
			break;
		default:
			break;
		}
		return true;
	}

	/**
	 * 清除缓存
	 * 
	 * @param dir
	 * @param numDays
	 * @return
	 */
	private int clearCacheFolder(File dir, long numDays) {
		int deletedFiles = 0;
		if (dir != null && dir.isDirectory()) {
			try {
				for (File child : dir.listFiles()) {
					if (child.isDirectory()) {
						deletedFiles += clearCacheFolder(child, numDays);
					}
					if (child.lastModified() < numDays) {
						if (child.delete()) {
							deletedFiles++;
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return deletedFiles;
	}

	// enable为true时，菜单添加图标有效，enable为false时无效。4.0系统默认无效
	private void setIconEnable(Menu menu, boolean enable) {
		try {
			Class<?> clazz = Class.forName("com.android.internal.view.menu.MenuBuilder");
			Method m = clazz.getDeclaredMethod("setOptionalIconsVisible", boolean.class);
			m.setAccessible(true);

			// MenuBuilder实现Menu接口，创建菜单时，传进来的menu其实就是MenuBuilder对象(java的多态特征)
			m.invoke(menu, enable);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 更新历史地址到内存
	 * 
	 * @param urls
	 * @param input
	 */
	private void updateHistory(String urls, String input) {
		if (urls.contains(input)) {// 如果历史中已经含有了
			return;
		}
		String[] history = urls.split(",");
		String[] outHistory = new String[3];
		if (history.length >= 2) {
			outHistory[2] = history[1];
		}
		if (history.length >= 1) {
			outHistory[1] = history[0];
		}
		outHistory[0] = input;
		String out = outHistory[0] + "," + outHistory[1] + "," + outHistory[2];
		Editor editor = sp.edit();
		editor.putString("history", out);
		editor.commit();
	}

	/**
	 * 转化地址并加载
	 * 
	 * @param web
	 * @param url
	 */
	private void LoadUrl(WebView web, String url) {
		// web.loadUrl("http:\\\\" + url + "\\");
		web.loadUrl("http:\\\\" + url + "\\");
	}

	/**
	 * 将字符串转化为集合
	 * 
	 * @param s
	 * @return
	 */
	public String[] strToStringSet(String s) {
		return s.replace("null", "").split(",");
	}
	
	/**
	 * 判断网络连接
	 * @return
	 */
	private boolean isNetworkAvailable(){
		ConnectivityManager cm=(ConnectivityManager) MainActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info=cm.getActiveNetworkInfo();
		if (info!=null) {
			return true;
		}
		return false;
	}
}
