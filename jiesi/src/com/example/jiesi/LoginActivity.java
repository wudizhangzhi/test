package com.example.jiesi;

import java.security.MessageDigest;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends Activity implements OnClickListener {
	Button btn_login;
	EditText edt_username,edt_password;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		initView();
	}

	private void initView() {
		btn_login=(Button) findViewById(R.id.btn_login);
		edt_username=(EditText) findViewById(R.id.edt_username);
		edt_password=(EditText) findViewById(R.id.edt_password);
		btn_login.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO ´«µÝÊý¾Ý
		
	}
}
