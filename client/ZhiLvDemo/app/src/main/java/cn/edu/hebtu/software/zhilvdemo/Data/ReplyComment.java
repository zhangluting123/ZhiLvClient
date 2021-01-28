package cn.edu.hebtu.software.zhilvdemo.Data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * @ProjectName:    ZhiLv
 * @Description:    java类作用描述
 * @Author:         张璐婷
 * @CreateDate:     2021/1/28 10:56
 * @Version:        1.0
 */
public class ReplyComment implements Parcelable {
	private Integer replyId;
	private Comment comment;
	private User replyUser;
	private String replyContent;
	private Date replyTime;
	private ReplyComment replyComment;

	public ReplyComment(){}

    protected ReplyComment(Parcel in) {
        if (in.readByte() == 0) {
            replyId = null;
        } else {
            replyId = in.readInt();
        }
        comment = in.readParcelable(Comment.class.getClassLoader());
        replyUser = in.readParcelable(User.class.getClassLoader());
        replyContent = in.readString();
        replyComment = in.readParcelable(ReplyComment.class.getClassLoader());
        replyTime = new Date(in.readLong());
    }

    public static final Creator<ReplyComment> CREATOR = new Creator<ReplyComment>() {
        @Override
        public ReplyComment createFromParcel(Parcel in) {
            return new ReplyComment(in);
        }

        @Override
        public ReplyComment[] newArray(int size) {
            return new ReplyComment[size];
        }
    };

    public Integer getReplyId() {
		return replyId;
	}
	public void setReplyId(Integer replyId) {
		this.replyId = replyId;
	}
	public Comment getComment() {
		return comment;
	}
	public void setComment(Comment comment) {
		this.comment = comment;
	}
	public User getReplyUser() {
		return replyUser;
	}
	public void setReplyUser(User replyUser) {
		this.replyUser = replyUser;
	}
	public String getReplyContent() {
		return replyContent;
	}
	public void setReplyContent(String replyContent) {
		this.replyContent = replyContent;
	}
	public Date getReplyTime() {
		return replyTime;
	}
	public void setReplyTime(Date replyTime) {
		this.replyTime = replyTime;
	}
	public ReplyComment getReplyComment() {
		return replyComment;
	}

	public void setReplyComment(ReplyComment replyComment) {
		this.replyComment = replyComment;
	}


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (replyId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(replyId);
        }
        dest.writeParcelable(comment, flags);
        dest.writeParcelable(replyUser, flags);
        dest.writeString(replyContent);
        dest.writeParcelable(replyComment, flags);
        dest.writeLong(replyTime.getTime());
    }
}
