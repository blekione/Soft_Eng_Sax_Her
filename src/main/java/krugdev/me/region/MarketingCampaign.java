package krugdev.me.region;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;

public class MarketingCampaign {
	
	private LocalDate startDate;		
	private LocalDate endDate;
	private Region region;
	private MarketingManager marketingManager;
	
	private Collection<RegionSite> regionSites;
	private String name;
	private double targetMultiplier;
	
	public static class Builder{
		private final LocalDate startDate;		
		private final LocalDate endDate;
		private final Region region;
		private final MarketingManager marketingManager;
		
		private Collection<RegionSite> regionSites = new HashSet<>();
		private String name = "unknown";
		private double targetMultiplier = 1.0d;
		
		public Builder(LocalDate startDate, LocalDate endDate, Region region, MarketingManager manager) {
			this.startDate =startDate;
			this.endDate = endDate;
			this.region = region;
			this.marketingManager = manager;
		}
		
		public MarketingCampaign build() {
			return new MarketingCampaign(this);
		}

		public Builder site(RegionSite site) {
			this.regionSites.add(site);
			return this;
		}
	}	
	
	private MarketingCampaign(Builder builder) {
		this.startDate = builder.startDate;
		this.endDate = builder.endDate;
		this.region = builder.region;
		this.marketingManager = builder.marketingManager;
		this.regionSites = builder.regionSites;
		this.name = builder.name;
		this.targetMultiplier = builder.targetMultiplier;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public Region getRegion() {
		return region;
	}

	public MarketingManager getManager() {
		return marketingManager;
	}

	public Collection<RegionSite> getRegionSites() {
		return regionSites;
	}

	public String getName() {
		return name;
	}

	public double getTargetMultiplier() {
		return targetMultiplier;
	}
}
