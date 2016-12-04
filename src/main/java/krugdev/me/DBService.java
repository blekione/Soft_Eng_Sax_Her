package krugdev.me;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Map.Entry;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.Query;


public class DBService {
	
	private EntityManagerFactory emf;
	private EntityManager em;

	public DBService(String database) {
		emf = Persistence.createEntityManagerFactory(database);
	}

	public void persist(Object... objects) {
		em = emf.createEntityManager();
		em.getTransaction().begin();
		for(Object object : objects) {
		em.persist(object);
		}
		em.getTransaction().commit();
		em.close();
	}
	
	public <T> Optional<T> findEntity(String queryName, Map<String, Object> queryParam, Class<T> class1) {
		em = emf.createEntityManager();
		Query query = setQuery(em, queryName, queryParam);
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
	
	private Query setQuery(EntityManager em, String queryName, Map<String, Object> queryParam) {
		em = emf.createEntityManager();
		Query query = em.createNamedQuery(queryName);
		for (Entry<String, Object> entry : queryParam.entrySet()) {
			query.setParameter(entry.getKey(), entry.getValue());
		}
		return query;
	}
	
	public <T> List<T> findEntities(String queryName, Map<String, Object> queryParam, Class<T> class1) {
		em = emf.createEntityManager();
		Query query = setQuery(em, queryName, queryParam);
		@SuppressWarnings("unchecked")
		List<T> entities = query.getResultList();
			em.close();
		return entities;
	}	

	public void update(Object... entities) {
		em = emf.createEntityManager();
		em.getTransaction().begin();
		for (Object entity : entities) {
			em.persist(em.contains(entity) ? entity : em.merge(entity));
		}
		em.getTransaction().commit();
		em.close();
	}
	
	public void remove(Object... entities) {
		em = emf.createEntityManager();
		em.getTransaction().begin();
		
		for (Object entity : entities) {
			em.remove(em.contains(entity) ? entity : em.merge(entity));
		}
		em.getTransaction().commit();
		em.close();	
	}
	
	public void close() {
		emf.close();
	}
}
