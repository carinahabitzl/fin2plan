package at.campus02.bp2.mbean;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;

@ManagedBean
public class LoginBean {
	
	private String email;
	private String password;
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	public void validateEmail(FacesContext context, 
			UIComponent component,
			Object value) throws ValidatorException {
		
		if (value == null) {
			return;
		}
		
		String regex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";
		
		if (!value.toString().matches(regex)) {
			FacesMessage message = new FacesMessage("Ungültige E-Mail Adresse");
			throw new ValidatorException(message);
		}
	}
}
