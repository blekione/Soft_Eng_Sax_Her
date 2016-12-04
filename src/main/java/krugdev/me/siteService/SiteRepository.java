package krugdev.me.siteService;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import krugdev.me.DBService;
import krugdev.me.membershipService.MembershipService;
import krugdev.me.siteService.domain.ChargingStructure;
import krugdev.me.siteService.domain.Site;
import krugdev.me.siteService.domain.Visitor;

public class SiteRepository {
	
	public final DBService DB_SERVICE;
	
	public SiteRepository(DBService dbService, MembershipService membershipService) {
		DB_SERVICE = dbService;
	}
	
	/**
	 * 
	 * @param name - name of the site to be look for in the database or name of the new site
	 * @return if the site with 'name' exists in the database method returns it, 
	 * otherwise method returns new instance of the site with name 'name'
	 * 
	 */
	public Site newSite(String name) {
		Site site;
		try {
			site = findSite(name);
		} catch (IllegalArgumentException e) { 
			site = Site.instanceOf(name);
			DB_SERVICE.persist(site);
		}
		return site;
	}

	public Site findSite(String name) {
		String queryName = "Site.findSiteByName";
		Map<String, Object> queryParam = new HashMap<>();
		queryParam.put("name", name);
		Optional<Site> siteDB = DB_SERVICE.findEntity(queryName, queryParam, Site.class);
		if (siteDB.isPresent()) {
			return siteDB.get();
		} else { 
			throw new IllegalArgumentException("DB has no Site entity with name [" + name + "]");
		}
	}

	/**
	 * adds {@link Visitor} to the {@link Site} 
	 * @param site, visitor
	 * @throws IllegalStateException if {@link MembershipService} is not set for this Site instance
	 */
	public void addVisitorToSite(Site site, Visitor visitor) {
		site.addVisitor(visitor);
		DB_SERVICE.update(visitor, site);
	}

	public void setChargingStructureForSite(Site site, ChargingStructure chargingStructure) {
		site.setCharginStructure(chargingStructure);
		DB_SERVICE.update(site, chargingStructure);
	}

	public int getSiteVisitorsCountForPeriod(Site site, LocalDate startDate, LocalDate endDate) {
		String queryName = "Visitor.findSiteVisitorsByPeriod";
		Map<String, Object> queryParam = new HashMap<>();
		queryParam.put("site", site);
		queryParam.put("startDate", startDate);
		queryParam.put("endDate", endDate);
		List<Visitor> visitors = DB_SERVICE.findEntities(queryName, queryParam, Visitor.class);
		return visitors.size();
	}
}
