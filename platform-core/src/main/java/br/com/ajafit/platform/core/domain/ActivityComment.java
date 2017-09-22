package br.com.ajafit.platform.core.domain;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

@Entity
public class ActivityComment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "activity_comment_id")
	private long id;

	@ManyToOne
	@JoinColumn(name = "activity_id")
	private Activity activity;

	private Date date;

	@Lob
	private String value;

}
