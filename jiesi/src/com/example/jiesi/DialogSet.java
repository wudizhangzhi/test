package com.example.jiesi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 设置IP地址对话框
 * @author Administrator
 *
 */
public class DialogSet extends Dialog implements android.view.View.OnClickListener, TextWatcher, OnFocusChangeListener {
	private Button btn_positive, btn_negative;
	// EditText edt_ip;
	AutoCompleteTextView edt_ip;
	TextView tv_tip;

	String IP = "";
	ArrayAdapter<String> adapter ;
	String[] data;

	public DialogSet(Context context,String[] s) {
		super(context);
		this.data=s;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_set);

		this.setTitle("设置");
		btn_positive = (Button) findViewById(R.id.btn_positive);
		btn_negative = (Button) findViewById(R.id.btn_negative);
		edt_ip = (AutoCompleteTextView) findViewById(R.id.edt_ip);
		tv_tip = (TextView) findViewById(R.id.tv_tip);

		btn_positive.setOnClickListener(this);
		btn_negative.setOnClickListener(this);
		edt_ip.addTextChangedListener(this);

//		edt_ip.setCompletionHint("最近5条记录");
		adapter= new ArrayAdapter<String>(getContext(), R.layout.item_simpledropdown_1line,
				R.id.tv_dropdown, data);
		edt_ip.setAdapter(adapter);
		edt_ip.setThreshold(1);
		edt_ip.setOnFocusChangeListener(this);
	}

	// 确认按钮的接口
	OnPositiveClickListener positiveListener;

	interface OnPositiveClickListener {
		void onClick(String ip);
	}

	public void setOnPositiveClickListener(OnPositiveClickListener listener) {
		this.positiveListener = listener;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_positive:// 确定
//			IP = "http:\\\\" + edt_ip.getText().toString() + "\\";
			IP=stripString(edt_ip.getText().toString());
			if (edt_ip.getText().toString().equals("") || edt_ip.getText().toString() == null) {
				tv_tip.setText("IP地址不能为空！");
				tv_tip.setVisibility(View.VISIBLE);
				return;
			}
			if (positiveListener != null) {
				positiveListener.onClick(IP);
				Log.i("ip", IP);
			}
			break;
		case R.id.btn_negative:// 取消
			Toast.makeText(getContext(), "取消", Toast.LENGTH_SHORT).show();
			this.dismiss();
			break;
		default:
			break;
		}
	}

	@Override
	public void afterTextChanged(Editable s) {

	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		if (!s.equals("") && s != null) {
			tv_tip.setVisibility(View.GONE);
		}
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		if (hasFocus) {
			if (!data[0].equals("")&&data!=null) {
				edt_ip.showDropDown();
			}
		}
		Log.i("hasFocus", hasFocus+"");
	}
	
	/**
	 * 去除字符串的制表符
	 * @param s
	 * @return
	 */
	private String stripString(String s){
		return s.replaceAll("\\s*", "");
	}
}
