package krugdev.me.siteService;

import krugdev.me.membershipService.MembershipService;
import krugdev.me.siteService.domain.Site;
import krugdev.me.siteService.domain.Visitor;

public class SiteService {

	public final MembershipService membershipService;
	
	public SiteService(MembershipService membershipService) {
		this.membershipService = membershipService;
	}
	
	public void informMembershipServiceAboutVisit(Site site, Visitor visitor) {
		if(visitor.isMember()) {
			checkIfMembershipServiceIsNotSet();
			membershipService.reportSiteVisit(site, visitor.getMembershipId());
		}
	}
	
	private void checkIfMembershipServiceIsNotSet() {
		if(membershipService == null) {
			throw new IllegalStateException("MembershipService is not set");
		}
	}
}
