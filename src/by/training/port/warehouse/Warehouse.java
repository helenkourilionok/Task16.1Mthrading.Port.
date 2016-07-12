package by.training.port.warehouse;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Warehouse implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2535487002458636871L;
	private List<Container> containerList;//������ ����������� �� ������ 
	private int size;//������(�����������) ������

	public Warehouse() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Warehouse(int size) {
		containerList = new ArrayList<Container>(size);
		this.size = size;
	}
	//���������� ���������� �� �����
	public boolean addContainer(Container container) {	
		return containerList.add(container);
	}
	//���������� ������ ����������� �� �����
	public boolean addContainer(List<Container> containers) {
		return containerList.addAll(containers);	
	}
	//����� ��������� ����������,����������� �� ������
	public Container getContainer() {
			return containerList.remove(0);
	}
	//����� ��������� ��������� ����������� �������� amount
	//��� ���� ��������� ��������� �� ������
	public List<Container> getContainer(int amount) {		
			List<Container> cargo = new ArrayList<Container>(containerList.subList(0, amount));
			containerList.removeAll(cargo);
			return cargo;
	}
	//����� ��������� ��������� �������(�����������) ������
	//�������� �������(�����������) ������
	public int getSize(){
		return size;
	}
	//��������� ��������� ������� ������
	//�.�. ���-�� ����������� ���������� �� ������
	//���� ��������� ��������� ������� ������
	//�.�. ������� �� �� ������ ��������� �����������
	public int getRealSize(){
		return containerList.size();
	}
	//��������� ���������� ����� �� ������
	//�.�. ���-�� �����������, ������� �� ����� ��������� �� �����
	//����� ��������� ���������� ����� �� ������
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
