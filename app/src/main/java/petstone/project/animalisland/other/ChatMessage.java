package petstone.project.animalisland.other;

import java.util.Date;

public class ChatMessage {
    String uid = "";
    int readed = 0;

    Date date = new Date();
    String article = "";

    public String getUid() {
        return uid;
    }
    public int getReaded() {
        return readed;
    }
    public Date getDate() {
        return date;
    }
    public String getArticle() {
        return article;
    }

    public void setNickname(String uid) {
        this.uid = uid;
    }
    public void setDate(Date date) {
        this.date = date;
    }
    public void setArticle(String article) {
        this.article = article;
    }
    public void setReaded(int readed) { this.readed = readed; }
}
