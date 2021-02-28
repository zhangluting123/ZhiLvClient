package cn.edu.hebtu.software.zhilvdemo.Util;

import android.content.Context;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import cn.edu.hebtu.software.zhilvdemo.R;

/**
 * @ProjectName:    MoJi
 * @Description:    判断是否连接服务器
 * @Author:         张璐婷
 * @CreateDate:     2019/12/6 10:05
 * @Version:        1.0
 */
public class DetermineConnServer {
    /**
     * 判断是否连接服务器
     * @return
     */
    public static boolean isConnByHttp(Context context){
        boolean isConn = false;
        URL url;
        HttpURLConnection conn = null;
        try {
            url = new URL("http://"+ context.getResources().getString(R.string.internet_ip)+":8080/ZhiLvProject/");
            conn = (HttpURLConnection)url.openConnection();
            conn.setConnectTimeout(2000);//检测时长

            if(conn.getResponseCode()==200){
                isConn = true;
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }finally{
            conn.disconnect();
        }
        return isConn;
    }
}
