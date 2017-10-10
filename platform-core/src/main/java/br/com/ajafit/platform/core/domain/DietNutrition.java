package br.com.ajafit.platform.core.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class DietNutrition {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "diet_nutrition_id")
	private long id;

	@ManyToOne
	@JoinColumn(name = "diet_id")
	private Diet diet;

	@ManyToOne
	@JoinColumn(name = "nutrition_table_id")
	private NutritionTable nutritionTable;

	private int value;

}
