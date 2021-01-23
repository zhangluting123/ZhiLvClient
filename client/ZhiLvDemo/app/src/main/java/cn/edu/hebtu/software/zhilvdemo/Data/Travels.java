package cn.edu.hebtu.software.zhilvdemo.Data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;
import java.util.List;

import cn.edu.hebtu.software.zhilvdemo.Util.DateUtil;

/** 
 * @ProjectName:    ZhiLv
 * @Description:    java类作用描述
 * @Author:         张璐婷
 * @CreateDate:     2021/1/22 15:00
 * @Version:        1.0
 */
public class Travels implements Parcelable {
	private Integer travelsId;
	private String title;
	private String route;
	private String scene;
	private String ticket;
	private String hotel;
	private String tips;
	private Topic topic;
	private String location;
	private Date uploadTime;
	private MoreDetail detail;
	private List<Img> imgList;
	private List<String> imgPathList;
	private User user;
    public Travels(){

    }


    protected Travels(Parcel in) {
        if (in.readByte() == 0) {
            travelsId = null;
        } else {
            travelsId = in.readInt();
        }
        title = in.readString();
        route = in.readString();
        scene = in.readString();
        ticket = in.readString();
        hotel = in.readString();
        tips = in.readString();
        location = in.readString();
        topic = in.readParcelable(Topic.class.getClassLoader());
        detail = in.readParcelable(MoreDetail.class.getClassLoader());
        imgList = in.createTypedArrayList(Img.CREATOR);
        imgPathList = in.createStringArrayList();
        user = in.readParcelable(User.class.getClassLoader());
        uploadTime = new Date(in.readLong());
    }

    public static final Creator<Travels> CREATOR = new Creator<Travels>() {
        @Override
        public Travels createFromParcel(Parcel in) {
            return new Travels(in);
        }

        @Override
        public Travels[] newArray(int size) {
            return new Travels[size];
        }
    };

    public Integer getTravelsId() {
		return travelsId;
	}
	public void setTravelsId(Integer travelsId) {
		this.travelsId = travelsId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getRoute() {
		return route;
	}
	public void setRoute(String route) {
		this.route = route;
	}
	public String getScene() {
		return scene;
	}
	public void setScene(String scene) {
		this.scene = scene;
	}
	public String getTicket() {
		return ticket;
	}
	public void setTicket(String ticket) {
		this.ticket = ticket;
	}
	public String getHotel() {
		return hotel;
	}
	public void setHotel(String hotel) {
		this.hotel = hotel;
	}
	public String getTips() {
		return tips;
	}
	public void setTips(String tips) {
		this.tips = tips;
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
	public List<Img> getImgList() {
		return imgList;
	}
	public void setImgList(List<Img> imgList) {
		this.imgList = imgList;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}

    public List<String> getImgPathList() {
        return imgPathList;
    }

    public void setImgPathList(List<String> imgPathList) {
        this.imgPathList = imgPathList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (travelsId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(travelsId);
        }
        dest.writeString(title);
        dest.writeString(route);
        dest.writeString(scene);
        dest.writeString(ticket);
        dest.writeString(hotel);
        dest.writeString(tips);
        dest.writeString(location);
        dest.writeParcelable(topic, flags);
        dest.writeParcelable(detail, flags);
        dest.writeTypedList(imgList);
        dest.writeStringList(imgPathList);
        dest.writeParcelable(user, flags);
        dest.writeLong(uploadTime.getTime());
    }
}
