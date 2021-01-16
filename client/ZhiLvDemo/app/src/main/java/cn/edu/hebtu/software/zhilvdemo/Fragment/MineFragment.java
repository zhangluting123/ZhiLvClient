package cn.edu.hebtu.software.zhilvdemo.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import cn.edu.hebtu.software.zhilvdemo.Activity.LoginActivity;
import cn.edu.hebtu.software.zhilvdemo.Adapter.ChannelPagerAdapter;
import cn.edu.hebtu.software.zhilvdemo.DetailActivity.MyAttentionListActivity;
import cn.edu.hebtu.software.zhilvdemo.DetailActivity.MyFansListActivity;
import cn.edu.hebtu.software.zhilvdemo.DetailActivity.SettingActivity;
import cn.edu.hebtu.software.zhilvdemo.Fragment.Mine.CollectionMineFragment;
import cn.edu.hebtu.software.zhilvdemo.Fragment.Mine.GoodMineFragment;
import cn.edu.hebtu.software.zhilvdemo.Fragment.Mine.TravelsMineFragment;
import cn.edu.hebtu.software.zhilvdemo.R;

/**
 * @ProjectName:    ZhiLv
 * @Description:    我的界面
 * @Author:         张璐婷
 * @CreateDate:     2020/12/14 19:45
 * @Version:        1.0
 */
public class MineFragment extends Fragment {
    private View view;
    private ChannelPagerAdapter adapter;
    private ViewPager pages;
    private PagerSlidingTabStrip tabs;
    private List<Fragment> fragmentList;
    private String[] titles = {"游记","点赞","收藏"};

    private Button logout;
    private ImageView setting;
    private ImageView userHead;
    private ImageView userSex;
    private TextView userName;
    private TextView userSign;
    private TextView attentionNum;
    private TextView fansNum;
    private LinearLayout attention;
    private LinearLayout fans;
    private MyCustomListener customListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(null == view){
            view = inflater.inflate(R.layout.fragment_mine, container, false);
            initTabsPager();
            getViews();
            registListener();

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
        pages.setOffscreenPageLimit(titles.length);
        initFragment();
        adapter = new ChannelPagerAdapter(super.getActivity().getSupportFragmentManager(),fragmentList,titles);
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
        tabs.setTextSize(30);

        tabs.setViewPager(pages);
    }

    private void initFragment() {
        fragmentList = new ArrayList<Fragment>();
        Fragment travelsMineFragment = new TravelsMineFragment();
        Fragment goodMineFragment = new GoodMineFragment();
        Fragment collectionMineFragment = new CollectionMineFragment();
        fragmentList.add(travelsMineFragment);
        fragmentList.add(goodMineFragment);
        fragmentList.add(collectionMineFragment);
    }

    /**
     *  @author: 张璐婷
     *  @time: 2020/12/14  19:52
     *  @Description: 获取控件
     */
    private void getViews(){
        logout = view.findViewById(R.id.mine_logout);
        setting = view.findViewById(R.id.mine_setting);
        userHead = view.findViewById(R.id.mine_user_head);
        userSex = view.findViewById(R.id.mine_user_sex);
        userName = view.findViewById(R.id.mine_user_name);
        userSign = view.findViewById(R.id.mine_user_sign);
        attentionNum = view.findViewById(R.id.mine_attention_num);
        fansNum = view.findViewById(R.id.mine_fans_num);
        attention = view.findViewById(R.id.mine_attention);
        fans = view.findViewById(R.id.mine_fans);
    }

    /**
     *  @author: 张璐婷
     *  @time: 2020/12/14  20:04
     *  @Description: 注册监听器
     */
    private void registListener(){
        customListener = new MyCustomListener();
        logout.setOnClickListener(customListener);
        setting.setOnClickListener(customListener);
        userHead.setOnClickListener(customListener);
        attention.setOnClickListener(customListener);
        fans.setOnClickListener(customListener);

    }

    /**
     *  @author: 张璐婷
     *  @time: 2020/12/14  20:06
     *  @Description: 自定义事件监听器
     */
    class MyCustomListener implements View.OnClickListener{
        @SuppressLint("NonConstantResourceId")
        @Override
        public void onClick(View view) {
            Intent intent = null;
            switch (view.getId()){
                case R.id.mine_logout:
                    break;
                case R.id.mine_setting:
                    intent = new Intent(getActivity().getApplicationContext(), SettingActivity.class);
                    startActivity(intent);
                    break;
                case R.id.mine_user_head:
                    //TODO 判断当前用户为空
                    intent = new Intent(getActivity().getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                    break;
                case R.id.mine_attention:
                    intent = new Intent(getActivity().getApplicationContext(), MyAttentionListActivity.class);
                    startActivity(intent);
                    break;
                case R.id.mine_fans:
                    intent = new Intent(getActivity().getApplicationContext(), MyFansListActivity.class);
                    startActivity(intent);
                    break;
                default:
            }
        }
    }


}
