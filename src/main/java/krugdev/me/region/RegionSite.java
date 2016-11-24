package krugdev.me.region;

import java.time.LocalDate;

import krugdev.me.siteService.Site;

public class RegionSite {

	public Site getSite() {
		return null;

	}

	public void setRating(SiteRating rating) {
		
	}

	
	public int getSiteVisitorsCount(LocalDate endDate) {
	// TODO modify to use local site
		Site site = getSite();
		return site.getVisitorsCountForPeriod(getFirstDayOfTheYear(endDate), endDate);	
	}

	private LocalDate getFirstDayOfTheYear(LocalDate date) {
		return date.withMonth(1).withDayOfMonth(1);
	}

	public void setPriorityForMarketingCampaigh() {
		
	}

	public int getVisitorsTarget() {
		return 0;
	}

}
