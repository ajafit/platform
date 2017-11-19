package br.com.ajafit.platform.core.service.dto;

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

import lombok.Getter;
import lombok.Setter;

@XmlRootElement
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = Inclusion.NON_NULL)
@Getter
@Setter
public class EntityDTO {

	private long id;
	private String gender;
	private String name;
	private String password;
	private String value;
	private String email;
	private String descriptions;
	private Date date;
	private String validationHash;
	private String validationChoose;
	private String validationIMGReference;
	private byte[] validationIMG;
	private String accessToken;
	private String[] profiles;
	private String[] icons;
	private String iconTips;

}
