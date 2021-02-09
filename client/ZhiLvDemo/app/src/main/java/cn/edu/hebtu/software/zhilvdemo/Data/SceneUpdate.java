package cn.edu.hebtu.software.zhilvdemo.Data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * @ProjectName:    ZhiLv
 * @Description:    java类作用描述
 * @Author:         张璐婷
 * @CreateDate:     2021/2/8 14:38
 * @Version:        1.0
 */
public class SceneUpdate implements Parcelable {
	private Integer updateId;
	private User user;
	private Integer sceneId;
	private Date updateTime;

    protected SceneUpdate(Parcel in) {
        if (in.readByte() == 0) {
            updateId = null;
        } else {
            updateId = in.readInt();
        }
        user = in.readParcelable(User.class.getClassLoader());
        if (in.readByte() == 0) {
            sceneId = null;
        } else {
            sceneId = in.readInt();
        }
        if(in.readByte() == 0){
            updateTime = null;
        }else{
            updateTime = new Date(in.readLong());
        }
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (updateId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(updateId);
        }
        dest.writeParcelable(user, flags);
        if (sceneId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(sceneId);
        }
        if(updateTime == null){
            dest.writeByte((byte)0);
        }else{
            dest.writeByte((byte)1);
            dest.writeLong(updateTime.getTime());
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SceneUpdate> CREATOR = new Creator<SceneUpdate>() {
        @Override
        public SceneUpdate createFromParcel(Parcel in) {
            return new SceneUpdate(in);
        }

        @Override
        public SceneUpdate[] newArray(int size) {
            return new SceneUpdate[size];
        }
    };

    public Integer getUpdateId() {
		return updateId;
	}
	public void setUpdateId(Integer updateId) {
		this.updateId = updateId;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Integer getSceneId() {
		return sceneId;
	}
	public void setSceneId(Integer sceneId) {
		this.sceneId = sceneId;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	

}
