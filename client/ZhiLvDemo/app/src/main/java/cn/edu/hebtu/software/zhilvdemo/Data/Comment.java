package cn.edu.hebtu.software.zhilvdemo.Data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * @ProjectName:    ZhiLv
 * @Description:    java类作用描述
 * @Author:         张璐婷
 * @CreateDate:     2021/1/28 10:54
 * @Version:        1.0
 */
public class Comment implements Parcelable {
	private Integer id;
	private String content;
	private Date time;
	private User user;
	private Integer replyCount;  //回复评论的数量
	private Travels travels;
	private Video video;

	public Comment(){}

    protected Comment(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readInt();
        }
        content = in.readString();
        user = in.readParcelable(User.class.getClassLoader());
        if (in.readByte() == 0) {
            replyCount = null;
        } else {
            replyCount = in.readInt();
        }
        travels = in.readParcelable(Travels.class.getClassLoader());
        video = in.readParcelable(Video.class.getClassLoader());
        time = new Date(in.readLong());
    }

    public static final Creator<Comment> CREATOR = new Creator<Comment>() {
        @Override
        public Comment createFromParcel(Parcel in) {
            return new Comment(in);
        }

        @Override
        public Comment[] newArray(int size) {
            return new Comment[size];
        }
    };

    public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Integer getReplyCount() {
		return replyCount;
	}
	public void setReplyCount(Integer replyCount) {
		this.replyCount = replyCount;
	}
	public Travels getTravels() {
		return travels;
	}
	public void setTravels(Travels travels) {
		this.travels = travels;
	}
	public Video getVideo() {
		return video;
	}
	public void setVideo(Video video) {
		this.video = video;
	}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(id);
        }
        dest.writeString(content);
        dest.writeParcelable(user, flags);
        if (replyCount == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(replyCount);
        }
        dest.writeParcelable(travels, flags);
        dest.writeParcelable(video, flags);
        dest.writeLong(time.getTime());
    }
}
