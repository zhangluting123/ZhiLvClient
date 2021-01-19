package cn.edu.hebtu.software.zhilvdemo.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import cn.edu.hebtu.software.zhilvdemo.R;

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
    private List<String> mDatas;

    public SceneRecommendAdapter(Context context, int itemLayout, List<String> mDatas) {
        this.context = context;
        this.itemLayout = itemLayout;
        this.mDatas = mDatas;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(itemLayout, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SceneRecommendAdapter.ViewHolder holder, int position) {
        //设置位置
        if(position % 2 == 0){
            holder.left.setVisibility(View.VISIBLE);
            holder.right.setVisibility(View.GONE);
//            Glide.with(context).load("http://"+ip+":8080/MoJi/"+sceneList.get(position).getPath()).into(scene);
            holder.place.setText(mDatas.get(position));
//            holder.content.setText();

        }else{
            holder.left.setVisibility(View.GONE);
            holder.right.setVisibility(View.VISIBLE);
//            Glide.with(context).load("http://"+ip+":8080/MoJi/"+sceneList.get(position).getPath()).into(scene2);
            holder.place2.setText(mDatas.get(position));
//            holder.content2.setText();
        }

        holder.love.setImageResource(R.drawable.good_noselected);
//        holder.loveCount.setText(NumStrUtil.getNumStr(sceneList.get(position).getLike()));

    }


    @Override
    public int getItemCount() {
        if(null != mDatas){
            return mDatas.size();
        }else{
            return 0;
        }

    }


    public class ViewHolder extends  RecyclerView.ViewHolder{
        RelativeLayout left;
        RelativeLayout right;
        ImageView scene;
        TextView place;
        TextView content;
        ImageView share;
        TextView loveCount;
        ImageView love;
        ImageView comment;
        ImageView scene2;
        TextView place2 ;
        TextView content2;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            left= itemView.findViewById(R.id.left);
            right= itemView.findViewById(R.id.right);
            scene= itemView.findViewById(R.id.iv_scene);
            place = itemView.findViewById(R.id.tv_scene_place);
            content= itemView.findViewById(R.id.tv_scene_content);
            share = itemView.findViewById(R.id.iv_share_scene);
            loveCount= itemView.findViewById(R.id.tv_love_count);
            love = itemView.findViewById(R.id.iv_love);
            comment = itemView.findViewById(R.id.iv_comment);
            scene2= itemView.findViewById(R.id.iv_scene2);
            place2 = itemView.findViewById(R.id.tv_scene_place_2);
            content2 = itemView.findViewById(R.id.tv_scene_content2);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(null != onItemClickListener){
                        onItemClickListener.onItemClick(getLayoutPosition());
                    }
                }
            });
            share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    shareScene(getLayoutPosition());
                }
            });

            love.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Scene scene = sceneList.get(position);
                    if (love.getTag().equals("noselected")) {
                        love.setTag("selected");
                        love.setImageResource(R.drawable.good_selected);
//                        scene.setLike(scene.getLike()+1);
//                        loveCount.setText(NumStrUtil.getNumStr(scene.getLike()));
//                        changeLove(true,scene.getId());
                    }else{
                        love.setTag("noselected");
                        love.setImageResource(R.drawable.good_noselected);
//                        scene.setLike(scene.getLike()-1);
//                        loveCount.setText(NumStrUtil.getNumStr(scene.getLike()));
//                        changeLove(false,scene.getId());
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

    /**
     *  @author: 张璐婷
     *  @time: 2020/12/23  16:07
     *  @Description: 分享景点
     */
    private void shareScene(int position) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//        String path  = "http://"+ip+":8080/MoJi/"+sceneList.get(position).getPath();
//        shareIntent.putExtra(Intent.EXTRA_TEXT,"MoJi景点推荐: "+sceneList.get(position).getPlace()+"\n"
//                +"简介："+sceneList.get(position).getContent()+"\n"
//                +"图片链接："+path);
        context.startActivity(shareIntent);
    }

    public void replaceAll(List<String> mDatas){
        this.mDatas = mDatas;
        notifyItemRangeInserted(0, 3);
    }
}
