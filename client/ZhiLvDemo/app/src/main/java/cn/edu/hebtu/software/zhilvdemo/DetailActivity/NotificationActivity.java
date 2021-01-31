package cn.edu.hebtu.software.zhilvdemo.DetailActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import cn.edu.hebtu.software.zhilvdemo.Adapter.NotificationAdapter;
import cn.edu.hebtu.software.zhilvdemo.Data.Notification;
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
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

public class NotificationActivity extends AppCompatActivity {
    private List<Notification> notificationList;
    private NotificationAdapter adapter;
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
                    notificationList = (List<Notification>)msg.obj;
                    adapter.refresh(notificationList);
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        data = (MyApplication) getApplication();
        initView();
        if(null != data.getUser()){
            getNotificaitonData();
        }


    }

    private void initView(){
        Toolbar toolbar = findViewById(R.id.notification_toolbar);
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
        adapter = new NotificationAdapter(notificationList, R.layout.item_notification, this);
        recyclerView.setAdapter(adapter);
    }

    private void getNotificaitonData(){
        new Thread(){
            @Override
            public void run() {
                if(DetermineConnServer.isConnByHttp(getApplicationContext())){
                    try {
                        URL url = new URL("http://"+data.getIp()+":8080/ZhiLvProject/notification/list?userId="+data.getUser().getUserId());
                        URLConnection conn = url.openConnection();
                        InputStream in = conn.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                        String str = reader.readLine();
                        Message msg = Message.obtain();
                        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                        Type type = new TypeToken<List<Notification>>(){}.getType();
                        List<Notification> list = gson.fromJson(str,type);
                        msg.what = 1002;
                        msg.obj = list;
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