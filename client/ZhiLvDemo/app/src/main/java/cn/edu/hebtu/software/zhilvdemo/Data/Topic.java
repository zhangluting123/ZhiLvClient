package cn.edu.hebtu.software.zhilvdemo.Data;

/**
 * @ProjectName:    ZhiLv
 * @Description:    java类作用描述
 * @Author:         张璐婷
 * @CreateDate:     2021/1/20 12:47
 * @Version:        1.0
 */
public class Topic {
	private Integer topicId;
	private String title;
	private Integer userId;
	public Integer getTopicId() {
		return topicId;
	}
	public void setTopicId(Integer topicId) {
		this.topicId = topicId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	@Override
	public String toString() {
		return "Topic [topicId=" + topicId + ", title=" + title + ", userId=" + userId + "]";
	}
	
	
}
