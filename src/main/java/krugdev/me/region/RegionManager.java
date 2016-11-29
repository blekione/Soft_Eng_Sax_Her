package krugdev.me.region;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

import krugdev.me.membershipService.MembershipService;
import krugdev.me.siteService.ChargingStructure;
import krugdev.me.siteService.Site;
import krugdev.me.siteService.SiteDBService;
import krugdev.me.siteService.Visitor;
import krugdev.me.siteService.VisitorType;

public class RegionManager {
	public static void main(String[] args) throws InterruptedException {
		RegionDBService regionDBService = new RegionDBService();
		SiteDBService siteDBService  =new SiteDBService();
		MembershipService membershipService = new MembershipService();
		
		LocalDate endOfTheYear = LocalDate.of(2016, 12, 31);
		
		String name = "London";
		
		Region region1 = Region.instanceOf(name, regionDBService);	
		
		
		// add site
		Site site = Site.instanceOf("Big Ben", siteDBService);
		
		RegionSite regionSite = new RegionSite.Builder(site, RegionSiteType.BUILDING, region1).target(10000).build();
		
		region1.addSite(regionSite);
		 
		// */
		
		/*
		// remove site 
				List<RegionSite> regionSites = new ArrayList<>();
				
				region1.getSites().forEach(v -> regionSites.add(v));

				region1.removeSite(regionSites.get(0));
		// */
		
		/*
		// replace site 
		List<RegionSite> regionSites = new ArrayList<>();
		
		region1.getSites().forEach(v -> regionSites.add(v));

		region1.replaceSite(regionSites.get(0), regionSite);
		// */
		
		siteDBService.close();
		regionDBService.close();
	}
}
