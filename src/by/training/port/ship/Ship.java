package by.training.port.ship;

import java.util.List;
import java.util.Random;

import org.apache.logging.log4j.Logger;

import by.training.port.portcomonents.Berth;
import by.training.port.portcomonents.Port;
import by.training.port.portcomonents.PortException;
import by.training.port.warehouse.Container;
import by.training.port.warehouse.Warehouse;

import org.apache.logging.log4j.LogManager;

public class Ship implements Runnable {

	private final static Logger logger = LogManager.getRootLogger();
	private volatile boolean stopThread = false;

	private String name;//имя корабля
	private Port port;//порт содержит общий ресурс для всех кораблей - склад порта
	private Warehouse shipWarehouse;//склад корабля

	public Ship(String name, Port port, int shipWarehouseSize) {
		this.name = name;
		this.port = port;
		shipWarehouse = new Warehouse(shipWarehouseSize);
	}

	public void setContainersToWarehouse(List<Container> containerList) {
		shipWarehouse.addContainer(containerList);
	}

	public String getName() {
		return name;
	}

	public void stopThread() {
		stopThread = true;
	}

	public void run() {
		try {
			while (!stopThread) {
				atSea();
				port.checkArrivalShip();
				//здесь семафор разрешает доступ к порту
				//если в порту все причалы заняты
				//то корабль-поток приостанавливается
				try
				{
				 inPort();
				}
				finally{
					port.shipLeaving();
				}
			}
		} catch (InterruptedException e) {
			logger.error("С кораблем случилась неприятность и он уничтожен.", e);
		} catch (PortException e) {
			logger.error("Не найден причал, который зарезервирован за кораблём", e);//!!! переписать сообщение
		}
	}

	private void atSea() throws InterruptedException {
		Thread.sleep(1000);
	}

	private void inPort() throws PortException, InterruptedException {

		boolean isLockedBerth = false;
		Berth berth = null;
		try {
			//блокируем(захватываем) причал
			isLockedBerth = port.lockBerth(this);
			
			if (isLockedBerth) {
				//получение зарезервированного лодкой причала 
				berth = port.getBerth(this);
				logger.debug("Корабль " + name + " пришвартовался к причалу " + berth.getId());
				ShipAction action = getNextAction();
				//выполнение каких-либо действий кораблём
				executeAction(action, berth);
			} else {
				logger.debug("Кораблю " + name + " отказано в швартовке к причалу ");
			}
		} finally {
			if (isLockedBerth){
				port.unlockBerth(this);
				//снимаем блокировку с причала
				logger.debug("Корабль " + name + " отошел от причала " + berth.getId());
			}
		}
		
	}
	
	//выполнение действия кораблём в порту
	private void executeAction(ShipAction action, Berth berth) throws InterruptedException {
		switch (action) {
		case LOAD_TO_PORT:
 				loadToPort(berth);
			break;
		case LOAD_FROM_PORT:
				loadFromPort(berth);
			break;
		}
	}
	
	//загрузка контенеров с корабля на склад порта
	private boolean loadToPort(Berth berth) throws InterruptedException {

		int containersNumberToMove = conteinersCount(shipWarehouse.getRealSize());
		boolean result = false;

		logger.debug("Корабль " + name + " хочет загрузить " + containersNumberToMove
				+ " контейнеров на склад порта.");
		
		//непосредственно загрузка контейнеров
		//с корабля на склад порта
		result = berth.add(shipWarehouse, containersNumberToMove);
		
		if (!result) {
			logger.debug("Недостаточно места на складе порта для выгрузки кораблем "
					+ name + " " + containersNumberToMove + " контейнеров.");
		} else {
			logger.debug("Корабль " + name + " выгрузил " + containersNumberToMove
					+ " контейнеров в порт.");
			
		}
		return result;
	}

	//загрузка контейнеров с порта на корабль
	private boolean loadFromPort(Berth berth) throws InterruptedException {
		
		int containersNumberToMove = conteinersCount(shipWarehouse.getFreeSize());
		boolean result = false;
		logger.debug("Корабль " + name + " хочет загрузить " + containersNumberToMove
				+ " контейнеров со склада порта.");
		//непосредственно транспортировка
		//контейнеров с склада порта на корабль
		result = berth.get(shipWarehouse, containersNumberToMove);
		
		if (result) {
			logger.debug("Корабль " + name + " загрузил " + containersNumberToMove
					+ " контейнеров из порта.");
		} else {
			logger.debug("Недостаточно места на на корабле " + name
					+ " для погрузки " + containersNumberToMove + " контейнеров из порта.");
		}
		
		return result;
	}
	
	//метод задаёт кол-во контейнеров
	//для транспортировки
	private int conteinersCount(int maxCount) {
		Random random = new Random();
		return random.nextInt(maxCount);
	}
	
	//метод для определения действия 
	//выполняемого кораблём в порту
	private ShipAction getNextAction() {
		Random random = new Random();
		int value = random.nextInt(4000);
		if (value < 1000) {
			return ShipAction.LOAD_TO_PORT;
		} else if (value < 2000) {
			return ShipAction.LOAD_FROM_PORT;
		}
		return ShipAction.LOAD_TO_PORT;
	}

	enum ShipAction {
		LOAD_TO_PORT, LOAD_FROM_PORT
	}
}
