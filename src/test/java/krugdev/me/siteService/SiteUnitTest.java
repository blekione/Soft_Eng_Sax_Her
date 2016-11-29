package krugdev.me.siteService;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;

import krugdev.me.membershipService.MembershipService;

public class SiteUnitTest {

	private Site site;
	private MembershipService membershipService;
	
	private static final Visitor ANY_VISITOR = mock(Visitor.class, "any_visitor");
	private static final SiteDBService DB_SERVICE = mock(SiteDBService.class);
	private static final String SITE_NAME = "Kew Garden";

	@Before
	public void setUp() {
		membershipService = mock(MembershipService.class);
		when(DB_SERVICE.getEntity(SITE_NAME)).thenReturn(Optional.empty());
		site  = Site.instanceOf(SITE_NAME, DB_SERVICE);
		site.setMembershipService(membershipService);
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
	public void shouldSendIdToMembershipServiceIfVisitorIsAMember() { 
		String id = "12345";
		
		when(ANY_VISITOR.isMember()).thenReturn(true);
		when(ANY_VISITOR.getMembershipId()).thenReturn(id);
		site.addVisitor(ANY_VISITOR);
		
		verify(membershipService).receive(id);
	}
	
	@Test(expected = IllegalStateException.class)
	public void shouldThrowIllegalStateExceptionIfMembershipServiceIsNotSet() {
		site  =  Site.instanceOf(SITE_NAME, DB_SERVICE);
		String id = "12345";
		when(ANY_VISITOR.isMember()).thenReturn(true);
		when(ANY_VISITOR.getMembershipId()).thenReturn(id);
		site.addVisitor(ANY_VISITOR);
	}
	
	@Test
	public void shouldSetChargingStructure() {
		site = Site.instanceOf(SITE_NAME, DB_SERVICE);
		ChargingStructure chargingStructure = mock(ChargingStructure.class);
		site.setCharginStructure(chargingStructure);
		assertEquals(chargingStructure, site.getChargingStructure());
	}
	
	@Test 
	public void shouldReturnVisitCountForPeriod() {
		LocalDate startDate = LocalDate.now().minusDays(1);
		LocalDate endDate = LocalDate.now().plusDays(1);
		Visitor visitor = mock(Visitor.class);
		List<Visitor> visitors = new ArrayList<>();
		visitors.add(visitor);
		visitors.add(visitor);
		when(DB_SERVICE.findVisitors(site, startDate, endDate)).thenReturn(visitors);
		
		assertEquals(2, site.getVisitorsCountForPeriod(startDate, endDate));
	}
}
