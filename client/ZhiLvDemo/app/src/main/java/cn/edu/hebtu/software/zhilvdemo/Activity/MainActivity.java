package cn.edu.hebtu.software.zhilvdemo.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTabHost;
import cn.edu.hebtu.software.zhilvdemo.DetailActivity.MyAttentionListActivity;
import cn.edu.hebtu.software.zhilvdemo.Fragment.AddTravelsFragment;
import cn.edu.hebtu.software.zhilvdemo.Fragment.DestinationFragment;
import cn.edu.hebtu.software.zhilvdemo.Fragment.HomeFragment;
import cn.edu.hebtu.software.zhilvdemo.Fragment.MessageFragment;
import cn.edu.hebtu.software.zhilvdemo.Fragment.MineFragment;
import cn.edu.hebtu.software.zhilvdemo.R;
import cn.edu.hebtu.software.zhilvdemo.Setting.MyApplication;
import cn.edu.hebtu.software.zhilvdemo.Util.PermissionUtil;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ActionMenuView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ProjectName:    ZhiLv
 * @Description:    主界面
 * @Author:         张璐婷
 * @CreateDate:     2020/12/13 18:12
 * @Version:        1.0
 */
public class MainActivity extends AppCompatActivity {
    private long exitTime;
    private final Map<String, ImageView> imageViewMap = new HashMap<>();
    private final Map<String, TextView> textViewMap = new HashMap<>();
    private LocationClient locationClient;
    private LocationClientOption locationClientOption;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MyApplication data = (MyApplication)getApplication();
        data.setIp(getString(R.string.internet_ip));

        //如果定位权限已经开启，则进行定位，否则开启权限，在权限回调成功后开启定位
        if(PermissionUtil.openLocationPermission(this)){
            locationOption();
        }

