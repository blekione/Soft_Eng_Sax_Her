package krugdev.me.region;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;


import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import krugdev.me.region.domain.MarketingCampaign;
import krugdev.me.region.domain.RegionSite;

@RunWith(JUnitParamsRunner.class)
public class MarketingCampaignTest {

	private static final LocalDate VALID_START_DATE = LocalDate.now().plusDays(10);
	private static final LocalDate END_DATE = LocalDate.now().plusDays(50);
	private MarketingCampaign.Builder builder = 
			new MarketingCampaign.Builder(VALID_START_DATE, END_DATE);
	private MarketingCampaign campaign;
	
	@Test
	public void builderShouldBuildCampaignWithDefaultValues() {
		MarketingCampaign campaign = builder.build();
		
		assertEquals(VALID_START_DATE, campaign.getStartDate());
		assertEquals(END_DATE, campaign.getEndDate());
		
		assertEquals("unknown", campaign.getName());
		assertEquals(1.0d, campaign.getTargetMultiplier(), 0.001);
	}
	
	@Test
	public void builderShouldBuildCampaingWithSetName() {
		String name = "Stonehange";
		MarketingCampaign campaign = builder.name(name).build();
		assertEquals(name, campaign.getName());
	}
	
	@Test
	public void builderShouldBuildCampaignWithSetTargetMultiplier() {
		double targetMultiplier = 1.2d;
		MarketingCampaign campaign = builder.targetMultiplier(targetMultiplier).build();
		assertTrue(campaign.getTargetMultiplier() == targetMultiplier);
	}
	
	public Object[] getInvalidStartDates() {
		return new LocalDate[] {
								LocalDate.now().minusDays(1),
								LocalDate.now().minusWeeks(3),
								LocalDate.now().minusMonths(3),
								LocalDate.now().minusYears(2)
								};
	}
	
	@Test(expected = IllegalArgumentException.class)
	@Parameters(method = "getInvalidStartDates")
	public void ShouldThrowIllegalArgumentExcIfStartDateInThePast(LocalDate invalidStartDate) {
		campaign = 
				new MarketingCampaign.Builder(invalidStartDate, END_DATE).build();
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowIllegalArgumentExcIfEndDateIsLessThanStartDate() {
		LocalDate endDateLessThanStartDate = VALID_START_DATE.minusDays(1);
		campaign = 
				new MarketingCampaign.Builder(
						VALID_START_DATE, endDateLessThanStartDate)
				.build();
	}
	
	@Test
	public void shouldAddRegionSites() {
		Set<RegionSite> regionSites = new HashSet<>();
		RegionSite regionSiteA = mock(RegionSite.class);
		RegionSite regionSiteB = mock(RegionSite.class);
		regionSites.add(regionSiteA);
		regionSites.add(regionSiteB);
		campaign = new MarketingCampaign.Builder(VALID_START_DATE, END_DATE).regionSites(regionSites).build();
		assertEquals(2, campaign.getRegionSites().size());
	}
}