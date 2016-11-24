package krugdev.me.region;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;

import org.junit.Test;
import org.junit.runner.RunWith;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

@RunWith(JUnitParamsRunner.class)
public class RegionUnitTest {

	private static final RegionName ANY_REGION_NAME = RegionName.LONDON;
	Region region = new Region(ANY_REGION_NAME);
	RegionSite ANY_REGION_SITE = mock(RegionSite.class);
	LocalDate END_OF_THE_YEAR_DATE = LocalDate.of(2016, 12, 31);
	MarketingCampaign ANY_MARKETING_CAMPAIGN = mock(MarketingCampaign.class);
	
	@Test
	public void shouldSetRegionName() {
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
	
	
	
	public Object[] getDatesWhichAreNotEndOfTheYear() {
		return new LocalDate[] {
				LocalDate.of(2016, 1, 1),
				LocalDate.of(2016, 12, 30),
				LocalDate.of(2017, 5, 21)
				};
	}
	
	@Test(expected = IllegalArgumentException.class)
	@Parameters(method = "getDatesWhichAreNotEndOfTheYear")
	public void shouldThrowIllegalArgumentExcIfDateIsNotEndOfTheYear(LocalDate notEndOfTheYear) {
		region.addSite(ANY_REGION_SITE);
		region.tagSites(notEndOfTheYear);
	}
	
	@Test
	public void shouldReplaceSiteByNewAfterTaging() {
		region.addSite(ANY_REGION_SITE);
		RegionSite newEvaluatedSite = mock(RegionSite.class);
		when(ANY_REGION_SITE.evaluateAtTheEndOfTheYear(END_OF_THE_YEAR_DATE)).thenReturn(newEvaluatedSite);
		region.tagSites(END_OF_THE_YEAR_DATE);
		assertEquals(newEvaluatedSite, region.getSites().iterator().next());
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
	public void shouldReturnTheLastCampaign() {
		shouldAddManyCampaigns();
		assertEquals(ANY_MARKETING_CAMPAIGN, region.getLastMarketingCampaign());
	}
	
	@Test
	public void shouldReplaceCampaign() {
		shouldAddManyCampaigns();
		MarketingCampaign replacementCampaign = mock(MarketingCampaign.class);
		region.replaceMarketingCampaign(ANY_MARKETING_CAMPAIGN, replacementCampaign);
		assertEquals(replacementCampaign, region.getLastMarketingCampaign());
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowIllegalArgumentExcIfCampaignToBeReplaceCantBeFound() {
		shouldAddManyCampaigns();
		MarketingCampaign notExistingCampaign = mock(MarketingCampaign.class);
		MarketingCampaign replacementCampaign = mock(MarketingCampaign.class);
		region.replaceMarketingCampaign(notExistingCampaign, replacementCampaign);
	}
}
