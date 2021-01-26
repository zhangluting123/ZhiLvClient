package cn.edu.hebtu.software.zhilvdemo.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import cn.edu.hebtu.software.zhilvdemo.Data.Note;
import cn.edu.hebtu.software.zhilvdemo.Data.Travels;
import cn.edu.hebtu.software.zhilvdemo.Data.Video;
import cn.edu.hebtu.software.zhilvdemo.DetailActivity.OtherUserInfoActivity;
import cn.edu.hebtu.software.zhilvdemo.R;
import cn.edu.hebtu.software.zhilvdemo.Setting.MyApplication;


/**
 * @ProjectName:    ZhiLv
 * @Description:    瀑布流适配器
 * @Author:         张璐婷
 * @CreateDate:     2020/12/21  14:55
 * @Version:        1.0
 */
public class StaggeredGridAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    //定义两个标识，因为有两种类型
    private static final int TYPE_TRAVELS = 0;
    private static final int TYPE_VIDEO = 1;

    private List<Note> mDatas = new ArrayList<>();
    private Context context;
    private MyApplication data;

    public StaggeredGridAdapter(List<Note> mDatas,  Context context) {
        this.mDatas = mDatas;
        this.context = context;
        data = (MyApplication)context.getApplicationContext();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == TYPE_TRAVELS) {
            view = LayoutInflater.from(context).inflate(R.layout.item_staggered_grid_travels, parent, false);
            return new MyViewHolderTravels(view);
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.item_staggered_grid_video, parent, false);
            return new MyViewHolderVideo(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        RequestOptions options = new RequestOptions();
        if(holder instanceof MyViewHolderTravels){
            Travels travels = mDatas.get(position).getTravels();
            ((MyViewHolderTravels) holder).travelTitle.setText(travels.getTitle());
            if(travels.getImgList().size() > 0){
                Glide.with(context).
                        load("http://"+data.getIp()+":8080/ZhiLvProject/"+travels.getImgList().get(0).getPath())
                        .apply(options)
                        .into(((MyViewHolderTravels)holder).travelImg);
            }
            ((MyViewHolderTravels) holder).userName.setText(travels.getUser().getUserName());
            Glide.with(context).
                    load("http://"+data.getIp()+":8080/ZhiLvProject/"+travels.getUser().getUserHead())
                    .apply(options.circleCrop())
                    .into(((MyViewHolderTravels)holder).userHead);
        }else{
            Video video = mDatas.get(position).getVideo();
            ((MyViewHolderVideo) holder).videoTitle.setText(mDatas.get(position).getVideo().getTitle());
            Glide.with(context).
                    load("http://"+data.getIp()+":8080/ZhiLvProject/"+video.getImg())
                    .apply(options)
                    .into(((MyViewHolderVideo)holder).video);
            ((MyViewHolderVideo) holder).userName.setText(video.getUser().getUserName());
            Glide.with(context).
                    load("http://"+data.getIp()+":8080/ZhiLvProject/"+video.getUser().getUserHead())
                    .apply(options.circleCrop())
                    .into(((MyViewHolderVideo)holder).userHead);
        }


    }

    @Override
    public int getItemCount() {
        if(null != mDatas){
            return mDatas.size();
        }else{
            return 0;
        }

    }

    public class MyViewHolderTravels extends RecyclerView.ViewHolder {
        private ImageView travelImg;
        private TextView travelTitle;
        private ImageView userHead;
        private TextView userName;
        public MyViewHolderTravels(@NonNull View itemView) {
            super(itemView);
            travelImg = itemView.findViewById(R.id.stagger_iv_travelImg);
            travelTitle = itemView.findViewById(R.id.stagger_tv_travelTitle);
            userHead = itemView.findViewById(R.id.stagger_iv_userHead);
            userName = itemView.findViewById(R.id.stagger_tv_userName);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(null != onItemClickListener){
                        onItemClickListener.onItemClick(getLayoutPosition());
                    }
                }
            });
            userHead.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, OtherUserInfoActivity.class);
                    intent.putExtra("other", mDatas.get(getLayoutPosition()).getTravels().getUser());
                    context.startActivity(intent);
                }
            });
        }
    }

    public class MyViewHolderVideo extends RecyclerView.ViewHolder{
        private ImageView video;
        private TextView videoTitle;
        private ImageView userHead;
        private TextView userName;
        public MyViewHolderVideo(@NonNull View itemView) {
            super(itemView);
            video = itemView.findViewById(R.id.stagger_iv_videoImg);
            videoTitle = itemView.findViewById(R.id.stagger_tv_videoTitle);
            userHead = itemView.findViewById(R.id.stagger_iv_userHead);
            userName = itemView.findViewById(R.id.stagger_tv_userName);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(null != onItemClickListener){
                        onItemClickListener.onItemClick(getLayoutPosition());
                    }
                }
            });
            userHead.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, OtherUserInfoActivity.class);
                    intent.putExtra("other", mDatas.get(getLayoutPosition()).getVideo().getUser());
                    context.startActivity(intent);
                }
            });
        }
    }

    //复写一个方法，来返回条目类型
    @Override
    public int getItemViewType(int position) {
        if(!mDatas.get(position).isFlag()){
            return TYPE_VIDEO;
        }else{
            return TYPE_TRAVELS;
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

    public void replaceAll(List<Note> mDatas){
        this.mDatas = mDatas;
        notifyDataSetChanged();
//        notifyItemRangeInserted(0, 3);
    }
}









