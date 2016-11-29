package krugdev.me.region;

import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.Query;

public class RegionDBService {

	EntityManagerFactory emf;
	EntityManager em;
	
	public RegionDBService() {
		emf = Persistence.createEntityManagerFactory("sax_her");
	}

	public void persist(Object object) {
		em = emf.createEntityManager();
		em.getTransaction().begin();
		if (em.contains(object)) {
			em.persist(object);
			
		} else {
			em.merge(object);
		}
		em.getTransaction().commit();
		em.close();
	}
	
	public <T> Optional<T> getGenericEntity(String queryName, int id) {
		em = emf.createEntityManager();
		Query query = em.createNamedQuery(queryName);
		query.setParameter("id", id);
		Optional<T> optionalEntity;
		try {
			@SuppressWarnings("unchecked")
			T entity = (T) query.getSingleResult();
			optionalEntity = Optional.of(entity);
		} catch	(NoResultException e) {
			optionalEntity = Optional.empty();
		} finally {
			em.close();
		}
		return optionalEntity;	
	}

	public <T> Optional<T> getEntity(String name) {
		em = emf.createEntityManager();
		Query query = em.createNamedQuery("findRegionByName");
		query.setParameter("name", name);
		Optional<T> optionalEntity;
		try {
			@SuppressWarnings("unchecked")
			T entity = (T) query.getSingleResult();
			optionalEntity = Optional.of(entity);
		} catch	(NoResultException e) {
			optionalEntity = Optional.empty();
		} finally {
			em.close();
		}
		return optionalEntity;	
	}
	
	public void remove(Object entity) {
		em = emf.createEntityManager();
		em.getTransaction().begin();
		em.remove(em.contains(entity) ? 
				entity 
				: em.merge(entity));
		em.getTransaction().commit();
		em.close();
	}
	
	public void close() {
		emf.close();
	}

	public void merge(Object entity) {
		em = emf.createEntityManager();
		em.getTransaction().begin();
		em.merge(entity);
		em.getTransaction().commit();
		em.close();
	}
	
}
