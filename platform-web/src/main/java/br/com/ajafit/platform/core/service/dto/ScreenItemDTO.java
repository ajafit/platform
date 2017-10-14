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

	private Long id;
	private String descriptions;
	private String image;
	private String value;
	private String valueFinal;
	private Integer priority;
	private Integer amount;

	private ScreenItemDTO[] items;

}
