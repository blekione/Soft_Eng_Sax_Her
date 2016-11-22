package krugdev.me.siteService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.Query;

public class SiteDBService {
	
	EntityManagerFactory emf;
	EntityManager em;
	
	public SiteDBService() {
		emf = Persistence.createEntityManagerFactory("sax_her");
	}

	public Collection<Visitor> getVisitorsForPeriod(LocalDate startDate, LocalDate endDate) {
		// TODO Auto-generated method stub
		return new ArrayList<Visitor>();
	}

	public void persist(Object object) {
		em = emf.createEntityManager();
		em.getTransaction().begin();
		em.persist(object);
		em.getTransaction().commit();
		em.close();
	}

	public Optional<Site> getSite(String siteName) {
		em = emf.createEntityManager();
		Query query = em.createNamedQuery("findSiteByName");
		query.setParameter("name", siteName);
		Optional<Site> optionalSite;
		try {
			Site site = (Site)query.getSingleResult();
			optionalSite = Optional.of(site);
		} catch	(NoResultException e) {
			optionalSite = Optional.empty();
		} finally {
			em.close();
		}
		return optionalSite;
			
	}
	
	public void close() {
		emf.close();
	}
}
