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
	
	
	public void replaceMarketingCampaign(MarketingCampaign campaingToBeReplace,
			MarketingCampaign replacementCampaign) {
		int indexOfcampaignToBeReplaced = campaigns.indexOf(campaingToBeReplace);
		if(indexOfcampaignToBeReplaced == -1) {// campaign is not found
			throw new IllegalArgumentException("Can't find the campaing to be replace");
		}
		campaigns.set(indexOfcampaignToBeReplaced, replacementCampaign);
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

	
	
}
