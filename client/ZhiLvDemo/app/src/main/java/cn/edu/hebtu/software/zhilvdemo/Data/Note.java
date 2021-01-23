package cn.edu.hebtu.software.zhilvdemo.Data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

import cn.edu.hebtu.software.zhilvdemo.Util.DateUtil;

/**
 * @ProjectName:    ZhiLv
 * @Description:    检索数据
 * @Author:         张璐婷
 * @CreateDate:     2021/1/22 16:50
 * @Version:        1.0
 */
public class Note implements Parcelable {
	private boolean flag;  //flag=true travels; flag=false video
	private Date time;
	private Travels travels;
	private Video video;

    protected Note(Parcel in) {
        flag = in.readByte() != 0;
        travels = in.readParcelable(Travels.class.getClassLoader());
        video = in.readParcelable(Video.class.getClassLoader());
        time = new Date(in.readLong());
    }

    public static final Creator<Note> CREATOR = new Creator<Note>() {
        @Override
        public Note createFromParcel(Parcel in) {
            return new Note(in);
        }

        @Override
        public Note[] newArray(int size) {
            return new Note[size];
        }
    };

    public boolean isFlag() {
		return flag;
	}
	public void setFlag(boolean flag) {
		this.flag = flag;
	}
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
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
        dest.writeByte((byte) (flag ? 1 : 0));
        dest.writeParcelable(travels, flags);
        dest.writeParcelable(video, flags);
        dest.writeLong(time.getTime());
    }
}
