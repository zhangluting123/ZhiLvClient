package cn.edu.hebtu.software.zhilvdemo.Data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @ProjectName:    ZhiLv
 * @Description:    java类作用描述
 * @Author:         张璐婷
 * @CreateDate:     2021/1/28 10:55
 * @Version:        1.0
 */
public class MailMyComment implements Parcelable {
	private Integer id;
	private User user;
	private Comment comment;
	private ReplyComment replyComment;
	private Character crFlag;
	private Integer readFlag;

    protected MailMyComment(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readInt();
        }
        user = in.readParcelable(User.class.getClassLoader());
        comment = in.readParcelable(Comment.class.getClassLoader());
        int tmpCrFlag = in.readInt();
        crFlag = tmpCrFlag != Integer.MAX_VALUE ? (char) tmpCrFlag : null;
        if (in.readByte() == 0) {
            readFlag = null;
        } else {
            readFlag = in.readInt();
        }
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(id);
        }
        dest.writeParcelable(user, flags);
        dest.writeParcelable(comment, flags);
        dest.writeInt(crFlag != null ? (int) crFlag : Integer.MAX_VALUE);
        if (readFlag == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(readFlag);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MailMyComment> CREATOR = new Creator<MailMyComment>() {
        @Override
        public MailMyComment createFromParcel(Parcel in) {
            return new MailMyComment(in);
        }

        @Override
        public MailMyComment[] newArray(int size) {
            return new MailMyComment[size];
        }
    };

    public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Comment getComment() {
		return comment;
	}

	public void setComment(Comment comment) {
		this.comment = comment;
	}

	public ReplyComment getReplyComment() {
		return replyComment;
	}

	public void setReplyComment(ReplyComment replyComment) {
		this.replyComment = replyComment;
	}

	public Character getCrFlag() {
		return crFlag;
	}

	public void setCrFlag(Character crFlag) {
		this.crFlag = crFlag;
	}

	public Integer getReadFlag() {
		return readFlag;
	}

	public void setReadFlag(Integer readFlag) {
		this.readFlag = readFlag;
	}
	
	
}
