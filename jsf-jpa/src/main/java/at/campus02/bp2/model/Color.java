package at.campus02.bp2.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "COLOR")
public class Color implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID", unique = true, nullable = false)
	private Long id;
	
	private String hex;
	
	private String name;

	
	// Getter
	
	public String getHex() {
		return hex;
	}

	public String getName() {
		return name;
	}
	

	

	
}
