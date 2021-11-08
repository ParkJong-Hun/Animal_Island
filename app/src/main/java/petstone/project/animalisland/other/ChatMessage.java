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
}
