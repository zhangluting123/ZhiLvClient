package cn.edu.hebtu.software.zhilvdemo.DetailActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import cn.edu.hebtu.software.zhilvdemo.Adapter.StaggeredGridAdapter;
import cn.edu.hebtu.software.zhilvdemo.Data.Note;
import cn.edu.hebtu.software.zhilvdemo.Data.Topic;
import cn.edu.hebtu.software.zhilvdemo.Fragment.Home.LocalHomeFragment;
import cn.edu.hebtu.software.zhilvdemo.R;
import cn.edu.hebtu.software.zhilvdemo.Setting.MyApplication;
import cn.edu.hebtu.software.zhilvdemo.Util.DetermineConnServer;

import android.annotation.SuppressLint;
import android.content.Intent;
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
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class TopicDetailActivity extends AppCompatActivity {
    private List<Note> mDatas;
    private StaggeredGridAdapter adapter;
    private Topic topic;
    private MyApplication data;
    @SuppressLint("HandlerLeak")
    private final Handler mHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case 1001:
                    Toast.makeText(getApplicationContext(), (CharSequence)msg.obj,Toast.LENGTH_SHORT).show();
                    break;
                case 1002:
                    mDatas = (List<Note>) msg.obj;
                    adapter.replaceAll(mDatas);
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic_detail);
        data = (MyApplication)getApplication();

        Intent intent = getIntent();
        topic = intent.getParcelableExtra("topic");

        Toolbar toolbar = findViewById(R.id.topic_detail_toolbar);
        toolbar.setTitle(topic.getTitle());
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        RecyclerView recyclerView = findViewById(R.id.topic_detail_recycler);
        //准备布局管理器
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new StaggeredGridAdapter(mDatas,this);
        adapter.setOnItemClickListener(new StaggeredGridAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if(!mDatas.get(position).isFlag()){
                    Intent intent = new Intent(TopicDetailActivity.this, VideoDetailActivity.class);
                    intent.putExtra("video", mDatas.get(position).getVideo());
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(TopicDetailActivity.this ,TravelDetailActivity.class);
                    intent.putExtra("travels", mDatas.get(position).getTravels());
                    startActivity(intent);
                }
            }
        });
        recyclerView.setAdapter(adapter);

        initDatas();
    }

    class GetListThread extends Thread{
        @Override
        public void run() {
            try {
                Message msg = Message.obtain();
                if(DetermineConnServer.isConnByHttp(getApplicationContext())) {
                    URL url = new URL("http://" + data.getIp() + ":8080/ZhiLvProject/note/topiclist?topicId="+topic.getTopicId());
                    URLConnection conn = url.openConnection();
                    InputStream in = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in,"utf-8"));
                    String info = reader.readLine();
                    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                    Type type = new TypeToken<List<Note>>(){}.getType();
                    List<Note> noteList = gson.fromJson(info,type);
                    msg.what = 1002;
                    msg.obj = noteList;
                }else {
                    msg.what = 1001;
                    msg.obj = "未连接到服务器";
                }
                mHandler.sendMessage(msg);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void initDatas(){
        Thread thread = new GetListThread();
        thread.start();
    }



}