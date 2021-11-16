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




    public PetfriendUser(String uid, String nickname, String address, String carrerImgUri, String profileImg, String days, String info, String schedule, String originalAddress) {
        this.uid = uid;
        this.nickname = nickname;
        this.address = address;
        this.carrerImgUri = carrerImgUri;
        this.profileImgUri = profileImg;
        Days = days;
        Info = info;
        this.schedule = schedule;
        this.originalAddress = originalAddress;
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

    private String uid;
    private String nickname;
    private String address;
    private String carrerImgUri;
    private String profileImgUri;
    private String Days;
    private String Info;
    private String schedule;
    private String originalAddress;


    public String getCarrerImgUri() {
        return carrerImgUri;
    }

    public void setCarrerImgUri(String carrerImgUri) {
        this.carrerImgUri = carrerImgUri;
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
}