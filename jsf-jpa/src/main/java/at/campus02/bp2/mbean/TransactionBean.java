package at.campus02.bp2.mbean;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

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
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "Die Transaction " + newTransaction.getId() + " wurde gespeichert"));
        
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
}
