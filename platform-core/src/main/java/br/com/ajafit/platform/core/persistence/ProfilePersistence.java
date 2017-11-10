package br.com.ajafit.platform.core.persistence;

import java.util.Collection;
import java.util.stream.Collectors;

import javax.persistence.Query;

import br.com.ajafit.platform.core.domain.Coach;
import br.com.ajafit.platform.core.domain.Coachee;
import br.com.ajafit.platform.core.domain.Factory;
import br.com.ajafit.platform.core.domain.Manager;
import br.com.ajafit.platform.core.domain.Person;
import br.com.ajafit.platform.core.domain.Profile;

public abstract class ProfilePersistence extends BasePersistence {

	public void removePerson(Person person) {
		em.remove(person);
	}

	public Person createPerson(Person person) {
		em.persist(person);
		return person;
	}

	public Person updatePerson(Person person) {
		person = em.merge(person);
		return person;
	}

	public Collection<Person> filterPersons() {
		Query query = em.createQuery("select r from Person r order by r.name");
		query.setMaxResults(50);
		return query.getResultList();
	}

	public Collection<Person> filterPersons(String filter) {
		Query query = em.createQuery("select r from Person r where r.name like :FILTER or r.email like :FILTER");
		query.setParameter("FILTER", "%" + filter + "%");
		query.setMaxResults(50);
		return query.getResultList();
	}

	public Person getPersonByToken(String token) {
		Query query = em.createQuery("from Person p where p.token = :TOKEN");
		query.setParameter("TOKEN", token);
		if (query.getResultList().isEmpty())
			return null;

		return (Person) query.getResultList().iterator().next();

	}

	public Person getPersonByEmailAndPassword(String email, String password) {
		Query query = em.createQuery("from Person p where p.email = :EMAIL and p.password = :PASSWORD");
		query.setParameter("EMAIL", email).setParameter("PASSWORD", password);
		Collection<Person> col = query.getResultList();
		if (col.isEmpty()) {
			return null;
		} else {
			return (Person) col.iterator().next();
		}

	}

	public Collection<Profile> getProfilesFromPerson(Person person) {
		Query query = em.createQuery("from Profile p where p.person = :PERSON");
		query.setParameter("PERSON", person);
		/*
		 * return ((Collection<Profile>) query.getResultList()).stream().filter((Profile
		 * p) -> !(p instanceof Coach)) .collect(Collectors.toList());
		 */
		return query.getResultList();
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
		query.setParameter("FILTER", "%" + filter + "%");
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

	public void removeCouchee(Coachee couchee) {
		em.remove(couchee);

	}

	public Coachee getCoacheeById(long id) {
		return em.find(Coachee.class, id);
	}

}
