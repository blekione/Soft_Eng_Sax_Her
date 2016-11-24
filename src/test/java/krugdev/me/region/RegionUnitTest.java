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
	
	public Object[] getBronzeCount() {
		return new Integer[] {0, 20, 9999, 5000};
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
	@Parameters(method = "getBronzeCount")
	public void shouldRateSiteBRONZEbasedOnPopularity(int bronzeVisitorsCount) {
		when(ANY_REGION_SITE.getSiteVisitorsCount(END_OF_THE_YEAR_DATE)).thenReturn(bronzeVisitorsCount);
		region.addSite(ANY_REGION_SITE);				
		region.tagSites(END_OF_THE_YEAR_DATE);
		
		verify(ANY_REGION_SITE).setRating(SiteRating.BRONZE);
	}
	
	public Object[] getSilverCount() {
		return new Integer[] {10000, 29999, 15000, 24976};
	}
	
	@Test
	@Parameters(method = "getSilverCount")
	public void shouldRateSiteSILVERbasedOnPopularity(int silverVisitorsCount) {
		when(ANY_REGION_SITE.getSiteVisitorsCount(END_OF_THE_YEAR_DATE)).thenReturn(silverVisitorsCount);
		region.addSite(ANY_REGION_SITE);				
		region.tagSites(END_OF_THE_YEAR_DATE);
		
		verify(ANY_REGION_SITE).setRating(SiteRating.SILVER);
	}
	
	public Object[] getGoldCount() {
		return new Integer[] {30000, 100000, 56987};
	}
	
	@Test
	@Parameters(method = "getGoldCount")
	public void shouldRateSiteGOLDbasedOnPopularity(int goldVisitorsCount) {
		when(ANY_REGION_SITE.getSiteVisitorsCount(END_OF_THE_YEAR_DATE)).thenReturn(goldVisitorsCount);
		region.addSite(ANY_REGION_SITE);				
		region.tagSites(END_OF_THE_YEAR_DATE);
		
		verify(ANY_REGION_SITE).setRating(SiteRating.GOLD);
	}
	
	public Object[] getCountLessThanTarget() {
		return new Integer[] {0, 14999, 5698};
	}
	
	@Test
	@Parameters(method = "getCountLessThanTarget")
	public void shouldMarkSiteForCampaingIfTargetNotMet(int visitorCountLessThenTarget) {
		int siteTarget = 15000;
		when(ANY_REGION_SITE.getSiteVisitorsCount(END_OF_THE_YEAR_DATE)).thenReturn(visitorCountLessThenTarget);
		when(ANY_REGION_SITE.getVisitorsTarget()).thenReturn(siteTarget);
		region.addSite(ANY_REGION_SITE);
		region.tagSites(END_OF_THE_YEAR_DATE);
		
		verify(ANY_REGION_SITE).setPriorityForMarketingCampaigh();
	}

	public Object[] getCountMoreThanTarget() {
		return new Integer[] {15000, 100000, 25438};
	}

	@Test
	@Parameters(method = "getCountMoreThanTarget")
	public void shouldNotMarkSiteForCampaignIfTargetMet(int visitorCountMoreThenTarget) {
		int siteTarget = 15000;
		when(ANY_REGION_SITE.getSiteVisitorsCount(END_OF_THE_YEAR_DATE)).thenReturn(visitorCountMoreThenTarget);
		when(ANY_REGION_SITE.getVisitorsTarget()).thenReturn(siteTarget);
		region.addSite(ANY_REGION_SITE);
		region.tagSites(END_OF_THE_YEAR_DATE);
		
		verify(ANY_REGION_SITE, never()).setPriorityForMarketingCampaigh();
	}
	
	
}
