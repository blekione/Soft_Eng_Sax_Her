package krugdev.me.region;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

public class Region {
	private RegionName name;
	private Collection<RegionSite> regionSites = new HashSet<>();
	private List<MarketingCampaign> campaigns = new ArrayList<>();
	
	public Region(RegionName name) {
		this.name = name;
	}

	public void tagSites(LocalDate endDate) {
		if(dateIsnotTheLastDayOfTheYear(endDate)) {
			throw new IllegalArgumentException("\'endDate\' need to be last day of the year"
					+ " and it is [" + endDate.toString() + "].");
		} 
		Collection<RegionSite> evaluatedRegionSites = new HashSet<>();
		regionSites.stream().forEach(v -> {
			evaluatedRegionSites.add(v.evaluateAtTheEndOfTheYear(endDate));
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
	}

	public Collection<MarketingCampaign> getMarketingCampaigns() {
		return campaigns;
	}

	public MarketingCampaign getLastMarketingCampaign() {
		return campaigns.get(campaigns.size()-1);
	}
	
	public RegionName getName() {
		return name;
	}

	public void addSite(RegionSite site) {
		regionSites.add(site);
	}
	
	public Collection<RegionSite> getSites() {
		return regionSites;
	}

	public void removeSite(RegionSite regionSite) {
		regionSites.remove(regionSite);
	}
	
	public void replaceSite(RegionSite oldSite, RegionSite newSite) {
		regionSites.remove(oldSite);
		regionSites.add(newSite);
	}
}
