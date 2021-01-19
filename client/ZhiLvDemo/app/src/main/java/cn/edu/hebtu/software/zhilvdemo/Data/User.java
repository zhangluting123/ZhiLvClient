package cn.edu.hebtu.software.zhilvdemo.Data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;


/**
 * @ProjectName:    ZhiLv
 * @Description:    java类作用描述
 * @Author:         张璐婷
 * @CreateDate:     2021/1/18 13:28
 * @Version:        1.0
 */
public class User implements Parcelable {
	private Integer userId;
	private String phone;
	private String email;
	private String password;
	private String userHead;
	private String userName;
	private String sex;
	private Date birth;
	private String signature;

	public User(){

    }

    public User(Integer userId, String phone, String email, String password, String userHead, String userName, String sex, Date birth, String signature) {
        this.userId = userId;
        this.phone = phone;
        this.email = email;
        this.password = password;
        this.userHead = userHead;
        this.userName = userName;
        this.sex = sex;
        this.birth = birth;
        this.signature = signature;
    }

    protected User(Parcel in) {
        if (in.readByte() == 0) {
            userId = null;
        } else {
            userId = in.readInt();
        }
        phone = in.readString();
        email = in.readString();
        password = in.readString();
        userHead = in.readString();
        userName = in.readString();
        sex = in.readString();
        signature = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getUserHead() {
		return userHead;
	}
	public void setUserHead(String userHead) {
		this.userHead = userHead;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}

    public Date getBirth() {
        return birth;
    }

    public void setBirth(Date birth) {
        this.birth = birth;
    }

    public String getSignature() {
		return signature;
	}
	public void setSignature(String signature) {
		this.signature = signature;
	}


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        if (userId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(userId);
        }
        dest.writeString(phone);
        dest.writeString(email);
        dest.writeString(password);
        dest.writeString(userHead);
        dest.writeString(userName);
        dest.writeString(sex);
        dest.writeString(signature);
    }


}
