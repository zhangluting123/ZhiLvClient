package cn.edu.hebtu.software.zhilvdemo.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import cn.edu.hebtu.software.zhilvdemo.Data.MoreDetail;
import cn.edu.hebtu.software.zhilvdemo.Data.User;
import cn.edu.hebtu.software.zhilvdemo.DetailActivity.AddTravelsActivity;
import cn.edu.hebtu.software.zhilvdemo.Fragment.DestinationFragment;
import cn.edu.hebtu.software.zhilvdemo.Fragment.HomeFragment;
import cn.edu.hebtu.software.zhilvdemo.Fragment.MessageFragment;
import cn.edu.hebtu.software.zhilvdemo.Fragment.MineFragment;
import cn.edu.hebtu.software.zhilvdemo.R;
import cn.edu.hebtu.software.zhilvdemo.Setting.MyApplication;
import cn.edu.hebtu.software.zhilvdemo.Util.DateUtil;
import cn.edu.hebtu.software.zhilvdemo.Util.PermissionUtil;
import cn.edu.hebtu.software.zhilvdemo.Util.SharedUtil;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
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
import com.next.easynavigation.view.EasyNavigationBar;

import java.util.ArrayList;
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
    private EasyNavigationBar navigationBar;
    private List<Fragment> fragments = new ArrayList<>();
    private String[] tabText = {"首页", "目的地", "", "消息", "我的"};
    //未选中icon
    private int[] normalIcon = {R.mipmap.home1, R.mipmap.destination1, R.mipmap.add_travels, R.mipmap.message1, R.mipmap.mine1};
    //选中时icon
    private int[] selectIcon = {R.mipmap.home2, R.mipmap.destination2, R.mipmap.add_travels, R.mipmap.message2, R.mipmap.mine2};
    private LocationClient locationClient;
    private LocationClientOption locationClientOption;

    private MyApplication data;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        data = (MyApplication)getApplication();
        data.setIp(getString(R.string.internet_ip));
        //如果定位权限已经开启，则进行定位，否则开启权限，在权限回调成功后开启定位
        if(PermissionUtil.openLocationPermission(this)){
            locationOption();
        }

        //判断是否第一次下载APP
        boolean isFirstIn = SharedUtil.getBoolean("isGuide", this,"isFirstIn",true);
        if(isFirstIn){
            SharedUtil.putBoolean("isGuide", getApplicationContext(),"isFirstIn", false);
        }else{
            //获取保存在本地的信息
            data.setUser(getUserMsgLocal());
        }

        initView();
    }

    private User getUserMsgLocal(){
        if(-1 >= SharedUtil.getInt("userMsg",this,"userId")){
            return null;
        }else {
            User user = new User();
            user.setUserId(SharedUtil.getInt("userMsg",this,"userId"));
            user.setPhone(SharedUtil.getString("userMsg", this, "phone"));
            user.setEmail(SharedUtil.getString("userMsg", this, "email"));
            user.setPassword(SharedUtil.getString("userMsg", this, "password"));
            user.setUserHead(SharedUtil.getString("userMsg", this, "userHead"));
            user.setUserName(SharedUtil.getString("userMsg", this, "userName"));
            user.setSex(SharedUtil.getString("userMsg", this, "sex"));
            user.setBirth(DateUtil.getDate(SharedUtil.getString("userMsg", this, "birth")));
            user.setSignature(SharedUtil.getString("userMsg", this, "signature"));
            return user;
        }
    }

    private void initView(){
        navigationBar = findViewById(R.id.navigationBar);

        fragments.add(new HomeFragment());
        fragments.add(new DestinationFragment());
        fragments.add(new MessageFragment());
        fragments.add(new MineFragment());

        navigationBar.titleItems(tabText)
                .normalIconItems(normalIcon)
                .selectIconItems(selectIcon)
                .selectTextColor(getResources().getColor(R.color.MyThemeColor))
                .fragmentList(fragments)
                .mode(EasyNavigationBar.MODE_ADD)
                .fragmentManager(getSupportFragmentManager())
                .build();

        navigationBar.getAddImage().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(null != data.getUser()){
                    startActivity(new Intent(MainActivity.this, AddTravelsActivity.class));
                }else{
                    Toast.makeText(getApplicationContext(), "请先登录", Toast.LENGTH_SHORT).show();
                }
            }
        });
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
        locationClientOption.setScanSpan(1000 * 5);//5秒定位
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