package c_fence_customer.anipr.com.myapplication;



import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class MainActivity extends ActionBarActivity {
    EditText mobileNo,otp,otp1;
    CheckBox check ;
    Button signIn,forgotPwd;
    private ProgressDialog pDialog;

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
                if((mobileNo.getText().toString().length()!=0)&&(otp.getText().toString().length()!=0)){
                    if(otp1.getText().toString().equals(otp.getText().toString())){
                        if(check.isChecked()){
                            pDialog = new ProgressDialog(MainActivity.this);
                            pDialog.setMessage("Please wait...");
                            pDialog.setCancelable(false);
                            pDialog.show();
                            ParseUser.logInInBackground(mobileNo.getText().toString(),otp.getText().toString(),new LogInCallback() {
                                @Override
                                public void done(ParseUser parseUser, ParseException e) {
                                    if(pDialog.isShowing()){
                                        pDialog.hide();
                                    }
                                    if(e==null){
                                        Intent i = new Intent(MainActivity.this,Home.class);

                                        startActivity(i);
                                    }else{
                                        AlertDialog.Builder bulider = new AlertDialog.Builder(
                                                MainActivity.this);
                                        bulider.setMessage(
                                                "Invalid Credentials")
                                                .setTitle("POS Response")
                                                .setPositiveButton("ok", null);
                                        AlertDialog dialog = bulider.create();
                                        dialog.show();
                                    }
                                }
                            });

                        }else{
                            Toast.makeText(getApplicationContext(), "Please tick the checkbox.", Toast.LENGTH_SHORT).show();

                        }


                    }else {
                        Toast.makeText(getApplicationContext(), "OTP doesn't match.", Toast.LENGTH_SHORT).show();
                    }

                }else{
                    Toast.makeText(getApplicationContext(), "Fill Up all the Fields", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

}
