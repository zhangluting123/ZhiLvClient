package cn.edu.hebtu.software.zhilvdemo.Util;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * @ProjectName:    ZhiLv
 * @Description:    
 * @Author:         张璐婷
 * @CreateDate:     2020/12/22  15:19
 * @Version:        1.0
 */
public class AssetsUtil {

    /**
     * 将assets资产目录下的文件拷贝到系统目录下
     */
    public static void copyAssetsDB(Context context) {
        final File file = new File(context.getFilesDir(),"test-video.mp4");//getFilesDir()方法用于获取/data/data//files目录
        Log.e("Assets文件路径---->",context.getFilesDir()+"");
        if(file.exists()){//文件存在了就不需要拷贝了
            Log.e("Assets","数据库文件已经存在,不需要再拷贝");
            return;
        }
        new Thread(){
            public void run() {
                Log.e("Assets","进行数据库文件拷贝");
                try {
                    //获取资产目录管理器
                    AssetManager assetManager = context.getAssets();
                    InputStream is = assetManager.open("test-video.mp4");//输入流
                    FileOutputStream fos = new FileOutputStream(file);//输出流
                    byte[] buffer = new byte[1024];
                    int len = 0;
                    while((len=is.read(buffer))!=-1){
                        fos.write(buffer,0,len);
                    }
                    fos.close();
                    is.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            };
        }.start();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}

