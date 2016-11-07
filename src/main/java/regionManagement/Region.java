package regionManagement;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import regionManagement.exceptions.WrongSiteException;

@Entity
public class Region {
	
	@Id
	@GeneratedValue
	private Integer id;
	@OneToMany(mappedBy="region")
	private List<Site> sites;
	private MarketingManager manager;
	private List<AdvertisementCampaign> campaigns;
	private RegionName regionName;
	
	//default constructor required by JPA
	public Region() {
	}
	
	public Region(RegionName regionName) {
		this.regionName = regionName;
		this.sites = new ArrayList<Site>();
	}
	
	public void addSite(Site site) throws WrongSiteException {
		if (sites.contains(site)) {
			throw new WrongSiteException("Site " + site.getName()
					+ "(id: " + site.getId() + ") already exists in region "
					+ this.regionName.getName());
		} else {
			sites.add(site);
		}
	}
	
	public void addSites(List<Site> newSites)
			throws WrongSiteException {
		for (Site site : newSites) {
			addSite(site);
		}
	}
	
	public boolean removeSite(Site site) {
		return sites.remove(site);
	}
	
	public List<Site> getSites() {
		return sites;
	}

	public void setMarketingManager(MarketingManager manager) {
		this.manager = manager;
	}
	
	public void addCampaign(AdvertisementCampaign campaign) {
		initializeRegionalCampaignsIfThisCampaignIsRegionFirst();
		if (checkIfManagerIsNotSet()) {
			throw new IllegalStateException("Before you can start Advertisement Campaign,"
					+ " you need to set Marketing Manager for region " + this.getName());
		} 
		campaigns.add(campaign);
	}

	private void initializeRegionalCampaignsIfThisCampaignIsRegionFirst() {
		if (campaigns == null) {
			campaigns = new ArrayList<>();
		}
	}

	private boolean checkIfManagerIsNotSet() {
		if (manager == null) {
			return true;
			}
		return false;
	}

	public String getName() {
		return regionName.getName();
	}
	
	public List<AdvertisementCampaign> getCampaigns() {
		return campaigns;
	}
	
	public MarketingManager getManager() {
		return manager;
	}
}
