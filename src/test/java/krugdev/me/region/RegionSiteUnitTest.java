package krugdev.me.region;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import krugdev.me.siteService.Site;


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
		assertTrue(regionSite.getMarketingCampaigns().isEmpty());
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
	
	@Test
	public void builderShouldBuildRegionSiteWithCampaign() {
		MarketingCampaign campaign = mock(MarketingCampaign.class);
		RegionSite regionSite = builder.marketingCampaign(campaign).build();
		assertTrue(regionSite.getMarketingCampaigns().size() == 1);
	}
	
	@Test
	public void builderShouldBuildRegionSiteWithCollectionOfCampaigns() {
		MarketingCampaign campaignA = mock(MarketingCampaign.class);
		MarketingCampaign campaignB = mock(MarketingCampaign.class);
		List<MarketingCampaign> campaigns = new ArrayList<>();
			campaigns.add(campaignA);
			campaigns.add(campaignB);
		RegionSite regionSite = builder.marketingCampaigns(campaigns).build();
		assertTrue(regionSite.getMarketingCampaigns().size() == 2);
	}
	
	@Test
	public void shouldReturnLastCampaign() {
		MarketingCampaign campaignA = mock(MarketingCampaign.class);
		MarketingCampaign campaignB = mock(MarketingCampaign.class);
		RegionSite regionSite = builder.marketingCampaign(campaignA).marketingCampaign(campaignB).build();
		assertEquals(campaignB, regionSite.getLastMarketingCampaign().get());
	}
	
	@Test
	public void shouldReturnEmptyOptionalIfSiteHasNoCampaigns() {
		RegionSite regionSite = builder.build();
		assertFalse(regionSite.getLastMarketingCampaign().isPresent());
	}
	
	public Object[] getDifferentVisitorsCount() {
		return new Integer[] {0, 5000, 9999, 10000, 16879, 19999, 20000, 28387, 29999, 30000, 56043};
	}

	@Test
	@Parameters(method = "getDifferentVisitorsCount")
	public void shouldReturnRegionalSiteWithSameRegionTypeSiteAfterEvaluation(int visitorsCount) {
		RegionSite regionSite = builder.build();
		when(ANY_SITE.getVisitorsCountForPeriod(START_OF_THE_YEAR_DATE, END_OF_THE_YEAR_DATE))
			.thenReturn(visitorsCount);
		RegionSite evaluatedRegionSite = regionSite.evaluateAtTheEndOfTheYear(END_OF_THE_YEAR_DATE);
		assertEquals(regionSite.getSite(), evaluatedRegionSite.getSite());
		assertEquals(regionSite.getType(), evaluatedRegionSite.getType());
		assertEquals(regionSite.getRegion(), evaluatedRegionSite.getRegion());
	}

	public Object[] getBronzeCount() {
		return new Integer[] {0, 20, 9999, 5000};
	}
	
	@Test
	@Parameters(method = "getBronzeCount")
	public void shouldReturnRegionalSiteWithBRONZEratingAfterEvaluation(int bronzeVisitorsCount) {
		RegionSite regionSite = builder.build();
		when(ANY_SITE.getVisitorsCountForPeriod(START_OF_THE_YEAR_DATE, END_OF_THE_YEAR_DATE))
			.thenReturn(bronzeVisitorsCount);
		RegionSite evaluatedSite = regionSite.evaluateAtTheEndOfTheYear(END_OF_THE_YEAR_DATE);
		assertEquals(RegionSiteRating.BRONZE, evaluatedSite.getRating());
	}
	
	public Object[] getSilverCount() {
		return new Integer[] {10000, 29999, 15000, 24976};
	}
	
	@Test
	@Parameters(method = "getSilverCount")
	public void shouldReturnSiteWithSILVERratingAfterEvaluation(int silverVisitorsCount) {
		RegionSite regionSite = builder.build();
		when(ANY_SITE.getVisitorsCountForPeriod(START_OF_THE_YEAR_DATE, END_OF_THE_YEAR_DATE))
			.thenReturn(silverVisitorsCount);
		RegionSite evaluatedSite = regionSite.evaluateAtTheEndOfTheYear(END_OF_THE_YEAR_DATE);
		assertEquals(RegionSiteRating.SILVER, evaluatedSite.getRating());
	}
	
	public Object[] getGoldCount() {
		return new Integer[] {30000, 100000, 56987};
	}
	
	@Test
	@Parameters(method = "getGoldCount")
	public void shouldReturnRegionalSiteWithGOLDratingAfterEvaluation(int goldVisitorsCount) {
		RegionSite regionSite = builder.build();
		when(ANY_SITE.getVisitorsCountForPeriod(START_OF_THE_YEAR_DATE, END_OF_THE_YEAR_DATE))
			.thenReturn(goldVisitorsCount);
		RegionSite evaluatedSite = regionSite.evaluateAtTheEndOfTheYear(END_OF_THE_YEAR_DATE);
		assertEquals(RegionSiteRating.GOLD, evaluatedSite.getRating());
	}
	
	public Object[] getCountLessThanTarget() {
		return new Integer[] {0, 14999, 5698};
	}
	
	@Test
	@Parameters(method = "getCountLessThanTarget")
	public void shouldReturnRegionalSiteWithPriorityForMarketingCampaignIfTargetNotMet(int visitorCountLessThenTarget) {
		int siteTarget = 15000;
		RegionSite regionSite = builder.target(siteTarget).build();
		when(ANY_SITE.getVisitorsCountForPeriod(START_OF_THE_YEAR_DATE, END_OF_THE_YEAR_DATE))
			.thenReturn(visitorCountLessThenTarget);
		RegionSite evaluatedRegionSite = regionSite.evaluateAtTheEndOfTheYear(END_OF_THE_YEAR_DATE);
		assertTrue(evaluatedRegionSite.isPriorityForCampaing());
	}

	public Object[] getCountMoreThanTarget() {
		return new Integer[] {15000, 100000, 25438};
	}

	@Test
	@Parameters(method = "getCountMoreThanTarget")
	public void shouldReturnRegionalSiteWithNoPriorityForMarketingCampaignIfTargetMet(int visitorCountMoreThenTarget) {
		int siteTarget = 15000;
		RegionSite regionSite = builder.target(siteTarget).campaignPriority().build();
		when(ANY_SITE.getVisitorsCountForPeriod(START_OF_THE_YEAR_DATE, END_OF_THE_YEAR_DATE))
			.thenReturn(visitorCountMoreThenTarget);
		RegionSite evaluatedRegionSite = regionSite.evaluateAtTheEndOfTheYear(END_OF_THE_YEAR_DATE);
		assertFalse(evaluatedRegionSite.isPriorityForCampaing());
	}
}
