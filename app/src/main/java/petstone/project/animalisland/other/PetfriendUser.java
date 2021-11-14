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




    public PetfriendUser(String uid, String nickname, String address, String carrerImgUri, String profileImg) {
        this.uid = uid;
        this.nickname = nickname;
        this.address = address;
        this.carrerImgUri = carrerImgUri;
        this.profileImgUri = profileImg;
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
}