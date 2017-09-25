package br.com.ajafit.platform.core.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(value = "CASHIER")
public class Cashier extends Profile {

}
