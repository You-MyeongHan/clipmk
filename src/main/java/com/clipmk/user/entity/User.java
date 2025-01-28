package com.clipmk.user.entity;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.clipmk.user.dto.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="user")
public class User implements UserDetails{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
	private String uid;
	@JsonIgnore
	private String pwd;
	private String nick;
	@JsonIgnore
	private String email;
	@JsonIgnore
	private Boolean emailReceive;
	@Enumerated(EnumType.STRING)
	@JsonIgnore
	private Role role;
	@Column(nullable = false)
	private Integer point;
	@Nullable
	private String regionCode;
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of(new SimpleGrantedAuthority(role.name()));
	}
	
	@JsonIgnore
	@Override
    public boolean isAccountNonExpired() {
        return true; // 유효한 계정
    }

	@JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true; // 사용불가(잠금)하지 않은 계정
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true; // 비밀번호가 만료되지 않음
    }

    @JsonIgnore
	@Override
	public String getPassword() {
		return pwd;
	}

    @JsonIgnore
	@Override
	public String getUsername() {
		return nick;
	}
	
    @JsonIgnore
	public String getUid() {
		return uid;
	}

    @JsonIgnore
	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}
}
