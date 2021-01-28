package cn.edu.hebtu.software.zhilvdemo.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import javax.security.auth.login.LoginException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import cn.edu.hebtu.software.zhilvdemo.Activity.LoginActivity;
import cn.edu.hebtu.software.zhilvdemo.Activity.MainActivity;
import cn.edu.hebtu.software.zhilvdemo.Adapter.ChannelPagerAdapter;
import cn.edu.hebtu.software.zhilvdemo.Data.User;
import cn.edu.hebtu.software.zhilvdemo.DetailActivity.MyAttentionListActivity;
import cn.edu.hebtu.software.zhilvdemo.DetailActivity.MyFansListActivity;
import cn.edu.hebtu.software.zhilvdemo.DetailActivity.SettingActivity;
import cn.edu.hebtu.software.zhilvdemo.Fragment.Mine.CollectionMineFragment;
import cn.edu.hebtu.software.zhilvdemo.Fragment.Mine.GoodMineFragment;
import cn.edu.hebtu.software.zhilvdemo.Fragment.Mine.TravelsMineFragment;
import cn.edu.hebtu.software.zhilvdemo.R;
import cn.edu.hebtu.software.zhilvdemo.Setting.MyApplication;
import cn.edu.hebtu.software.zhilvdemo.Util.DateUtil;
import cn.edu.hebtu.software.zhilvdemo.Util.DetermineConnServer;
import cn.edu.hebtu.software.zhilvdemo.Util.SharedUtil;

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

    private MyApplication data;
    private List<User> attenList;
    private List<User> fansList ;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(null == view){
            view = inflater.inflate(R.layout.fragment_mine, container, false);
            data = (MyApplication)getActivity().getApplication();
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
        adapter = new ChannelPagerAdapter(super.getChildFragmentManager(),fragmentList,titles);
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
                    data.setUser(null);
                    clearUserMsgLocal();
                    intent = new Intent(getActivity().getApplication(), MainActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                    break;
                case R.id.mine_setting:
                    if(null == data.getUser()){
                        Toast.makeText(getActivity().getApplicationContext(), "登录解锁更多功能", Toast.LENGTH_SHORT).show();
                    }else{
                        intent = new Intent(getActivity().getApplicationContext(), SettingActivity.class);
                        startActivity(intent);
                    }
                    break;
                case R.id.mine_user_head:
                    if(null == data.getUser()){
                        intent = new Intent(getActivity().getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                    }
                    break;
                case R.id.mine_attention:
                    intent = new Intent(getActivity().getApplicationContext(), MyAttentionListActivity.class);
                    intent.putParcelableArrayListExtra("attenList", (ArrayList<? extends Parcelable>) attenList);
                    startActivity(intent);
                    break;
                case R.id.mine_fans:
                    intent = new Intent(getActivity().getApplicationContext(), MyFansListActivity.class);
                    intent.putParcelableArrayListExtra("fansList", (ArrayList<? extends Parcelable>) fansList);
                    startActivity(intent);
                    break;
                default:
            }
        }
    }

    private void clearUserMsgLocal(){
        SharedUtil.putInt("userMsg",getActivity(),"userId",-1);
        SharedUtil.putString("userMsg",getActivity(),"phone",null);
        SharedUtil.putString("userMsg",getActivity(),"email",null);
        SharedUtil.putString("userMsg",getActivity(),"password",null);
        SharedUtil.putString("userMsg",getActivity(),"userHead",null);
        SharedUtil.putString("userMsg",getActivity(),"userName",null);
        SharedUtil.putString("userMsg",getActivity(),"sex",null);
        SharedUtil.putString("userMsg",getActivity(),"birth", null);
        SharedUtil.putString("userMsg",getActivity(),"signature",null);
    }

    class FindAttenionList extends Thread{
            @Override
            public void run() {
                try {
                    if(DetermineConnServer.isConnByHttp(getActivity())){
                        URL url = new URL("http://"+data.getIp()+":8080/ZhiLvProject/user/followList?userId="+data.getUser().getUserId());
                        attenList = new ArrayList<>();
                        URLConnection conn = url.openConnection();
                        InputStream in = conn.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(in, "utf-8"));
                        String info = reader.readLine();
                        if(null != info){
                            Gson gson = new Gson();
                            Type type = new TypeToken<List<User>>(){}.getType();
                            attenList = gson.fromJson(info,type);
                            Log.e("attenList", info);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
    }

    class FindFansList extends Thread{
        @Override
        public void run() {
            try {
                if(DetermineConnServer.isConnByHttp(getActivity())){
                    URL url = new URL("http://"+data.getIp()+":8080/ZhiLvProject/user/fansList?userId="+data.getUser().getUserId());
                    fansList = new ArrayList<>();
                    URLConnection conn = url.openConnection();
                    InputStream in = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in, "utf-8"));
                    String info = reader.readLine();
                    if(null != info){
                        Gson gson = new Gson();
                        Type type = new TypeToken<List<User>>(){}.getType();
                        fansList = gson.fromJson(info,type);
                        Log.e("attenList", info);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }



    @Override
    public void onResume() {
        super.onResume();
        User user = data.getUser();
        if(null != user){
            Thread thread1 = new FindAttenionList();
            Thread thread2 = new FindFansList();
            thread1.start();
            thread2.start();
            try {
                thread1.join();
                thread2.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            RequestOptions option = new RequestOptions().circleCrop();
            Glide.with(this).load("http://" + data.getIp() + ":8080/ZhiLvProject/"+ user.getUserHead()).apply(option).into(userHead);
            userName.setText(user.getUserName());
            userSign.setText(user.getSignature());
            if("girl".equals(user.getSex())){
                userSex.setImageResource(R.mipmap.home_girl);
            }else{
                userSex.setImageResource(R.mipmap.home_boy);
            }
            attentionNum.setText(attenList.size()+"");
            fansNum.setText(fansList.size()+"");
        }
    }
}
