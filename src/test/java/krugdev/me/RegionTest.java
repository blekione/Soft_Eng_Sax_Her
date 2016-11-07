package krugdev.me;

import java.util.ArrayList;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import regionManagement.AdvertisementCampaign;
import regionManagement.AdvertisementCampaignBuilder;
import regionManagement.Region;
import regionManagement.RegionName;
import regionManagement.Site;
import regionManagement.exceptions.WrongSiteException;

public class RegionTest {
	
	@Rule
	public final ExpectedException exception = ExpectedException.none();

	@Test
	public void testIfAddingSameSiteToSameRegionThrowsException()
			throws WrongSiteException {
		exception.expect(WrongSiteException.class);
		Region region = new Region(RegionName.LONDON);
		Site site1 = new Site(region);
			site1.setName("Big Ben");
		Site site2 = new Site(region);
			site2.setName("Cutty Sark");
		region.addSite(site1);
		region.addSite(site2);
		region.addSite(site1);
	}

	@Test
	public void testIfAddingSitesListWithSiteWhichIsAlreadyPartOfTheRegionThrowsException()
			throws WrongSiteException {
		exception.expect(WrongSiteException.class);
		Region region = new Region(RegionName.LONDON);
		Site site1 = new Site(region);
			site1.setName("Big Ben");
		Site site2 = new Site(region);
			site2.setName("Cutty Sark");
		List<Site> sites = new ArrayList<>();
			sites.add(site1);
			sites.add(site2);
		region.addSite(site1);
		region.addSites(sites);
	}
	
	@Test
	public void testIfAddingCampaignBeforeMarketingManagerIsSetForRegionThrowsException() 
			throws WrongSiteException, IllegalStateException {
		exception.expect(IllegalStateException.class);
		Region region = new Region(RegionName.LONDON);
		Site site = new Site(region);
		AdvertisementCampaignBuilder campaignBuilder = new AdvertisementCampaignBuilder(region);		
		AdvertisementCampaign campaign = campaignBuilder.addSite(site).build();
		region.addCampaign(campaign);
	}
}
