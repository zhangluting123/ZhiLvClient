package cn.edu.hebtu.software.zhilvdemo.Setting;

import android.app.Application;

import com.baidu.mapapi.SDKInitializer;
import com.mob.MobSDK;

import cn.edu.hebtu.software.zhilvdemo.Data.MoreDetail;
import cn.edu.hebtu.software.zhilvdemo.Data.Topic;
import cn.edu.hebtu.software.zhilvdemo.Data.User;

/**
 * @ProjectName:    ZhiLv
 * @Description:    
 * @Author:         张璐婷
 * @CreateDate:     2020/12/29  20:10
 * @Version:        1.0
 */
public class MyApplication extends Application {
    private Topic topic;
    private String city;
    private String province;
    private String addrName;
    private double latitude;
    private double longitude;
    private String searchText;
    private String searchCity;
    private double searchLatitude;
    private double searchLongitude;
    private MoreDetail moreDetail;

    private String ip;
    private User user;

    @Override
    public void onCreate() {
        super.onCreate();
        //百度地图初始化
        SDKInitializer.initialize(getApplicationContext());
        MobSDK.init(this,"320ceaf58e03a","637027746d83b354d031bd8f6c05903d");

    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Topic getTopic() {
        return topic;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getAddrName() {
        return addrName;
    }

    public void setAddrName(String addrName) {
        this.addrName = addrName;
    }

    public String getSearchText() {
        return searchText;
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }

    public String getSearchCity() {
        return searchCity;
    }

    public void setSearchCity(String searchCity) {
        this.searchCity = searchCity;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getSearchLatitude() {
        return searchLatitude;
    }

    public void setSearchLatitude(double searchLatitude) {
        this.searchLatitude = searchLatitude;
    }

    public double getSearchLongitude() {
        return searchLongitude;
    }

    public void setSearchLongitude(double searchLongitude) {
        this.searchLongitude = searchLongitude;
    }

    public MoreDetail getMoreDetail() {
        return moreDetail;
    }

    public void setMoreDetail(MoreDetail moreDetail) {
        this.moreDetail = moreDetail;
    }
}
