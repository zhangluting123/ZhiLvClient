package cn.edu.hebtu.software.zhilvdemo.UploadAndDownload;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

import cn.edu.hebtu.software.zhilvdemo.Data.MoreDetail;
import cn.edu.hebtu.software.zhilvdemo.Data.Video;
import cn.edu.hebtu.software.zhilvdemo.Util.DateUtil;
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
 * @CreateDate:     2021/1/26  16:24
 * @Version:        1.0
 */
public class EditVideoTask extends AsyncTask<String, Void, String> {
    private Activity activity;
    private Video video;

    public EditVideoTask(Activity activity, Video video) {
        this.activity = activity;
        this.video = video;
    }

    /**
     * 访问服务器，上传video信息，接收响应并返回
     */
    @Override
    protected String doInBackground(String... strings) {
        if(DetermineConnServer.isConnByHttp(activity)){
            OkHttpClient client = new OkHttpClient();
            //传参
            MultipartBody.Builder builder = new MultipartBody.Builder();
            builder.setType(MultipartBody.ALTERNATIVE);
            if(null != video.getVideoId()) {
                builder.addFormDataPart("videoId", video.getVideoId() + "");
            }
            builder.addFormDataPart("title", video.getTitle())
                    .addFormDataPart("content", video.getContent())
                    .addFormDataPart("location", video.getLocation())
                    .addFormDataPart("userId", video.getUser().getUserId()+"");
            if(null != video.getTopic()){
                builder.addFormDataPart("topicId", video.getTopic().getTopicId()+"");
            }
            if(null != video.getDetail()){
                MoreDetail detail = video.getDetail();
                builder.addFormDataPart("destination", detail.getDestination());
                if(null != detail.getTraffic()) {
                    builder.addFormDataPart("traffic", detail.getTraffic());
                }
                if(null != detail.getBeginDate()) {
                    builder.addFormDataPart("beginDate", DateUtil.getDateStr(detail.getBeginDate()));
                }
                if(null != detail.getDays()){
                    builder.addFormDataPart("days", detail.getDays()+"");
                }
                if(null != detail.getPeople()){
                    builder.addFormDataPart("people", detail.getPeople());
                }
                if(null != detail.getMoney()){
                    builder.addFormDataPart("money", detail.getMoney()+"");
                }
            }
            if(null != video.getPath()){
                builder.addFormDataPart("duration", video.getDuration())
                        .addFormDataPart("size", video.getSize());
                //以二进制流的方式
                MediaType type = MediaType.parse("application/octet-stream");
                //传视频
                File file = new File(video.getPath());
                builder.addPart(Headers.of("Content-Disposition","form-data; name='file';filename=" + file.getName()),
                        RequestBody.create(file, type)
                );

                //以图片形式
                MediaType type2 = MediaType.parse("image/jpeg");
                File file2 = new File(video.getImg());
                builder.addPart(Headers.of("Content-Disposition","form-data; name='file';filename=" + file2.getName()),
                        RequestBody.create(file2,type2));
            }

            RequestBody requestBody = builder.build();
            Request request=new Request.Builder()
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
