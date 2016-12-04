package krugdev.me.region;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;

import org.junit.Test;
import org.junit.runner.RunWith;

import junitparams.JUnitParamsRunner;
import krugdev.me.region.domain.MarketingCampaign;
import krugdev.me.region.domain.Region;
import krugdev.me.region.domain.RegionSite;

@RunWith(JUnitParamsRunner.class)
public class RegionUnitTest {

	private static final String ANY_REGION_NAME = "London";
	Region region = Region.instanceOf(ANY_REGION_NAME);
	RegionSite ANY_REGION_SITE = mock(RegionSite.class);
	LocalDate END_OF_THE_YEAR_DATE = LocalDate.of(2016, 12, 31);
	MarketingCampaign ANY_MARKETING_CAMPAIGN = mock(MarketingCampaign.class);
	
	@Test
	public void shouldReturnInstanceOfRegionWithSetName() {
		Region testRegion = Region.instanceOf(ANY_REGION_NAME);
		assertEquals(ANY_REGION_NAME, region.getName());
	}
	
	@Test
	public void shouldAddSite() {
		region.addSite(ANY_REGION_SITE);
		assertEquals(1, region.getSites().size());
	}

	@Test
	public void shouldAddMultipleSites() {
		RegionSite siteA = mock(RegionSite.class);
		RegionSite siteB = mock(RegionSite.class);
		region.addSite(siteA);
		region.addSite(siteB);
		assertEquals(2, region.getSites().size());
	}
	
	@Test
	public void shouldRemoveSite() {
		region.addSite(ANY_REGION_SITE);
		region.removeSite(ANY_REGION_SITE);
		assertTrue(region.getSites().size() == 0);
	}
	
	@Test
	public void shouldAddMarketingCampaign() {
		region.addMarketingCampaign(ANY_MARKETING_CAMPAIGN);
		assertTrue(region.getMarketingCampaigns().size() == 1);
	}
	
	@Test
	public void shouldAddManyCampaigns() {
		MarketingCampaign campaign1 = mock(MarketingCampaign.class);
		region.addMarketingCampaign(campaign1);
		region.addMarketingCampaign(ANY_MARKETING_CAMPAIGN);
		assertTrue(region.getMarketingCampaigns().size() == 2);
	}
	
	@Test
	public void shouldReplaceSite() {
		RegionSite anySite = mock(RegionSite.class);
		RegionSite oldSite = mock(RegionSite.class, "oldSite");
		RegionSite newSite = mock(RegionSite.class, "newSite");
		
		region.addSite(anySite);
		region.addSite(oldSite);
		region.replaceSite(oldSite, newSite);
		
		assertTrue(region.getSites().contains(newSite));
		assertFalse(region.getSites().contains(oldSite));
	}
}
