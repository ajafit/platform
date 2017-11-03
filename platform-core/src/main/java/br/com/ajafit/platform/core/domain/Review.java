package br.com.ajafit.platform.core.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
@Entity
public class Review {

	@EmbeddedId
	private ReviewPK id;

	private int rate;

	private Date date;

	@Column(columnDefinition = "text")
	private String value;

}
