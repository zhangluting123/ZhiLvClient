package cn.edu.hebtu.software.zhilvdemo.DetailActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import cn.edu.hebtu.software.zhilvdemo.Adapter.AuditAdapter;
import cn.edu.hebtu.software.zhilvdemo.Data.AuditMessage;
import cn.edu.hebtu.software.zhilvdemo.R;
import cn.edu.hebtu.software.zhilvdemo.Setting.MyApplication;
import cn.edu.hebtu.software.zhilvdemo.Util.DetermineConnServer;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

public class AuditActivity extends AppCompatActivity {
    private List<AuditMessage> mDatas;
    private AuditAdapter adapter;
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
                    mDatas = (List<AuditMessage>)msg.obj;
                    adapter.refresh(mDatas);
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audit);
        data = (MyApplication) getApplication();
        initView();
        if(null != data.getUser()){
            getAuditData();
        }
    }

    private void initView(){
        Toolbar toolbar = findViewById(R.id.audit_toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(manager);
        adapter = new AuditAdapter(mDatas, this);
        recyclerView.setAdapter(adapter);
    }

    private void getAuditData(){
        new Thread(){
            @Override
            public void run() {
                HttpURLConnection conn = null;
                try {
                    URL url = new URL("http://"+data.getIp()+":8080/ZhiLvProject/audit/message/list?userId="+data.getUser().getUserId());
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setConnectTimeout(500);//检测时长
                    Message msg = Message.obtain();
                    if(conn.getResponseCode() == 200){
                        InputStream in = conn.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                        String str = reader.readLine();

                        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                        Type type = new TypeToken<List<AuditMessage>>(){}.getType();
                        List<AuditMessage> list = gson.fromJson(str,type);
                        msg.what = 1002;
                        msg.obj = list;

                        in.close();
                        reader.close();
                    }else {
                        msg.obj = "未连接到服务器";
                        msg.what = 1001;
                    }
                    mHandler.sendMessage(msg);
                } catch (IOException e) {
                    e.printStackTrace();
                } finally{
                    conn.disconnect();

                }
            }
        }.start();
    }
}