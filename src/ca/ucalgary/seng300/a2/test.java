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

public class test {
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

	// Tests behavior when the correct coins are added, but not enough of them are
	// added to buy a pop
	@Test
	public void testNotEnoughCoins() {
		try {
			vm.getCoinSlot().addCoin(new Coin(25));
		} catch (DisabledException e) {
			fail();
		}
		vm.getSelectionButton(0).press();
		assertEquals("Credit: 25\nNot enough credit\n", outContent.toString());
		assertEquals(25, logic.getCredit());
	}

	// Tests what happens when a button is pushed with no money added whatsoever
	@Test
	public void testNoCoins() {
		vm.getSelectionButton(0).press();
		assertEquals("Not enough credit\n", outContent.toString());
		assertEquals(0, logic.getCredit());
	}

	// Tests to see if the coin slot rejects the money once the receptacle is full,
	// and if vending can still proceed as intended after too many coins were added
	@Test
	public void testAddOverCapacity() {
		String a = "";
		try {
			for (int i = 0; i < 201; i++) {
				vm.getCoinSlot().addCoin(new Coin(25));
			}
		} catch (DisabledException e) {
			fail();
		}
		vm.getSelectionButton(0).press();
		for (int i = 25; i < 5001 ; i += 25) {
			a += "Credit: " + Integer.toString(i) + "\n";
		}
		
		a += "Coin return slot is full, please take your change\n25 coin rejected. Please insert valid coin.\n\nThank you for your purchase!\n";
		assertEquals(a, outContent.toString());
		assertEquals(0, logic.getCredit());
	}

	// Tests behavior upon emptying a pop can rack.
	// The coin receptacle should still have the money that a user entered if pop
	// was not vended
	@Test
	public void testEmptyPopCanRack() {
		String s = "";
		try {
			for (int i = 0; i < 11; i++) {
				vm.getCoinSlot().addCoin(new Coin(100));
				vm.getSelectionButton(0).press();
				if (i < 10) {
					s += "Credit: 100\nThank you for your purchase!\n";
				}
			}
		} catch (DisabledException e) {
			fail();
		}
		s += "Credit: 100\nSorry, all out of that selection\n";
		System.out.println(s);
		System.out.println(outContent.toString());
		assertTrue(outContent.toString().contains(s));
		//assertEquals(s, outContent.toString());
		int credit = logic.getCredit();
		assertEquals(100, logic.getCredit());
	}

	// Tests pressing an invalid button
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

	// Tests such that if a user adds too little money,and presses a button, the
	// money stays in the receptacle
	// and they can add more money until they have enough.
	@Test
	public void testCoinsKeptInReceptacle() {
		try {
			vm.getCoinSlot().addCoin(new Coin(100));
		} catch (DisabledException e) {
			fail();
		}
		vm.getSelectionButton(5).press();
		assertEquals(100, logic.getCredit());
		try {
			vm.getCoinSlot().addCoin(new Coin(100));
		} catch (DisabledException e) {
			fail();
		}
		vm.getSelectionButton(5).press();
		assertEquals("Credit: 100\nNot enough credit\nCredit: 200\nThank you for your purchase!\n", outContent.toString());
		assertEquals(0, logic.getCredit());
	}

	// Tests that invalid coins don't count towards money used to buy pop
	@Test
	public void testAddInvalidCoins() {
		try {
			vm.getCoinSlot().addCoin(new Coin(300));
		} catch (DisabledException e) {
			fail();
		}
		assertEquals(0, logic.getCredit());
	}

}
