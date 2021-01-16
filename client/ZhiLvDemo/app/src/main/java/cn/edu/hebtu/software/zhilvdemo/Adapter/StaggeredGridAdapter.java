package cn.edu.hebtu.software.zhilvdemo.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import cn.edu.hebtu.software.zhilvdemo.DetailActivity.OtherUserInfoActivity;
import cn.edu.hebtu.software.zhilvdemo.R;


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

    private List<String> mDatas = new ArrayList<>();
    private Context context;

    public StaggeredGridAdapter(List<String> mDatas,  Context context) {
        this.mDatas = mDatas;
        this.context = context;
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
        if(holder instanceof MyViewHolderTravels){
            ((MyViewHolderTravels) holder).travelTitle.setText(mDatas.get(position));
        }else{
            ((MyViewHolderVideo) holder).videoTitle.setText(mDatas.get(position));

//            Bitmap bitmap = getVideoThumbnail("http://localhost:8080/MoJi/video/test-video.mp4");
//            ViewGroup.LayoutParams params = ((MyViewHolderVideo) holder).video.getLayoutParams();
//            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
//            params.height = (int) (bitmap.getHeight()*0.5);
//            ((MyViewHolderVideo) holder).video.setLayoutParams(params);
//            if(null != bitmap) {
//                ((MyViewHolderVideo) holder).video.setImageBitmap(bitmap);
//            }
            ((MyViewHolderVideo) holder).video.setImageResource(R.mipmap.img_video_default);
        }


    }

    @Override
    public int getItemCount() {
        return mDatas.size();
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
                    context.startActivity(intent);
                }
            });
        }
    }

    //复写一个方法，来返回条目类型
    @Override
    public int getItemViewType(int position) {
        if(mDatas.get(position).contains("2")){
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

    public void replaceAll(List<String> mDatas){
        this.mDatas = mDatas;
        notifyItemRangeInserted(0, 3);
    }
}









