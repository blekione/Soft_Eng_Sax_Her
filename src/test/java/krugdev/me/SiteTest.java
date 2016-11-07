package krugdev.me;

import javax.persistence.EntityManager;

import org.junit.Test;

import junit.framework.TestCase;
import regionManagement.PersistenceManager;
import regionManagement.Region;
import regionManagement.Site;

public class SiteTest extends TestCase {
	/*
	@Test
	public void testDatabase() {
		Region region = new Region();
		Site site = new Site(region);
		site.setName("Castell de Mare");
		site.addVisitor(2);
		site.addVisitor(4);
		site.addVisitor(6);
		
		EntityManager em = PersistenceManager.INSTANCE.getEntityManager();
		em.getTransaction().begin();
		em.persist(region);
		em.persist(site);
		em.getTransaction().commit();
		em.close();
		PersistenceManager.INSTANCE.close();
	}
	*/
}
