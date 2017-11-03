package br.com.ajafit.platform.core.service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
class NutritionInfoDTO {
	private String key;
	private String value;
	private String vd;
}
