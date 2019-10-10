package ca.ucalgary.seng300.a2;

import org.lsmr.vending.Coin;
import org.lsmr.vending.hardware.AbstractHardware;
import org.lsmr.vending.hardware.AbstractHardwareListener;
import org.lsmr.vending.hardware.CoinRack;
import org.lsmr.vending.hardware.CoinRackListener;
import org.lsmr.vending.hardware.VendingMachine;

public class MyCoinRackListener implements CoinRackListener {
	
	private VendingMachine vm;
	private EventWriter ew;
	private Logic logic;
	
	public MyCoinRackListener(VendingMachine vend, EventWriter ew, Logic logic) {
		this.vm = vend;
		this.ew = ew;
		this.logic = logic;
	}

	@Override
	public void enabled(AbstractHardware<? extends AbstractHardwareListener> hardware) {
		// TODO Auto-generated method stub

	}

	@Override
	public void disabled(AbstractHardware<? extends AbstractHardwareListener> hardware) {
		// TODO Auto-generated method stub

	}

	@Override
	public void coinsFull(CoinRack rack) {
		vm.getOutOfOrderLight().activate();
	}

	@Override
	public void coinsEmpty(CoinRack rack) {
		// TODO Auto-generated method stub

	}

	@Override
	public void coinAdded(CoinRack rack, Coin coin) {
		// TODO Auto-generated method stub

	}

	@Override
	public void coinRemoved(CoinRack rack, Coin coin) {
		// TODO Auto-generated method stub

	}

	@Override
	public void coinsLoaded(CoinRack rack, Coin... coins) {
		// TODO Auto-generated method stub

	}

	@Override
	public void coinsUnloaded(CoinRack rack, Coin... coins) {
		// TODO Auto-generated method stub

	}

}
