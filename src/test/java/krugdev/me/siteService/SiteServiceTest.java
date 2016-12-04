package krugdev.me.siteService;

import static org.mockito.Mockito.*;

import java.time.LocalDate;

import org.junit.Test;

import krugdev.me.membershipService.MembershipService;
import krugdev.me.siteService.domain.Site;
import krugdev.me.siteService.domain.Visitor;

public class SiteServiceTest {

	@Test
	public void shouldReportToMembershipServiceIfVisitorIsAMember() {
		Site site  = mock(Site.class);
		MembershipService membershipService = mock(MembershipService.class);
		String membershipNo = "1234";
		Visitor visitor = new Visitor.Builder(LocalDate.now(),site).membership(membershipNo).build();
		SiteService siteService = new SiteService(membershipService);
		siteService.informMembershipServiceAboutVisit(site, visitor);
		verify(membershipService).reportSiteVisit(site, membershipNo);
		
	}

}
