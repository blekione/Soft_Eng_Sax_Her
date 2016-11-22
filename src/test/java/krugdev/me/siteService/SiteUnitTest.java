package krugdev.me.siteService;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

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
		when(DB_SERVICE.getSite(SITE_NAME)).thenReturn(Optional.empty());
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
		site  = new Site();
		String id = "12345";
		when(ANY_VISITOR.isMember()).thenReturn(true);
		when(ANY_VISITOR.getMembershipId()).thenReturn(id);
		site.addVisitor(ANY_VISITOR);
	}
}
