package petstone.project.animalisland.other;

import java.sql.Timestamp;
import java.util.Date;

public class ChatList {
    private String whoName = "";
    private String updatedMessage = "";
    private Date updatedDate = new Date();
    private Integer newCount = 0;
    private String uid = "";

    public String getUid() {
        return uid;
    }
    public void setUid(String uid) {
        this.uid = uid;
    }
    public String getWhoName() {
        return whoName;
    }
    public void setWhoName(String whoName) {
        this.whoName = whoName;
    }
    public String getUpdatedMessage() {
        return updatedMessage;
    }
    public void setUpdatedMessage(String updatedMessage) {
        this.updatedMessage = updatedMessage;
    }
    public Date getUpdatedDate() {
        return updatedDate;
    }
    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }
    public Integer getNewCount() {
        return newCount;
    }
    public void setNewCount(Integer newCount) {
        this.newCount = newCount;
    }
}
