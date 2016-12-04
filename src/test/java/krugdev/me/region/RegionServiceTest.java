package krugdev.me.region;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import krugdev.me.region.domain.MarketingCampaign;
import krugdev.me.region.domain.Region;
import krugdev.me.region.domain.RegionSite;


public class RegionServiceTest {
			
	@Test
	public void shouldGetOnlyRegionSitesViableForCampaign() {
		
		RegionSite regionSiteA = mock(RegionSite.class);
		RegionSite regionSiteB = mock(RegionSite.class);
		
		Set<RegionSite> lastCampaignSites = new HashSet<>();
		lastCampaignSites.add(regionSiteA);
		MarketingCampaign lastCampaign = mock(MarketingCampaign.class);
		when(lastCampaign.getRegionSites()).thenReturn(lastCampaignSites);
		
		Set<RegionSite> regionSites = new HashSet<>();
		regionSites.add(regionSiteA);
		regionSites.add(regionSiteB);
		List<MarketingCampaign> regionCampaigns = Arrays.asList(lastCampaign);
		Region region = mock(Region.class);
		when(region.getMarketingCampaigns()).thenReturn(regionCampaigns);
		when(region.getSites()).thenReturn(regionSites);
		
		RegionService regionService = new RegionService();
		
		Set<RegionSite> resultSites = regionService.getRegionSitesForCampaign(region);
		
		assertEquals(regionSiteB, resultSites.iterator().next());
	}
}
