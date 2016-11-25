package krugdev.me.region;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import krugdev.me.siteService.Site;

public class RegionSite {
	
	private Site site;
	private SiteType type;
	private Region region;
	
	private SiteRating rating;
	private boolean priorityForCampaign;
	private int visitorsTarget;
	private List<MarketingCampaign> marketingCampaigns;

	public static class Builder {
		private final Site site;
		private final SiteType type;
		private final Region region;
		
		private SiteRating rating = SiteRating.BRONZE;
		private boolean priorityForCampaign = false;
		private int visitorsTarget = 1000;
		private List<MarketingCampaign> marketingCampaigns = new ArrayList<>();
		
		public Builder(Site site, SiteType type, Region region) {
			this.site = site;
			this.type = type;
			this.region = region;
		}
		
		public RegionSite build() {
			return new RegionSite(this);
		}

		public Builder rating(SiteRating rating) {
			this.rating = rating;
			return this;
		}

		public Builder target(int target) {
			this.visitorsTarget = target;
			return this;
		}

		public Builder campaignPriority() {
			this.priorityForCampaign = true;
			return this;
		}

		public Builder marketingCampaign(MarketingCampaign campaign) {
			this.marketingCampaigns.add(campaign);
			return this;
		}

		public Builder marketingCampaigns(List<MarketingCampaign> campaigns) {
			this.marketingCampaigns = campaigns;
			return this;
		}
	}
	
	private RegionSite(Builder builder) {
		this.site = builder.site;
		this.type = builder.type;
		this.region = builder.region;
		this.rating = builder.rating;
		this.priorityForCampaign = builder.priorityForCampaign;
		this.visitorsTarget = builder.visitorsTarget;
		this.marketingCampaigns = builder.marketingCampaigns;
	}
	
	public RegionSite evaluateAtTheEndOfTheYear(LocalDate endDate) {
		int visitorsCount = getSiteVisitorsCount(endDate);
		RegionSite.Builder evaluatedSiteBuilder = new RegionSite.Builder(this.site, this.type, region)
				.rating(evaluateRating(visitorsCount));
		if(visitorsCountUnderTheTarget(visitorsCount)) {
			return evaluatedSiteBuilder.campaignPriority().build();
		} else {
			return evaluatedSiteBuilder.build();
		}
	}
	
	private int getSiteVisitorsCount(LocalDate endDate) {
		return site.getVisitorsCountForPeriod(getFirstDayOfTheYear(endDate), endDate);	
	}
	
	private LocalDate getFirstDayOfTheYear(LocalDate date) {
		return date.withMonth(1).withDayOfMonth(1);
	}

	private SiteRating evaluateRating(int visitorsCount) {
		if (visitorsCount < 10000) {
			return SiteRating.BRONZE;
		} else if (visitorsCount >= 10000 && visitorsCount < 30000) {
			return SiteRating.SILVER;
		} else if (visitorsCount >= 30000) {
			return SiteRating.GOLD;
		}
		return SiteRating.BRONZE;
	}
	
	private boolean visitorsCountUnderTheTarget(int visitorsCount) {
		if(visitorsCount < visitorsTarget) {
			return true;
		}
		return false;
	}

	public int getVisitorsTarget() {
		return visitorsTarget;
	}

	public Region getRegion() {
		return region;
	}

	public SiteType getType() {
		return type;
	}
	
	public Site getSite() {
		return site;
	}

	public SiteRating getRating() {
		return rating;
	}

	public boolean isPriorityForCampaing() {
		return priorityForCampaign;
	}

	public List<MarketingCampaign> getMarketingCampaigns() {
		return marketingCampaigns;
	}

	public Optional<MarketingCampaign> getLastMarketingCampaign() {
		if (marketingCampaigns.size() >= 1) {
		return Optional.of(marketingCampaigns.get(marketingCampaigns.size() - 1));	
		}
		return Optional.empty();
	}
}
