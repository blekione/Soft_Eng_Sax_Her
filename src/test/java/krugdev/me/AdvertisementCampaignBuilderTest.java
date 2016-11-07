package krugdev.me;

import static org.junit.Assert.*;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import regionManagement.AdvertisementCampaign;
import regionManagement.AdvertisementCampaignBuilder;
import regionManagement.MarketingManager;
import regionManagement.Region;
import regionManagement.RegionName;
import regionManagement.Site;
import regionManagement.exceptions.WrongSiteException;

public class AdvertisementCampaignBuilderTest {
	

	@Rule
	public final ExpectedException exception = ExpectedException.none();

	AdvertisementCampaignBuilder campaignBuilder;
	
	@Before
	public void buildCampaign() 
			throws WrongSiteException {
		Region region = new Region(RegionName.LONDON);
		
		Site site1 = new Site(region);
			site1.setName("Big Ben");
		Site site2 = new Site(region);
			site2.setName("Cutty Sark");
		region.addSite(site1);
		region.addSite(site2);
		
		MarketingManager manager = new MarketingManager("Marcin", region);
		region.setMarketingManager(manager);
		
		AdvertisementCampaignBuilder camapaignBuilder = new AdvertisementCampaignBuilder(region);
		campaignBuilder = camapaignBuilder.addSite(site1);
	}
	
	@Test
	public void testIfRegionIsSet() {
		AdvertisementCampaign campaign = campaignBuilder.build(); 
		assertEquals(campaign.getRegion().getName(), "London");
	}
	
	@Test
	public void testIfCampaignIsAssignedToRegion() {
		AdvertisementCampaign campaign = campaignBuilder.build();
		List<AdvertisementCampaign> regionalCampaigns = campaign.getRegion().getCampaigns();
		assertEquals(regionalCampaigns.get(regionalCampaigns.size() - 1), campaign);
	}
	
	@Test
	public void testIfSiteIsAddedToCampaign() {
		AdvertisementCampaign campaign = campaignBuilder.build();
		assertEquals(campaign.getSites().get(0), campaign.getRegion().getSites().get(0));
	}

	@Test
	public void testIfManagerIsSet() {
		AdvertisementCampaign campaign = campaignBuilder.build();
		assertEquals(campaign.getManager(), campaign.getRegion().getManager());
	}
	
	@Test 
	public void testIfStartDateIsSet() {
		LocalDate campaignStartDate = LocalDate.now().plusDays(1);
		Integer campaignDuration = 40;
		AdvertisementCampaign campaign = 
				campaignBuilder.setStartDate(campaignStartDate)
					.setDuration(campaignDuration)
					.build();
		assertEquals(campaign.getStartDate(), campaignStartDate);
	}
	
	public void testIfEndDataIsSet() {
		LocalDate campaignStartDate = LocalDate.now().plusDays(1);
		Integer campaignDuration = 40;
		AdvertisementCampaign campaign = 
				campaignBuilder.setStartDate(campaignStartDate)
					.setDuration(campaignDuration)
					.build();
		assertEquals(campaign.getEndDate(), campaignStartDate.plusDays(campaignDuration));
	}
	
	@Test
	public void testIfExceptionIsThrownIfStartDateIsSetInThePast() {
		exception.expect(DateTimeException.class);
		LocalDate campaignStartDate = LocalDate.now().minusDays(2);
		Integer campaignDuration = 40;
		campaignBuilder.setStartDate(campaignStartDate)
					.setDuration(campaignDuration)
					.build();
	}
	
	@Test
	public void testIfAdvertisementCampaignBuildThrowExceptionWhenCheckNotPassed() {
		//TODO write test for the builder
	}
	
}
