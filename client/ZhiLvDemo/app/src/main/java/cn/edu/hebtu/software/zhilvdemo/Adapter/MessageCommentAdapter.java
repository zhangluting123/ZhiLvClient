package cn.edu.hebtu.software.zhilvdemo.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import cn.edu.hebtu.software.zhilvdemo.R;

/**
 * @ProjectName:    ZhiLv
 * @Description:    
 * @Author:         张璐婷
 * @CreateDate:     2020/12/23  22:27
 * @Version:        1.0
 */
public class MessageCommentAdapter extends RecyclerView.Adapter<MessageCommentAdapter.ViewHolder> {
    private List<String> myComments = new ArrayList<>();
    private int itemLayoutId;
    private Context context;

    public MessageCommentAdapter(List<String> myComments, int itemLayoutId, Context context) {
        this.myComments = myComments;
        this.itemLayoutId = itemLayoutId;
        this.context = context;
    }


    @NonNull
    @Override
    public MessageCommentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(itemLayoutId, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageCommentAdapter.ViewHolder holder, int position) {
        holder.afterUserName.setText(myComments.get(position));

        //        if(myComments.get(position).getReadFlag() == 0){//未读
//            viewHolder.readFlag.setImageResource(R.mipmap.unread_flag);
//        }else{
//            viewHolder.readFlag.setImageResource(0);
//        }
//        RequestOptions options = new RequestOptions().circleCrop();
//        if(myComments.get(position).getCrFlag() == 'C') {//我的动态评论信息
//            Glide.with(context).load("http://" + ip + ":8080/MoJi/" + myComments.get(position).getComment().getUser().getUserHeadImg()).apply(options).into(viewHolder.afterHead);
//            viewHolder.afterUserName.setText(myComments.get(position).getComment().getUser().getUserName());
//            viewHolder.afterContent.setText(myComments.get(position).getComment().getCommentContent());
//            viewHolder.afterDate.setText(myComments.get(position).getComment().getCommentTime());
//            //动态可见，评论不可见
//            viewHolder.beforeNote.setVisibility(View.VISIBLE);
//            viewHolder.beforeComment.setVisibility(View.GONE);
//            viewHolder.noteTitle.setText(myComments.get(position).getComment().getNote().getTitle());
//        }else if(myComments.get(position).getCrFlag() == 'R'){//我的评论回复信息
//            Glide.with(context).load("http://" + ip + ":8080/MoJi/" + myComments.get(position).getReplyComment().getReplyUser().getUserHeadImg()).apply(options).into(viewHolder.afterHead);
//            viewHolder.afterUserName.setText(myComments.get(position).getReplyComment().getReplyUser().getUserName());
//            viewHolder.afterContent.setText(myComments.get(position).getReplyComment().getReplyContent());
//            viewHolder.afterDate.setText(myComments.get(position).getReplyComment().getReplyTime());
//            //动态不可见，评论可见
//            viewHolder.beforeNote.setVisibility(View.GONE);
//            viewHolder.beforeComment.setVisibility(View.VISIBLE);
//            if(null != myComments.get(position).getReplyComment().getReplyComment()) {//有父元素
//                viewHolder.beforeUserName.setText(myComments.get(position).getReplyComment().getReplyComment().getReplyUser().getUserName());
//                viewHolder.beforeContent.setText(myComments.get(position).getReplyComment().getReplyComment().getReplyContent());
//            }else{
//                viewHolder.beforeUserName.setText(myComments.get(position).getReplyComment().getComment().getUser().getUserName());
//                viewHolder.beforeContent.setText(myComments.get(position).getReplyComment().getComment().getCommentContent());
//            }
//        }

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
        private LinearLayout beforeNote;
        private TextView noteTitle;
        private RelativeLayout beforeComment;
        private TextView beforeUserName;
        private TextView beforeContent;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            readFlag = itemView.findViewById(R.id.iv_read_flag);
            afterHead = itemView.findViewById(R.id.iv_after_Head);
            afterUserName = itemView.findViewById(R.id.tv_after_userName);
            afterContent = itemView.findViewById(R.id.tv_after_content);
            afterDate = itemView.findViewById(R.id.tv_after_Date);
            beforeNote = itemView.findViewById(R.id.ll_before_note);
            noteTitle = itemView.findViewById(R.id.tv_noteTitle);
            beforeComment = itemView.findViewById(R.id.rl_before_comment);
            beforeUserName = itemView.findViewById(R.id.tv_before_userName);
            beforeContent = itemView.findViewById(R.id.tv_before_content);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(null != onItemClickListener){
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
}
