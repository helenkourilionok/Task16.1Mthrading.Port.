package by.training.port.warehouse;

import java.io.Serializable;

public class Container implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6172273354063901248L;
	private int id;//идентификатор контейнера	
	public Container() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Container(int id){
		this.id = id;
	}
	//метод получения идентификатора контейнера
	public int getId(){
		return id;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
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
		Container other = (Container) obj;
		if (id != other.id)
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "Container [id=" + id + "]";
	}
}
