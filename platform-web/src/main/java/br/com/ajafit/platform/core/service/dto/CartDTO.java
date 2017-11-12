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
public class CartDTO {

	private Long cartId;
	private ScreenItemDTO items[];
	private String subTotalValue;
	private String estimatedShipping;
	private String region;

}
