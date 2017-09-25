package br.com.ajafit.platform.core.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(value = "MANAGER")
public class Manager extends Profile {

	
}
