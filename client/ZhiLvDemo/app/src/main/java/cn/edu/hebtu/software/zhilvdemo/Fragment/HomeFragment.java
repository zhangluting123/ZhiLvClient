package cn.edu.hebtu.software.zhilvdemo.Fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.astuetz.PagerSlidingTabStrip;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import cn.edu.hebtu.software.zhilvdemo.Adapter.ChannelPagerAdapter;
import cn.edu.hebtu.software.zhilvdemo.Fragment.Home.AttentionHomeFragment;
import cn.edu.hebtu.software.zhilvdemo.Fragment.Home.LocalHomeFragment;
import cn.edu.hebtu.software.zhilvdemo.Fragment.Home.RecommendHomeFragment;
import cn.edu.hebtu.software.zhilvdemo.R;


/**
 * @ProjectName:    ZhiLv
 * @Description:    主界面
 * @Author:         张璐婷
 * @CreateDate:     2020/12/14 11:32
 * @Version:        1.0
 */
public class HomeFragment extends Fragment {
    private View view;
    private ChannelPagerAdapter adapter;
    private ViewPager pages;
    private PagerSlidingTabStrip tabs;
    private List<Fragment> fragmentList;
    private String[] titles = {"同城","关注","推荐"};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(null == view){
            view = inflater.inflate(R.layout.fragment_home, container, false);
            initTabsPager();
//            getViews();

        }
        ViewGroup parent = (ViewGroup) view.getParent();
        if(null != parent){
            parent.removeView(view);
        }

        return view;
    }

    private void initTabsPager(){
        pages =view.findViewById(R.id.pager_viewpager);
        tabs = view.findViewById(R.id.pager_tabs);
        initFragment();
        Log.i("HomeCFManager", super.getChildFragmentManager()+"");
        Log.i("HomeParentManager", getParentFragment()+"");
        adapter = new ChannelPagerAdapter(super.getChildFragmentManager(),
                fragmentList,titles);
        pages.setAdapter(adapter);
        //每个选项卡相同权重，必须设置在setViewPager()之前
        tabs.setShouldExpand(true);
        //标签之间分隔线的颜色
        tabs.setDividerColor(Color.TRANSPARENT);
        //滑动指示器的颜色
        tabs.setIndicatorColor(getResources().getColor(R.color.MyThemeColor));
        //滑动指示器的高度
        tabs.setIndicatorHeight(4);
        tabs.setUnderlineColor(Color.TRANSPARENT);
        tabs.setTextSize(35);
        tabs.setTextColor(getResources().getColor(R.color.gray_deep));

        tabs.setViewPager(pages);
    }

    private void initFragment() {
        fragmentList = new ArrayList<Fragment>();
        Fragment localHomeFragment = new LocalHomeFragment();
        Fragment attentionHomeFragment = new AttentionHomeFragment();
        Fragment RecommendHomeFragment = new RecommendHomeFragment();
        fragmentList.add(localHomeFragment);
        fragmentList.add(attentionHomeFragment);
        fragmentList.add(RecommendHomeFragment);
    }

}




