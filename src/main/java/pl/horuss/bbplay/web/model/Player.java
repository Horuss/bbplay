package pl.horuss.bbplay.web.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
public class Player {

	@Id
	@GeneratedValue
	@Column(name = "pla_id")
	private Long id;

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "pla_us_id", foreignKey = @ForeignKey(name = "fk_pla_us"))
	private User user;

	@Column(name = "pla_num")
	private Integer number;

	@Column(name = "pla_last_name")
	private String lastName;

	@Column(name = "pla_first_name")
	private String firstName;

	@Column(name = "pla_role")
	private String role;

	@Column(name = "pla_pos")
	private String position;

	@Column(name = "pla_pos2")
	private String position2;

	@Column(name = "pla_birthdate")
	private Date birthdate;

	@Column(name = "pla_height")
	private Integer height;

	@Column(name = "pla_email")
	private String email;

	@Column(name = "pla_phone")
	private String phone;

	@Column(name = "pla_comment")
	private String comment;

	public Player() {
		this.firstName = "";
		this.lastName = "";
		this.position = "";
		this.position2 = "";
		this.role = "";
		this.height = 0;
		this.email = "";
		this.phone = "";
		this.comment = "";
		this.number = 0;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getPosition2() {
		return position2;
	}

	public void setPosition2(String position2) {
		this.position2 = position2;
	}

	public Date getBirthdate() {
		return birthdate;
	}

	public void setBirthdate(Date birthdate) {
		this.birthdate = birthdate;
	}

	public Integer getHeight() {
		return height;
	}

	public void setHeight(Integer height) {
		this.height = height;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

}
