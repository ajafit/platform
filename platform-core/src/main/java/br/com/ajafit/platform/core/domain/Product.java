package br.com.ajafit.platform.core.domain;

import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@PrimaryKeyJoinColumn(name = "product_id", referencedColumnName = "saleable_id")
public class Product extends Saleable {

	private String ingredients;

	@ManyToOne
	@JoinColumn(name = "product_type_id")
	private ProductType type;

	@ManyToOne
	@JoinColumn(name = "product_flavor_id")
	private ProductFlavor flavor;

	@OneToMany(fetch = FetchType.EAGER)
	@JoinColumn(name = "product_id")
	private Collection<ProductNutrition> productNutritions;
}
