package cn.longhaiyan.user.bean;

import cn.longhaiyan.user.domain.UserInfo;

/**
 * Created by chenxb on 17-5-19.
 */
public class User {
    private int id;
    private int avatarId;
    private String name;
    private String userName;

    public User() {

    }

    public User(UserInfo userInfo, String name) {
        this.name = name;
        this.id = userInfo.getId();
        this.avatarId = userInfo.getAvatarId();
        this.userName = userInfo.getUserName();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAvatarId() {
        return avatarId;
    }

    public void setAvatarId(int avatarId) {
        this.avatarId = avatarId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
