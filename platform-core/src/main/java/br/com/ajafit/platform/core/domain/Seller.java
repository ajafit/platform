package br.com.ajafit.platform.core.domain;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;

@Entity
@PrimaryKeyJoinColumn(name = "seller_id", referencedColumnName = "profile_id")
public class Seller extends Balance {

}
