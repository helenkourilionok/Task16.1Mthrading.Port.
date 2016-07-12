package by.training.port.portcomonents;

import java.util.List;

import by.training.port.warehouse.Container;
import by.training.port.warehouse.Warehouse;

public class Berth {

	private int id;//������������� �����
	private Warehouse portWarehouse;//����� �����
	
	public Berth(int id,Warehouse portWarehouse) {
		this.id = id;
		this.portWarehouse = portWarehouse;
	}
	//����� ��������� �������������� �������
	public int getId() {
		return id;
	}
	//����� ��������� �������
	//��������� �� ������ �������(shipWarehouse) 
	//����������� ����������(numberOfConteiners) �� ����� �����(portWarehouse)
	public boolean add(Warehouse shipWarehouse, int numberOfConteiners) throws InterruptedException {
		boolean result = false;
		//�������������� ������ � ������������ ������� - ������ �����
		//��� ��� ����� ����� ����� ��� ���� ��������
		synchronized (portWarehouse) {
			if(portWarehouse.getFreeSize()>=numberOfConteiners){
				//�������� - ���������� �� ����� �� ������ ����� ��� �������� �����
				result = true; 
				List<Container> listContainer = shipWarehouse.getContainer(numberOfConteiners);
				portWarehouse.addContainer(listContainer);
				//���������� ���� �� ������ ������� �� ����� �����
			}
		}
		
		return result;
		
	}
	//����� �������� ������� 
	//��������� ����������� �� ������ �����(portWarehouse)
	//����������(numberOfConteiners) �� ����� �������(portWarehouse)  
	public boolean get(Warehouse shipWarehouse, int numberOfConteiners) throws InterruptedException {
		boolean result = false;
		//�������������� ������ � ����������� ������ �����		
		synchronized (portWarehouse) {
				if(portWarehouse.getRealSize()>=numberOfConteiners){
					//�������� ������� ��������� ���-�� ����������� �� ������
					result = true;
					List<Container> listContainers = portWarehouse.getContainer(numberOfConteiners);
					shipWarehouse.addContainer(listContainers);
					//����������� ���������� � ������ ����� �� ����� �������
				}
		}
		return result;
	}
	
	
}
