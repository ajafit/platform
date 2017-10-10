package br.com.ajafit.platform.core.service.dto;

import br.com.ajafit.platform.core.domain.Person;

public class EntityDTOConverter {

	public static EntityDTO parse(Person person) {
		EntityDTO dto = new EntityDTO();
		dto.setId(person.getId());
		dto.setName(person.getName());
		dto.setDate(person.getRegister());
		dto.setEmail(person.getEmail());
		return dto;
	}
}
