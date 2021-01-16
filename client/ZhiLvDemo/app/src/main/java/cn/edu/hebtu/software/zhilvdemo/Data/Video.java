package cn.edu.hebtu.software.zhilvdemo.Data;

import android.os.Parcel;
import android.os.Parcelable;

import java.sql.Time;

/**
 * @ProjectName:    ZhiLv
 * @Description:    video实体类
 * @Author:         张璐婷
 * @CreateDate:     2021/1/1  11:03
 * @Version:        1.0
 */
public class Video implements Parcelable {
    private String videoId;//id
    private String path;//路径
    private String title;//标题
    private String content;//内容
    private String duration;//时长
    private String size;//大小
    private String uploadTime;//上传时间

//    private User user;//用户信息

    public Video() {
    }

    protected Video(Parcel in) {
        videoId = in.readString();
        path = in.readString();
        title = in.readString();
        content = in.readString();
        duration = in.readString();
        size = in.readString();
        uploadTime = in.readString();
//        user = in.readParcelable(User.class.getClassLoader());
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

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
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

    public String getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(String uploadTime) {
        this.uploadTime = uploadTime;
    }



//    public User getUser() {
//        return user;
//    }
//
//    public void setUser(User user) {
//        this.user = user;
//    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(videoId);
        dest.writeString(path);
        dest.writeString(title);
        dest.writeString(content);
        dest.writeString(duration);
        dest.writeString(size);
        dest.writeString(uploadTime);

//        dest.writeParcelable(user, flags);
    }
}
