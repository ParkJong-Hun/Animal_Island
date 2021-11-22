package petstone.project.animalisland.other;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;

import com.google.firebase.storage.StorageReference;

public class FreeRehomeList {

    StorageReference main_img;
    Drawable  gender;
    String type, breed, birth, local, date, did;

    public void setImg(StorageReference main) {
        main_img = main;
    }

    public void setGenderImg(Drawable sgender) {
        gender = sgender;
    }

    public void setType(String stype) { type = stype; };

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

    public void setDid(String sdid) { did = sdid; }

    public StorageReference getImg() {
        return this.main_img;
    }

    public Drawable getGender() {
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

    public String getDid() { return this.did; }

}
