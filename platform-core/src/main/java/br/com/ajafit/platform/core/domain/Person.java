package br.com.ajafit.platform.core.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
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
	private Date tokenDate;

	private Date register;

	private String image;
	@Column(columnDefinition = "character")
	@Enumerated(EnumType.STRING)
	private PersonGender gender;

	@Enumerated(EnumType.STRING)
	private PersonAuthType authType;

}
