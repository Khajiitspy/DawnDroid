package com.example.dawn.dto.Account;

public class UserItemDTO {
    private int id;
    private String email;
    private String userName;
    private String avatar;

    public UserItemDTO(int id, String email, String userName, String avatar) {
        this.id = id;
        this.email = email;
        this.userName = userName;
        this.avatar = avatar;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return userName;
    }

    public void setName(String name) {
        this.userName = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImage() { return avatar; }

    public void setImage(String image) { this.avatar = image; }
}
