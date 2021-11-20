package petstone.project.animalisland.other;

public class PetfriendFireUser {

    public PetfriendFireUser()
    {

    }


    public String getUid() {
        return uid;
    }

    public String getNickname() {
        return nickname;
    }

    public String getAddress() {
        return address;
    }

    private String uid;
    private String nickname;
    private String address;

    public String getCarrerImgName() {
        return carrerImgName;
    }

    public String getProfileImgUri() {
        return profileImgUri;
    }

    public String getDays() {
        return days;
    }

    public String getInfo() {
        return Info;
    }

    public String getSchedule() {
        return schedule;
    }

    public String getOriginalAddress() {
        return originalAddress;
    }

    public String getPay() {
        return pay;
    }

    public boolean isHwaldong_sancheck() {
        return hwaldong_sancheck;
    }

    public boolean isHwaldong_dolbom() {
        return hwaldong_dolbom;
    }

    public boolean isHwaldong_beauty() {
        return hwaldong_beauty;
    }

    public boolean isPetfriend() {
        return petfriend;
    }

    public PetfriendFireUser(String uid, String nickname, String address, String carrerImgName, String profileImgUri, String days, String info, String schedule, String originalAddress, String pay, int payint, boolean hwaldong_sancheck, boolean hwaldong_dolbom, boolean hwaldong_beauty, boolean petfriend) {
        this.uid = uid;
        this.nickname = nickname;
        this.address = address;
        this.carrerImgName = carrerImgName;
        this.profileImgUri = profileImgUri;
        this.days = days;
        Info = info;
        this.schedule = schedule;
        this.originalAddress = originalAddress;
        this.pay = pay;
        this.payint = payint;
        this.hwaldong_sancheck = hwaldong_sancheck;
        this.hwaldong_dolbom = hwaldong_dolbom;
        this.hwaldong_beauty = hwaldong_beauty;
        this.petfriend = petfriend;
 ;
    }

    private String carrerImgName;
    private String profileImgUri;
    private String days;
    private String Info;
    private String schedule;
    private String originalAddress;
    private String pay;
    private int payint;
    private boolean hwaldong_sancheck;
    private boolean hwaldong_dolbom;
    private boolean hwaldong_beauty;
    private boolean petfriend;


    public int getPayint() {
        return payint;
    }
}
