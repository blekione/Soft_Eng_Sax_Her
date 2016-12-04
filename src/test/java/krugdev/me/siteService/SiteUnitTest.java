package krugdev.me.siteService;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;


import org.junit.Before;
import org.junit.Test;

import krugdev.me.siteService.domain.ChargingStructure;
import krugdev.me.siteService.domain.Site;
import krugdev.me.siteService.domain.Visitor;


public class SiteUnitTest {

	private Site site;
	
	private static final Visitor ANY_VISITOR = mock(Visitor.class, "any_visitor");
	private static final String SITE_NAME = "Kew Garden";

	@Before
	public void setUp() {
		site  = Site.instanceOf(SITE_NAME);
	}
	
	@Test
	public void shouldAddVisitor() {
		site.addVisitor(ANY_VISITOR);
		assertEquals(1, site.getVisitorsCount());
	}
	
	@Test
	public void shouldAddManyVisitors() {
		Visitor visitorA = mock(Visitor.class, "visitorA");
		Visitor visitorB = mock(Visitor.class, "visitorB");
		site.addVisitor(visitorA);
		site.addVisitor(visitorB);
		assertEquals(2, site.getVisitorsCount());
	}
	
	@Test
	public void shouldSetChargingStructure() {
		site = Site.instanceOf(SITE_NAME);
		ChargingStructure chargingStructure = mock(ChargingStructure.class);
		site.setCharginStructure(chargingStructure);
		assertEquals(chargingStructure, site.getChargingStructure());
	}
}
