package ca.ucalgary.seng300.a2;

import org.lsmr.vending.hardware.AbstractHardware;
import org.lsmr.vending.hardware.AbstractHardwareListener;
import org.lsmr.vending.hardware.IndicatorLight;
import org.lsmr.vending.hardware.IndicatorLightListener;
import org.lsmr.vending.hardware.VendingMachine;

public class MyExactChangeLightListener implements IndicatorLightListener {
	
	private VendingMachine vm;
	private EventWriter ew;
	private Logic logic;

	public MyExactChangeLightListener(VendingMachine vm, EventWriter ew, Logic logic) {
		this.vm = vm;
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
	public void activated(IndicatorLight light) {
		vm.getDisplay().display("Exact Change Light is on!");
		ew.logEvent("Exact Change Light activated.");
		
	}

	@Override
	public void deactivated(IndicatorLight light) {
		ew.logEvent("Exact Change Light deactivated.");
		
	}
	
}
