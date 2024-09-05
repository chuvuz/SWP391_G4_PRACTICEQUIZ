package com.quiz.g4.entity;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
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

	@ManyToOne
	@JoinColumn(name = "subject_id")
	private Subject subject; // Chỉ áp dụng cho giáo viên, có thể null cho học sinh và admin

	@Column(name = "full_name", nullable = false)
	private String fullName;

	@Column(name = "password", nullable = false)
	private String password;

	@Column(nullable = false, unique = true, length = 45)
	private String email;

	@Column(name = "created_date")
	private LocalDate createdDate = LocalDate.now();

	@Column(name = "updated_date")
	private LocalDate updatedDate = LocalDate.now();

	@Column(name = "is_active", nullable = false)
	private Boolean isActive = true;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return Collections.singletonList(new SimpleGrantedAuthority(role.getRoleName()));
	}

	@Override
	public String getUsername() {
		return email;  // Sử dụng email làm tên đăng nhập
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

	@OneToMany(mappedBy = "createdBy")
	private Set<Quiz> quizzes;

	@OneToMany(mappedBy = "user")
	private Set<RecordStudent> records;
}
