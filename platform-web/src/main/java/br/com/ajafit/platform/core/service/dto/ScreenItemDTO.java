package br.com.ajafit.platform.core.service.dto;

import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

import lombok.Getter;
import lombok.Setter;

@XmlRootElement
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = Inclusion.NON_EMPTY)
@Getter
@Setter
public class ScreenItemDTO {

	private Long screenId;
	private Long couponId;
	private Long itemId;
	private String name;
	private String descriptions;
	private String imageLarge;
	private String image1;
	private String image2;
	private String image3;
	private String video;
	private String value;
	private String valueFinal;
	private Integer priority;
	private Integer amount;
	private Boolean unavailable;
	private Integer amountToGetOneFree;
	private NutritionInfoDTO[] nutritionInfo;
	private ReviewDTO[] reviews;
	private String rate;
	private Integer totalReviews;
	private String ingredients;
	private ScreenItemDTO[] items;
	private ScreenItemDTO[] related;

}
