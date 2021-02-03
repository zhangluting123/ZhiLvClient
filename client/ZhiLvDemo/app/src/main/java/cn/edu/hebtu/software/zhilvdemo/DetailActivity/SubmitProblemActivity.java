package cn.edu.hebtu.software.zhilvdemo.DetailActivity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import cn.edu.hebtu.software.zhilvdemo.R;
import cn.edu.hebtu.software.zhilvdemo.Setting.MyApplication;
import cn.edu.hebtu.software.zhilvdemo.Util.DetermineConnServer;

import android.annotation.SuppressLint;
import android.os.Build;
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
import java.net.URL;
import java.net.URLConnection;

public class SubmitProblemActivity extends AppCompatActivity {
    private EditText edtProblem;
    private MyApplication data;
    @SuppressLint("HandlerLeak")
    private final Handler mHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case 1001:
                    Toast.makeText(getApplicationContext(), (CharSequence)msg.obj, Toast.LENGTH_SHORT).show();
                    break;
                case 1002:
                    finish();
                    break;
            }
        }
    };
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_problem);
        data = (MyApplication)getApplication();

        edtProblem = findViewById(R.id.edt_problem);
        Toolbar toolbar = findViewById(R.id.problem_toolbar);
        Button btnSubmit = findViewById(R.id.btnSubmit);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addProblem();
            }
        });
    }

    private void addProblem(){
        new Thread(){
            @Override
            public void run() {
                if(DetermineConnServer.isConnByHttp(getApplicationContext())){
                    try {
                        URL url = new URL("http://"+data.getIp()+":8080/ZhiLvProject/problem/add?userId="+data.getUser().getUserId()+"&content="+edtProblem.getText().toString());
                        URLConnection conn = url.openConnection();
                        InputStream in = conn.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                        String str = reader.readLine();
                        Message msg = Message.obtain();
                        if("OK".equals(str)){
                            msg.what = 1002;
                        }else{
                            msg.what = 1001;
                            msg.obj = "发布失败";
                        }
                        mHandler.sendMessage(msg);
                        in.close();
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else{
                    Message message  = Message.obtain();
                    message.obj = "未连接到服务器";
                    message.what = 1001;
                    mHandler.sendMessage(message);
                }
            }
        }.start();
    }
}