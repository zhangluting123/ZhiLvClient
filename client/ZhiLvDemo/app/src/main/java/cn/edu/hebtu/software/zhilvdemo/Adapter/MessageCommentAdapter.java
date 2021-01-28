package cn.edu.hebtu.software.zhilvdemo.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import cn.edu.hebtu.software.zhilvdemo.Data.MailMyComment;
import cn.edu.hebtu.software.zhilvdemo.R;
import cn.edu.hebtu.software.zhilvdemo.Setting.MyApplication;
import cn.edu.hebtu.software.zhilvdemo.Util.DateUtil;
import cn.edu.hebtu.software.zhilvdemo.Util.DetermineConnServer;

/**
 * @ProjectName:    ZhiLv
 * @Description:    
 * @Author:         张璐婷
 * @CreateDate:     2020/12/23  22:27
 * @Version:        1.0
 */
public class MessageCommentAdapter extends RecyclerView.Adapter<MessageCommentAdapter.ViewHolder> {
    private List<MailMyComment> myComments = new ArrayList<>();
    private int itemLayoutId;
    private Context context;
    private MyApplication data;
    private int pos;
    @SuppressLint("HandlerLeak")
    private final Handler mHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case 1001:
                    Toast.makeText(context, (CharSequence)msg.obj, Toast.LENGTH_SHORT).show();
                    break;
                case 1003:
                    myComments.get(pos).setReadFlag(1);
                    refresh(myComments);
                    pos = -1;
                    break;
                case 1004:
                    Toast.makeText(context, "删除成功", Toast.LENGTH_SHORT).show();
                    myComments.remove(pos);
                    refresh(myComments);
                    pos = -1;
                    break;
            }
        }
    };

    public MessageCommentAdapter(List<MailMyComment> myComments, int itemLayoutId, Context context) {
        this.myComments = myComments;
        this.itemLayoutId = itemLayoutId;
        this.context = context;
        data = (MyApplication)context.getApplicationContext();
        pos = -1;
    }

    @NonNull
    @Override
    public MessageCommentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(itemLayoutId, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageCommentAdapter.ViewHolder holder, int position) {
        if(myComments.get(position).getReadFlag() == 0){//未读
            holder.readFlag.setImageResource(R.mipmap.unread_flag);
        }else{
            holder.readFlag.setImageResource(0);
        }
        RequestOptions options = new RequestOptions().circleCrop();
        if(myComments.get(position).getCrFlag() == 'C') {//我的动态评论信息
            Glide.with(context).load("http://" + data.getIp() + ":8080/ZhiLvProject/" + myComments.get(position).getComment().getUser().getUserHead()).apply(options).into(holder.afterHead);
            holder.afterUserName.setText(myComments.get(position).getComment().getUser().getUserName());
            holder.afterContent.setText(myComments.get(position).getComment().getContent());
            holder.afterDate.setText(DateUtil.showTime(myComments.get(position).getComment().getTime()));
        }else if(myComments.get(position).getCrFlag() == 'R'){//我的评论回复信息
            Glide.with(context).load("http://" + data.getIp() + ":8080/ZhiLvProject/" + myComments.get(position).getReplyComment().getReplyUser().getUserHead()).apply(options).into(holder.afterHead);
            holder.afterUserName.setText(myComments.get(position).getReplyComment().getReplyUser().getUserName());
            holder.afterContent.setText(myComments.get(position).getReplyComment().getReplyContent());
            holder.afterDate.setText(DateUtil.showTime(myComments.get(position).getReplyComment().getReplyTime()));
        }
    }

    @Override
    public int getItemCount() {
        if(null != myComments) {
            return myComments.size();
        }else{
            return 0;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView readFlag;
        private ImageView afterHead;
        private TextView afterUserName;
        private TextView afterContent;
        private TextView afterDate;
        private TextView delete;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            readFlag = itemView.findViewById(R.id.iv_read_flag);
            afterHead = itemView.findViewById(R.id.iv_after_Head);
            afterUserName = itemView.findViewById(R.id.tv_after_userName);
            afterContent = itemView.findViewById(R.id.tv_after_content);
            afterDate = itemView.findViewById(R.id.tv_after_Date);
            delete = itemView.findViewById(R.id.tv_delete);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(null != onItemClickListener){
                        onItemClickListener.onItemClick(getLayoutPosition());
                    }
                }
            });
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pos = getLayoutPosition();
                    delete(myComments.get(getLayoutPosition()).getId());
                }
            });
            afterContent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(myComments.get(getLayoutPosition()).getReadFlag() == 0) {
                        setRead(myComments.get(getLayoutPosition()).getId());
                    }
                    pos = getLayoutPosition();
                }
            });
        }
    }


    //==============设置每一项的点击事件=========================

    //定义接口
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    private OnItemClickListener onItemClickListener;

    //写一个公共的方法
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public void refresh(List<MailMyComment> mailMyComments){
        this.myComments = mailMyComments;
        notifyDataSetChanged();
    }


    /**
     *  @author: 张璐婷
     *  @time: 2021/1/28  15:32
     *  @Description: 删除消息
     */
    private void delete(Integer myCommentId){
        new Thread(){
            @Override
            public void run() {
                if(DetermineConnServer.isConnByHttp(context)){
                    try {
                        URL url = new URL("http://"+ data.getIp() +":8080/ZhiLvProject/mailmycomment/delete?myCommentId=" + myCommentId);
                        URLConnection conn = url.openConnection();
                        InputStream in = conn.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(in,"utf-8"));
                        if(reader.readLine().equals("OK")){
                            Message msg = Message.obtain();
                            msg.what = 1004;
                            mHandler.sendMessage(msg);
                        }
                        reader.close();
                        in.close();
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else{
                    Message msg = Message.obtain();
                    msg.what = 1001;
                    msg.obj = "未连接到服务器";
                    mHandler.sendMessage(msg);
                }
            }
        }.start();
    }

    /**
     *  @author: 张璐婷
     *  @time: 2021/1/28  15:29
     *  @Description: 设置消息为已读
     */
    private void setRead(Integer myCommentId){
        new Thread(){
            @Override
            public void run() {
                if(DetermineConnServer.isConnByHttp(context)){
                    try {
                        URL url = new URL("http://"+ data.getIp() +":8080/ZhiLvProject/mailmycomment/update?myCommentId=" + myCommentId);
                        URLConnection conn = url.openConnection();
                        InputStream in = conn.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(in,"utf-8"));
                        if(reader.readLine().equals("OK")){
                            Message msg = Message.obtain();
                            msg.what = 1003;
                            mHandler.sendMessage(msg);
                        }
                        reader.close();
                        in.close();
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else{
                    Message msg = Message.obtain();
                    msg.what = 1001;
                    msg.obj = "未连接到服务器";
                    mHandler.sendMessage(msg);
                }
            }
        }.start();
    }
}
