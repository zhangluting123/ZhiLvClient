package cn.edu.hebtu.software.zhilvdemo.Fragment.Mine;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import cn.edu.hebtu.software.zhilvdemo.Adapter.StaggeredGridAdapter;
import cn.edu.hebtu.software.zhilvdemo.Data.Note;
import cn.edu.hebtu.software.zhilvdemo.DetailActivity.TravelDetailActivity;
import cn.edu.hebtu.software.zhilvdemo.DetailActivity.VideoDetailActivity;
import cn.edu.hebtu.software.zhilvdemo.R;
import cn.edu.hebtu.software.zhilvdemo.Setting.MyApplication;
import cn.edu.hebtu.software.zhilvdemo.Util.DetermineConnServer;

/**
 * @ProjectName:    ZhiLv
 * @Description:    我的收藏
 * @Author:         张璐婷
 * @CreateDate:     2020/12/22 18:06
 * @Version:        1.0
 */
public class CollectionMineFragment extends Fragment {
    private View view;
    private RecyclerView mRecyclerView;
    private List<Note> mDatas = new ArrayList<>();
    private StaggeredGridAdapter adapter;
    private MyApplication data;

    @SuppressLint("HandlerLeak")
    private final Handler mHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case 1001:
                    Toast.makeText(getActivity().getApplicationContext(), (CharSequence)msg.obj,Toast.LENGTH_SHORT).show();
                    break;
                case 1002:
                    mDatas = (List<Note>) msg.obj;
                    adapter.replaceAll(mDatas);
                    break;
            }
        }
    };
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_collection_mine, container, false);
        mRecyclerView = view.findViewById(R.id.collection_mine_recycler);
        data = (MyApplication)getActivity().getApplication();

        showStagger();
        initDatas();
        return view;
    }

    /**
     *  @author: 张璐婷
     *  @time: 2020/12/21  14:50
     *  @Description: 设置瀑布流效果
     */
    private void showStagger(){
        //准备布局管理器
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        //设置布局管理器到RecyclerView里
        mRecyclerView.setLayoutManager(layoutManager);
        //创建适配器
        adapter = new StaggeredGridAdapter(mDatas,getActivity());
        adapter.setOnItemClickListener(new StaggeredGridAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if(!mDatas.get(position).isFlag()){
                    Intent intent = new Intent(getActivity(), VideoDetailActivity.class);
                    intent.putExtra("video", mDatas.get(position).getVideo());
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(getActivity(), TravelDetailActivity.class);
                    intent.putExtra("travels", mDatas.get(position).getTravels());
                    startActivity(intent);
                }
            }
        });
        mRecyclerView.setAdapter(adapter);

    }

    private void initDatas(){
        if(null != data.getUser()){
            Thread thread = new GetListThread();
            thread.start();
        }
    }


    class GetListThread extends Thread{
        @Override
        public void run() {
            try {
                Message msg = Message.obtain();
                if(DetermineConnServer.isConnByHttp(getActivity().getApplicationContext())) {
                    URL url = new URL("http://" + data.getIp() + ":8080/ZhiLvProject/note/collectionlist?userId="+data.getUser().getUserId() );
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

    @Override
    public void onResume() {
        super.onResume();
        initDatas();
    }
}