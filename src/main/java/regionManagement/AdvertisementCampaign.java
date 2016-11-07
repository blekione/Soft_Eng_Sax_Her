package regionManagement;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AdvertisementCampaign {

	private MarketingManager manager;
	private List<Site> sites;
	private LocalDate startDate;
	private LocalDate endDate;
	private Double visitorsTargetMultiplier = 1.1;
	private Region region;
	
	public AdvertisementCampaign() {
		this.sites = new ArrayList<>();
	}
	
	public MarketingManager getManager() {
		return manager;
	}
	public void setManager(MarketingManager manager) {
		this.manager = manager;
	}
	public List<Site> getSites() {
		return sites;
	}
	public void addSite(Site site) {
		this.sites.add(site);
	}
	public LocalDate getStartDate() {
		return startDate;
	}
	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}
	public LocalDate getEndDate() {
		return endDate;
	}
	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}
	public Double getVisitorsTargetMultiplier() {
		return visitorsTargetMultiplier;
	}
	public void setVisitorsTargetMultiplier(Double visitorsTargetMultiplier) {
		this.visitorsTargetMultiplier = visitorsTargetMultiplier;
	}
	public Region getRegion() {
		return region;
	}
	public void setRegion(Region region) {
		this.region = region;
	}
}
