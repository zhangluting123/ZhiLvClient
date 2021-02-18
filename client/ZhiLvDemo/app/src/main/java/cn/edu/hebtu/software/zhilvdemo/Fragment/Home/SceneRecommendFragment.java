package cn.edu.hebtu.software.zhilvdemo.Fragment.Home;

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
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import cn.edu.hebtu.software.zhilvdemo.Adapter.SceneRecommendAdapter;
import cn.edu.hebtu.software.zhilvdemo.Data.Scene;
import cn.edu.hebtu.software.zhilvdemo.DetailActivity.SceneDetailActivity;
import cn.edu.hebtu.software.zhilvdemo.R;
import cn.edu.hebtu.software.zhilvdemo.Setting.MyApplication;
import cn.edu.hebtu.software.zhilvdemo.Util.DetermineConnServer;

/**
 * @ProjectName:    ZhiLv
 * @Description:    景点推荐
 * @Author:         张璐婷
 * @CreateDate:     2020/12/18 21:32
 * @Version:        1.0
 */
public class SceneRecommendFragment extends Fragment {
    private List<Scene> mDatas;
    private View view;

    private SceneRecommendAdapter adapter;
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
                    mDatas = (List<Scene>) msg.obj;
                    adapter.refresh(mDatas);
                    break;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_scene_recommend, container, false);
        data = (MyApplication)getActivity().getApplication();

        initView();
        initDatas();

        return view;
    }

    private void initView(){
        RecyclerView recyclerView = view.findViewById(R.id.scene_recycler);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(manager);
        adapter = new SceneRecommendAdapter(getActivity(), R.layout.item_scene_recommend, mDatas);
        adapter.setOnItemClickListener(new SceneRecommendAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(getActivity().getApplicationContext(), SceneDetailActivity.class);
                intent.putExtra("scene", mDatas.get(position));
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(adapter);

        SmartRefreshLayout refreshLayout = view.findViewById(R.id.refresh_layout);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        initDatas();
                        adapter.refresh(mDatas);
                        refreshLayout.finishRefresh();
                    }
                }, 1000);
            }
        });

    }

    private void initDatas() {
        Thread thread = new GetListThread();
        thread.start();
    }

    class GetListThread extends Thread{
        @Override
        public void run() {
            try {
                Message msg = Message.obtain();
                if(DetermineConnServer.isConnByHttp(getActivity().getApplicationContext())) {
                    String str = null;
                    if(null == data.getUser()){
                        str = "http://" + data.getIp() + ":8080/ZhiLvProject/recommend/scene/getRecommendList";
                    }else{
                        str = "http://" + data.getIp() + ":8080/ZhiLvProject/recommend/scene/getRecommendList?userId="+data.getUser().getUserId();
                    }
                    URL url = new URL(str);
                    URLConnection conn = url.openConnection();
                    InputStream in = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in,"utf-8"));
                    String info = reader.readLine();
                    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                    Type type = new TypeToken<List<Scene>>(){}.getType();
                    List<Scene> list = gson.fromJson(info,type);
                    msg.what = 1002;
                    msg.obj = list;
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
}