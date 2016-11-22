package krugdev.me.siteService;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.Test;
import org.junit.runner.RunWith;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

@RunWith(JUnitParamsRunner.class)
public class ChargingStructureBuilderTest {
	
	private static final LocalDate ANY_DATE = LocalDate.now();
	private static final Site ANY_SITE = mock(Site.class);

	public Object[] getVisitors() {
		
		String id = "12345";
		return new Visitor[] { 
				new Visitor.Builder(ANY_DATE, ANY_SITE).type(VisitorType.ADULT).build(),
				new Visitor.Builder(ANY_DATE, ANY_SITE).type(VisitorType.CHILD).build(),
				new Visitor.Builder(ANY_DATE, ANY_SITE).type(VisitorType.ADULT_FAMILY).build(),
				new Visitor.Builder(ANY_DATE, ANY_SITE).type(VisitorType.CHILD_FAMILY).build(),
				new Visitor.Builder(ANY_DATE, ANY_SITE).type(VisitorType.ADULT).membership(id).build(),
				new Visitor.Builder(ANY_DATE, ANY_SITE).type(VisitorType.CHILD).membership(id).build(),
				new Visitor.Builder(ANY_DATE, ANY_SITE).type(VisitorType.ADULT_FAMILY).membership(id).build(),
				new Visitor.Builder(ANY_DATE, ANY_SITE).type(VisitorType.CHILD_FAMILY).membership(id).build(),
				};
	}
	
	@Test
	@Parameters(method = "getVisitors")
	public void shouldReturnDefaultPriceForEachTypeOfVisitor(Visitor visitor) {
		ChargingStructure chargingStructure = new ChargingStructure.Builder().build();
		assertEquals(BigDecimal.ZERO, chargingStructure.getPrice(visitor));
	}
	
	@Test
	public void shouldReturnNewPriceForAdultNoMember() {
		BigDecimal newPrice = BigDecimal.valueOf(20.0);
		Visitor adult = new Visitor.Builder(ANY_DATE, ANY_SITE).type(VisitorType.ADULT).build();
		ChargingStructure chargingStructure = 
				new ChargingStructure.Builder().priceAdultNoMember(newPrice).build();
		assertEquals(newPrice, chargingStructure.getPrice(adult));
	}
}
