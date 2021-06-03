package petstone.project.animalisland;

import android.graphics.drawable.Drawable;

public class FreeRehomeList {

    Drawable main_img, gender;
    String breed, age, local, date, nickname;

    public void setImg(Drawable main) {
        main_img = main;
    }

    public void setGenderImg(Drawable sgender) {
        gender = sgender;
    }

    public void setBreed(String sbreed) {
        breed = sbreed;
    }

    public void setAge(String sage) {
        age = sage;
    }

    public void setLocal(String slocal) {
        local = slocal;
    }

    public void setDate(String sdate) {
        date = sdate;
    }

    public void setNickname(String snickname) {
        nickname = snickname;
    }

    public Drawable getImg() {
        return this.main_img;
    }

    public Drawable getGender() {
        return this.gender;
    }

    public String getBreed() {
        return this.breed;
    }

    public String getAge() {
        return this.age;
    }

    public String getLocal() {
        return this.local;
    }

    public String getDate() {
        return this.date;
    }

    public String getNickname() {
        return this.nickname;
    }

}
