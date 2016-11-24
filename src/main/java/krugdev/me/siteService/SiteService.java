package krugdev.me.siteService;


import java.time.LocalDate;

public class SiteService {
	
	public static void main(String[] args) {
				
		SiteDBService dbService = new SiteDBService();
		Site site1 = Site.instanceOf("Big Ben", dbService);
		System.out.println("returned site is " + site1.getName());
		
//		ChargingStructure chargingStructure = 
//				new ChargingStructure.Builder().priceAdultMember(BigDecimal.valueOf(20.0)).build();
//		
//		site1.setCharginStructure(chargingStructure);
		
//		Site site2 = Site.instanceOf("Big Jacek", dbService);
//		site2.setCharginStructure(chargingStructure);
		
//		Visitor visitor = new Visitor.Builder(LocalDate.now().plusDays(4), site1).build();
//		site1.addVisitor(visitor);
//		System.out.println("after visitor");
//		System.out.println(site1.getVisitorsCount());
		
		int count = site1.getVisitorsCountForPeriod(LocalDate.now().plusDays(1), LocalDate.now().minusDays(1));
		
		System.out.println(count);
		
		dbService.close();
	}
}
