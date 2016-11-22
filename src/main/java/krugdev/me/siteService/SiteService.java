package krugdev.me.siteService;

import java.time.LocalDate;

public class SiteService {
	
	public static void main(String[] args) {
		
		
		SiteDBService dbService = new SiteDBService();
		Site site1 = Site.instanceOf("Big Ben", dbService);
		System.out.println("returned site is " + site1.getName());
		
		Visitor visitor = new Visitor.Builder(LocalDate.now(), site1).build();
		site1.addVisitor(visitor);
		System.out.println("after visitor");
		System.out.println(site1.getVisitorsCount());
		
		dbService.close();
	}
}
