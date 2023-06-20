package at.campus02.bp2.mbean;

import java.util.List;


import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.persistence.EntityManager;

import at.campus02.bp2.model.Partner;
import at.campus02.bp2.utils.EntityManagerFactoryProvider;


@FacesConverter(forClass = Partner.class)
public class PartnerConverter implements Converter {

	@Override
	  public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String value) {
	    Object partnerObject = null;

	    if ((value != null) && !"".equals(value)) {
		      EntityManager entityManager = EntityManagerFactoryProvider.get().createEntityManager();
	    	try {
	    	      Integer partnerId = Integer.valueOf(value);

	    	      partnerObject = entityManager.find(Partner.class, partnerId);
	    	} catch (NumberFormatException e) {
	    		List<Partner> partnerList = entityManager.createQuery("from Partner", Partner.class).getResultList();
	    		if (!partnerList.isEmpty()) {
	    			partnerObject = null;
	    		}
	    	}
	    }

	    return partnerObject;
	  }

	@Override
	  public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object value) {
	    String strValue = "";

	    if (value != null) {
	      if (value instanceof String) {

	      } else {
	    	  Partner partner = (Partner) value;
	        strValue = Long.toString(partner.getId());
	      }
	    }

	    return strValue;
	  }


	
	
}