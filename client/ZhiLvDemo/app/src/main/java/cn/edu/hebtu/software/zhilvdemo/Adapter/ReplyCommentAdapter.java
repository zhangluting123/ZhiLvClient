package cn.edu.hebtu.software.zhilvdemo.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import cn.edu.hebtu.software.zhilvdemo.Data.ReplyComment;
import cn.edu.hebtu.software.zhilvdemo.DetailActivity.OtherUserInfoActivity;
import cn.edu.hebtu.software.zhilvdemo.R;
import cn.edu.hebtu.software.zhilvdemo.Setting.MyApplication;
import cn.edu.hebtu.software.zhilvdemo.Util.DateUtil;


/**
 * @ProjectName:    ZhiLv
 * @Description:    回复消息适配器
 * @Author:         张璐婷
 * @CreateDate:     2021/1/28 11:02
 * @Version:        1.0
 */
public class ReplyCommentAdapter extends BaseAdapter {
    private List<ReplyComment> replyCommentList;
    private Context context;
    private int itemLayoutId;

    private ViewHolder viewHolder;

    private MyApplication data;
    private String ip;

    public ReplyCommentAdapter(List<ReplyComment> replyCommentList, Context context, int itemLayoutId) {
        this.replyCommentList = replyCommentList;
        this.context = context;
        this.itemLayoutId = itemLayoutId;
    }

    @Override
    public int getCount() {
        if(null != replyCommentList){
            return replyCommentList.size();
        }else{
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        if(null != replyCommentList){
            return replyCommentList.get(position);
        }else{
            return null;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        data = (MyApplication)context.getApplicationContext();
        ip = data.getIp();
        if(null == convertView) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(itemLayoutId, null);
            viewHolder.replyContent = convertView.findViewById(R.id.tv_replyContent);
            viewHolder.userName = convertView.findViewById(R.id.user_name);
            viewHolder.replyDate = convertView.findViewById(R.id.tv_replyDate);
            viewHolder.replyHead = convertView.findViewById(R.id.iv_replyHead);
            viewHolder.parent = convertView.findViewById(R.id.rl_parent);
            viewHolder.atSomeBody = convertView.findViewById(R.id.tv_atSomeBody);
            viewHolder.commentContent = convertView.findViewById(R.id.tv_comment_content);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }

        RequestOptions options = new RequestOptions().circleCrop();
        if(replyCommentList != null && replyCommentList.size() > 0){
            if(null != replyCommentList.get(position).getReplyUser().getUserHead()){
                Glide.with(context).load("http://" + ip + ":8080/ZhiLvProject/" + replyCommentList.get(position).getReplyUser().getUserHead()).apply(options).into(viewHolder.replyHead);
            }
            viewHolder.replyContent.setText(replyCommentList.get(position).getReplyContent());
            viewHolder.replyDate.setText(DateUtil.showTime(replyCommentList.get(position).getReplyTime()));
            viewHolder.userName.setText(replyCommentList.get(position).getReplyUser().getUserName());
            if(replyCommentList.get(position).getReplyComment() != null){
                viewHolder.parent.setVisibility(View.VISIBLE);
                viewHolder.atSomeBody.setText(replyCommentList.get(position).getReplyComment().getReplyUser().getUserName());
                viewHolder.commentContent.setText(replyCommentList.get(position).getReplyComment().getReplyContent());
            }else{
                viewHolder.parent.setVisibility(View.GONE);
            }
            viewHolder.replyHead.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, OtherUserInfoActivity.class);
                    intent.putExtra("other",replyCommentList.get(position).getReplyUser());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
                    context.startActivity(intent);
                }
            });
        }

        return convertView;
    }

    final static class ViewHolder{
        private ImageView replyHead;
        private TextView userName;
        private TextView replyContent;
        private TextView replyDate;
        private RelativeLayout parent;
        private TextView atSomeBody;
        private TextView commentContent;
    }

    public void flush(List<ReplyComment> replyComments){
        this.replyCommentList = replyComments;
        notifyDataSetChanged();
    }
}
