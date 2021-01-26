package cn.edu.hebtu.software.zhilvdemo.Data;

import android.os.Parcel;
import android.os.Parcelable;

import java.sql.Time;
import java.util.Date;

/**
 * @ProjectName:    ZhiLv
 * @Description:    video实体类
 * @Author:         张璐婷
 * @CreateDate:     2021/1/1  11:03
 * @Version:        1.0
 */
public class Video implements Parcelable {
    private Integer videoId;
    private String path;
    private String img;
    private String title;
    private String content;
    private Topic topic;
    private String location;
    private String duration;
    private String size;
    private Date uploadTime;
    private MoreDetail detail;
    private User user;

    public Video(){}

    protected Video(Parcel in) {
        if (in.readByte() == 0) {
            videoId = null;
        } else {
            videoId = in.readInt();
        }
        path = in.readString();
        img = in.readString();
        title = in.readString();
        content = in.readString();
        topic = in.readParcelable(Topic.class.getClassLoader());
        location = in.readString();
        size = in.readString();
        detail = in.readParcelable(MoreDetail.class.getClassLoader());
        user = in.readParcelable(User.class.getClassLoader());
        duration = in.readString();
        uploadTime = new Date(in.readLong());
    }

    public static final Creator<Video> CREATOR = new Creator<Video>() {
        @Override
        public Video createFromParcel(Parcel in) {
            return new Video(in);
        }

        @Override
        public Video[] newArray(int size) {
            return new Video[size];
        }
    };

    public Integer getVideoId() {
        return videoId;
    }
    public void setVideoId(Integer videoId) {
        this.videoId = videoId;
    }
    public String getPath() {
        return path;
    }
    public void setPath(String path) {
        this.path = path;
    }
    public String getImg() {
        return img;
    }
    public void setImg(String img) {
        this.img = img;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public Topic getTopic() {
        return topic;
    }
    public void setTopic(Topic topic) {
        this.topic = topic;
    }
    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
    }
    public String getDuration() {
        return duration;
    }
    public void setDuration(String duration) {
        this.duration = duration;
    }
    public String getSize() {
        return size;
    }
    public void setSize(String size) {
        this.size = size;
    }
    public Date getUploadTime() {
        return uploadTime;
    }
    public void setUploadTime(Date uploadTime) {
        this.uploadTime = uploadTime;
    }
    public MoreDetail getDetail() {
        return detail;
    }
    public void setDetail(MoreDetail detail) {
        this.detail = detail;
    }
    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (videoId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(videoId);
        }
        dest.writeString(path);
        dest.writeString(img);
        dest.writeString(title);
        dest.writeString(content);
        dest.writeParcelable(topic, flags);
        dest.writeString(location);
        dest.writeString(size);
        dest.writeParcelable(detail, flags);
        dest.writeParcelable(user, flags);
        dest.writeLong(uploadTime.getTime());
        dest.writeString(duration);
    }
}
