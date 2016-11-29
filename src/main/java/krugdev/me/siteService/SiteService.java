package krugdev.me.siteService;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import krugdev.me.membershipService.MembershipService;

public class SiteService {
	
	public static void main(String[] args) {
				
		SiteDBService dbService = new SiteDBService();
		Site site1 = Site.instanceOf("Buckingham Palace", dbService);
		System.out.println(site1.getVisitorsCount());
		
		dbService.close();
	}
}
