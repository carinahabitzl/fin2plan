package at.campus02.bp2.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "TRANSACTION")
public class Transaction implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID", unique = true, nullable = false)
	private int id;
	
	private String description;

	private Date date;
	
	@OneToOne
    @JoinColumn(name = "partner_id")
	private Partner partner;
	
	/*private Category category;*/
	
	private double amount;

	/*getCategory of Transaction*/
	
	
	public Category getCategory() {
		return this.getPartner().getCategory();
	}
	
	
	// Getter und Setter
	
	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Partner getPartner() {
		return partner;
	}

	public void setPartner(Partner partner) {
		this.partner = partner;
	}


	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
}
