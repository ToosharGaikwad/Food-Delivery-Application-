package com.FoodServe.Dilevery.entity;

import java.nio.file.attribute.UserDefinedFileAttributeView;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.FoodServe.Dilevery.Enum.Role;

import jakarta.persistence.*;

@Entity
@Table(
    name = "users",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = "email")
    }
)
public class User implements UserDetails{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    private String username;
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role;

    // ✅ REQUIRED by JPA
    public User() {}

    // ✅ Optional convenience constructor (NO id here)
    public User(String email, String username, String password,Role role) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.role= role;
    }
    
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + this.email));
    }

    // getters & setters
    public Long getId() {
        return id;
    }
    
   
   

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

	public String getEmail() {
        return email;
    }
 
    public void setEmail(String email) {
        this.email = email;
    }

    
    public String getUsername() {
        return email;   // ✅ MUST be email
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
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

//	@Override
//	public Collection<? extends GrantedAuthority> getAuthorities() {
//		// TODO Auto-generated method stub
//		return null;
//	}
}