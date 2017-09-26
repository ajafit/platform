package br.com.ajafit.platform.core.service.dto;

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

@XmlRootElement
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = Inclusion.NON_EMPTY)
public class EntityDTO {

	private long id;
	private String gender;
	private String name;
	private String email;
	private String descriptions;
	private Date date;
	private String validation;
	private String validationIMGReference;
	private byte[] validationIMG;
	
	public String getValidationIMGReference() {
		return validationIMGReference;
	}

	public void setValidationIMGReference(String validationIMGReference) {
		this.validationIMGReference = validationIMGReference;
	}

	public byte[] getValidationIMG() {
		return validationIMG;
	}

	public void setValidationIMG(byte[] validationIMG) {
		this.validationIMG = validationIMG;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getValidation() {
		return validation;
	}

	public void setValidation(String validation) {
		this.validation = validation;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getDescriptions() {
		return descriptions;
	}

	public void setDescriptions(String descriptions) {
		this.descriptions = descriptions;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

}
