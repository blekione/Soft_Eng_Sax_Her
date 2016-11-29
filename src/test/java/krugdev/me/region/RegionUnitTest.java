package krugdev.me.region;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

@RunWith(JUnitParamsRunner.class)
public class RegionUnitTest {

	private static final String ANY_REGION_NAME = "London";
	private RegionDBService dbService = mock(RegionDBService.class);
	Region region = Region.instanceOf(ANY_REGION_NAME, dbService);
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
	public void shouldReturnSitesValidForCampaign() {
		MarketingCampaign notLastRegionCampaign = mock(MarketingCampaign.class);
		MarketingCampaign lastRegionCampaign = mock(MarketingCampaign.class);
		
		region.addMarketingCampaign(notLastRegionCampaign);
		region.addMarketingCampaign(lastRegionCampaign);
	
		RegionSite prioritySiteA = mock(RegionSite.class, "prioritSiteA");
		RegionSite prioritySiteB = mock(RegionSite.class, "prioritySiteB");
		RegionSite prioritySiteInLastCampaign = mock(RegionSite.class, "prioritySiteInlastCampaign");
		RegionSite siteA = mock(RegionSite.class, "siteA");
		RegionSite siteB = mock(RegionSite.class, "siteB");
		RegionSite siteInLastCampaign = mock(RegionSite.class, "siteInLastCampaign");
		
		// set return values from RegionSites for priority in next campaign
		
		when(prioritySiteA.isPriorityForCampaing()).thenReturn(true);
		when(prioritySiteB.isPriorityForCampaing()).thenReturn(true);
		when(prioritySiteInLastCampaign.isPriorityForCampaing()).thenReturn(true);
		when(siteA.isPriorityForCampaing()).thenReturn(false);
		when(siteB.isPriorityForCampaing()).thenReturn(false);
		when(siteInLastCampaign.isPriorityForCampaing()).thenReturn(false);
		
		// set return value of lastCampaing for RegionSite
		
		when(prioritySiteA.getLastMarketingCampaign()).thenReturn(Optional.of(notLastRegionCampaign));
		when(prioritySiteB.getLastMarketingCampaign()).thenReturn(Optional.of(notLastRegionCampaign));
		when(prioritySiteInLastCampaign.getLastMarketingCampaign()).thenReturn(Optional.of(lastRegionCampaign));
		when(siteA.getLastMarketingCampaign()).thenReturn(Optional.of(notLastRegionCampaign));
		when(siteB.getLastMarketingCampaign()).thenReturn(Optional.of(notLastRegionCampaign));
		when(siteInLastCampaign.getLastMarketingCampaign()).thenReturn(Optional.of(lastRegionCampaign));
		
		// create collections which will assert against
		List<RegionSite> priorityNotInLastCampaign = Arrays.asList(prioritySiteA, prioritySiteB);
		List<RegionSite> notInLastCampaign = Arrays.asList(siteA, siteB);
		
		// add all region sites to region
		region.addSite(prioritySiteA);
		region.addSite(prioritySiteB);
		region.addSite(prioritySiteInLastCampaign);
		region.addSite(siteA);
		region.addSite(siteB);
		region.addSite(siteInLastCampaign);

		assertTrue(region.getPrioritySites().containsAll(priorityNotInLastCampaign));
		assertTrue(region.getNonPrioritySites().containsAll(notInLastCampaign));
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
