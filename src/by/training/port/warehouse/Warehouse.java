package by.training.port.warehouse;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Warehouse implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2535487002458636871L;
	private List<Container> containerList;//список контейнеров на складе 
	private int size;//размер(вместимость) склада

	public Warehouse() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Warehouse(int size) {
		containerList = new ArrayList<Container>(size);
		this.size = size;
	}
	//добавление контейнера на склад
	public boolean addContainer(Container container) {	
		return containerList.add(container);
	}
	//добавление списка контейнеров на склад
	public boolean addContainer(List<Container> containers) {
		return containerList.addAll(containers);	
	}
	//метод получения контейнера,хранящегося на складе
	public Container getContainer() {
			return containerList.remove(0);
	}
	//метод получения подсписка контейнеров размером amount
	//при этом контенеры удаляются со склада
	public List<Container> getContainer(int amount) {		
			List<Container> cargo = new ArrayList<Container>(containerList.subList(0, amount));
			containerList.removeAll(cargo);
			return cargo;
	}
	//метод получения реального размера(вместимости) склада
	//полчение размера(вместимости) склада
	public int getSize(){
		return size;
	}
	//получение реального размера склада
	//т.е. кол-ва контейнеров хранящихся на складе
	//меод получения реального размера склада
	//т.е. сколько на нём сейчас находится контейнеров
	public int getRealSize(){
		return containerList.size();
	}
	//получение свободного места на складе
	//т.е. кол-ва контейнеров, которое мы можем поместить на склад
	//метод получения свободного места на складе
	public int getFreeSize(){
		return size - containerList.size();
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((containerList == null) ? 0 : containerList.hashCode());
		result = prime * result + size;
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Warehouse other = (Warehouse) obj;
		if (containerList == null) {
			if (other.containerList != null)
				return false;
		} else if (!containerList.equals(other.containerList))
			return false;
		if (size != other.size)
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "Warehouse [containerList=" + containerList + ", size=" + size + "]";
	}
	
}
