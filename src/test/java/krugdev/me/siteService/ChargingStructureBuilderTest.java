package krugdev.me.siteService;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.Tuple;

import org.junit.Test;
import org.junit.runner.RunWith;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

@RunWith(JUnitParamsRunner.class)
public class ChargingStructureBuilderTest {
	
	private static final LocalDate ANY_DATE = LocalDate.now();
	private static final Site ANY_SITE = mock(Site.class);

	public Object[] getVisitors() {
		return new VisitorType[] {
				VisitorType.ADULT,
				VisitorType.ADULT_FAMILY,
				VisitorType.CHILD,
				VisitorType.CHILD_FAMILY
		};
	}
	
	@Test
	@Parameters(method = "getVisitors")
	public void shouldReturnDefaultPriceForEachTypeOfVisitor(VisitorType visitorType) {
		ChargingStructure chargingStructure = new ChargingStructure.Builder().build();
	
		assertEquals(BigDecimal.ZERO, chargingStructure.getPrice(visitorType, true));
		assertEquals(BigDecimal.ZERO, chargingStructure.getPrice(visitorType, false));
	}
	
	@Test
	public void shouldReturnNewPriceForAdultNoMember() {
		BigDecimal newPrice = BigDecimal.valueOf(20.0);
		ChargingStructure chargingStructure = 
				new ChargingStructure.Builder().priceAdultNoMember(newPrice).build();
		assertEquals(newPrice, chargingStructure.getPrice(VisitorType.ADULT, false));
	}
}
