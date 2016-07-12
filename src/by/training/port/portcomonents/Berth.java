package by.training.port.portcomonents;

import java.util.List;

import by.training.port.warehouse.Container;
import by.training.port.warehouse.Warehouse;

public class Berth {

	private int id;//идентификатор порта
	private Warehouse portWarehouse;//склад порта
	
	public Berth(int id,Warehouse portWarehouse) {
		this.id = id;
		this.portWarehouse = portWarehouse;
	}
	//метод получения идентификатора причала
	public int getId() {
		return id;
	}
	//метод разгрузки корабля
	//позволяет со склада корабля(shipWarehouse) 
	//переместить контейнеры(numberOfConteiners) на склад порта(portWarehouse)
	public boolean add(Warehouse shipWarehouse, int numberOfConteiners) throws InterruptedException {
		boolean result = false;
		//синхронизируем доступ к разделяемому ресурсу - складу порта
		//так как склад порта общий для всех кораблей
		synchronized (portWarehouse) {
			if(portWarehouse.getFreeSize()>=numberOfConteiners){
				//проверка - достаточно ли места на складе порта для принятия груза
				result = true; 
				List<Container> listContainer = shipWarehouse.getContainer(numberOfConteiners);
				portWarehouse.addContainer(listContainer);
				//перемещаем груз со склада корабля на склад порта
			}
		}
		
		return result;
		
	}
	//метод загрузки корабля 
	//позволяет переместить из склада порта(portWarehouse)
	//контейнеры(numberOfConteiners) на склад корабля(portWarehouse)  
	public boolean get(Warehouse shipWarehouse, int numberOfConteiners) throws InterruptedException {
		boolean result = false;
		//синхронизируем доступ к разделямому складу порта		
		synchronized (portWarehouse) {
				if(portWarehouse.getRealSize()>=numberOfConteiners){
					//проверка наличия заданного кол-ва контейнеров на складе
					result = true;
					List<Container> listContainers = portWarehouse.getContainer(numberOfConteiners);
					shipWarehouse.addContainer(listContainers);
					//переммещаем контейнеры с склада порта на склад корабля
				}
		}
		return result;
	}
	
	
}
