package br.com.ajafit.platform.core.domain;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;

@Entity
@PrimaryKeyJoinColumn(name = "product_id", referencedColumnName = "saleable_id")
public class Product extends Saleable {

}
