package at.campus02.bp2.mbean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import at.campus02.bp2.model.Category;
import at.campus02.bp2.model.CategoryType;
import at.campus02.bp2.utils.EntityManagerFactoryProvider;

@ManagedBean
@SessionScoped
public class CategoryBean {

	private EntityManager entityManager;

	private Category newCategory = new Category();
	private List<Category> categoryList = new ArrayList<Category>();
	private List<CategoryType> categoryTypeList;
	
	public CategoryBean(){
	}
//
	@PostConstruct
	public void createEntityManager() {
		entityManager = EntityManagerFactoryProvider.get().createEntityManager();
		categoryTypeList = Arrays.asList(CategoryType.values());
	}

	@PreDestroy
	public void closeEntityManager() {
		entityManager.close();
	}
	
	public void loadCategoriesFromDB() {
		categoryList = entityManager.createQuery("from Category", Category.class).getResultList();
	}
	
	public void save() {
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
		entityManager.merge(newCategory);
		transaction.commit();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "Die Kategorie " + newCategory.getName() + " wurde gespeichert"));
        newCategory = new Category();
	}
	
	public List<Category> getCategoryList() {
		loadCategoriesFromDB();
		return categoryList;
	}
	public void setCategoryList(List<Category> categoryList) {
		this.categoryList = categoryList;
	}

	public Category getNewCategory() {
		return newCategory;
	}
	public void setNewCategory(Category newCategory) {
		this.newCategory = newCategory;
	}
		
	public List<CategoryType> getCategoryTypeList() {
        return categoryTypeList;
    }

    public void setCategoryTypeList(List<CategoryType> categoryTypeList) {
        this.categoryTypeList = categoryTypeList;
    }
	
    public void editCategory(Category category) {
        EntityTransaction transaction = entityManager.getTransaction();

        try {
            transaction.begin();

            // Kategorie aus der Datenbank laden
            Category categoryToEdit = entityManager.find(Category.class, category.getId());

            if (categoryToEdit != null) {
                // Kategorie aktualisieren
                categoryToEdit.setName(category.getName());
                categoryToEdit.setType(category.getType());

                entityManager.merge(categoryToEdit); // Kategorie aktualisieren
                transaction.commit();
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Erfolg", "Kategorie wurde bearbeitet."));
                                
                partnerBean.updatePartnerCategories(categoryToEdit);
                transactionBean.updateTransactionCategories(categoryToEdit);
                //this.setCategoryList(this.getCategoryList());
                //partnerBean.setPartnerList(partnerBean.getPartnerList());
                
            } else {
                transaction.rollback();
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fehler", "Kategorie konnte nicht gefunden werden."));
            }
        } catch (Exception e) {
            transaction.rollback();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fehler", "Fehler beim Bearbeiten der Kategorie: " + e.getMessage()));
        }
    }
	
	public void deleteCategory(Category category) {
        EntityTransaction transaction = entityManager.getTransaction();
        
        try {
            transaction.begin();
            
            // Kategorie aus der Datenbank laden
            Category categoryToDelete = entityManager.find(Category.class, category.getId());
            
            if (categoryToDelete != null) {
                entityManager.remove(categoryToDelete); // Kategorie löschen
                transaction.commit();
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Erfolg", "Kategorie wurde gelöscht."));
            } else {
                transaction.rollback();
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fehler", "Kategorie konnte nicht gefunden werden."));
            }
        } catch (Exception e) {
            transaction.rollback();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fehler", "Fehler beim Löschen der Kategorie: " + e.getMessage()));
        }
    }
	
	@ManagedProperty(value="#{partnerBean}")
    private PartnerBean partnerBean;

	public PartnerBean getPartnerBean() {
		return partnerBean;
	}
	public void setPartnerBean(PartnerBean partnerBean) {
		this.partnerBean = partnerBean;
	}
	
	@ManagedProperty(value="#{transactionBean}")
    private TransactionBean transactionBean;

	public TransactionBean getTransactionBean() {
		return transactionBean;
	}
	public void setTransactionBean(TransactionBean transactionBean) {
		this.transactionBean = transactionBean;
	}
	
	
}
