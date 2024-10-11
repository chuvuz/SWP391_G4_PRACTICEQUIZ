package com.quiz.g4.dto;

public class UserDTO {
    private String fullName;
    private String roleName;
    private String email;

    public UserDTO(String fullName, String roleName, String email) {
        this.fullName = fullName;
        this.roleName = roleName;
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "UserDTO{" +
                "fullName='" + fullName + '\'' +
                ", roleName='" + roleName + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
