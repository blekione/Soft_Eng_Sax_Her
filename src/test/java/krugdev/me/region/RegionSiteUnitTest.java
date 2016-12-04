package krugdev.me.region;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;

import org.junit.Test;
import org.junit.runner.RunWith;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import krugdev.me.region.domain.Region;
import krugdev.me.region.domain.RegionSite;
import krugdev.me.region.domain.RegionSiteRating;
import krugdev.me.region.domain.RegionSiteType;
import krugdev.me.siteService.domain.Site;


@RunWith(JUnitParamsRunner.class)
public class RegionSiteUnitTest {

	private static final RegionSiteType ANY_TYPE = RegionSiteType.GARDEN;
	LocalDate END_OF_THE_YEAR_DATE = LocalDate.of(2016, 12, 31);
	LocalDate START_OF_THE_YEAR_DATE = LocalDate.of(2016, 1, 1);
	Site ANY_SITE = mock(Site.class);
	Region ANY_REGION = mock(Region.class);
	private final RegionSite.Builder builder = new RegionSite.Builder(ANY_SITE, ANY_TYPE, ANY_REGION);
	
	@Test
	public void builderShoudlBuildSiteWithDefaultValues() {
		RegionSiteRating defaultRating = RegionSiteRating.BRONZE;
		int defaultVisitorsTarget = 1000;
		RegionSite regionSite = builder.build();
		
		assertEquals(ANY_SITE, regionSite.getSite());
		assertEquals(ANY_REGION, regionSite.getRegion());
		assertEquals(ANY_TYPE, regionSite.getType());
		assertEquals(defaultRating, regionSite.getRating());
		assertEquals(defaultVisitorsTarget, regionSite.getVisitorsTarget());
		assertFalse(regionSite.isPriorityForCampaing());
	}
	
	public Object[] getRatings() {
		return new RegionSiteRating[] {RegionSiteRating.BRONZE, RegionSiteRating.SILVER, RegionSiteRating.GOLD};
	}
	
	@Test
	@Parameters(method = "getRatings")
	public void buliderShouldBuildRegionSiteWithSetRating(RegionSiteRating rating) {
		RegionSite regionSite = builder.rating(rating).build();
		assertEquals(rating, regionSite.getRating());
	}
	
	@Test
	public void builderShouldBuildRegionSiteWithSetVisitorsTarget() {
		int anyTarget = 8000;
		RegionSite regionSite = builder.target(anyTarget).build();
		assertEquals(anyTarget, regionSite.getVisitorsTarget());
	}
	
	@Test
	public void builderShouldBUildRegionSiteWithSetPriorityForCampaign() {
		RegionSite regionSite = builder.campaignPriority().build();
		assertTrue(regionSite.isPriorityForCampaing());
	}	
	
	public Object[] getDifferentVisitorsCount() {
		return new Integer[] {0, 5000, 9999, 10000, 16879, 19999, 20000, 28387, 29999, 30000, 56043};
	}
}
