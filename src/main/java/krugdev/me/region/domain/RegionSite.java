package krugdev.me.region.domain;


import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;

import krugdev.me.siteService.domain.Site;

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

	// required by JPA
	protected RegionSite() {}
	
	public static class Builder {
		private final Site site;
		private final RegionSiteType type;
		private final Region region;
		
		private RegionSiteRating rating = RegionSiteRating.BRONZE;
		private boolean priorityForCampaign = false;
		private int visitorsTarget = 1000;
		
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
	}
	
	private RegionSite(Builder builder) {
		this.site = builder.site;
		this.type = builder.type;
		this.region = builder.region;
		this.rating = builder.rating;
		this.priorityForCampaign = builder.priorityForCampaign;
		this.visitorsTarget = builder.visitorsTarget;
	}
	
	public int getVisitorsTarget() {
		return visitorsTarget;
	}
	
	public void setRegionTarget(int newTarget) {
		this.visitorsTarget = newTarget;
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
	
	public String getPriorityString() {
		if (priorityForCampaign) {
			return "Yes";
		} else {
			return "No";
		}
	}
	
	public int getId() {
		return id;
	}
	
	public String getSiteName() {
		return site.getName();
	}

	@Override
	public boolean equals(Object obj) {
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
