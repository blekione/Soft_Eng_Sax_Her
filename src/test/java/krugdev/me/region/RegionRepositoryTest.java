package krugdev.me.region;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import krugdev.me.DBService;
import krugdev.me.JDBCExample;
import krugdev.me.region.domain.MarketingCampaign;
import krugdev.me.region.domain.Region;
import krugdev.me.region.domain.RegionSite;
import krugdev.me.region.domain.RegionSiteType;
import krugdev.me.siteService.SiteRepository;
import krugdev.me.siteService.domain.Site;

public class RegionRepositoryTest {
	
	private static DBService dbService = new DBService("sax_her_test");
	private RegionRepository regionRepository;
	Region region;
	SiteRepository siteRepository;
	Site site;
	
	@Before
	public void setUp() {
		List<String> tablesNames = new ArrayList<>();
		// do not change order as it will not clean tables
		tablesNames.add("CampaignSites");
		tablesNames.add("RegionCampaigns");
		tablesNames.add("RegionSite");
		tablesNames.add("MarketingCampaign");
		tablesNames.add("Region");
		JDBCExample.deleteTable(tablesNames);
		regionRepository = new RegionRepository(dbService);
		region = regionRepository.newRegion("London");
		siteRepository = new SiteRepository(dbService, null);
		site = siteRepository.newSite("Big Ben");	
	}
	
	@AfterClass
	public static void closedbConncetion() {
		dbService.close();
	}

	@Test
	public void shouldAddNewRegionToDB() {
		Region region = regionRepository.newRegion("London");
		Region regionDB = regionRepository.findRegion("London");
		assertEquals(region, regionDB);
	}
	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowIllegalArgumentExcWhenRegionNotInDB() {
		regionRepository.findRegion("Lon");
	}
	
	@Test
	public void shouldAddRegionSiteToRegion() {
		RegionSite regionSite = new RegionSite.Builder(site, RegionSiteType.BUILDING, region).build();
		regionRepository.addRegionSite(region, regionSite);
		Region dbRegion = regionRepository.findRegion("London");
		assertEquals(1, dbRegion.getSites().size());
	}
	
	@Test
	public void shouldAddManyRegionSitesToRegion() {
		Site siteA = siteRepository.newSite("Big Ben");
		RegionSite regionSiteA = new RegionSite.Builder(siteA, RegionSiteType.BUILDING, region).build();
		Site siteB = siteRepository.newSite("Crystal Palace");
		RegionSite regionSiteB = new RegionSite.Builder(siteB, RegionSiteType.BUILDING, region).build();
		regionRepository.addRegionSite(region, regionSiteA);
		regionRepository.addRegionSite(region, regionSiteB);
		Region dbRegion = regionRepository.findRegion("London");
		assertEquals(2, dbRegion.getSites().size());
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void shouldAddSameSiteOnlyOneTime() {
		Site siteA = siteRepository.newSite("Big Big");
		RegionSite regionSiteA = new RegionSite.Builder(siteA, RegionSiteType.BUILDING, region).build();
		Site siteB = siteRepository.newSite("Cry Palace");
		RegionSite regionSiteB = new RegionSite.Builder(siteB, RegionSiteType.BUILDING, region).build();
		regionRepository.addRegionSite(region, regionSiteA);
		regionRepository.addRegionSite(region, regionSiteB);
		regionRepository.addRegionSite(region, regionSiteA);
	}
	
	@Test
	public void shouldRemoveSiteFormRegion() {
		RegionSite regionSite = new RegionSite.Builder(site, RegionSiteType.BUILDING, region)
				.target(1100).build();
		regionRepository.addRegionSite(region, regionSite);
		Region dbRegion = regionRepository.findRegion("London");
		RegionSite persistedRegionSite = dbRegion.getSites().iterator().next();
		regionRepository.removeRegionSite(dbRegion, persistedRegionSite);		
		assertEquals(0, dbRegion.getSites().size());
	}
	
	@Test
	public void shouldReplaceSite() {
		RegionSite regionSiteA = new RegionSite.Builder(site, RegionSiteType.BUILDING, region)
				.target(1100).build();
		regionRepository.addRegionSite(region, regionSiteA);
		Region dbRegion = regionRepository.findRegion("London");
		RegionSite oldSite = dbRegion.getSites().iterator().next();
		RegionSite newSite = new RegionSite.Builder(site, RegionSiteType.BUILDING, region)
				.target(1200).build();
		regionRepository.replaceRegionSite(dbRegion, oldSite, newSite);
		dbRegion = regionRepository.findRegion("London");
		RegionSite persistedRegionSite = dbRegion.getSites().iterator().next();
		assertEquals(newSite.getVisitorsTarget(), persistedRegionSite.getVisitorsTarget());
	}
	
	@Test
	public void shouldAddCampaign() {
		RegionSite regionSiteA = new RegionSite.Builder(site, RegionSiteType.BUILDING, region).target(1000).build();
		RegionSite regionSiteB = new RegionSite.Builder(site, RegionSiteType.BUILDING, region).target(1100).build();
		regionRepository.addRegionSite(region, regionSiteA);
		regionRepository.addRegionSite(region, regionSiteB);
		Region dbRegion = regionRepository.findRegion("London");
		Set<RegionSite> dbRegionSites = dbRegion.getSites();
		MarketingCampaign campaign = 
				new MarketingCampaign.Builder(LocalDate.now(), LocalDate.now().plusDays(40))
				.regionSites(dbRegionSites).name("1").build();
		regionRepository.addMarketingCampaign(region, campaign);
		dbRegion = regionRepository.findRegion("London");
		assertEquals(1, dbRegion.getMarketingCampaigns().size());
	}
}
