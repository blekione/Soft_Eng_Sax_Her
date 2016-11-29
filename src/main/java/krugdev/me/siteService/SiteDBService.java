package krugdev.me.siteService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.Query;

public class SiteDBService {
	
	private EntityManagerFactory emf;
	private EntityManager em;
	
	public SiteDBService() {
		emf = Persistence.createEntityManagerFactory("sax_her");
	}

	public void persist(Object object) {
		em = emf.createEntityManager();
		em.getTransaction().begin();
		em.persist(object);
		em.getTransaction().commit();
		em.close();
	}

	public <T> Optional<T> getEntity(String siteName) {
		em = emf.createEntityManager();
		Query query = em.createNamedQuery("findSiteByName");
		query.setParameter("name", siteName);
		Optional<T> optionalSite;
		try {
			@SuppressWarnings("unchecked")
			T entity = (T) query.getSingleResult();
			optionalSite = Optional.of(entity);
		} catch	(NoResultException e) {
			optionalSite = Optional.empty();
		} finally {
			em.close();
		}
		return optionalSite;		
	}

	public void update(Site site) {
		em = emf.createEntityManager();
		em.getTransaction().begin();
		em.merge(site);
		em.getTransaction().commit();
	}

	public void remove(Object entity) {
		em = emf.createEntityManager();
		em.getTransaction().begin();
		em.remove(em.contains(entity) ? entity : em.merge(entity));
		em.getTransaction().commit();
		
	}

	public List<Visitor> findVisitors(Site site, LocalDate startDate, LocalDate endDate) {
		em = emf.createEntityManager();
		Query query = em.createNamedQuery("findSiteVisitorsByPeriod");
		query.setParameter("site", site)
			.setParameter("startDate", startDate)
			.setParameter("endDate", endDate);
		@SuppressWarnings("unchecked")
		List<Visitor> visitors = query.getResultList();
		return visitors;
	}
	
	public void close() {
		emf.close();
	}
}
