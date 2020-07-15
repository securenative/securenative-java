package com.securenative.models;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class UserTraits {
    private String name;
    private String email;
    private String phone;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private Date createdAt;

    public UserTraits(String name) {
        this(name, null, null);
    }

    public UserTraits(String name, String email) {
        this(name, email, null);
    }

    public UserTraits(String name, String email, String phone) {
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    public UserTraits(String name, String email, Date createdAt) {
        this.name = name;
        this.email = email;
        this.createdAt = createdAt;
    }

    public UserTraits(String name, String email, String phone, Date createdAt) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.createdAt = createdAt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
