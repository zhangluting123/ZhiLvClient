package cn.edu.hebtu.software.zhilvdemo.Fragment.Mine;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import cn.edu.hebtu.software.zhilvdemo.Adapter.StaggeredGridAdapter;
import cn.edu.hebtu.software.zhilvdemo.Data.Note;
import cn.edu.hebtu.software.zhilvdemo.DetailActivity.TravelDetailActivity;
import cn.edu.hebtu.software.zhilvdemo.DetailActivity.VideoDetailActivity;
import cn.edu.hebtu.software.zhilvdemo.R;

/**
 * @ProjectName:    ZhiLv
 * @Description:    我的游记
 * @Author:         张璐婷
 * @CreateDate:     2020/12/14$ 12:19$
 * @Version:        1.0
 */
public class TravelsMineFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private List<Note> mDatas = new ArrayList<>();
    private String mTitle = "Travels";
    private View view;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_travels_mine,container,false);
        mRecyclerView = view.findViewById(R.id.travels_mine_recycler);

//            initDatas();
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
                if(!mDatas.get(position).isFlag()){
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

}
