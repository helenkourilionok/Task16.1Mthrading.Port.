package by.training.port.main;

import java.util.ArrayList;
import java.util.List;

import by.training.port.portcomonents.Port;
import by.training.port.ship.Ship;
import by.training.port.warehouse.Container;

public class Main {

	public static void main(String[] args) throws InterruptedException {

		int countBerth = 2;
		int warehousePortSize = 15;
		List<Container> containerList = new ArrayList<Container>(warehousePortSize);
		for (int i=0; i<warehousePortSize; i++){
			containerList.add(new Container(i));
		}

		Port port = new Port(countBerth, 900);
		port.setContainersToWarehouse(containerList);
		
		containerList = new ArrayList<Container>(warehousePortSize);
		for (int i=0; i<warehousePortSize; i++){
			containerList.add(new Container(i+30));
		}
		Ship ship1 = new Ship("Ship1", port, 90);
		ship1.setContainersToWarehouse(containerList);
		
		containerList = new ArrayList<Container>(warehousePortSize);
		for (int i=0; i<warehousePortSize; i++){
			containerList.add(new Container(i+60));
		}
		Ship ship2 = new Ship("Ship2", port, 90);
		ship2.setContainersToWarehouse(containerList);
		
		containerList = new ArrayList<Container>(warehousePortSize);
		for (int i=0; i<warehousePortSize; i++){
			containerList.add(new Container(i+60));
		}
		Ship ship3 = new Ship("Ship3", port, 90);
		ship3.setContainersToWarehouse(containerList);		
		//создан один порт один общий для всех склад порта
		//создано 3 корабля для которых есть общий порт
		//корабли имеют одинаковую вместимость - 90
		//вместимость хранилища порта - 900
		
		new Thread(ship1).start();		
		new Thread(ship2).start();		
		new Thread(ship3).start();
		

		Thread.sleep(3000);
		
		ship1.stopThread();
		ship2.stopThread();
		ship3.stopThread();

	}

}
