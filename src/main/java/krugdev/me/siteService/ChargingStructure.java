package krugdev.me.siteService;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class ChargingStructure {
	
	@Id
	@GeneratedValue
	@Column(name = "STRUCTURE_ID")
	private int id;
	private BigDecimal adultMember;
	private BigDecimal adultNoMember;
	private BigDecimal childMember;
	private BigDecimal childNoMember;
	private BigDecimal familyAdultMember;
	private BigDecimal familyChildMember;
	private BigDecimal familyAdultNoMember;
	private BigDecimal familyChildNoMember;
	
	// required by JPA
	protected ChargingStructure() {}
	
	public static class Builder {
		private BigDecimal adultMember = BigDecimal.ZERO;
		private BigDecimal adultNoMember = BigDecimal.ZERO;
		private BigDecimal childMember = BigDecimal.ZERO;
		private BigDecimal childNoMember = BigDecimal.ZERO;
		private BigDecimal familyAdultMember = BigDecimal.ZERO;
		private BigDecimal familyChildMember = BigDecimal.ZERO;
		private BigDecimal familyAdultNoMember = BigDecimal.ZERO;
		private BigDecimal familyChildNoMember = BigDecimal.ZERO;
		
		public Builder priceAdultMember(BigDecimal price) {
			this.adultMember = price;
			return this;
		}
		
		public Builder priceAdultNoMember(BigDecimal price) {
			this.adultNoMember = price;
			return this;
		}
		
		public Builder priceChildMember(BigDecimal price) {
			this.childMember = price;
			return this;
		}
		
		public Builder priceChildNoMember(BigDecimal price) {
			this.childNoMember = price;
			return this;
		}

		public Builder priceFamilyAdultMember(BigDecimal price) {
			this.familyAdultMember = price;
			return this;
		}

		public Builder priceFamilyChildMember(BigDecimal price) {
			this.familyChildMember = price;
			return this;
		}
		
		public Builder priceFamilyAdultNoMember(BigDecimal price) {
			this.familyAdultNoMember = price;
			return this;
		}

		public Builder priceFamilyChildNoMember(BigDecimal price) {
			this.familyChildNoMember = price;
			return this;
		}
		
		public ChargingStructure build() {
			return new ChargingStructure(this);
		}
	}

	private ChargingStructure(Builder builder) {
		this.adultMember = builder.adultMember;
		this.adultNoMember = builder.adultNoMember;
		this.childMember = builder.childMember;
		this.childNoMember = builder.childNoMember;
		this.familyAdultMember = builder.familyAdultMember;
		this.familyAdultNoMember = builder.familyAdultNoMember;
		this.familyChildMember = builder.familyChildMember;
		this.familyChildNoMember = builder.familyChildNoMember;
	}

	public BigDecimal getPrice(Visitor visitor){
		if (visitor.isMember()) {
			switch(visitor.getType()) {
			case CHILD: return childMember; 
			case ADULT_FAMILY: return familyAdultMember;
			case CHILD_FAMILY: return familyChildMember;
			case ADULT:  
			default: return adultMember;
			}
		} else {
			switch(visitor.getType()) {
			case CHILD: return childNoMember;
			case ADULT_FAMILY: return familyAdultNoMember;
			case CHILD_FAMILY: return familyChildNoMember;
			case ADULT: 
			default: return adultNoMember;
			}
		}
	}
}
