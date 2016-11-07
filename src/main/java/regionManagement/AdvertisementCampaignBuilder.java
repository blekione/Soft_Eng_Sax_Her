package regionManagement;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.List;

import regionManagement.exceptions.ArgumentOutOfRangeException;
import regionManagement.exceptions.WrongSiteException;

public class AdvertisementCampaignBuilder {
	
	AdvertisementCampaign campaign;
	
	public AdvertisementCampaignBuilder(Region region) {
		this.campaign = new AdvertisementCampaign();
		this.campaign.setRegion(region);
		region.addCampaign(campaign);
		setManager(region.getManager());
	}
	
	private void setManager(MarketingManager manager) {
		campaign.setManager(manager);
	}
	
	public AdvertisementCampaignBuilder addSite(Site site)
			throws WrongSiteException {
		
		if(checkIfSiteFillAllRequirements(site)) {
			campaign.addSite(site);
		}
		return this;		
	}

	private boolean checkIfSiteFillAllRequirements(Site site)
			throws WrongSiteException {
		if (checkIfSiteWasInPreviousCampaign()){
			throw new WrongSiteException("Site " + site.getName()
					+ "was promoted in previous campaign.");
		} else if (checkIfSiteIsNotInRegion(site)) {
			throw new WrongSiteException("Site " + site.getName() 
					+ " is not in Region: " + campaign.getRegion().getName());			
		}
		return true;
	}

	private boolean checkIfSiteWasInPreviousCampaign() {
		// TODO get last campaign from database and check if site was not there
		return false;
	}
	
	private boolean checkIfSiteIsNotInRegion(Site site) {
		if (site.getRegion().equals(campaign.getRegion())) {
			return false;
		} else {
			return true;	
		}
	}

	public AdvertisementCampaignBuilder addSites(List<Site> sites)
			throws WrongSiteException {
		for (Site site : sites) {
			this.addSite(site);
		}
		return this;
	}
	
	public AdvertisementCampaignBuilder setStartDate(LocalDate date) 
			throws DateTimeException {
		if (checkIfStartDateIsNotInThePast(date)) {
			campaign.setStartDate(date);
		} 	
		return this;
	}
	
	private boolean checkIfStartDateIsNotInThePast(LocalDate date) {
		if (date.compareTo(LocalDate.now()) >= 0) {
			return true;
		} else {
			throw new DateTimeException("Entered campaign start date (" + date.toString()
			+ ") can't be befor today's date: " + LocalDate.now());
		}
	}

	public AdvertisementCampaignBuilder setDuration(Integer days) {
		//TODO reconsider check for how long campaign can last
		campaign.setEndDate(campaign.getStartDate().plusDays(days));
		return this;
	}
	
	public AdvertisementCampaignBuilder setSiteTargetMultiplier(Double targetMultiplayer) 
			throws ArgumentOutOfRangeException {
		checkForExceptionIfVisitorTargetIsOutOfRange(targetMultiplayer);
		campaign.setVisitorsTargetMultiplier(targetMultiplayer);
		return this;
	}
	
	private void checkForExceptionIfVisitorTargetIsOutOfRange(Double targetMultiplayer) 
			throws ArgumentOutOfRangeException {
		//TODO reconsider check for max vistors target multiplayer
		if (targetMultiplayer < 1.0) {
			throw new ArgumentOutOfRangeException("Target Multiplayer can't be less than 1,"
					+ " your value is :" + targetMultiplayer);
		}
	}

	public AdvertisementCampaign build() {
		
		return this.campaign;
	}
}
