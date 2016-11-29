package krugdev.me.siteService;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import krugdev.me.membershipService.MembershipService;

@Entity
@NamedQueries({
@NamedQuery(
	name = "findSiteByName",
	query= "Select s from Site s where s.name = :name"),
@NamedQuery(
		name = "findSiteByChargingStructure",
		query= "Select s from Site s where s.chargingStructure = :chargingStructure")
})
public class Site  {
	
	@Id
	@GeneratedValue
	private int siteId;
	
	private String name;
	
	@OneToMany(mappedBy="visitedSite", fetch = FetchType.EAGER)
	@Fetch(FetchMode.SELECT)
	private Collection<Visitor> visitors = new HashSet<>();
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="STRUCTURE_ID")
	private ChargingStructure chargingStructure;
	
	@Transient
	private SiteDBService dbService;
	
	@Transient
	private MembershipService membershipService;

	
	
	// required by the JPA
	protected Site() {}
	
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
		Optional<Site> optionalSite = dbService.getEntity(name);
		if (optionalSite.isPresent()) {
			site = optionalSite.get();
			site.setDbService(dbService);
		} else {
			site = new Site(name, dbService);
			dbService.persist(site);
		}
		return site;
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
	
	public void setMembershipService(MembershipService membershipService) {
		this.membershipService = membershipService;
	}
	
	public void setDbService(SiteDBService dbService) {
		this.dbService = dbService;		
	}

	public void setCharginStructure(ChargingStructure chargingStructure) {
		if (this.chargingStructure == null) {
			instaniateChargingStructure(chargingStructure);
		} else {
			replaceChargingStructure(chargingStructure);
		}
	}
	
	private void instaniateChargingStructure(ChargingStructure newStructure) {
		this.chargingStructure = newStructure;
		dbService.update(this);
	}
	
	// replaces in database old ChargingStructure by the new one 
	private void replaceChargingStructure(ChargingStructure newStructure){
		ChargingStructure structureToRemoveFromDB = this.chargingStructure;
		instaniateChargingStructure(newStructure);
		dbService.remove(structureToRemoveFromDB);
	}

	public ChargingStructure getChargingStructure() {
		return chargingStructure;
	}
	
	public String getName() {
		return name;
	}

	public int getVisitorsCount() {
		return visitors.size();
	}
	
	public int getVisitorsCountForPeriod(LocalDate startDate, LocalDate endDate) {
		List<Visitor> visitors = dbService.findVisitors(this, startDate, endDate);
		return visitors.size();
	}
	
	public boolean isDBConSet() {
		if (dbService == null) {
			return false;
		} else {
			return true;
		}	
	}
}
