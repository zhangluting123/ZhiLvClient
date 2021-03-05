package cn.edu.hebtu.software.zhilvdemo.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import cn.edu.hebtu.software.zhilvdemo.Data.User;
import cn.edu.hebtu.software.zhilvdemo.R;
import cn.edu.hebtu.software.zhilvdemo.Setting.MyApplication;
import cn.edu.hebtu.software.zhilvdemo.Util.DateUtil;
import cn.edu.hebtu.software.zhilvdemo.Util.DetermineConnServer;
import cn.edu.hebtu.software.zhilvdemo.Util.JudgeStrUtil;
import cn.edu.hebtu.software.zhilvdemo.Util.SharedUtil;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.method.TransformationMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class LoginActivity extends AppCompatActivity {
    private EditText inputPhoneOrEmail;
    private EditText inputPassword;
    private ImageView ivEye;
    private Button btnLoginIn;
    private TextView fgpwd;
    private TextView signUp;

    private boolean isPhone;
    private boolean isEmail;
    private boolean isHideFirst = true;//输入框密码是否是隐藏的，默认为true

    private MyApplication data;
    @SuppressLint("HandlerLeak")
    private final Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case 1001:
                    Toast.makeText(LoginActivity.this, (CharSequence) msg.obj, Toast.LENGTH_LONG).show();
                    break;
                case 1002:
                    data.setUser((User)msg.obj);
                    //将登陆信息保存在本地
                    setUserMsgLocal(data.getUser());
                    finish();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getViews();
        registListener();
        data = (MyApplication)getApplication();
    }

    private void getViews(){
        inputPhoneOrEmail = findViewById(R.id.edt_input_phoneOrEmail);
        inputPassword = findViewById(R.id.edt_input_password);
        ivEye = findViewById(R.id.iv_eye);
        btnLoginIn = findViewById(R.id.btn_login_in);
        fgpwd = findViewById(R.id.tv_fgpwd);
        signUp = findViewById(R.id.tv_sign_up);
    }

    private void registListener(){
        CustomOnClickListener listener = new CustomOnClickListener();
        ivEye.setOnClickListener(listener);
        btnLoginIn.setOnClickListener(listener);
        fgpwd.setOnClickListener(listener);
        signUp.setOnClickListener(listener);
    }

    class CustomOnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            Intent intent = null;
            switch (v.getId()){
                case R.id.iv_eye:
                    if (isHideFirst == true) {
                        ivEye.setImageResource(R.mipmap.yanjing);
                        //明文
                        HideReturnsTransformationMethod method1 = HideReturnsTransformationMethod.getInstance();
                        inputPassword.setTransformationMethod(method1);
                        isHideFirst = false;
                    } else {
                        ivEye.setImageResource(R.mipmap.biyanjing);
                        //密文
                        TransformationMethod method = PasswordTransformationMethod.getInstance();
                        inputPassword.setTransformationMethod(method);
                        isHideFirst = true;
                    }
                    break;
                case R.id.btn_login_in:
                    isPhone = JudgeStrUtil.isPhone(inputPhoneOrEmail.getText().toString().trim());
                    isEmail = JudgeStrUtil.isEmail(inputPhoneOrEmail.getText().toString().trim());
                    String pwd = inputPassword.getText().toString().trim();
                    if(!isPhone && !isEmail ){
                        Toast.makeText(getApplicationContext(), "账号格式有误", Toast.LENGTH_SHORT).show();
                    }else {
                        login(pwd);
                    }
                    break;
                case R.id.tv_fgpwd:
                    intent = new Intent(LoginActivity.this, ForgetPwdActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                case R.id.tv_sign_up:
                    intent = new Intent(LoginActivity.this, RegistActivity.class);
                    startActivity(intent);
                    finish();
                    break;
            }
        }
    }

    private void setUserMsgLocal(User user){
        SharedUtil.putInt("userMsg",this,"userId",user.getUserId());
        SharedUtil.putString("userMsg",this,"phone",user.getPhone());
        SharedUtil.putString("userMsg",this,"email",user.getEmail());
        SharedUtil.putString("userMsg",this,"password",user.getPassword());
        SharedUtil.putString("userMsg",this,"userHead",user.getUserHead());
        SharedUtil.putString("userMsg",this,"userName",user.getUserName());
        SharedUtil.putString("userMsg",this,"sex",user.getSex());
        SharedUtil.putString("userMsg",this,"birth", DateUtil.getDateStr(user.getBirth()));
        SharedUtil.putString("userMsg",this,"signature",user.getSignature());
    }

    private void login(String pwd) {
        new Thread(){
            public void run(){
                try {
                    Message msg = Message.obtain();
                    String urlstr = null;
                    if(isPhone && !isEmail){
                        urlstr = "http://" + data.getIp() + ":8080/ZhiLvProject/user/login?phone=" + inputPhoneOrEmail.getText().toString() + "&password=" + pwd;
                    }else if(!isPhone && isEmail){
                        urlstr = "http://" + data.getIp() + ":8080/ZhiLvProject/user/login?email=" + inputPhoneOrEmail.getText().toString() + "&password=" + pwd;
                    }
                    if(DetermineConnServer.isConnByHttp(LoginActivity.this)){
                        User threadUser = new User();
                        URL url = new URL(urlstr);
                        URLConnection conn = url.openConnection();
                        InputStream in = conn.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(in, "utf-8"));
                        String str = reader.readLine();
                        if("NoUser".equals(str)){
                            msg.what = 1001;
                            msg.obj = "用户名或密码错误";
                        }else {
                            Gson gson = new Gson();
                            Log.e("user=", str);
                            threadUser = gson.fromJson(str, User.class);
                            msg.what = 1002;
                            msg.obj = threadUser;
                        }
                    }else {
                        msg.what = 1001;
                        msg.obj = "未连接到服务器";
                    }
                    handler.sendMessage(msg);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK ){
            finish();
        }
        return true;
    }
}