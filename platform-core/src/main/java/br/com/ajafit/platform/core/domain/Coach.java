package br.com.ajafit.platform.core.domain;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;

@Entity
@PrimaryKeyJoinColumn(name = "coach_id", referencedColumnName = "profile_id")
public abstract class Coach extends Balance {

}
