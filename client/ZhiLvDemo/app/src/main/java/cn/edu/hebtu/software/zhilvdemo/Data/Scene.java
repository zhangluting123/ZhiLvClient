package cn.edu.hebtu.software.zhilvdemo.Data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * @ProjectName:    ZhiLv
 * @Description:    java类作用描述
 * @Author:         张璐婷
 * @CreateDate:     2021/2/8 14:39
 * @Version:        1.0
 */
public class Scene implements Parcelable {
	private Integer sceneId;
	private String address;
	private String path;
	private String title;
	private String content;
	private String rule;
	private String openTime;
	private String traffic;
	private String ticket;
	private String costTime;
	private String phone;
	private String website;
	private Double latitude;
	private Double longitude;
	private List<SceneUpdate> sceneUpdate;

	public Scene(){}


    protected Scene(Parcel in) {
        if (in.readByte() == 0) {
            sceneId = null;
        } else {
            sceneId = in.readInt();
        }
        address = in.readString();
        path = in.readString();
        title = in.readString();
        content = in.readString();
        rule = in.readString();
        openTime = in.readString();
        traffic = in.readString();
        ticket = in.readString();
        costTime = in.readString();
        phone = in.readString();
        website = in.readString();
        if (in.readByte() == 0) {
            latitude = null;
        } else {
            latitude = in.readDouble();
        }
        if (in.readByte() == 0) {
            longitude = null;
        } else {
            longitude = in.readDouble();
        }
        sceneUpdate = in.createTypedArrayList(SceneUpdate.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (sceneId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(sceneId);
        }
        dest.writeString(address);
        dest.writeString(path);
        dest.writeString(title);
        dest.writeString(content);
        dest.writeString(rule);
        dest.writeString(openTime);
        dest.writeString(traffic);
        dest.writeString(ticket);
        dest.writeString(costTime);
        dest.writeString(phone);
        dest.writeString(website);
        if (latitude == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(latitude);
        }
        if (longitude == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(longitude);
        }
        dest.writeTypedList(sceneUpdate);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Scene> CREATOR = new Creator<Scene>() {
        @Override
        public Scene createFromParcel(Parcel in) {
            return new Scene(in);
        }

        @Override
        public Scene[] newArray(int size) {
            return new Scene[size];
        }
    };

    public Integer getSceneId() {
		return sceneId;
	}
	public void setSceneId(Integer sceneId) {
		this.sceneId = sceneId;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
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
	public String getRule() {
		return rule;
	}
	public void setRule(String rule) {
		this.rule = rule;
	}
	public String getOpenTime() {
		return openTime;
	}
	public void setOpenTime(String openTime) {
		this.openTime = openTime;
	}
	public String getTraffic() {
		return traffic;
	}
	public void setTraffic(String traffic) {
		this.traffic = traffic;
	}
	public String getTicket() {
		return ticket;
	}
	public void setTicket(String ticket) {
		this.ticket = ticket;
	}
	public String getCostTime() {
		return costTime;
	}
	public void setCostTime(String costTime) {
		this.costTime = costTime;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getWebsite() {
		return website;
	}
	public void setWebsite(String website) {
		this.website = website;
	}
	public List<SceneUpdate> getSceneUpdate() {
		return sceneUpdate;
	}
	public void setSceneUpdate(List<SceneUpdate> sceneUpdate) {
		this.sceneUpdate = sceneUpdate;
	}
	public Double getLatitude() {
		return latitude;
	}
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	public Double getLongitude() {
		return longitude;
	}
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

}
