package pt.iscte.row_timer.beans;

public class RowTimerException extends Exception {
   public RowTimerException(Exception e) {
	   super(e);
   }
   
   public RowTimerException(String message) {
	   super(message);
   }
}
