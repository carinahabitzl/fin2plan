package at.campus02.bp2.mbean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import at.campus02.bp2.model.Category;
import at.campus02.bp2.model.Partner;
import at.campus02.bp2.utils.EntityManagerFactoryProvider;

@ManagedBean
@SessionScoped
public class PartnerBean {

	private EntityManager entityManager;

	private Partner newPartner = new Partner();
	private List<Partner> partnerList = new ArrayList<Partner>();
	private List<Category> categoryList;
	
	public List<Category> getCategoryList() {
		return categoryList;
	}
	public PartnerBean(){
	}
//
	@PostConstruct
	public void createEntityManager() {
		entityManager = EntityManagerFactoryProvider.get().createEntityManager();
	}

	@PreDestroy
	public void closeEntityManager() {
		entityManager.close();
	}
	
	public void loadPartnerFromDB() {
		partnerList = entityManager.createQuery("from Partner", Partner.class).getResultList();
	}
	
	public void save() {
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
		entityManager.merge(newPartner);
		transaction.commit();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "Der Partner " + newPartner.getName() + " wurde gespeichert"));
        
	}

	public List<Partner> getPartnerList() {
		loadPartnerFromDB();
		return partnerList;
	}
	public void setPartnerList(List<Partner> partnerList) {
		this.partnerList = partnerList;
	}

	public Partner getNewPartner() {
		return newPartner;
	}
	public void setNewPartner(Partner newPartner) {
		this.newPartner = newPartner;
	}
	
	public void editPartner(Partner partner) {		
	}
	
	public void deletePartner(Partner partner) {
        EntityTransaction transaction = entityManager.getTransaction();
        
        try {
            transaction.begin();
            
            // Kategorie aus der Datenbank laden
            Partner partnerToDelete = entityManager.find(Partner.class, partner.getId());
            
            if (partnerToDelete != null) {
                entityManager.remove(partnerToDelete); // Partner löschen
                transaction.commit();
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Erfolg", "Partner wurde gelöscht."));
            } else {
                transaction.rollback();
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fehler", "Partner konnte nicht gefunden werden."));
            }
        } catch (Exception e) {
            transaction.rollback();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fehler", "Fehler beim Löschen des Partners: " + e.getMessage()));
        }
    }
}
