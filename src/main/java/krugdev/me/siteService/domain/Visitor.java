package krugdev.me.siteService.domain;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;

@Entity
@NamedQuery(
		name = "Visitor.findSiteVisitorsByPeriod",
		query= "Select v from Visitor v "
				+ "where v.visitedSite = :site "
				+ "and v.visitDate >= :startDate "
				+ "and v.visitDate <= :endDate")
public class Visitor {
	
	@Id
	@GeneratedValue
	private int id;
	
	private LocalDate visitDate;
	private VisitorType visitorType;
	@ManyToOne(fetch = FetchType.EAGER)
	private Site visitedSite;

	private String membershipId;
	private BigDecimal visitPrice;
	
	protected Visitor() {}

	public static class Builder {
		// required parameters
		private final LocalDate visitDate;
		private final Site visitedSite;
		
		// optional parameters
		private VisitorType visitorType = VisitorType.ADULT;
		private String membershipId = "";
		private BigDecimal visitPrice = BigDecimal.ZERO;

		public Builder(LocalDate visitDate, Site site) {
			this.visitDate = visitDate;
			this.visitedSite = site;
		}
		
		public Builder type(VisitorType type) {
			this.visitorType = type;
			return this;
		}

		public Builder membership(String id) {
			this.membershipId = id;
			return this;
		}

		public Builder visitPrice(BigDecimal price) {
			this.visitPrice = price;
			return this;
		}
		
		public Visitor build() {
			return new Visitor(this);
		}
	}

	private Visitor(Builder builder) {
		this.visitDate = builder.visitDate;
		this.visitorType = builder.visitorType;
		this.membershipId = builder.membershipId;
		this.visitPrice = builder.visitPrice;
		this.visitedSite = builder.visitedSite;
	}

	public boolean isMember() {
		if (membershipId.equals("")) {
			return false;
		} else {
			return true;
		}
	}

	public String getMembershipId() {
		if (membershipId.equals("")) {
			throw new IllegalStateException("visitor is not a member of Saxon Heritage");
		}
		return membershipId;
	}

	public LocalDate getVisitDate() {
		return visitDate;
	}

	public VisitorType getType() {
		return visitorType;
	}

	public BigDecimal getVisitPrice() {
		return visitPrice;
	}

	public Site getSite() {
		return visitedSite;
	}
}
