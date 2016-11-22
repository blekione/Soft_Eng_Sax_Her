package krugdev.me.siteService;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import krugdev.me.membershipService.MembershipService;


@Entity
@NamedQuery(
	name = "findSiteByName",
	query= "Select s from Site s where s.name = :name")
public class Site {

	@Id
	@GeneratedValue
	private int id;
	private String name;
	@OneToMany(mappedBy="visitedSite", fetch = FetchType.EAGER)
	private Collection<Visitor> visitors = new HashSet<>();
	
	@Transient
	private SiteDBService dbService;
	@Transient
	private MembershipService membershipService;
	
	// required by the JPA
	public Site() {}
	
	private Site(String name, SiteDBService dbService) {
		this.name = name;
		setDbService(dbService);
		
	}
	

	/**
	 * 
	 * @param name - name of the site to be look for in the database or name of the new site
	 * @return if the site with 'name' exists in the database method returns it, 
	 * otherwise method returns new instance of the site with name 'name'
	 * 
	 */
	public static Site instanceOf(String name, SiteDBService dbService) {
		Site site;
		Optional<Site> optionalSite = dbService.getSite(name);
		if (optionalSite.isPresent()) {
			site = optionalSite.get();
			site.setDbService(dbService);
		} else {
			site = new Site(name, dbService);
		}
		return site;
	}
	
	private void setDbService(SiteDBService dbService) {
		this.dbService = dbService;		
	}
	
	/**
	 * adds 'visitor' to the collection and if visitor is a member of charity, method notify 
	 * {@link MembershipService} about the visit
	 * @param visitor
	 * @throws IllegalStateException if {@link MembershipService} is not set for this Site instance
	 */
	public void addVisitor(Visitor visitor) {
		if(visitor.isMember()) {
			checkIfMembershipServiceIsNotSet();
			membershipService.receive(visitor.getMembershipId());
		}
		visitors.add(visitor);
		dbService.persist(visitor);
	}
	
	private void checkIfMembershipServiceIsNotSet() {
		if(membershipService == null) {
			throw new IllegalStateException("MembershipService is not set");
		}
	}
	
	public String getName() {
		return name;
	}

	public int getVisitorsCount() {
		return visitors.size();
	}
	
	public void setMembershipService(MembershipService membershipService) {
		this.membershipService = membershipService;
	}
}
