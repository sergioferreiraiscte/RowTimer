package pt.iscte.row_timer.beans;

public class RowTimerException extends Exception {
	private int code;

	public RowTimerException(Exception e) {
		super(e);
	}

	public RowTimerException(String message) {
		super(message);
	}
	
	public RowTimerException(String message,int code) {
		super(message);
		this.code = code;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

}
