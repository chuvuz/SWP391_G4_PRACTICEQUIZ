package com.quiz.g4.entity;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "users")
public class User implements UserDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id")
	private Integer userId;

	@ManyToOne
	@JoinColumn(name = "role_id", nullable = false)
	private Role role;

	@Column(name = "full_name", nullable = false)
	private String fullName;

	@Column(name = "description")
	private String description;

	@Column(name = "password", nullable = false)
	private String password;

	@Column(nullable = false, unique = true, length = 45)
	private String email;

	@Column(name = "profile_image")
	private String profileImage;

	@Column(name = "created_date")
	private LocalDate createdDate = LocalDate.now();

	@Column(name = "updated_date")
	private LocalDate updatedDate = LocalDate.now();

	@Column(name = "is_active", nullable = false)
	private Boolean isActive = true;



	@Column(name = "phone", nullable = false, length = 10)
	@Pattern(regexp = "^\\d{10}$", message = "Phone number must be exactly 10 digits")
	private String phone;

	@Column(name = "date_of_birth")
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)

	private LocalDate dateOfBirth;

	@Column(name = "gender", nullable = false)
	@ColumnDefault("1")
	private Boolean gender = true; // True có thể đại diện cho "Nam", False cho "Nữ"

	@OneToMany(mappedBy = "createdBy")
	private Set<Quiz> quizzes;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return Collections.singletonList(new SimpleGrantedAuthority(role.getRoleName()));
	}

	@Override
	public String getUsername() {
		return email;
	}

	@Override
	public String getPassword() {
		return password;
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
		return isActive;
	}

	public boolean isActive() {
		return isActive;
	}
}
