package cn.edu.hebtu.software.zhilvdemo.Fragment.Home;

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
import cn.edu.hebtu.software.zhilvdemo.R;

/**
 * @ProjectName:    ZhiLv
 * @Description:    推荐
 * @Author:         张璐婷
 * @CreateDate:     2020/12/18  19:50
 * @Version:        1.0
 */
public class RecommendHomeFragment extends Fragment {
    private View view;
    private ChannelPagerAdapter adapter;
    private ViewPager pages;
    private PagerSlidingTabStrip tabs;
    private List<Fragment> fragmentList;
    private String[] titles = {"景点","游记","用户"};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_recommend_home, container, false);
        initTabsPager();

        return view;
    }

    private void initTabsPager(){
        pages =view.findViewById(R.id.pager_viewpager);
        tabs = view.findViewById(R.id.pager_tabs);
        initFragment();
        Log.i("RecommendCFManager", super.getChildFragmentManager()+"");
        Log.i("RecommendPManager", getParentFragment()+"");
        adapter = new ChannelPagerAdapter(super.getChildFragmentManager(),fragmentList,titles);
        pages.setAdapter(adapter);
//        tabs.setShouldExpand(true);
        tabs.setDividerColor(Color.TRANSPARENT);
        tabs.setIndicatorColor(getResources().getColor(R.color.MyThemeColor));
        tabs.setIndicatorHeight(4);
        tabs.setUnderlineColor(Color.TRANSPARENT);
        tabs.setTextSize(30);
        tabs.setTextColor(getResources().getColor(R.color.gray_middle));

        tabs.setViewPager(pages);
    }

    private void initFragment() {
        fragmentList = new ArrayList<Fragment>();
        Fragment sceneRecommendFragment = new SceneRecommendFragment();
        Fragment travelsRecommendFragment = new TravelsRecommendFragment();
        Fragment userRecommendFragment = new UserRecommendFragment();
        fragmentList.add(sceneRecommendFragment);
        fragmentList.add(travelsRecommendFragment);
        fragmentList.add(userRecommendFragment);
    }

}
