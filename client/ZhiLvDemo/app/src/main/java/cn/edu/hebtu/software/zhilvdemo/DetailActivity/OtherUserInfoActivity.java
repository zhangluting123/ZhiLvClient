package cn.edu.hebtu.software.zhilvdemo.DetailActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import cn.edu.hebtu.software.zhilvdemo.Adapter.ChannelPagerAdapter;
import cn.edu.hebtu.software.zhilvdemo.Data.User;
import cn.edu.hebtu.software.zhilvdemo.Fragment.Other.TravelsOtherFragment;
import cn.edu.hebtu.software.zhilvdemo.R;
import cn.edu.hebtu.software.zhilvdemo.Setting.MyApplication;
import cn.edu.hebtu.software.zhilvdemo.Util.DetermineConnServer;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class OtherUserInfoActivity extends AppCompatActivity {
    private ChannelPagerAdapter adapter;
    private ViewPager pages;
    private PagerSlidingTabStrip tabs;
    private List<Fragment> fragmentList;
    private String[] titles = {"游记"};
    private ImageView userHead;
    private ImageView userSex;
    private TextView userName;
    private TextView userSign;
    private Button btnAttention;

    private MyApplication data;
    private User other;

    @SuppressLint("HandlerLeak")
    private final Handler mHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case 1001:
                    Toast.makeText(OtherUserInfoActivity.this, (CharSequence)msg.obj, Toast.LENGTH_SHORT).show();
                    break;
                case 1002:
                    btnAttention.setText((String)msg.obj);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_user_info);
        data = (MyApplication)getApplication();
        Intent intent = getIntent();
        other = intent.getParcelableExtra("other");

        initTabsPager();
        getViews();

        Glide.with(this).
                load("http://"+data.getIp()+":8080/ZhiLvProject/"+other.getUserHead())
                .apply(new RequestOptions().circleCrop())
                .into(userHead);
        userName.setText(other.getUserName());
        userSign.setText(other.getSignature());
        if("girl".equals(other.getSex())){
            userSex.setImageResource(R.mipmap.home_girl);
        }else{
            userSex.setImageResource(R.mipmap.home_boy);
        }
        if(null != data.getUser()){
            if(data.getUser().getUserId().equals(other.getUserId())){
                btnAttention.setText("myself");
            }else {
                checkFollow();

            }
        }

        btnAttention.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(null == data.getUser()){
                    Toast.makeText(OtherUserInfoActivity.this, "请先登录", Toast.LENGTH_SHORT).show();
                    return;
                }
                String nowMsg = btnAttention.getText().toString().trim();
                if(nowMsg.equals("+ 关注")){
                    follow();
                    btnAttention.setText("已关注");
                }else if(nowMsg.equals("已关注")){
                    noFollow();
                    btnAttention.setText("+ 关注");
                }
            }
        });

    }

    private void initTabsPager(){
        pages = findViewById(R.id.pager_viewpager);
        tabs = findViewById(R.id.pager_tabs);
        pages.setOffscreenPageLimit(titles.length);
        initFragment();
        adapter = new ChannelPagerAdapter(getSupportFragmentManager(),fragmentList,titles);
        pages.setAdapter(adapter);
        //每个选项卡相同权重，必须设置在setViewPager()之前
//        tabs.setShouldExpand(true);
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
        Fragment travelsOtherFragment = new TravelsOtherFragment(other.getUserId());
        fragmentList.add(travelsOtherFragment);
    }


    private void getViews(){
        userHead = findViewById(R.id.mine_user_head);
        userSex = findViewById(R.id.mine_user_sex);
        userName = findViewById(R.id.mine_user_name);
        userSign = findViewById(R.id.mine_user_sign);
        btnAttention = findViewById(R.id.btn_attention);
    }

    private void checkFollow(){
        new Thread(){
            @Override
            public void run() {
                try {
                    if(DetermineConnServer.isConnByHttp(getApplicationContext())) {
                        URL url = new URL("http://" + data.getIp() + ":8080/ZhiLvProject/user/ifFollow?mineId="+data.getUser().getUserId()+"&otherId="+other.getUserId());
                        URLConnection conn = url.openConnection();
                        InputStream in = conn.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(in, "utf-8"));
                        String info = reader.readLine();
                        Message message = Message.obtain();
                        message.what = 1002;
                        if("YES".equals(info)){
                            message.obj = "已关注";
                        }else{
                            message.obj = "+ 关注";
                        }
                        mHandler.sendMessage(message);
                    }else{
                        Message message = Message.obtain();
                        message.what = 1001;
                        message.obj = "未连接到服务器";
                        mHandler.sendMessage(message);
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private void follow() {
        new Thread() {
            @Override
            public void run() {
                try {
                    if(DetermineConnServer.isConnByHttp(OtherUserInfoActivity.this)) {
                        URL url = new URL("http://" + data.getIp() + ":8080/ZhiLvProject/user/follow?mineId="+data.getUser().getUserId()+"&otherId="+other.getUserId());
                        URLConnection conn = url.openConnection();
                        InputStream in = conn.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(in, "utf-8"));
                        String info = reader.readLine();
                        Message message = Message.obtain();
                        message.what = 1001;
                        if("OK".equals(info)){
                            message.obj = "关注成功";
                        }else{
                            message.obj = "关注失败";
                        }
                        mHandler.sendMessage(message);
                    }else{
                        Message message = Message.obtain();
                        message.what = 1001;
                        message.obj = "未连接到服务器";
                        mHandler.sendMessage(message);
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private void noFollow() {
        new Thread() {
            @Override
            public void run() {
                try {
                    if(DetermineConnServer.isConnByHttp(OtherUserInfoActivity.this)) {
                        URL url = new URL("http://" + data.getIp() + ":8080/ZhiLvProject/user/noFollow?mineId="+data.getUser().getUserId()+"&otherId="+other.getUserId());
                        URLConnection conn = url.openConnection();
                        InputStream in = conn.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(in, "utf-8"));
                        String info = reader.readLine();
                        Message message = Message.obtain();
                        message.what = 1001;
                        if("OK".equals(info)){
                            message.obj = "取关成功";
                        }else{
                            message.obj = "取关失败";
                        }
                        mHandler.sendMessage(message);
                    }else{
                        Message message = Message.obtain();
                        message.what = 1001;
                        message.obj = "未连接到服务器";
                        mHandler.sendMessage(message);
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            finish();
        }
        return true;
    }

}