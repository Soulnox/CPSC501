package ca.ucalgary.seng300.a2;

import org.lsmr.vending.Coin;
import org.lsmr.vending.hardware.CapacityExceededException;
import org.lsmr.vending.hardware.DisabledException;
import org.lsmr.vending.hardware.Display;
import org.lsmr.vending.hardware.EmptyException;
import org.lsmr.vending.hardware.VendingMachine;

public class Logic {
	private VendingMachine vm;
	private EventWriter ew;
	private int credit = 0;
	private int[] coinKinds;
	
	//listeners
	//private ButtonListener buttonListener;
	//private PCRListener popCanRackListener;
	private MyCoinReturnListener returnListener;
	private MyCoinSlotListener slotListener;
	private MyDeliveryChuteListener deliveryListener;
	private MyDisplayListener displayListener;

	//private MyCoinRackListener coinRackListener;
	private MyExactChangeIndicatorLightListener exactChangeListener;
	private MyOutOfOrderLightListener outOfOrderListener;
	
	private MyCoinReceptacleListener receptacleListener;
	
	//constructor for logic class
	public Logic(VendingMachine vend, EventWriter write) {
		
		vm = vend;
		ew = write;
		credit = 0;
		
		//buttonListener = new ButtonListener(vm, ew, this);
		//popCanRackListener = new PCRListener(vm, ew, this);
		returnListener = new MyCoinReturnListener(vm, ew, this);
		slotListener = new MyCoinSlotListener(vm, ew, this);
		deliveryListener = new MyDeliveryChuteListener(vm, ew, this);
		displayListener = new MyDisplayListener(vm, ew, this);
		exactChangeListener = new MyExactChangeIndicatorLightListener(vm, ew, this);
		outOfOrderListener = new MyOutOfOrderLightListener(vm, ew, this);
		receptacleListener = new MyCoinReceptacleListener(vm, ew, this);

		//coinRackListener = new MyCoinRackListener(vm, ew, this);
		
		
		// Register the listeners to their respective classes 
		vm.getCoinReceptacle().register(receptacleListener);
		vm.getDisplay().register(displayListener);
		vm.getCoinSlot().register(slotListener);
		vm.getCoinReturn().register(returnListener);
		vm.getExactChangeLight().register(exactChangeListener);
		vm.getOutOfOrderLight().register(outOfOrderListener);
		vm.getDeliveryChute().register(deliveryListener);
		
		for (int i = 0; i< vm.getNumberOfCoinRacks(); ++i) {
			vm.getCoinRack(i).register(new MyCoinRackListener(vm, ew, this));
		}
		

		for (int i = 0; i < vm.getNumberOfPopCanRacks(); i++) {
			vm.getPopCanRack(i).register(new MyPopCanRackListener(vm, ew, this));
			vm.getSelectionButton(i).register(new MyPushButtonListener(vm, ew, this));
		}
		
		//Array of coin kinds for change return
		coinKinds = new int[vm.getNumberOfCoinRacks()];
		for(int i=0; i<vm.getNumberOfCoinRacks(); i++) {
			coinKinds[i] = vm.getCoinKindForCoinRack(i);
		}
		
		//Sort coinKinds array in descending order
		int temp;
		for(int i = 0; i < coinKinds.length; i++) {
			for(int j = 1; j < coinKinds.length; j++) {
				if(coinKinds[j] > coinKinds[j-1]) {
					temp = coinKinds[j];
					coinKinds[j] = coinKinds[j-1];
					coinKinds[j-1] = temp;
				}
			}
		}
		
	}
	
	public boolean hasPop(int popNum) {
		if (vm.getPopCanRack(popNum).size() > 0)
			return true;
		else
			return false;
	}
	
	//Logic method for returning change via Greed method
	public void returnCoins() {		

		for(int i = 0; i < coinKinds.length;) {
			if(credit >= coinKinds[i]) {
				try {
					vm.getCoinRackForCoinKind(coinKinds[i]).releaseCoin();
					credit -= coinKinds[i];
				}
				catch(DisabledException e) {
					
				}
				catch(EmptyException e) {
					i++;
				}
				catch(CapacityExceededException e) {
					//should never happen
				}

			}
			else if(credit >= coinKinds[coinKinds.length-1]) {
				i++;
			}
			else if(credit == 0) {
				vm.getExactChangeLight().deactivate();
				break;
			}
			else {
				vm.getExactChangeLight().activate();
				break;
			}
		}
	}
	
	
	public int getCredit() {
		return credit;
	}
	
	public void changeCredit(int amount) {
		credit = credit +  amount;
	}
	

	public void setOutofOrder() {

		vm.getCoinSlot().disable();
		vm.getCoinReceptacle().disable();

		for(int i = 0; i < vm.getNumberOfPopCanRacks(); i++)
		    vm.getPopCanRack(i).disable();

		for(int i = 0; i < vm.getNumberOfCoinRacks(); i++)
		    vm.getCoinRack(i).disable();

	}
	
}
