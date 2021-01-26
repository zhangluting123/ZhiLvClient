package cn.edu.hebtu.software.zhilvdemo.UploadAndDownload;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.List;

import cn.edu.hebtu.software.zhilvdemo.Data.MoreDetail;
import cn.edu.hebtu.software.zhilvdemo.Data.Travels;
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
 * @Description:    上传游记
 * @Author:         张璐婷
 * @CreateDate:     2021/1/22 14:57
 * @Version:        1.0
 */
public class UploadTravelsTask extends AsyncTask<String, Void, String> {
    private Activity activity;
    private Travels travels;

    public UploadTravelsTask(Activity activity, Travels travels) {
        this.activity = activity;
        this.travels = travels;
    }

    /**
     * 访问服务器，上传note信息，接收响应并返回
     */
    @Override
    protected String doInBackground(String... strings) {
        if(DetermineConnServer.isConnByHttp(activity)){
            OkHttpClient client = new OkHttpClient();
            //传参
            MultipartBody.Builder builder = new MultipartBody.Builder();
            builder.setType(MultipartBody.ALTERNATIVE);
            if(null != travels.getTravelsId()) {
                builder.addFormDataPart("travelsId", travels.getTravelsId() + "");
            }
            builder.addFormDataPart("title", travels.getTitle())
                        .addFormDataPart("route", travels.getRoute())
                        .addFormDataPart("scene", travels.getScene())
                        .addFormDataPart("ticket", travels.getTicket())
                        .addFormDataPart("hotel", travels.getHotel())
                        .addFormDataPart("tips", travels.getTips())
                        .addFormDataPart("location", travels.getLocation())
                        .addFormDataPart("userId", travels.getUser().getUserId()+"");
            if(null != travels.getTopic()){
                builder.addFormDataPart("topicId", travels.getTopic().getTopicId()+"");
            }
            if(null != travels.getDetail()){
                MoreDetail detail = travels.getDetail();
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
            }else{
                builder.addFormDataPart("destination", "北京");
            }

            //传图片
            MediaType type = MediaType.parse("image/jpeg");
            List<String> imgList = travels.getImgPathList();
            RequestBody[] body = new RequestBody[imgList.size()];
            for(int i = 0; i < imgList.size(); i++){
                if(imgList.get(i).contains("http")){
                    builder.addFormDataPart("httpImg", imgList.get(i));
                }else{
                    File file = new File(imgList.get(i));
                    body[i] = RequestBody.create(file, type);
                    builder.addPart(Headers.of(
                            "Content-Disposition",
                            "form-data; name='file';filename=" + file.getName()),
                            body[i]
                    );
                }
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
            Toast.makeText(activity,"发布成功", Toast.LENGTH_SHORT).show();
            activity.finish();
        }else if("ERROR".equals(s)){
            Toast.makeText(activity,"发布失败", Toast.LENGTH_SHORT).show();
        }
    }
}
