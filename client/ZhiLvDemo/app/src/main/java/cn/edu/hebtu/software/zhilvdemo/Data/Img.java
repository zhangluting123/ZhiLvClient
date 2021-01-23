package cn.edu.hebtu.software.zhilvdemo.Data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @ProjectName:    ZhiLv
 * @Description:    java类作用描述
 * @Author:         张璐婷
 * @CreateDate:     2021/1/22 15:00
 * @Version:        1.0
 */
public class Img implements Parcelable {
	private Integer imgId;
	private String path;
	private Integer travelsId;

	public Img(){

    }

    protected Img(Parcel in) {
        if (in.readByte() == 0) {
            imgId = null;
        } else {
            imgId = in.readInt();
        }
        path = in.readString();
        if (in.readByte() == 0) {
            travelsId = null;
        } else {
            travelsId = in.readInt();
        }
    }

    public static final Creator<Img> CREATOR = new Creator<Img>() {
        @Override
        public Img createFromParcel(Parcel in) {
            return new Img(in);
        }

        @Override
        public Img[] newArray(int size) {
            return new Img[size];
        }
    };

    public Integer getImgId() {
		return imgId;
	}
	public void setImgId(Integer imgId) {
		this.imgId = imgId;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public Integer getTravelsId() {
		return travelsId;
	}
	public void setTravelsId(Integer travelsId) {
		this.travelsId = travelsId;
	}
	@Override
	public String toString() {
		return "Img [imgId=" + imgId + ", path=" + path + ", travelsId=" + travelsId + "]";
	}


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (imgId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(imgId);
        }
        dest.writeString(path);
        if (travelsId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(travelsId);
        }
    }
}
