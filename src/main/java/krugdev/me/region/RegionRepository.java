package krugdev.me.region;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import krugdev.me.DBService;
import krugdev.me.region.domain.MarketingCampaign;
import krugdev.me.region.domain.Region;
import krugdev.me.region.domain.RegionSite;

public class RegionRepository {

	private final DBService DB_SERVICE;
	
	public RegionRepository(DBService dbService) {
		this.DB_SERVICE = dbService;
	}

	public Region newRegion(String name) {
		try{
			return findRegion(name);
		} catch (IllegalArgumentException e) {
			Region region = Region.instanceOf(name);
			DB_SERVICE.persist(region);
			return region;
		}
	}

	public Region findRegion(String name) {
		String queryName = "Region.findRegionByName";
		Map<String, Object> queryParam = new HashMap<>();
		queryParam.put("name", name);
		Optional<Region> dbRegion = DB_SERVICE.findEntity(queryName, queryParam, Region.class);
		if (dbRegion.isPresent()) {
			return dbRegion.get();
		} else {
			throw new IllegalArgumentException("Region with name [" + name + "] doesn't exist in DB"); 
		}
	}

	public void addRegionSite(Region region, RegionSite regionSite) {
		try {
			region.addSite(regionSite);
		} catch (IllegalArgumentException e) {
			throw e;
		}
		DB_SERVICE.update(regionSite);
	}

	public void removeRegionSite(Region region, RegionSite regionSite) {
		region.removeSite(regionSite);
		DB_SERVICE.remove(regionSite);
	}

	public void replaceRegionSite(Region region, RegionSite oldSite, RegionSite newSite) {
		region.replaceSite(oldSite, newSite);
		DB_SERVICE.update(newSite);
		DB_SERVICE.remove(oldSite);
	}

	public void addMarketingCampaign(Region region, MarketingCampaign campaign) {
		region.addMarketingCampaign(campaign);
		DB_SERVICE.update(region);
	}
	
}
