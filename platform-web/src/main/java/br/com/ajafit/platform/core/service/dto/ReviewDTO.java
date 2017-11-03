package br.com.ajafit.platform.core.service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReviewDTO {
	private Long profileId;
	private Long saleableId;
	private Integer rate;
	private String value;
	private String profileName;
	private String profileImage;
	private String date;

}