        initView();

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initView(){
        FragmentTabHost fragmentTabHost = findViewById(android.R.id.tabhost);
        fragmentTabHost.setup(this,
                getSupportFragmentManager(),//FragmentManager对象用来管理多个Fragment
                android.R.id.tabcontent);//真正显示内容页面的容器的id

        //主页
        TabHost.TabSpec home = fragmentTabHost.newTabSpec("home")
                .setIndicator(getTabSpecView("home",R.mipmap.home1,"主页"));
        fragmentTabHost.addTab(home, HomeFragment.class, null);
        //目的地
        TabHost.TabSpec destination = fragmentTabHost.newTabSpec("destination")
                .setIndicator(getTabSpecView("destination",R.mipmap.destination1,"目的地"));
        fragmentTabHost.addTab(destination, DestinationFragment.class, null);
        //添加
        TabHost.TabSpec addTravels = fragmentTabHost.newTabSpec("addTravels")
                .setIndicator(getAddView("addTravels",R.mipmap.add_travels));
        fragmentTabHost.addTab(addTravels, AddTravelsFragment.class,null);
        //消息
        TabHost.TabSpec message = fragmentTabHost.newTabSpec("message")
                .setIndicator(getTabSpecView("message",R.mipmap.message1,"消息"));
        fragmentTabHost.addTab(message, MessageFragment.class, null);

        //我的
        TabHost.TabSpec mine = fragmentTabHost.newTabSpec("mine")
                .setIndicator(getTabSpecView("mine",R.mipmap.mine1,"我的"));
        fragmentTabHost.addTab(mine, MineFragment.class, null);
        //默认选中第一项
//        int tab = 0;
        fragmentTabHost.setCurrentTab(0);
        imageViewMap.get("home").setImageResource(R.mipmap.home2);
        textViewMap.get("home").setTextColor(getResources().getColor(R.color.MyThemeColor));


        //切换选项卡的事件监听器
        fragmentTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                switch (tabId){
                    case "home":
                        imageViewMap.get("home").setImageResource(R.mipmap.home2);
                        imageViewMap.get("destination").setImageResource(R.mipmap.destination1);
                        imageViewMap.get("message").setImageResource(R.mipmap.message1);
                        imageViewMap.get("mine").setImageResource(R.mipmap.mine1);
                        textViewMap.get("home").setTextColor(getResources().getColor(R.color.MyThemeColor));
                        textViewMap.get("destination").setTextColor(Color.BLACK);
                        textViewMap.get("message").setTextColor(Color.BLACK);
                        textViewMap.get("mine").setTextColor(Color.BLACK);
                        break;
                    case "destination":
                        imageViewMap.get("home").setImageResource(R.mipmap.home1);
                        imageViewMap.get("destination").setImageResource(R.mipmap.destination2);
                        imageViewMap.get("message").setImageResource(R.mipmap.message1);
                        imageViewMap.get("mine").setImageResource(R.mipmap.mine1);
                        textViewMap.get("home").setTextColor(Color.BLACK);
                        textViewMap.get("destination").setTextColor(getResources().getColor(R.color.MyThemeColor));
                        textViewMap.get("message").setTextColor(Color.BLACK);
                        textViewMap.get("mine").setTextColor(Color.BLACK);
                        break;
                    case "message":
                        imageViewMap.get("home").setImageResource(R.mipmap.home1);
                        imageViewMap.get("destination").setImageResource(R.mipmap.destination1);
                        imageViewMap.get("message").setImageResource(R.mipmap.message2);
                        imageViewMap.get("mine").setImageResource(R.mipmap.mine1);
                        textViewMap.get("home").setTextColor(Color.BLACK);
                        textViewMap.get("destination").setTextColor(Color.BLACK);
                        textViewMap.get("message").setTextColor(getResources().getColor(R.color.MyThemeColor));
                        textViewMap.get("mine").setTextColor(Color.BLACK);
                        break;
                    case "mine":
                        imageViewMap.get("home").setImageResource(R.mipmap.home1);
                        imageViewMap.get("destination").setImageResource(R.mipmap.destination1);
                        imageViewMap.get("message").setImageResource(R.mipmap.message1);
                        imageViewMap.get("mine").setImageResource(R.mipmap.mine2);
                        textViewMap.get("home").setTextColor(Color.BLACK);
                        textViewMap.get("destination").setTextColor(Color.BLACK);
                        textViewMap.get("message").setTextColor(Color.BLACK);
                        textViewMap.get("mine").setTextColor(getResources().getColor(R.color.MyThemeColor));
                        break;
                }
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private View getTabSpecView(String tag, int imageResId, String title) {
        LayoutInflater layoutInflater = getLayoutInflater();
        @SuppressLint("InflateParams") 
        View view = layoutInflater.inflate(R.layout.tabspec_layout,null);

        //获取控件对象
        ImageView imageView = view.findViewById(R.id.iv_icon);
        imageView.setImageResource(imageResId);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(40, 40);
        imageView.setLayoutParams(lp);

        TextView textView = view.findViewById(R.id.tv_icon);
        textView.setText(title);

        imageViewMap.put(tag,imageView);
        textViewMap.put(tag,textView);
        return view;
    }

    private View getAddView(String tag, int imageResId){
        LayoutInflater layoutInflater = getLayoutInflater();
        @SuppressLint("InflateParams")
        View view = layoutInflater.inflate(R.layout.tabspec_layout,null);
        //获取控件对象
        ImageView imageView = view.findViewById(R.id.iv_icon);
        imageView.setImageResource(imageResId);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(80, 80);
        imageView.setLayoutParams(lp);

        imageViewMap.put(tag,imageView);
        return view;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // 获取到Activity下的Fragment
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        if (fragments != null) {
            // 查找在Fragment中onRequestPermissionsResult方法并调用
            for (Fragment fragment : fragments) {
                if (fragment != null) {
                    // 这里就会调用我们Fragment中的onRequestPermissionsResult方法
                    fragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
                }
            }
        }

        switch (requestCode) {
            case PermissionUtil.BAIDU_READ_PHONE_STATE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 获取到权限，作相应处理（调用定位SDK应当确保相关权限均被授权，否则可能引起定位失败）
                    //定位操作
                    locationOption();
                } else {
                    // 没有获取到权限，做特殊处理
                    Toast.makeText(getApplicationContext(), "获取位置权限失败，请手动开启", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 获取到Activity下的Fragment
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        if (fragments != null) {
            // 查找在Fragment中onRequestPermissionsResult方法并调用
            for (Fragment fragment : fragments) {
                if (fragment != null) {
                    // 这里就会调用我们Fragment中的onRequestPermissionsResult方法
                    fragment.onActivityResult(requestCode,resultCode,data);
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 获取到Activity下的Fragment
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        if (fragments != null) {
            // 查找在Fragment中onRequestPermissionsResult方法并调用
            for (Fragment fragment : fragments) {
                if (fragment != null) {
                    // 这里就会调用我们Fragment中的onRequestPermissionsResult方法
                    fragment.onResume();
                }
            }
        }
    }

    /**
     * @author: 张璐婷
     * @time: 2021/1/4  15:10
     * @Description: 定位操作
     */
    private void locationOption() {
        //1. 创建定位服务客户端类的对象
        locationClient = new LocationClient(this);
        //2. 创建定位客户端选项类的对象，并设置定位参数
        locationClientOption = new LocationClientOption();
        //设置定位参数
        //打开GPS
        locationClientOption.setOpenGps(true);
        //设置定位间隔时间
        locationClientOption.setScanSpan(1000 * 5);//10分钟
        SDKInitializer.setCoordType(CoordType.GCJ02);
        //设置定位模式:高精度模式
        locationClientOption.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //需要定位的地址数据
        locationClientOption.setIsNeedAddress(true);
        //需要地址描述
        locationClientOption.setIsNeedLocationDescribe(true);
        //需要周边POI信息
        locationClientOption.setIsNeedLocationPoiList(true);
        //3. 将定位选项参数应用给定位服务客户端类的对象
        locationClient.setLocOption(locationClientOption);
        //4. 开始定位
        locationClient.start();
        //5. 给定位客户端端类的对象注册定位监听器
        locationClient.registerLocationListener(new BDAbstractLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
                //获取定位详细数据
                //获取地址信息
                String addr = bdLocation.getAddrStr();
                Log.e("MainActivity百度地图", "地址：" + addr);

                MyApplication data = (MyApplication)getApplication();
                data.setCity(bdLocation.getCity());
                data.setProvince(bdLocation.getProvince());
                data.setAddrName(addr);
                data.setLatitude(bdLocation.getLatitude());
                data.setLongitude(bdLocation.getLongitude());
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //判断用户是否点击了返回键
        if(keyCode == KeyEvent.KEYCODE_BACK ){
            //获取当前时间毫秒数
            if(System.currentTimeMillis()-exitTime>2000){
                Toast.makeText(this,"再次点击退出程序",Toast.LENGTH_LONG).show();
                exitTime=System.currentTimeMillis();
            }else{
                this.finish();
            }
        }
        return true;
    }
}