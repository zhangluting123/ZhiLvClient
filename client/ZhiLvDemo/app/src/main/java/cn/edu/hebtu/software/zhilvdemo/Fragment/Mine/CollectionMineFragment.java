package cn.edu.hebtu.software.zhilvdemo.Fragment.Mine;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import cn.edu.hebtu.software.zhilvdemo.Adapter.StaggeredGridAdapter;
import cn.edu.hebtu.software.zhilvdemo.DetailActivity.TravelDetailActivity;
import cn.edu.hebtu.software.zhilvdemo.DetailActivity.VideoDetailActivity;
import cn.edu.hebtu.software.zhilvdemo.R;

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
    private List<String> mDatas = new ArrayList<>();
    private String mTitle = "Collection";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_collection_mine, container, false);
        mRecyclerView = view.findViewById(R.id.collection_mine_recycler);

        initDatas();
        showStagger();
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
        StaggeredGridAdapter adapter = new StaggeredGridAdapter(mDatas,getActivity());
        adapter.setOnItemClickListener(new StaggeredGridAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if(mDatas.get(position).contains("2")){
                    Intent intent = new Intent(getActivity(), VideoDetailActivity.class);
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(getActivity(), TravelDetailActivity.class);
                    startActivity(intent);
                }
            }
        });
        mRecyclerView.setAdapter(adapter);

    }

    private void initDatas(){
        for (int i = 0; i < 30; i++)
        {
            mDatas.add(mTitle + " -> " + i);
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