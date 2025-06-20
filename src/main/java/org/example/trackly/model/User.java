package org.example.trackly.model;

public class User {
    private Integer id;
    private String username;  // Tambahan field username
    private String email;
    private String password;
    private String profile_image;
    private String phone_number;

    public User() {

    }

    // Constructor dengan 5 parameter
    public User(String username, String email, String password, String profile_image, String phone_number) {
        setUsername(username);
        setEmail(email);
        setPassword(password);
        setProfileImage(profile_image);
        setPhoneNumber(phone_number);
    }

    // Getters & Setters (sudah lengkap)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProfileImage() {
        return profile_image;
    }

    public void setProfileImage(String profile_image) {
        this.profile_image = profile_image;
    }

    public String getPhoneNumber() {
        return phone_number;
    }

    public boolean hasPhoneNumber() {
        return phone_number != null && !phone_number.isEmpty();
    }

    public void setPhoneNumber(String phone_number) {
        this.phone_number = phone_number;
    }

    public boolean hasProfilePicture() {
        return profile_image != null && !profile_image.isEmpty();
    }
}
