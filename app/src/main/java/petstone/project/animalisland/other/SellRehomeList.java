package petstone.project.animalisland.other;

import android.graphics.drawable.Drawable;

import com.google.firebase.storage.StorageReference;

public class SellRehomeList {

    StorageReference main_img;
    int gender;
    String type, breed, birth, local, date, price, did;

    public void setImg(StorageReference main) {
        main_img = main;
    }

    public void setGenderImg(int sgender) {
        gender = sgender;
    }

    public void setType(String stype) { type = stype; }

    public void setBreed(String sbreed) {
        breed = sbreed;
    }

    public void setBirth(String sbirth) {
        birth = sbirth;
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

    public void setDid(String sdid) { did = sdid; }

    public StorageReference getImg() {
        return this.main_img;
    }

    public int getGender() {
        return this.gender;
    }

    public String getType() { return this.type; }

    public String getBreed() {
        return this.breed;
    }

    public String getBirth() {
        return this.birth;
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

    public String getDid() { return this.did; }

}
