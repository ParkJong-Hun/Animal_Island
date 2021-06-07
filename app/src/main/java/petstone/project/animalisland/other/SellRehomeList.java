package petstone.project.animalisland.other;

import android.graphics.drawable.Drawable;

public class SellRehomeList {

    Drawable main_img, gender;
    String breed, age, local, date, price, nickname;

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

    public void setPrice(String sprice) {
        price = sprice;
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

    public String getPrice() {
        return this.price;
    }

    public String getNickname() {
        return this.nickname;
    }

}
