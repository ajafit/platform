package br.com.ajafit.platform.core.domain;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

@Entity
public class Item {

	@EmbeddedId
	private ItemPK id;

	private int amount;
}
