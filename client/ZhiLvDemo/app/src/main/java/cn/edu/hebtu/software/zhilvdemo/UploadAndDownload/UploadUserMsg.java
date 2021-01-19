package cn.edu.hebtu.software.zhilvdemo.UploadAndDownload;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;


import cn.edu.hebtu.software.zhilvdemo.Data.User;
import cn.edu.hebtu.software.zhilvdemo.Setting.MyApplication;
import cn.edu.hebtu.software.zhilvdemo.Util.DateUtil;
import cn.edu.hebtu.software.zhilvdemo.Util.DetermineConnServer;
import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.mob.tools.utils.DeviceHelper.getApplication;

public class UploadUserMsg extends AsyncTask<String, Void, String> {
    private Context context;
    private User user;

    public UploadUserMsg(Context context, User user) {
        this.context = context;
        this.user = user;
    }

    /**
     * 访问服务器，上传User信息，接收响应并返回
     */
    @Override
    protected String doInBackground(String... strings) {
        //判断是否连接到服务器
        boolean b = DetermineConnServer.isConnByHttp(context);
        if(b){
            OkHttpClient client = new OkHttpClient();
            //传参
            MultipartBody.Builder builder = new MultipartBody.Builder();
            builder.setType(MultipartBody.ALTERNATIVE);
            builder.addFormDataPart("userId",user.getUserId()+"");
            if(null != user.getPhone()){
                builder.addFormDataPart("phone", user.getPhone());
            }
            if(null != user.getEmail()){
                builder.addFormDataPart("email", user.getEmail());
            }
            if(null != user.getPassword()){
                builder.addFormDataPart("password", user.getPassword());
            }
            if(null != user.getUserHead()){
                //更改头像
                File file = new File(user.getUserHead());
                MediaType type = MediaType.parse("image/jpeg");
                RequestBody headBody = RequestBody.create(file, type);
                builder.addFormDataPart("file", file.getName(), headBody);
            }
            if(null != user.getUserName()){
                builder.addFormDataPart("userName", user.getUserName());
                builder.addFormDataPart("birth", DateUtil.getDateStr(user.getBirth()));
                builder.addFormDataPart("signature", user.getSignature());
                builder.addFormDataPart("sex", user.getSex());
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
            }finally {
                if(null != response){
                    response.close();
                }
            }
            return null;
        }else{
            return "ERRORNET";
        }

    }

    @Override
    protected void onPostExecute(String s) {
        if("OK".equals(s)){
            //更新全局变量
            final MyApplication data = (MyApplication) getApplication();
            data.getUser().setUserId(user.getUserId());
            if(null != user.getPhone()){
                data.getUser().setPhone(user.getPhone());
            }
            if(null != user.getEmail()){
                data.getUser().setEmail(user.getEmail());
            }
            if(null != user.getPassword()){
                data.getUser().setPassword(user.getPassword());
            }
            if(null != user.getUserHead()){
                String n = new File(user.getUserHead()).getName();
                user.setUserHead("avatar/" + n);
                data.getUser().setUserHead(user.getUserHead());
            }
            if(null != user.getUserName()){
                data.getUser().setUserName(user.getUserName());
                data.getUser().setSignature(user.getSignature());
                data.getUser().setSex(user.getSex());
                data.getUser().setBirth(user.getBirth());
            }
            Toast.makeText(context, "修改成功！", Toast.LENGTH_SHORT).show();
        }else if("ERROR".equals(s)){
            Toast.makeText(context, "更新失败！", Toast.LENGTH_SHORT).show();
        }else if("ERRORNET".equals(s)){
            Toast.makeText(context, "未连接到服务器", Toast.LENGTH_SHORT).show();
        }
    }
}
