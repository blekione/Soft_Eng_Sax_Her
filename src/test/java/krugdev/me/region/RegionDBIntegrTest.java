package krugdev.me.region;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import krugdev.me.siteService.Site;
import krugdev.me.siteService.SiteDBService;

public class RegionDBIntegrTest {

	RegionDBService regionDBService = new RegionDBService();
	SiteDBService siteDBService = new SiteDBService();
	
	@Before
	public void setUp() {
		JDBCExample.deleteTable();
		regionDBService = new RegionDBService();
		siteDBService  =new SiteDBService();
	}
	
	@After
	public void closedbConncetion() {
		regionDBService.close();
		siteDBService.close();
	}
	
		
	@Test
	public void shouldAddAndRemoveRegionSiteFromRegion() {
		String name = "London";
		Site site = Site.instanceOf("Big Ben", siteDBService);
		
		Region region1 = Region.instanceOf(name, regionDBService);
		
		RegionSite regionSite = new RegionSite.Builder(site, RegionSiteType.BUILDING, region1).target(10000).build();
				
		region1.addSite(regionSite);
		
		assertEquals("test after adding site to region", 1, region1.getSites().size());
		
		region1 = Region.instanceOf(name, regionDBService);
		assertEquals("test size before removing site from region", 1, region1.getSites().size());	
		
		List<RegionSite> regionSites = new ArrayList<>();
		region1.getSites().forEach(v -> regionSites.add(v));
		
		region1.removeSite(regionSites.get(0));
		
		region1 = Region.instanceOf(name, regionDBService);
		assertEquals(0, region1.getSites().size());
		
	}
	
	@Test
	public void shouldReplaceRegionSiteByAnother() {
		String name = "London";
		Region region1 = Region.instanceOf(name, regionDBService);
		
		Site site = Site.instanceOf("Big Ben", siteDBService);
		RegionSite regionSite = new RegionSite.Builder(site, RegionSiteType.BUILDING, region1).target(10000).build();
		region1.addSite(regionSite);
		
		region1 = Region.instanceOf(name, regionDBService);
		List<RegionSite> regionSites = new ArrayList<>();
		region1.getSites().forEach(v -> regionSites.add(v));
		RegionSite oldRS = regionSites.get(0); 
		RegionSite newRS = new RegionSite.Builder(oldRS.getSite(), oldRS.getType(), oldRS.getRegion())
				.target(15000).build();
		region1.replaceSite(oldRS, newRS);
		
		region1 = Region.instanceOf(name, regionDBService);
		List<RegionSite> regionSitesAfter = new ArrayList<>();
		region1.getSites().forEach(v -> regionSitesAfter.add(v));
		RegionSite testRS = regionSitesAfter.get(0); 
		assertEquals(1, region1.getSites().size());
		assertEquals(15000, testRS.getVisitorsTarget());
	}
	
	@Test
	public void shouldAddCampaignToRegion() {
		String name = "London";
		Region region1 = Region.instanceOf(name, regionDBService);
		
		LocalDate startDate = LocalDate.now();
		LocalDate enddate = LocalDate.now().plusDays(30);
		
		MarketingCampaign campaign = new MarketingCampaign.Builder(startDate, enddate, region1)
				.targetMultiplier(1.2).build();
		Site site = Site.instanceOf("Big Ben", siteDBService);
		RegionSite regionSite = new RegionSite.Builder(site, RegionSiteType.BUILDING, region1).target(10000).build();
		region1.addSite(regionSite);
		region1.addMarketingCampaign(campaign);
		
		region1 = Region.instanceOf(name, regionDBService);
		assertEquals(1, region1.getMarketingCampaigns().size());
	}
	
	@Test
	public void shouldAddCampaignToRegionSite() {
		String name = "London";
		Region region1 = Region.instanceOf(name, regionDBService);
		
		LocalDate startDate = LocalDate.now();
		LocalDate enddate = LocalDate.now().plusDays(30);
		
		MarketingCampaign campaign = new MarketingCampaign.Builder(startDate, enddate, region1)
				.targetMultiplier(1.1).build();
		
		Site site = Site.instanceOf("Big Ben", siteDBService);
		RegionSite regionSite = new RegionSite.Builder(site, RegionSiteType.BUILDING, region1)
				.marketingCampaign(campaign).target(10000).build();
		region1.addSite(regionSite);
		
		region1 = Region.instanceOf(name, regionDBService);
		List<RegionSite> regionSitesAfter = new ArrayList<>();
		region1.getSites().forEach(v -> regionSitesAfter.add(v));
		RegionSite testRS = regionSitesAfter.get(0);
		assertEquals(1, testRS.getMarketingCampaigns().size());
		assertEquals(1.1, testRS.getLastMarketingCampaign().get().getTargetMultiplier(), 0.001);
	}
	
	@Test
	public void shouldReplaceRegionSiteWithCampaignByAnother() {
		String name = "London";
		Region region1 = Region.instanceOf(name, regionDBService);
		
		LocalDate startDate = LocalDate.now();
		LocalDate enddate = LocalDate.now().plusDays(30);
		
		MarketingCampaign campaign = new MarketingCampaign.Builder(startDate, enddate, region1)
				.targetMultiplier(1.1).build();
		Site site = Site.instanceOf("Big Ben", siteDBService);
		RegionSite regionSite = new RegionSite.Builder(site, RegionSiteType.BUILDING, region1)
				.marketingCampaign(campaign).target(10000).build();
		region1.addSite(regionSite);
		
		region1 = Region.instanceOf(name, regionDBService);
		assertEquals("my test", 1 , region1.getMarketingCampaigns().size());
		List<RegionSite> regionSites = new ArrayList<>();
		region1.getSites().forEach(v -> regionSites.add(v));
		RegionSite oldRS = regionSites.get(0);
		RegionSite newRS = new RegionSite.Builder(oldRS.getSite(), oldRS.getType(), oldRS.getRegion())
				.target(15000).marketingCampaign(campaign).build();
		region1.replaceSite(oldRS, newRS);
		
		region1 = Region.instanceOf(name, regionDBService);
		List<RegionSite> regionSitesAfter = new ArrayList<>();
		region1.getSites().forEach(v -> regionSitesAfter.add(v));
		RegionSite testRS = regionSitesAfter.get(0); 
		assertEquals("marketing", 1, region1.getMarketingCampaigns().size());
		assertEquals(1, region1.getSites().size());
		assertEquals(15000, testRS.getVisitorsTarget());
	}
}
