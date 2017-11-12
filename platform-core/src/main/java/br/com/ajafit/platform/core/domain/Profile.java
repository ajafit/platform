package br.com.ajafit.platform.core.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.TableGenerator;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Profile {

	@Id
	@TableGenerator(name = "PROFILE_GEN", table = "PROFILE_ID_Generator", pkColumnName = "name", valueColumnName = "sequence", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "PROFILE_GEN")
	@Column(name = "profile_id")
	private long id;

	private String token;

	@ManyToOne
	@JoinColumn(name = "person_id")
	private Person person;

	@ManyToOne
	@JoinColumn(name = "manager_id", nullable = true)
	private Manager manager;

}
