package cn.edu.hebtu.software.zhilvdemo.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import cn.edu.hebtu.software.zhilvdemo.Data.User;
import cn.edu.hebtu.software.zhilvdemo.R;
import cn.edu.hebtu.software.zhilvdemo.Setting.MyApplication;
import cn.edu.hebtu.software.zhilvdemo.UploadAndDownload.UploadUserMsg;
import cn.edu.hebtu.software.zhilvdemo.Util.DetermineConnServer;
import cn.edu.hebtu.software.zhilvdemo.Util.JudgeStrUtil;
import cn.edu.hebtu.software.zhilvdemo.Util.RandomNumber;
import cn.edu.hebtu.software.zhilvdemo.Util.SendEmail;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class ChangeEmailActivity extends AppCompatActivity {
    private EditText inputEmail;
    private EditText inputCode;
    private Button btnCode;
    private Button btnSubmit;
    private MyApplication data;
    private long verificationCode;//邮箱验证码
    @SuppressLint("HandlerLeak")
    private final Handler mHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case 1001:
                    Toast.makeText(ChangeEmailActivity.this, (CharSequence) msg.obj, Toast.LENGTH_LONG).show();
                    break;
                case 1002:
                    User user = new User();
                    user.setUserId(data.getUser().getUserId());
                    user.setEmail(inputEmail.getText().toString());
                    UploadUserMsg uploadUserMsg = new UploadUserMsg(getApplicationContext(), user);
                    uploadUserMsg.execute("http://"+ data.getIp() +":8080/ZhiLvProject/user/changeMsg");
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_email);
        data = (MyApplication)getApplication();
        getViews();
        if(null != data.getUser() && null != data.getUser().getEmail()){
            inputEmail.setText(data.getUser().getEmail());
        }
        btnCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(JudgeStrUtil.isEmail(inputEmail.getText().toString().trim())){
                    getEmailCode();
                }else{
                    Toast.makeText(ChangeEmailActivity.this, "邮箱输入有误",Toast.LENGTH_SHORT).show();
                    inputEmail.requestFocus();
                }
            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitEmail();
            }
        });
    }

    private void getViews(){
        inputEmail = findViewById(R.id.edt_input_email);
        inputCode = findViewById(R.id.edt_input_code);
        btnCode = findViewById(R.id.btn_getCode);
        btnSubmit = findViewById(R.id.btn_submit);
    }

    private void submitEmail() {
        if(Integer.parseInt(inputCode.getText().toString()) == verificationCode){ //验证码和输入一致
            sendToServer();
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
                    SendEmail se = new SendEmail(inputEmail.getText().toString().trim());
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

    private void sendToServer() {
        new Thread() {
            @Override
            public void run() {
                try {
                    Message message = Message.obtain();
                    String str = "http://" + data.getIp() + ":8080/ZhiLvProject/user/login?email=" + inputEmail.getText().toString() + "&password=" ;
                    if(DetermineConnServer.isConnByHttp(getApplicationContext())) {
                        URL url = new URL(str);
                        URLConnection conn = url.openConnection();
                        InputStream in = conn.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(in, "utf-8"));
                        String info = reader.readLine();
                        if ("NoUser".equals(info)) {
                            message.what = 1002;
                        }else{
                            message.what = 1001;
                            message.obj = "该邮箱已被注册";
                        }
                    }else{
                        message.what = 1001;
                        message.obj = "未连接到服务器";
                    }
                    mHandler.sendMessage(message);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}