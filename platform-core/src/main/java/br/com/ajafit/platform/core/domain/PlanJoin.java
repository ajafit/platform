package br.com.ajafit.platform.core.domain;

import java.util.Date;

import javax.persistence.Entity;

@Entity
public class PlanJoin extends Plan {

	private Date expire;
}
