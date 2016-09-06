package pl.horuss.bbplay.web.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
public class User implements UserDetails, Comparable<User> {

	private static final long serialVersionUID = 961898510928102753L;

	@Id
	@GeneratedValue
	@Column(name = "us_id")
	private long id;

	@Column(name = "us_name")
	private String name;

	@Column(name = "us_password")
	private String password;

	@OneToMany(mappedBy = "user")
	private List<UserRole> roles;

	public User() {

	}

	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<UserRole> getRoles() {
		return roles != null ? roles : (roles = new ArrayList<>());
	}

	public void setRoles(List<UserRole> roles) {
		this.roles = roles;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return getRoles().stream().map(it -> new SimpleGrantedAuthority(it.getName()))
				.collect(Collectors.toList());
	}

	@Override
	public String getUsername() {
		return getName();
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

	@Override
	public int compareTo(User o) {
		if (o == null) {
			return -1;
		}
		return new Long(this.id).compareTo(new Long(o.id));
	}

}
