package br.com.ajafit.platform.core.domain;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(of = { "id" })
@Entity
public class ScreenConfig {

	@EmbeddedId
	private ScreenConfigPK id;

	private int priority;
}
