package br.com.ajafit.platform.core.persistence;

import java.util.Collection;

import javax.persistence.Query;

import br.com.ajafit.platform.core.domain.Coachee;
import br.com.ajafit.platform.core.domain.Factory;
import br.com.ajafit.platform.core.domain.Manager;
import br.com.ajafit.platform.core.domain.Person;
import br.com.ajafit.platform.core.domain.Region;

public abstract class ProfilePersistence extends BasePersistence {

	public Person createPerson(Person person) {
		em.persist(person);
		return person;
	}

	public Person getPersonById(long id) {
		return em.find(Person.class, id);
	}

	public Manager createManager(Manager manager) {
		em.persist(manager);
		return manager;
	}

	public Manager getManagerById(long id) {
		return em.find(Manager.class, id);
	}
	public Collection<Manager> filterManager(String filter) {
		Query query = em.createQuery("select r from Manager r where r.name like :FILTER");
		query.setParameter("FILTER", "%"+filter+"%");
		return query.getResultList();
	}

	public Factory createFactory(Factory factory) {
		em.persist(factory);
		return factory;
	}

	public Factory getFactoryById(long id) {
		return em.find(Factory.class, id);
	}

	public Coachee createCouchee(Coachee couchee) {
		em.persist(couchee);
		return couchee;
	}

	public Coachee getCoacheeById(long id) {
		return em.find(Coachee.class, id);
	}

}
