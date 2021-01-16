package cn.edu.hebtu.software.zhilvdemo.Fragment.Home;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import cn.edu.hebtu.software.zhilvdemo.Adapter.SceneRecommendAdapter;
import cn.edu.hebtu.software.zhilvdemo.DetailActivity.SceneDetailActivity;
import cn.edu.hebtu.software.zhilvdemo.R;

/**
 * @ProjectName:    ZhiLv
 * @Description:    景点推荐
 * @Author:         张璐婷
 * @CreateDate:     2020/12/18 21:32
 * @Version:        1.0
 */
public class SceneRecommendFragment extends Fragment {
    private List<String> mDatas;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_scene_recommend, container, false);


        initDatas();
        initView();


        return view;
    }

    private void initView(){
        RecyclerView recyclerView = view.findViewById(R.id.scene_recycler);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(manager);
        SceneRecommendAdapter adapter = new SceneRecommendAdapter(getActivity(), R.layout.item_scene_recommend, mDatas);
        adapter.setOnItemClickListener(new SceneRecommendAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(getActivity().getApplicationContext(), SceneDetailActivity.class);
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
                        adapter.replaceAll(getData());
                        refreshLayout.finishRefresh();
                    }
                }, 1000);
            }
        });

    }

    private void initDatas(){
        mDatas = new ArrayList<>();
        for (int i = 0; i < 30; i++)
        {
            mDatas.add("Scene" + " -> " + i);
        }
    }

    private List<String> getData(){
        for (int i = 0; i < 3; i++)
        {
            mDatas.add(i," -> ADD" + i);
        }
        return mDatas;
    }
}