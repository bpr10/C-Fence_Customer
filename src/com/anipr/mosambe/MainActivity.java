package com.anipr.mosambe;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {
	EditText mobileNo,otp,otp1;
	CheckBox check ; 
	Button signIn,forgotPwd;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mobileNo = (EditText)findViewById(R.id.phone_no);
		otp = (EditText)findViewById(R.id.otp);
		otp1 = (EditText)findViewById(R.id.otp1);
		check = (CheckBox)findViewById(R.id.checkBox1);
		signIn = (Button)findViewById(R.id.sign_in);
		forgotPwd = (Button)findViewById(R.id.forgot_pwd);
		Log.d("appcontrolder", "passed");
		signIn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if((mobileNo.getText().toString().equals("9876543210"))&&(otp.getText().toString().equals("1234"))){
					if(otp1.getText().toString().equals("1234")){
						if(check.isChecked()){
							Intent i = new Intent(MainActivity.this,Home.class);
							startActivity(i);
						}else{
							Toast.makeText(getApplicationContext(), "Please tick the checkbox.", 1000).show();

						}
						
						
					}else {
						Toast.makeText(getApplicationContext(), "OTP doesn't match.", 1000).show();
					}
					
				}else{
					Toast.makeText(getApplicationContext(), "Invalid Credentials", 1000).show();
				}
			}
		});
		
		
	}
	
}
