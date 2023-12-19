package pw.proj.letsmeet.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "application_user")
public class User extends AbstractEntity implements UserDetails {

	private static final long serialVersionUID = 1202231012L;

	// **********************************************************************
	// POLA

	@Column(nullable = false, unique = true, length = 128)
	private String email;

	@Column(nullable = false, length = 128)
	private String password;

	@Column(length = 128)
	private String firstName;

	@Column(length = 128)
	private String lastName;

	@Column(length = 64)
	private String resetPasswordToken;

	@Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT false")
	private boolean deleted;

    @ManyToMany(mappedBy = "users")
    private Set<Meet> meets;

	// **********************************************************************
	// POLA NIETRWAŁE

	@Transient
	private Set<SimpleGrantedAuthority> authorities;

	@Transient
	public String getFullName() {
		return firstName + " " + lastName;
	}

	@Override
	@Transient
	public String getUsername() {
		return email;
	}

	@Override
	@Transient
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	@Transient
	public boolean isAccountNonExpired() {
		return !deleted;
	}

	@Override
	@Transient
	public boolean isAccountNonLocked() {
		return !deleted;
	}

	@Override
	@Transient
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	@Transient
	public boolean isEnabled() {
		return !deleted;
	}
}

