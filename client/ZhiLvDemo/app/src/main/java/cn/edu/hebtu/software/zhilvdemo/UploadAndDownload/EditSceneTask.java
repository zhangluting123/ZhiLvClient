package cn.edu.hebtu.software.zhilvdemo.UploadAndDownload;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.List;

import cn.edu.hebtu.software.zhilvdemo.Data.Scene;
import cn.edu.hebtu.software.zhilvdemo.Util.DetermineConnServer;
import okhttp3.Call;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * @ProjectName:    ZhiLv
 * @Description:    
 * @Author:         张璐婷
 * @CreateDate:     2021/2/8  16:17
 * @Version:        1.0
 */
public class EditSceneTask extends AsyncTask<String, Void, String> {
    private Activity activity;
    private Scene scene;
    private Integer userId;

    public EditSceneTask(Activity activity, Scene scene, Integer userId) {
        this.activity = activity;
        this.scene = scene;
        this.userId = userId;
    }

    @Override
    protected String doInBackground(String... strings) {
        if(DetermineConnServer.isConnByHttp(activity)){
            OkHttpClient client = new OkHttpClient();
            //传参
            MultipartBody.Builder builder = new MultipartBody.Builder();
            builder.setType(MultipartBody.ALTERNATIVE);
            builder.addFormDataPart("sceneId", scene.getSceneId()+"")
                    .addFormDataPart("title", scene.getTitle())
                    .addFormDataPart("content", scene.getContent())
                    .addFormDataPart("rule", scene.getRule())
                    .addFormDataPart("openTime", scene.getOpenTime())
                    .addFormDataPart("traffic", scene.getTraffic())
                    .addFormDataPart("ticket", scene.getTicket())
                    .addFormDataPart("costTime", scene.getCostTime())
                    .addFormDataPart("phone", scene.getPhone())
                    .addFormDataPart("website", scene.getWebsite())
                    .addFormDataPart("userId", userId+"");

            //更改图片
            if(null != scene.getPath()) {
                File file = new File(scene.getPath());
                MediaType type = MediaType.parse("image/jpeg");
                RequestBody headBody = RequestBody.create(file, type);
                builder.addFormDataPart("file", file.getName(), headBody);
            }

            RequestBody requestBody = builder.build();
            Request request = new Request.Builder()
                    .url(strings[0])
                    .post(requestBody)
                    .build();
            Call call = client.newCall(request);
            Response response = null;
            try {
                response = call.execute();
                return response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                response.close();
            }
            return null;
        }else{
            return "ERRORNET";
        }
    }

    @Override
    protected void onPostExecute(String s) {
        if("ERRORNET".equals(s)){
            Toast.makeText(activity, "未连接到服务器", Toast.LENGTH_SHORT).show();
        }else if("OK".equals(s)){
            Toast.makeText(activity,"编辑成功", Toast.LENGTH_SHORT).show();
            activity.finish();
        }else if("ERROR".equals(s)){
            Toast.makeText(activity,"编辑失败", Toast.LENGTH_SHORT).show();
        }
    }
}
