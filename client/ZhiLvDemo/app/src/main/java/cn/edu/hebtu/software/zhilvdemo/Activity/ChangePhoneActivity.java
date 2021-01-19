package cn.edu.hebtu.software.zhilvdemo.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import cn.edu.hebtu.software.zhilvdemo.Data.User;
import cn.edu.hebtu.software.zhilvdemo.R;
import cn.edu.hebtu.software.zhilvdemo.Setting.MyApplication;
import cn.edu.hebtu.software.zhilvdemo.UploadAndDownload.UploadUserMsg;
import cn.edu.hebtu.software.zhilvdemo.Util.DetermineConnServer;
import cn.edu.hebtu.software.zhilvdemo.Util.JudgeStrUtil;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class ChangePhoneActivity extends AppCompatActivity {
    private EditText inputPhone;
    private EditText inputCode;
    private Button btnCode;
    private Button btnSubmit;
    private MyApplication data;
    private EventHandler handler;
    @SuppressLint("HandlerLeak")
    private final Handler mHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case 1001:
                    Toast.makeText(ChangePhoneActivity.this, (CharSequence) msg.obj, Toast.LENGTH_SHORT).show();
                    break;
                case 1002:
                    User user = new User();
                    user.setUserId(data.getUser().getUserId());
                    user.setPhone(inputPhone.getText().toString());
                    UploadUserMsg uploadUserMsg = new UploadUserMsg(getApplicationContext(), user);
                    uploadUserMsg.execute("http://"+ data.getIp() +":8080/ZhiLvProject/user/changeMsg");
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_phone);
        data = (MyApplication)getApplication();
        getViews();
        if(null != data.getUser() && null != data.getUser().getPhone()){
            inputPhone.setText(data.getUser().getPhone());
        }
        btnCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(JudgeStrUtil.isPhone(inputPhone.getText().toString().trim())){
                    getPhoneCode();
                }else{
                    Toast.makeText(ChangePhoneActivity.this, "手机号输入有误",Toast.LENGTH_SHORT).show();
                    inputPhone.requestFocus();
                }
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitPhone();
            }
        });

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
                                sendToServer();
                            }
                        });

                    }else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE){
                        //获取验证码成功
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(ChangePhoneActivity.this,"验证码已发送",Toast.LENGTH_SHORT).show();
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
                                    Toast.makeText(ChangePhoneActivity.this,"提交错误信息", Toast.LENGTH_SHORT).show();
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
        inputPhone = findViewById(R.id.edt_input_phone);
        inputCode = findViewById(R.id.edt_input_code);
        btnCode = findViewById(R.id.btn_getCode);
        btnSubmit = findViewById(R.id.btn_submit);
    }

    //获取验证码
    public void getPhoneCode() {
        SMSSDK.getVerificationCode("86",inputPhone.getText().toString());
    }

    //后台验证
    public void submitPhone() {
        SMSSDK.submitVerificationCode("86",inputPhone.getText().toString(),inputCode.getText().toString());
    }


    private void sendToServer() {
        new Thread() {
            @Override
            public void run() {
                try {
                    Message message = Message.obtain();
                    String str = "http://" + data.getIp() + ":8080/ZhiLvProject/user/login?phone=" + inputPhone.getText().toString() + "&password=" ;
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
                            message.obj = "该手机号已被注册";
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