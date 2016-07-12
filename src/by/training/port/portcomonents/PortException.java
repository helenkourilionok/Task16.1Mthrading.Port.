package by.training.port.portcomonents;

public class PortException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2333704622446842989L;

	public PortException(String message){
		super(message);
	}
	
	public PortException(Exception exception){
		super(exception);
	}
}
