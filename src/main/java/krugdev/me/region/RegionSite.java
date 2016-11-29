package krugdev.me.region;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import krugdev.me.siteService.Site;

@Entity
@NamedQuery(
		name = "findRegionSiteById",
		query= "Select r from RegionSite r where r.id = :id")
public class RegionSite {
	@Id
	@GeneratedValue
	private int id;
	
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="siteId")
	private Site site;
	
	private RegionSiteType type;
	@ManyToOne
	private Region region;
	
	private RegionSiteRating rating;
	private boolean priorityForCampaign;
	private int visitorsTarget;
	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
//	@Fetch(FetchMode.SELECT)
	@JoinTable(name = "RegionSiteCampaigns")
	private List<MarketingCampaign> marketingCampaigns;

	// required by JPA
	protected RegionSite() {}
	
	public static class Builder {
		private final Site site;
		private final RegionSiteType type;
		private final Region region;
		
		private RegionSiteRating rating = RegionSiteRating.BRONZE;
		private boolean priorityForCampaign = false;
		private int visitorsTarget = 1000;
		private List<MarketingCampaign> marketingCampaigns = new ArrayList<>();
		
		public Builder(Site site, RegionSiteType type, Region region) {
			this.site = site;
			this.type = type;
			this.region = region;
		}
		
		public RegionSite build() {
			return new RegionSite(this);
		}

		public Builder rating(RegionSiteRating rating) {
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

	private RegionSiteRating evaluateRating(int visitorsCount) {
		if (visitorsCount < 10000) {
			return RegionSiteRating.BRONZE;
		} else if (visitorsCount >= 10000 && visitorsCount < 30000) {
			return RegionSiteRating.SILVER;
		} else if (visitorsCount >= 30000) {
			return RegionSiteRating.GOLD;
		}
		return RegionSiteRating.BRONZE;
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

	public RegionSiteType getType() {
		return type;
	}
	
	public Site getSite() {
		return site;
	}

	public RegionSiteRating getRating() {
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
	
	public int getId() {
		return id;
	}

	@Override
	public boolean equals(Object obj) {
//		TODO make correct equals for Site and Region
		if (obj instanceof RegionSite) {
			RegionSite regionSite = (RegionSite) obj;
		if (
					this.id == regionSite.getId()
					&& this.site.getName().equals(regionSite.getSite().getName())
					&& this.region.getName().equals(regionSite.getRegion().getName())
		){
				return true;
			} else return false;
		} 
		
		return false;
	}
	
	
}
