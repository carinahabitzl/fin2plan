package at.campus02.bp2.mbean;

import java.util.ArrayList;
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
import at.campus02.bp2.model.Partner;
import at.campus02.bp2.model.Transaction;
import at.campus02.bp2.utils.EntityManagerFactoryProvider;

@ManagedBean
@SessionScoped
public class TransactionBean {

	private EntityManager entityManager;

	private Transaction newTransaction = new Transaction();
	private List<Transaction> transactionList = new ArrayList<Transaction>();
	private List<Partner> partnerList;
	
	public TransactionBean(){
	}

	@PostConstruct
	public void createEntityManager() {
		entityManager = EntityManagerFactoryProvider.get().createEntityManager();
	}

	@PreDestroy
	public void closeEntityManager() {
		entityManager.close();
	}
	
	public void loadTransactionFromDB() {
		transactionList = entityManager.createQuery("from Transaction", Transaction.class).getResultList();
	}
	
	public void save() {
		EntityTransaction entityTransaction = entityManager.getTransaction();
		entityTransaction.begin();
		entityManager.merge(newTransaction);
		entityTransaction.commit();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "Eine neue Transaction wurde gespeichert"));
        newTransaction = new Transaction();
	}
	
	public List<Partner> getPartnerList() {
		return partnerList;
	}
	
	public List<Transaction> getTransactionList() {
		loadTransactionFromDB();
		return transactionList;
	}
	public void setTransactionList(List<Transaction> transactionList) {
		this.transactionList = transactionList;
	}

	public Transaction getNewTransaction() {
		return newTransaction;
	}
	public void setNewTransaction(Transaction newTransaction) {
		this.newTransaction = newTransaction;
	}
	
	public void editTransaction(Transaction transaction) {	
		
		EntityTransaction entityTransaction = entityManager.getTransaction();

        try {
            entityTransaction.begin();

            // Transaktion aus der Datenbank laden
            Transaction transactionToEdit = entityManager.find(Transaction.class, transaction.getId());

            if (transactionToEdit != null) {
                // Transaktion aktualisieren
                transactionToEdit.setDescription(transaction.getDescription());
                transactionToEdit.setPartner(transaction.getPartner());

                entityManager.merge(transactionToEdit); // Partner aktualisieren
                entityTransaction.commit();
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Erfolg", "Transaktion wurde bearbeitet."));
                dashboard.createEntityManager();
                
            } else {
                entityTransaction.rollback();
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fehler", "Transaktion konnte nicht gefunden werden."));
            }
        } catch (Exception e) {
            entityTransaction.rollback();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fehler", "Fehler beim Bearbeiten des Partners: " + e.getMessage()));
        }
		
	}
	
	public void deleteTransaction(Transaction transaction) {
        EntityTransaction entityTransaction = entityManager.getTransaction();
        
        try {
            entityTransaction.begin();
            
            // Transaktion aus der Datenbank laden
            Transaction transactionToDelete = entityManager.find(Transaction.class, transaction.getId());
            
            if (transactionToDelete != null) {
                entityManager.remove(transactionToDelete); // Transaction löschen
                entityTransaction.commit();
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Erfolg", "Transaction wurde gelöscht."));
            } else {
                entityTransaction.rollback();
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fehler", "Transaction konnte nicht gefunden werden."));
            }
        } catch (Exception e) {
            entityTransaction.rollback();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fehler", "Fehler beim Löschen der Transaction: " + e.getMessage()));
        }
    }
	
	public void updateTransactionPartners(Partner updatedPartner) {
        EntityTransaction entityTransaction = entityManager.getTransaction();

        try {
            entityTransaction.begin();

            List<Transaction> transactionsToUpdate = entityManager.createQuery("SELECT t FROM Transaction t WHERE t.partner = :partner", Transaction.class)
                    .setParameter("partner", updatedPartner)
                    .getResultList();

            for (Transaction transaction : transactionsToUpdate) {
                transaction.setPartner(updatedPartner);
                entityManager.merge(transaction);
            }

            entityTransaction.commit();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Erfolg", "Partner wurde bei Transaktionen aktualisiert."));
        } catch (Exception e) {
            entityTransaction.rollback();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fehler", "Fehler beim Aktualisieren der Partner bei Transaktionen: " + e.getMessage()));
        }
    }
	
	public void updateTransactionCategories(Category updatedCategory) {
	    EntityTransaction entityTransaction = entityManager.getTransaction();

	    try {
	        entityTransaction.begin();

	        // 1. Alle Partner finden, die die aktualisierte Kategorie verwenden
	        List<Partner> partnersToUpdate = entityManager.createQuery("SELECT p FROM Partner p WHERE p.category = :category", Partner.class)
	                .setParameter("category", updatedCategory)
	                .getResultList();

	        // 2. Für jeden Partner die Transaktionen finden und aktualisieren
	        for (Partner partner : partnersToUpdate) {
	            List<Transaction> transactionsToUpdate = entityManager.createQuery("SELECT t FROM Transaction t WHERE t.partner = :partner", Transaction.class)
	                    .setParameter("partner", partner)
	                    .getResultList();

	            for (Transaction transaction : transactionsToUpdate) {
	                // 3. Transaktion aktualisieren, falls sie die geänderte Kategorie verwendet
	                if (transaction.getPartner().getCategory().equals(updatedCategory)) {
	                    transaction.getPartner().setCategory(updatedCategory);
	                    entityManager.merge(transaction);
	                }
	            }
	        }

	        entityTransaction.commit();
	        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Erfolg", "Transaktionen wurden aktualisiert."));
	    } catch (Exception e) {
	        entityTransaction.rollback();
	        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fehler", "Fehler beim Aktualisieren der Transaktionen: " + e.getMessage()));
	    }
	}
	
	@ManagedProperty(value="#{dashboard}")
    private Dashboard dashboard;

	public Dashboard getDashboard() {
		return dashboard;
	}
	public void setDashboard(Dashboard dashboard) {
		this.dashboard = dashboard;
	}
	
	
}
