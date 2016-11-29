package krugdev.me.region;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;


@Entity
@NamedQuery(
		name = "findRegionByName",
		query= "Select r from Region r where r.name = :name")
public class Region {
	@Id
	@GeneratedValue
	private int id;
	private String name;
	@OneToMany(mappedBy = "region", fetch = FetchType.EAGER)
	private Set<RegionSite> regionSites;
	@OneToMany(mappedBy = "region", fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
	@Fetch(FetchMode.SELECT)
	private List<MarketingCampaign> campaigns;
	
	@Transient
	RegionDBService dbService;
	
	// required by JPA
	protected Region() {}
	
	private Region(String name, RegionDBService dbService) {
		this.name = name;
		regionSites = new HashSet<>();
		campaigns = new ArrayList<>();
		setDbService(dbService);
	}
	
	public static Region instanceOf(String name, RegionDBService dbService) {
		Region region;
		Optional<Region> optionalSite = dbService.getEntity(name);
		if (optionalSite.isPresent()) {
			region = optionalSite.get();
			region.setDbService(dbService);
		} else {
			region = new Region(name, dbService);
			dbService.persist(region);
		}
		return region;
	}


	private void setDbService(RegionDBService dbService) {
		this.dbService = dbService;
	}

	public void tagSites(LocalDate endDate) {
		if(dateIsnotTheLastDayOfTheYear(endDate)) {
			throw new IllegalArgumentException("\'endDate\' need to be last day of the year"
					+ " and it is [" + endDate.toString() + "].");
		} 
		
		Set<RegionSite> evaluatedRegionSites = new HashSet<>();
		regionSites.stream().forEach(v -> {
			RegionSite newRegion = v.evaluateAtTheEndOfTheYear(endDate);
			evaluatedRegionSites.add(newRegion);
			dbService.persist(newRegion);
			dbService.remove(v);
		});
		regionSites = evaluatedRegionSites;
	}
	
	private boolean dateIsnotTheLastDayOfTheYear(LocalDate date) {
		if (date.getMonthValue() == 12 && date.getDayOfMonth() == 31) {
			return false;
		}
		return true;
	}
	
	public Collection<RegionSite> getPrioritySites() {
		List<RegionSite> prioritySites = new ArrayList<>();
		List<RegionSite> sitesNotInLastCampaign = getSitesNotInLastCampaign();
		sitesNotInLastCampaign.stream().forEach(v -> {
			if(v.isPriorityForCampaing()) {
				prioritySites.add(v);
			}
		});
		return prioritySites;
	}	
	
	public Collection<RegionSite> getNonPrioritySites() {
		List<RegionSite> nonPrioritySites = new ArrayList<>();
		List<RegionSite> sitesNotInLastCampaign = getSitesNotInLastCampaign();
		sitesNotInLastCampaign.stream().forEach(v -> {
			if(!v.isPriorityForCampaing()) {
				nonPrioritySites.add(v);
			}
		});
		return nonPrioritySites;
	}
	
	private List<RegionSite> getSitesNotInLastCampaign() {
		List<RegionSite> sitesNotInLastCampaign = new ArrayList<>();	
		regionSites.stream().forEach(v -> {
			if(!v.getLastMarketingCampaign().equals(getLastMarketingCampaign())) {
				sitesNotInLastCampaign.add(v);
			}
		});
		return sitesNotInLastCampaign;
	}

	public void addMarketingCampaign(MarketingCampaign campaign) {
		campaigns.add(campaign);
		dbService.persist(campaign);
	}

	public Collection<MarketingCampaign> getMarketingCampaigns() {
		return campaigns;
	}

	public MarketingCampaign getLastMarketingCampaign() {
		return campaigns.get(campaigns.size()-1);
	}
	
	public String getName() {
		return name;
	}

	public void addSite(RegionSite regionSite) {
		dbService.persist(regionSite);
		regionSites.add(regionSite);
	}
	
	@OneToMany
	public Set<RegionSite> getSites() {
		return regionSites;
	}

	public void removeSite(RegionSite regionSite) {
		regionSites.remove(regionSite);
		dbService.remove(regionSite);
	}
	
	public void replaceSite(RegionSite oldSite, RegionSite newSite) {
		addSite(newSite);	
		removeSite(oldSite);
	}

}
