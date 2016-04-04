package pt.iscte.moss.row_timer.dao;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
/**
 * Entity and Data Access Object of Rowing Events
 * 
 * @author Sergio Ferreira
 *
 */

@Entity (name="pt.iscte.moss.row_timer.dao.Event")
@Table(name="event")
public class Event implements Serializable {
	private static final long serialVersionUID = -8107817603671468807L;
	private String name;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private String id;
	private Date startDate;
	private Date endDate;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
}
