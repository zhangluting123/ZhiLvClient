package cn.edu.hebtu.software.zhilvdemo.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;



import com.bumptech.glide.Glide;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import cn.edu.hebtu.software.zhilvdemo.Data.Scene;
import cn.edu.hebtu.software.zhilvdemo.R;
import cn.edu.hebtu.software.zhilvdemo.Setting.MyApplication;
import cn.edu.hebtu.software.zhilvdemo.Util.BaiduMapUtil.DistanceUtil;
import cn.edu.hebtu.software.zhilvdemo.Util.DetermineConnServer;

/**
 * @ProjectName:    ZhiLv
 * @Description:    景点推荐
 * @Author:         张璐婷
 * @CreateDate:     2020/12/23  15:49
 * @Version:        1.0
 */
public class SceneRecommendAdapter extends RecyclerView.Adapter<SceneRecommendAdapter.ViewHolder> {
    private Context context;
    private int itemLayout;
    private List<Scene> mDatas;
    private MyApplication data;

    public SceneRecommendAdapter(Context context, int itemLayout, List<Scene> mDatas) {
        this.context = context;
        this.itemLayout = itemLayout;
        this.mDatas = mDatas;
        data = (MyApplication)context.getApplicationContext();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(itemLayout, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SceneRecommendAdapter.ViewHolder holder, int position) {
        Glide.with(context).load("http://"+data.getIp()+":8080/ZhiLvProject/"+mDatas.get(position).getPath()).into(holder.path);
        holder.place.setText(mDatas.get(position).getTitle());
        holder.address.setText(mDatas.get(position).getAddress());
        holder.far.setText(DistanceUtil.getDistance(data.getLongitude(), data.getLatitude(), mDatas.get(position).getLongitude(), mDatas.get(position).getLatitude()));
        holder.ticket.setText(mDatas.get(position).getTicket());
        holder.openTime.setText(mDatas.get(position).getOpenTime());
    }


    @Override
    public int getItemCount() {
        if(null != mDatas){
            return mDatas.size();
        }else{
            return 0;
        }

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView path;
        TextView place;
        TextView address;
        TextView far;
        TextView ticket;
        TextView openTime;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            path = itemView.findViewById(R.id.iv_scene_path);
            place = itemView.findViewById(R.id.tv_scene_place);
            address = itemView.findViewById(R.id.tv_scene_address);
            far = itemView.findViewById(R.id.tv_scene_far);
            ticket = itemView.findViewById(R.id.tv_scene_ticket);
            openTime = itemView.findViewById(R.id.tv_scene_openTime);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(null != onItemClickListener){
                        if(null != data.getUser()){
                            addViews(getLayoutPosition());
                        }
                        onItemClickListener.onItemClick(getLayoutPosition());
                    }
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


    public void refresh(List<Scene> mDatas){
        this.mDatas = mDatas;
        notifyDataSetChanged();
    }

    /**
     *  @author: 张璐婷
     *  @time: 2021/2/10  15:54
     *  @Description: 添加浏览量
     */
    private void addViews(int position){
        new Thread(){
            @Override
            public void run() {
                if(DetermineConnServer.isConnByHttp(context)){
                    try {
                        String str = "http://"+data.getIp()+":8080/ZhiLvProject/views/add?userId="+data.getUser().getUserId()+"&videoId="+mDatas.get(position).getSceneId();
                        URL url = new URL(str);
                        URLConnection conn = url.openConnection();
                        conn.getInputStream();
                    }catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }
}
