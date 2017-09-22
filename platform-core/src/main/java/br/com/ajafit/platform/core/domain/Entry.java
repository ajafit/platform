package br.com.ajafit.platform.core.domain;

import java.util.Date;

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

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Entry {

	@Id
	@TableGenerator(name = "ENTRY_GEN", table = "ENTRY_ID_Generator", pkColumnName = "name", valueColumnName = "sequence", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "ENTRY_GEN")
	@Column(name = "entry_id")
	private long id;

	@ManyToOne
	@JoinColumn(name = "balance_id")
	private Balance balance;

	private Date date;

}
