package br.com.ajafit.platform.core.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Person {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "person_id")
	private long id;

	private String name;
	@Column(unique = true)
	private String email;
	private boolean emailValidation;

	private Date birth;
	private String password;
	private String token;

	private Date register;

	@Column(columnDefinition = "character")
	@Enumerated(EnumType.STRING)
	private PersonGender gender;

	@Enumerated(EnumType.STRING)
	private PersonAuthType authType;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean isEmailValidation() {
		return emailValidation;
	}

	public void setEmailValidation(boolean emailValidation) {
		this.emailValidation = emailValidation;
	}

	public Date getBirth() {
		return birth;
	}

	public void setBirth(Date birth) {
		this.birth = birth;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Date getRegister() {
		return register;
	}

	public void setRegister(Date register) {
		this.register = register;
	}

	public PersonGender getGender() {
		return gender;
	}

	public void setGender(PersonGender gender) {
		this.gender = gender;
	}

	public PersonAuthType getAuthType() {
		return authType;
	}

	public void setAuthType(PersonAuthType authType) {
		this.authType = authType;
	}

	@Override
	public String toString() {
		return "Person [id=" + id + ", name=" + name + ", email=" + email + ", emailValidation=" + emailValidation
				+ ", birth=" + birth + ", password=" + password + ", token=" + token + ", register=" + register
				+ ", gender=" + gender + ", authType=" + authType + "]";
	}

}
