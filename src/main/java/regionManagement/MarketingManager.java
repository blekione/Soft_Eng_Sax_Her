package regionManagement;

import java.util.ArrayList;
import java.util.List;

public class MarketingManager {
	
	@SuppressWarnings("unused")
	private int id; // will be set automatically by JPA when implemented
	private String name;
	private Region region;
	List<AdvertisementCampaign> campaigns;
	
	public MarketingManager(String name, Region region) {
		this.setName(name);
		campaigns = new ArrayList<>();
		this.region = region;
	}

	public void startCampaign() {
		AdvertisementCampaign newCampaign = new AdvertisementCampaign();
		campaigns.add(newCampaign);
		region.addCampaign(newCampaign);
	}
	
	public void assignCampaign(AdvertisementCampaign campaign) {
		campaigns.add(campaign);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
