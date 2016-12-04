package krugdev.me.siteService;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import krugdev.me.DBService;
import krugdev.me.JDBCExample;
import krugdev.me.membershipService.MembershipService;
import krugdev.me.siteService.domain.ChargingStructure;
import krugdev.me.siteService.domain.Site;
import krugdev.me.siteService.domain.Visitor;

public class SiteRepositoryTest {
	
	private Site site;
	
	private static DBService dbService;
	private SiteRepository siteRepository;
	private MembershipService membershipService = mock(MembershipService.class);
	
	@BeforeClass
	public static void setDBService() {
		dbService = new DBService("sax_her_test");
	}
	
	@Before
	public void setUp() {
		List<String> tablesNames = new ArrayList<>();
		tablesNames.add("Visitor");
		tablesNames.add("Site");	
		tablesNames.add("ChargingStructure");
		JDBCExample.deleteTable(tablesNames);
		siteRepository = new SiteRepository(dbService, membershipService);
	}
	
	@AfterClass
	public static void cleanUp() {
		dbService.close();
	}

	@Test
	public void shouldReturnNewSite() {
		site = siteRepository.newSite("London Eye");
		assertTrue(site instanceof Site);
	}
	
	@Test
	public void shouldAddNewSiteToDatabase() {
		site = siteRepository.newSite("London Eye");
		
		Site siteResult = siteRepository.findSite("London Eye");
		assertEquals(site, siteResult);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowIllegalArgumentExcIfCantFindSite() {
		siteRepository.findSite("Eye");
	}
	
	@Test
	public void shouldAddVisitorToSite() {
		Site site = siteRepository.newSite("London Eye");
		Visitor visitor = new Visitor.Builder(LocalDate.now(), site).build();
		siteRepository.addVisitorToSite(site, visitor);
		
		Site resultSite = siteRepository.findSite("London Eye");
		assertEquals(1, resultSite.getVisitorsCount());
	}
	
	@Test
	public void shouldAddChargingStructureToSite() {
		Site site = siteRepository.newSite("London Eye");
		ChargingStructure chargingStructure = new ChargingStructure.Builder()
				.priceAdultMember(BigDecimal.valueOf(20.0))
				.priceChildNoMember(BigDecimal.valueOf(15.0))
				.build();
		dbService.persist(chargingStructure);
		
		siteRepository.setChargingStructureForSite(site, chargingStructure);
		Site resultSite = siteRepository.findSite("London Eye");
		assertEquals(chargingStructure, resultSite.getChargingStructure());
	}
	
	@Test
	public void shouldGetSiteVisitorsForPeriod() {
		LocalDate startDate = LocalDate.now().minusDays(1);
		LocalDate endDate = LocalDate.now().plusDays(10);
		Site site = siteRepository.newSite("London Eye");
		Visitor visitor = new Visitor.Builder(LocalDate.now(), site).build();
		siteRepository.addVisitorToSite(site, visitor);
		visitor = new Visitor.Builder(LocalDate.now().plusDays(10), site).build();
		siteRepository.addVisitorToSite(site, visitor);
		visitor = new Visitor.Builder(LocalDate.now().plusDays(12), site).build();
		siteRepository.addVisitorToSite(site, visitor);
		
		int visitorsCount = siteRepository.getSiteVisitorsCountForPeriod(site, startDate, endDate);
		assertEquals(2, visitorsCount);
				
	}
}
