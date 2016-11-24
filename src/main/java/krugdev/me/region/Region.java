package krugdev.me.region;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;

public class Region {
	private RegionName name;
	private Collection<RegionSite> regionSites = new HashSet<>();
	
	public Region(RegionName name) {
		this.name = name;
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

	public void tagSites(LocalDate endDate) {
		if(dateIsnotTheLastDayOfTheYear(endDate)) {
			throw new IllegalArgumentException("\'endDate\' need to be last day of the year"
					+ " and it is [" + endDate.toString() + "].");
		} 
		regionSites.stream().forEach(v -> {
			changeRatingBasedOnVisitorsCount(v, endDate);
			checkIfSiteToBePrioritizedInNextMarketingCampaign(v, endDate);
		});
	}
	
	private boolean dateIsnotTheLastDayOfTheYear(LocalDate date) {
		if (date.getMonthValue() == 12 && date.getDayOfMonth() == 31) {
			return false;
		}
		return true;
	}
	
	private void changeRatingBasedOnVisitorsCount(RegionSite regionSite, LocalDate endDate) {
		int visitorsCount = regionSite.getSiteVisitorsCount(endDate);
		if (visitorsCount < 10000) {
			regionSite.setRating(SiteRating.BRONZE);
		} else if (visitorsCount >= 10000 && visitorsCount < 30000) {
			regionSite.setRating(SiteRating.SILVER);
		} else if (visitorsCount >= 30000) {
			regionSite.setRating(SiteRating.GOLD);
		}
	}
	

	private void checkIfSiteToBePrioritizedInNextMarketingCampaign(RegionSite regionSite, LocalDate endDate) {
		int visitorsCount = regionSite.getSiteVisitorsCount(endDate);
		int siteTarget = regionSite.getVisitorsTarget();
		if(visitorsCount < siteTarget) {
			regionSite.setPriorityForMarketingCampaigh();
		}
	}
}
