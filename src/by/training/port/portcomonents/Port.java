package by.training.port.portcomonents;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.logging.log4j.Logger;

import by.training.port.ship.Ship;
import by.training.port.warehouse.Container;
import by.training.port.warehouse.Warehouse;

import org.apache.logging.log4j.LogManager;

public class Port {
	private final static Logger logger = LogManager.getRootLogger();

	private List<Berth> berthList; // список причалов
	private Warehouse portWarehouse; // склад порта
	private Map<Ship, Berth> usedBerths; // какой корабль у какого причала стоит
	private Semaphore shipChecker;// проверяет сколько кораблей вошло в порт
	private Lock berthLocker;// объект предназначенный для блокировки причала

	public Port(int berthSize, int warehouseSize) {
		portWarehouse = new Warehouse(warehouseSize); // создаем пустое
														// хранилище
		berthList = new ArrayList<Berth>(berthSize); // создаем очередь причалов
		for (int i = 0; i < berthSize; i++) { // заполняем очередь причалов
												// непосредственно самими
												// причалами
			berthList.add(new Berth(i,portWarehouse));
		}
		usedBerths = new HashMap<Ship, Berth>(); // создаем объект, который
		// будет хранит корабль и причал,к которому он причалил

		// проверяет сколько кораблей вошло в порт
		// нужно, чтобы в порту было кораблей(потоков)
		// не более чем причалов
		this.shipChecker = new Semaphore(berthSize);
		// используется для блокировки(захвата) причала кораблём
		this.berthLocker = new ReentrantLock();

		logger.debug("Порт создан.");
	}

	public void setContainersToWarehouse(List<Container> containerList){
		 portWarehouse.addContainer(containerList); }
	
	// метод инкапсулирует обращение к семафору,который служит
	// чтобы пропустить корабли в порт в количестве
	// меньшим либо равным кол-ву пристаней
	public void checkArrivalShip() throws InterruptedException {
		shipChecker.acquire();
	}
	//метод освобождает семафор
	//таким образом другая лодка получает возможность 
	//захватить причал
	public void shipLeaving() {
		shipChecker.release();
	}
	
	//метод для блокировки(захвата) причала
	public boolean lockBerth(Ship ship) {
		boolean result = false;
		Berth berth;

		berthLocker.lock();
		//блокируем причал
		berth = berthList.remove(0);
		
		if (berth != null) {
			result = true;
			usedBerths.put(ship, berth);
			//устанавливаем соответствие между кораблём и причалом
		}

		return result;
	}

	//метод для разблокировки  причала
	public boolean unlockBerth(Ship ship) {
		//по адресу лодки получаем причал
		//удаляем его из очереди используемых причалов
		//возвращаем в общий список причалов
		Berth berth = usedBerths.get(ship);
		berthList.add(berth);
		usedBerths.remove(ship);
		
		berthLocker.unlock();
		//разблокировка причала
		return true;
	}

	//метод для получения объекта 
	//зарезервированного за лодкой причала
	public Berth getBerth(Ship ship) throws PortException {

		Berth berth = usedBerths.get(ship);
		if (berth == null) {
			throw new PortException("Try to use Berth without blocking.");
		}
		return berth;
	}

}
