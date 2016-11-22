package krugdev.me.siteService;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.Test;

public class VisitorBuilderUnitTest {
	
	private static final LocalDate ANY_VISIT_DATE = LocalDate.now();
	private static final Site ANY_SITE = mock(Site.class);
	

	@Test
	public void shouldReturnVisitorWithDefaultValues() {
		Visitor visitor = new Visitor.Builder(ANY_VISIT_DATE, ANY_SITE).build();
		assertEquals(ANY_VISIT_DATE, visitor.getVisitDate());
		assertEquals(ANY_SITE, visitor.getSite());
		assertEquals(VisitorType.ADULT, visitor.getType());
		assertFalse(visitor.isMember());
		assertEquals(BigDecimal.ZERO, visitor.getVisitPrice());
	}
	
	@Test(expected = IllegalStateException.class)
	public void shouldThrowExceptionIfAskedForMembershipIdNoAndIsNotMember() {
		Visitor visitor = new Visitor.Builder(ANY_VISIT_DATE, ANY_SITE).build();
		visitor.getMembershipId();
	}
	
	@Test
	public void shouldReturnTrueIfHaveMembership() {
		String membershipId = "12345";
		Visitor visitor = new Visitor.Builder(ANY_VISIT_DATE, ANY_SITE)
				.membership(membershipId).build();
		assertTrue(visitor.isMember());
	}
	
	@Test 
	public void shouldReturnMembershipIdIfIsSet() {
		String membershipId = "12345";
		Visitor visitor = new Visitor.Builder(ANY_VISIT_DATE, ANY_SITE)
				.membership(membershipId).build();
		assertEquals(membershipId, visitor.getMembershipId());
	}
	
	@Test
	public void shouldReturnPriceIfIsSet() {
		BigDecimal price = BigDecimal.valueOf(20.0);
		Visitor visitor = new Visitor.Builder(ANY_VISIT_DATE, ANY_SITE)
				.visitPrice(price).build();
		assertEquals(price, visitor.getVisitPrice());
	}
	
	@Test
	public void shouldReturnVisitorTypeIfSet() {
		VisitorType anyType = VisitorType.ADULT;
		Visitor visitor = new Visitor.Builder(ANY_VISIT_DATE, ANY_SITE)
				.type(anyType).build();
		assertEquals(anyType, visitor.getType());
	}
}
