package krugdev.me.region;

import java.util.HashSet;
import java.util.Set;

import krugdev.me.region.domain.MarketingCampaign;
import krugdev.me.region.domain.Region;
import krugdev.me.region.domain.RegionSite;

public class RegionService {

	
	public RegionService() {
	}

	public Set<RegionSite> getRegionSitesForCampaign(Region region) {
		Set<RegionSite> regionSites = region.getSites();
		MarketingCampaign lastCampaign = region.getMarketingCampaigns()
				.get(region.getMarketingCampaigns().size() - 1);
		Set<RegionSite> lastCampaignSites = lastCampaign.getRegionSites();
		Set<RegionSite> viableRegions = new HashSet<>();
		regionSites.forEach(v -> {
			if(!lastCampaignSites.contains(v)) {
				viableRegions.add(v);
			}
		});
		return viableRegions;
	}
}
