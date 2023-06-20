package at.campus02.bp2.mbean;

import java.util.List;


import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.persistence.EntityManager;

import at.campus02.bp2.model.Category;
import at.campus02.bp2.utils.EntityManagerFactoryProvider;


@FacesConverter(forClass = Category.class)
public class CategoryConverter implements Converter {

	@Override
	  public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String value) {
	    Object categoryObject = null;

	    if ((value != null) && !"".equals(value)) {
		      EntityManager entityManager = EntityManagerFactoryProvider.get().createEntityManager();
	    	try {
	    	      Integer categoryId = Integer.valueOf(value);

	    	      categoryObject = entityManager.find(Category.class, categoryId);
	    	} catch (NumberFormatException e) {
	    		List<Category> categoryList = entityManager.createQuery("from Category", Category.class).getResultList();
	    		if (!categoryList.isEmpty()) {
	    			categoryObject = null;
	    		}
	   	}
	    }

	    return categoryObject;
	  }

	@Override
	  public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object value) {
	    String strValue = "";

	    if (value != null) {
	      if (value instanceof String) {

	      } else {
	        Category category = (Category) value;
	        strValue = Long.toString(category.getId());
	      }
	    }

	    return strValue;
	  }


	
	
}