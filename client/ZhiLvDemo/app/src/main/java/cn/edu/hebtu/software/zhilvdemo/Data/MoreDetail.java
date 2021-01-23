package cn.edu.hebtu.software.zhilvdemo.Data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

import cn.edu.hebtu.software.zhilvdemo.Util.DateUtil;

/**
 * @ProjectName:    ZhiLv
 * @Description:    java类作用描述
 * @Author:         张璐婷
 * @CreateDate:     2021/1/22 15:00
 * @Version:        1.0
 */
public class MoreDetail implements Parcelable {
	private Integer moreId;
	private String destination;
	private String traffic;
	private Date beginDate;
	private Integer days;
	private String people;
	private Integer money;
	private Integer travelsId;
	private Integer videoId;

	public MoreDetail(){

    }

    protected MoreDetail(Parcel in) {
        if (in.readByte() == 0) {
            moreId = null;
        } else {
            moreId = in.readInt();
        }
        destination = in.readString();
        traffic = in.readString();
        if (in.readByte() == 0) {
            days = null;
        } else {
            days = in.readInt();
        }
        people = in.readString();
        if (in.readByte() == 0) {
            money = null;
        } else {
            money = in.readInt();
        }
        if (in.readByte() == 0) {
            travelsId = null;
        } else {
            travelsId = in.readInt();
        }
        if (in.readByte() == 0) {
            videoId = null;
        } else {
            videoId = in.readInt();
        }
        if(in.readByte() == 0){
            beginDate = null;
        }else{
            beginDate = new Date(in.readLong());
        }
    }

    public static final Creator<MoreDetail> CREATOR = new Creator<MoreDetail>() {
        @Override
        public MoreDetail createFromParcel(Parcel in) {
            return new MoreDetail(in);
        }

        @Override
        public MoreDetail[] newArray(int size) {
            return new MoreDetail[size];
        }
    };

    public Integer getMoreId() {
		return moreId;
	}
	public void setMoreId(Integer moreId) {
		this.moreId = moreId;
	}
	public String getDestination() {
		return destination;
	}
	public String getTraffic() {
		return traffic;
	}
	public void setTraffic(String traffic) {
		this.traffic = traffic;
	}
	public void setDestination(String destination) {
		this.destination = destination;
	}
	public Date getBeginDate() {
		return beginDate;
	}
	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}
	public Integer getDays() {
		return days;
	}
	public void setDays(Integer days) {
		this.days = days;
	}
	public String getPeople() {
		return people;
	}
	public void setPeople(String people) {
		this.people = people;
	}
	public Integer getMoney() {
		return money;
	}
	public void setMoney(Integer money) {
		this.money = money;
	}
	public Integer getTravelsId() {
		return travelsId;
	}
	public void setTravelsId(Integer travelsId) {
		this.travelsId = travelsId;
	}
	public Integer getVideoId() {
		return videoId;
	}
	public void setVideoId(Integer videoId) {
		this.videoId = videoId;
	}
	@Override
	public String toString() {
		return "MoreDetail [moreId=" + moreId + ", destination=" + destination + ", traffic=" + traffic + ", beginDate="
				+ beginDate + ", days=" + days + ", people=" + people + ", money=" + money + ", travelsId=" + travelsId
				+ ", videoId=" + videoId + "]";
	}


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (moreId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(moreId);
        }
        dest.writeString(destination);
        dest.writeString(traffic);
        if (days == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(days);
        }
        dest.writeString(people);
        if (money == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(money);
        }
        if (travelsId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(travelsId);
        }
        if (videoId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(videoId);
        }
        if(beginDate == null){
            dest.writeByte((byte) 0);
        }else{
            dest.writeByte((byte) 1);
            dest.writeLong(beginDate.getTime());
        }
    }
}
