package petstone.project.animalisland.other;

public class PetfriendUser {
/*
    public PetfriendUser(String uid)
    {
        this.uid = uid;
    }
    public String getUid() {
        return uid;
    }
    public void setUid(String uid) {
        this.uid = uid;
    }
 */


    public PetfriendUser(String uid, String nickname, String address, String carrerImgName, String profileImgUri, String days, String info, String schedule, String originalAddress, String pay, boolean hwaldong_sancheck, boolean hwaldong_dolbom, boolean hwaldong_beauty, boolean petfriend, int payint) {
        this.uid = uid;
        this.nickname = nickname;
        this.address = address;
        this.carrerImgName = carrerImgName;
        this.profileImgUri = profileImgUri;
        Days = days;
        Info = info;
        this.schedule = schedule;
        this.originalAddress = originalAddress;
        this.pay = pay;
        this.hwaldong_sancheck = hwaldong_sancheck;
        this.hwaldong_dolbom = hwaldong_dolbom;
        this.hwaldong_beauty = hwaldong_beauty;
        this.petfriend = petfriend;
        this.payint = payint;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCarrerImgName() {
        return carrerImgName;
    }

    public void setCarrerImgName(String carrerImgName) {
        this.carrerImgName = carrerImgName;
    }

    public String getProfileImgUri() {
        return profileImgUri;
    }

    public void setProfileImgUri(String profileImgUri) {
        this.profileImgUri = profileImgUri;
    }

    public String getDays() {
        return Days;
    }

    public void setDays(String days) {
        Days = days;
    }

    public String getInfo() {
        return Info;
    }

    public void setInfo(String info) {
        Info = info;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public String getOriginalAddress() {
        return originalAddress;
    }

    public void setOriginalAddress(String originalAddress) {
        this.originalAddress = originalAddress;
    }

    public String getPay() {
        return pay;
    }

    public void setPay(String pay) {
        this.pay = pay;
    }

    public boolean isHwaldong_sancheck() {
        return hwaldong_sancheck;
    }

    public void setHwaldong_sancheck(boolean hwaldong_sancheck) {
        this.hwaldong_sancheck = hwaldong_sancheck;
    }

    public boolean isHwaldong_dolbom() {
        return hwaldong_dolbom;
    }

    public void setHwaldong_dolbom(boolean hwaldong_dolbom) {
        this.hwaldong_dolbom = hwaldong_dolbom;
    }

    public boolean isHwaldong_beauty() {
        return hwaldong_beauty;
    }

    public void setHwaldong_beauty(boolean hwaldong_beauty) {
        this.hwaldong_beauty = hwaldong_beauty;
    }

    public boolean isPetfriend() {
        return petfriend;
    }

    public void setPetfriend(boolean petfriend) {
        this.petfriend = petfriend;
    }

    private String uid;
    private String nickname;
    private String address;
    private String carrerImgName;
    private String profileImgUri;
    private String Days;
    private String Info;
    private String schedule;
    private String originalAddress;
    private String pay;
    private boolean hwaldong_sancheck;
    private boolean hwaldong_dolbom;
    private boolean hwaldong_beauty;
    private boolean petfriend;
    private int payint;


    public int getPayint() {
        return payint;
    }

    public void setPayint(int payint) {
        this.payint = payint;
    }
}