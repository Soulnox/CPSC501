package ca.ucalgary.seng300.a2;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.lsmr.vending.Coin;
import org.lsmr.vending.hardware.DisabledException;
import org.lsmr.vending.hardware.VendingMachine;

public class CPSC501Tests {
	VendingMachine vm;
	Logic logic;
	MyCoinReceptacleListener crListener;
	private final ByteArrayOutputStream outContent = new ByteArrayOutputStream(); // necessary to capture printed output
	private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();

	@Before
	public void setUp() throws Exception {
		System.setOut(new PrintStream(outContent)); // necessary to capture printed output
		System.setErr(new PrintStream(errContent));

		EventWriter ew = new EventWriter("eventLog.txt");
		vm = new VendingMachine(new int[] { 1, 5, 10, 25, 100, 200 }, 6, 200, 10, 200, 200, 200);
		vm.configure(Arrays.asList("popA", "popB", "popC", "popD", "popE", "popF"),
				Arrays.asList(100, 100, 100, 100, 150, 200));
		
		vm.loadPopCans(10, 10, 10, 10, 10, 10);
		vm.loadCoins(0, 0, 0, 0, 0, 0);

		logic = new Logic(vm, ew);
	}

	@After
	public void cleanUp() {
		System.setOut(null);
		System.setErr(null);
	}
	
	@Test
	public void testDisplay() {
		vm.getDisplay().display("Test");
		assertTrue(outContent.toString().contains("Test"));
	}
	
	@Test
	// Basic test wherein the correct coins are added, enough money is added, and
	// the button is pressed
	public void testTrue() {
		try {
			vm.getCoinSlot().addCoin(new Coin(100));
		} catch (DisabledException e) {
			fail();
		}
		vm.getSelectionButton(0).press();
		assertEquals("Credit: 100\nThank you for your purchase!\n", outContent.toString());
		assertEquals(0, logic.getCredit());
	}
	
	@Test
	public void testPressInvalid() {
		boolean thrown = false;
		try {
			vm.getSelectionButton(7).press();
		} catch (ArrayIndexOutOfBoundsException e) {
			thrown = true;
		}
		assertTrue(thrown);
	}
	
	@Test
	public void testPressInvalidNegative() {
		boolean thrown = false;
		try {
			vm.getSelectionButton(-1).press();
		} catch (ArrayIndexOutOfBoundsException e) {
			thrown = true;
		}
		assertTrue(thrown);
	}
	
	// Tests to see if all the buttons work as intended given enough money.
	@Test
	public void testAllButtons() {
		try {
			vm.getCoinSlot().addCoin(new Coin(100));
		} catch (DisabledException e) {
			fail();
		}
		vm.getSelectionButton(0).press();
		assertEquals(0, logic.getCredit());

		try {
			vm.getCoinSlot().addCoin(new Coin(100));
		} catch (DisabledException e) {
			fail();
		}
		vm.getSelectionButton(1).press();
		assertEquals(0, logic.getCredit());

		try {
			vm.getCoinSlot().addCoin(new Coin(100));
		} catch (DisabledException e) {
			fail();
		}
		vm.getSelectionButton(2).press();
		assertEquals(0, logic.getCredit());

		try {
			vm.getCoinSlot().addCoin(new Coin(100));
		} catch (DisabledException e) {
			fail();
		}
		vm.getSelectionButton(3).press();
		assertEquals(0, logic.getCredit());

		try {
			vm.getCoinSlot().addCoin(new Coin(200));
		} catch (DisabledException e) {
			fail();
		}
		vm.getSelectionButton(4).press();
		assertEquals(50, logic.getCredit());

		try {
			vm.getCoinSlot().addCoin(new Coin(200));
		} catch (DisabledException e) {
			fail();
		}
		vm.getSelectionButton(5).press();
		int check = logic.getCredit();
		assertEquals(50, logic.getCredit());
	}
	
}
