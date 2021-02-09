package cn.edu.hebtu.software.zhilvdemo.Util.BaiduMapUtil;

import android.util.Log;

import com.baidu.mapapi.model.LatLng;

/**
 * @ProjectName:    ZhiLv
 * @Description:    
 * @Author:         张璐婷
 * @CreateDate:     2021/2/8  17:47
 * @Version:        1.0
 */
public class DistanceUtil {
    /**
     *  @author: 张璐婷
     *  @time: 2021/2/8  15:32
     *  @Description: 获得距离
     */
    public static String getDistance(double longitude1, double latitude1, double longitude2, double latitude2) {
        double distance = com.baidu.mapapi.utils.DistanceUtil.getDistance(new LatLng(latitude1,longitude1),new LatLng(latitude2, longitude2));
        int dkm = (int)distance/1000;
        String  d = null;
        if(dkm > 1){
            d = dkm + "km";
        }else{
            d = (int)distance +"m";
        }
        return "当前距离："+d + "";
    }

}
