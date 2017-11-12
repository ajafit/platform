package br.com.ajafit.platform.core.service.dto;

import java.util.LinkedList;

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
public class RegionDTO {

	private Long regionId;
	private String descriptions;
	private RegionDTO outer;
	private LinkedList<RegionDTO> inner = new LinkedList<>();
	private int level;

}
