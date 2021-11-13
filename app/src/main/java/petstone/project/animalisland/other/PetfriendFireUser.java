package petstone.project.animalisland.other;

public class PetfriendFireUser {

    public PetfriendFireUser()
    {

    }

    public PetfriendFireUser(String uid, String nickname, String address) {
        this.uid = uid;
        this.nickname = nickname;
        this.address = address;
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
}
