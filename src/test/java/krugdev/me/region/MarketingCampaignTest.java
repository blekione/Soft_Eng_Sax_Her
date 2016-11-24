package krugdev.me.region;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;

import org.junit.Test;

public class MarketingCampaignTest {

	private static final LocalDate START_DATE = LocalDate.now().plusDays(10);
	private static final LocalDate END_DATE = LocalDate.now().plusDays(50);
	private static final Region ANY_REGION = mock(Region.class);
	private static final MarketingManager ANY_MANAGER = mock(MarketingManager.class);
	private MarketingCampaign.Builder builder = 
			new MarketingCampaign.Builder(START_DATE, END_DATE, ANY_REGION, ANY_MANAGER);
	private static final RegionSite ANY_SITE = mock(RegionSite.class);
	
	@Test
	public void builderShouldBuildCampaignWithDefaultValues() {
		MarketingCampaign campaign = builder.build();
		
		assertEquals(START_DATE, campaign.getStartDate());
		assertEquals(END_DATE, campaign.getEndDate());
		assertEquals(ANY_REGION, campaign.getRegion());
		assertEquals(ANY_MANAGER, campaign.getManager());
		
		assertTrue(campaign.getRegionSites().size() == 0);
		assertEquals("unknown", campaign.getName());
		assertEquals(1.0d, campaign.getTargetMultiplier(), 0.001);
	}

	@Test
	public void builderShouldBuildCampaignWithSite() {
		MarketingCampaign campaign = builder.site(ANY_SITE).build();
		assertTrue(campaign.getRegionSites().size() == 1);
	}
	
	@Test
	public void builderShouldBuildCampaignWithManySites() {
		RegionSite siteA = mock(RegionSite.class);
		RegionSite siteB = mock(RegionSite.class);
		MarketingCampaign campaign = builder.site(siteA).site(siteB).build();
		
		assertTrue(campaign.getRegionSites().size() == 2);
	}
}
