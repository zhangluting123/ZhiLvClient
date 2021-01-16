package cn.edu.hebtu.software.zhilvdemo.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import cn.edu.hebtu.software.zhilvdemo.R;
import cn.edu.hebtu.software.zhilvdemo.Util.JudgeStrUtil;
import cn.edu.hebtu.software.zhilvdemo.Util.RandomNumber;
import cn.edu.hebtu.software.zhilvdemo.Util.SendEmail;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;


public class RegistActivity extends AppCompatActivity {
    private EditText inputPhoneOrEmail;
    private EditText inputCode;
    private Button btnGetCode;
    private EditText firstPwd;
    private EditText secondPwd;
    private Button btnRegist;

    private EventHandler handler;
    private boolean isPhone;
    private boolean isEmail;
    private long verificationCode;//邮箱验证码
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case 1001:
                    Toast.makeText(RegistActivity.this, (CharSequence) msg.obj, Toast.LENGTH_LONG).show();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);
        getViews();
        registListener();

        handler = new EventHandler(){
            @Override
            public void afterEvent(int event, int result, Object data) {
                if (result == SMSSDK.RESULT_COMPLETE){
                    //回调完成
                    if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                        //提交验证码成功
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // TODO 插入用户信息
//                                sendToServer();
                                Toast.makeText(RegistActivity.this, "验证码提交成功", Toast.LENGTH_SHORT).show();
                                inputCode.requestFocus();
                            }
                        });

                    }else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE){
                        //获取验证码成功
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(RegistActivity.this,"验证码已发送",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }else{
                    ((Throwable)data).printStackTrace();
                    Throwable throwable = (Throwable) data;
                    try {
                        JSONObject obj = new JSONObject(throwable.getMessage());
                        final String des = obj.optString("detail");
                        if (!TextUtils.isEmpty(des)){
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(RegistActivity.this,"提交错误信息", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        };

        SMSSDK.registerEventHandler(handler);
    }

    private void getViews(){
        inputPhoneOrEmail = findViewById(R.id.edt_input_phoneOrEmail);
        inputCode = findViewById(R.id.edt_input_code);
        btnGetCode = findViewById(R.id.btn_getCode);
        firstPwd = findViewById(R.id.edt_first_pwd);
        secondPwd = findViewById(R.id.edt_second_pwd);
        btnRegist = findViewById(R.id.btn_regist);
    }

    private void registListener(){
        CustomOnClickListener listener = new CustomOnClickListener();
        btnGetCode.setOnClickListener(listener);
        btnRegist.setOnClickListener(listener);
    }

    class CustomOnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn_getCode:
                    isPhone = JudgeStrUtil.isPhone(inputPhoneOrEmail.getText().toString().trim());
                    isEmail = JudgeStrUtil.isEmail(inputPhoneOrEmail.getText().toString().trim());
                    if (isPhone && !isEmail){
                        getPhoneCode();
                    }else if(!isPhone && isEmail){
                        getEmailCode();// 获取邮箱验证码
                    }else{
                        Toast.makeText(RegistActivity.this, "手机或邮箱账号有误",Toast.LENGTH_SHORT).show();
                        inputPhoneOrEmail.requestFocus();
                    }
                    break;
                case R.id.btn_regist:
                    if (firstPwd.getText().toString().equals(secondPwd.getText().toString()) ){
                        if(isPhone && !isEmail){
                            submitPhone();
                        }else if(!isPhone && isEmail){
                            submitEmail();//提交邮箱验证码
                        }
                    }else{
                        Toast.makeText(RegistActivity.this,"两次输入密码不一致！",Toast.LENGTH_SHORT).show();
                        secondPwd.requestFocus();
                    }
                    break;
            }
        }
    }


    private void submitEmail() {
        if(Integer.parseInt(inputCode.getText().toString()) == verificationCode){ //验证码和输入一致
            // TODO 插入用户信息
            Toast.makeText(this,"验证成功",Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "验证失败", Toast.LENGTH_SHORT).show();
        }
    }

    private void getEmailCode() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    RandomNumber rn = new RandomNumber();
                    verificationCode = rn.getRandomNumber(6);
                    SendEmail se = new SendEmail(inputPhoneOrEmail.getText().toString().trim());
                    se.sendHtmlEmail(verificationCode);//发送html邮件
                    Message msg = new Message();
                    msg.what = 1001;
                    msg.obj = "邮箱验证码发送成功";
                    mHandler.sendMessage(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    //获取验证码
    public void getPhoneCode() {
        SMSSDK.getVerificationCode("86",inputPhoneOrEmail.getText().toString());
    }

    //后台验证
    public void submitPhone() {
        SMSSDK.submitVerificationCode("86",inputPhoneOrEmail.getText().toString(),inputCode.getText().toString());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterEventHandler(handler);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK ){
            finish();
        }
        return true;
    }

}