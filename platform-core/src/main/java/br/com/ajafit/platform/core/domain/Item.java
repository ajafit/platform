package br.com.ajafit.platform.core.domain;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
public class Item {

	@EmbeddedId
	private ItemPK id;

	private int amount;
}
