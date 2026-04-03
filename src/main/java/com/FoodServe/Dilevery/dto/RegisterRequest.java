package com.FoodServe.Dilevery.dto;

public class RegisterRequest {

    private String email;
    private String username;
    private String password;
    private String role;

    public RegisterRequest() {}

    public String getEmail() {
        return email;
    }
    public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}