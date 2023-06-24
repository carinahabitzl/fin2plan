package at.campus02.bp2.mbean;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import at.campus02.bp2.model.CategoryType;
import at.campus02.bp2.model.Transaction;

@ManagedBean
@SessionScoped
public class Dashboard {
	
	private List<Transaction> transactions;
	
	public Dashboard() {
		transactions = new ArrayList<>();
	}
	
	public double totalIncome() {
		double result = 0;
		for (Transaction t : transactions) {
			if (t.getCategory().getType() == CategoryType.INCOME) {
				result += t.getAmount();
			}
		}
		return result;
	}
	
	public double totalExpenses() {
		double result = 0;
		for (Transaction t : transactions) {
			if (t.getCategory().getType() == CategoryType.EXPENSE) {
				result += t.getAmount();
			}
		}
		return result;
	}
	
	public double totalCashflow() {
		return totalIncome() - totalExpenses();
	}
	
	

	public List<Transaction> getTransactions() {
		return transactions;
	}

	public void setTransactions(List<Transaction> transactions) {
		this.transactions = transactions;
	}
	
	
}
