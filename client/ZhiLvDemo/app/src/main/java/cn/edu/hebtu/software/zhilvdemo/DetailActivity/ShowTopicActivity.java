package cn.edu.hebtu.software.zhilvdemo.DetailActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import cn.edu.hebtu.software.zhilvdemo.Adapter.TopicListAdapter;
import cn.edu.hebtu.software.zhilvdemo.Data.Topic;
import cn.edu.hebtu.software.zhilvdemo.R;
import cn.edu.hebtu.software.zhilvdemo.Setting.MyApplication;
import cn.edu.hebtu.software.zhilvdemo.Util.DetermineConnServer;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.gson.Gson;
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

public class ShowTopicActivity extends AppCompatActivity {
    private SearchView searchView;
    private List<Topic> mDatas;
    private MyApplication data;
    private TopicListAdapter adapter;
    @SuppressLint("HandlerLeak")
    private final Handler mHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case 1001:
                    Toast.makeText(ShowTopicActivity.this, (CharSequence)msg.obj, Toast.LENGTH_SHORT).show();
                    break;
                case 1002:
                    adapter.refresh((List<Topic>)msg.obj);
                    break;
                case 1003:
                    mDatas = (List<Topic>) msg.obj;
                    initRecycler();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_topic);
        data = (MyApplication)getApplication();
        initView();
        topicList();
    }

    private void initRecycler(){
        RecyclerView recyclerView = findViewById(R.id.topic_recyclerview);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(manager);
        adapter = new TopicListAdapter(this, R.layout.item_show_topic, mDatas);
        adapter.setOnItemClickListener(new TopicListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(ShowTopicActivity.this,TopicDetailActivity.class);
                intent.putExtra("topic", "话题详情页");
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(adapter);
    }

    private void initView(){
        //查找
        searchView = findViewById(R.id.searchView);
        searchView.setImeOptions(EditorInfo.IME_ACTION_SEARCH);//让键盘的回车键设置成搜索
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.e("searchTopic", "submit");
                searchTopic(query);
                //清除焦点，收软键盘
                searchView.clearFocus();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.e("searchTopic", "change");
                return false;
            }
        });
        //返回
        ImageButton back = findViewById(R.id.topic_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void searchTopic(String str){
        new Thread(){
            @Override
            public void run() {
                try {
                    Message msg = Message.obtain();
                    if(DetermineConnServer.isConnByHttp(getApplicationContext())) {
                        URL url = new URL("http://" + data.getIp() + ":8080/ZhiLvProject/topic/like?str=" + str );
                        URLConnection conn = url.openConnection();
                        InputStream in = conn.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(in,"utf-8"));
                        String info = reader.readLine();
                        Gson gson = new Gson();
                        Type type = new TypeToken<List<Topic>>(){}.getType();
                        List<Topic> topicList = gson.fromJson(info,type);
                        msg.what = 1002;
                        msg.obj = topicList;
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
        }.start();
    }

    private void topicList(){
        new Thread(){
            @Override
            public void run() {
                try {
                    Message msg = Message.obtain();
                    if(DetermineConnServer.isConnByHttp(getApplicationContext())) {
                        URL url = new URL("http://" + data.getIp() + ":8080/ZhiLvProject/topic/list" );
                        URLConnection conn = url.openConnection();
                        InputStream in = conn.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(in,"utf-8"));
                        String info = reader.readLine();
                        Gson gson = new Gson();
                        Type type = new TypeToken<List<Topic>>(){}.getType();
                        List<Topic> topicList = gson.fromJson(info,type);
                        msg.what = 1003;
                        msg.obj = topicList;
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
        }.start();
    }
}