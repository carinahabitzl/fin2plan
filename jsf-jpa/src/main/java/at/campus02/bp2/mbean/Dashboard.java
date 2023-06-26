package at.campus02.bp2.mbean;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.persistence.EntityManager;


import org.primefaces.model.charts.ChartData;
import org.primefaces.model.charts.donut.DonutChartDataSet;
import org.primefaces.model.charts.donut.DonutChartModel;
import org.primefaces.model.charts.line.LineChartDataSet;
import org.primefaces.model.charts.line.LineChartModel;
import org.primefaces.model.charts.line.LineChartOptions;
import org.primefaces.model.charts.optionconfig.title.Title;

import at.campus02.bp2.model.CategoryType;
import at.campus02.bp2.model.Transaction;
import at.campus02.bp2.utils.EntityManagerFactoryProvider;


@ManagedBean
@SessionScoped
public class Dashboard {

	private EntityManager entityManager;
	private List<Transaction> transactionList;
	
	private double totalIncome;
	private double totalExpences;
	
	private DonutChartModel donutModel;
	private LineChartModel lineModel;
			
	public Dashboard() {
	}
	
	@PostConstruct
	public void createEntityManager() {
		entityManager = EntityManagerFactoryProvider.get().createEntityManager();
		transactionList = entityManager.createQuery("from Transaction", Transaction.class).getResultList();
		createDonutModel();
		createLineModel();
	}

	@PreDestroy
	public void closeEntityManager() {
		entityManager.close();
	}
	
	
	public List<Transaction> getTransactionList() {
		return transactionList;
	}


	public double getTotalIncome() {
		double result = 0;
		for (Transaction t : transactionList) {
			if (t.getCategory().getType() == CategoryType.INCOME) {
				result += t.getAmount();
			}
		}
		totalIncome = result;
		return totalIncome;
	}
	
	public double getTotalExpenses() {
		double result = 0;
		for (Transaction t : transactionList) {
			if (t.getCategory().getType() == CategoryType.EXPENSE) {
				result += t.getAmount();
			}
		}
		totalExpences = result;
		return totalExpences;
	}
	
	
	public double totalCashflow() {
		return getTotalIncome() - getTotalExpenses();
	}
	
	// Funktioniert nicht, wenn in "createDonutModel" aufgerufen
	/*
	 * private HashMap<String, Double> totalExpencesPerCategory() { HashMap<String,
	 * Double> result = new HashMap<>();
	 * 
	 * for (Transaction t : transactionList) { if
	 * (t.getCategory().getType().equals(CategoryType.EXPENSE)) { String category =
	 * t.getCategory().getName(); double value = result.get(category); if
	 * (!result.containsKey(category)) { result.put(category, value); } else {
	 * result.put(category, value + t.getAmount()); } } }
	 * 
	 * return result; }
	 */
	
	public void createDonutModel() {
        donutModel = new DonutChartModel();
        ChartData data = new ChartData();
        
        DonutChartDataSet dataSet = new DonutChartDataSet();
        
        HashSet<String> categories = new HashSet<>();
        for (Transaction t : transactionList) {
        	if (t.getCategory().getType().equals(CategoryType.EXPENSE)) {
        			categories.add(t.getCategory().getName());
			}
        }
                
        List<Number> values = new ArrayList<>();        
        for (String s : categories) {
        		double sum = 0;
    			for (Transaction t : transactionList) {
    				if (t.getCategory().getName().equals(s)) {
						sum += t.getAmount();
					}
    			}
    			values.add(sum);
        }
        dataSet.setData(values);

        List<String> bgColors = new ArrayList<>();
        bgColors.add("rgb(255, 99, 132)");
        bgColors.add("rgb(54, 162, 235)");
        bgColors.add("rgb(255, 205, 86)");
        dataSet.setBackgroundColor(bgColors);

        data.addChartDataSet(dataSet);
        List<String> labels = new ArrayList<>();
        for (String s : categories) {
        		labels.add(s);
        }
        data.setLabels(labels);

        donutModel.setData(data);
	}

	public DonutChartModel getDonutModel() {
		return donutModel;
	}

	public void setDonutModel(DonutChartModel donutModel) {
		this.donutModel = donutModel;
	}

    public void createLineModel() {
        lineModel = new LineChartModel();
        ChartData data = new ChartData();

        LineChartDataSet dataSet = new LineChartDataSet();
        List<Object> values = new ArrayList<>();
        
       
        for (int i = 1; i < 13; i++) { 
        		double sum = 0;
			for (Transaction t : transactionList) {
				if (t.getCategory().getType().equals(CategoryType.INCOME)
						&& t.getDate().getMonth() == i) {
        					sum += t.getAmount();
				}
			}
			values.add(sum);
		}
        
        
        dataSet.setData(values);
        dataSet.setFill(false);
        dataSet.setLabel("Einnahmen");
        dataSet.setBorderColor("rgb(75, 192, 192)");
        dataSet.setTension(0.1);
        data.addChartDataSet(dataSet);

        List<String> labels = new ArrayList<>();
        labels.add("Jänner");
        labels.add("Februar");
        labels.add("März");
        labels.add("April");
        labels.add("Mai");
        labels.add("Juni");
        labels.add("Juli");
        labels.add("August");
        labels.add("September");
        labels.add("Oktober");
        labels.add("November");
        labels.add("Dezember");
        data.setLabels(labels);

        //Options
        LineChartOptions options = new LineChartOptions();
        Title title = new Title();
        title.setDisplay(true);
        title.setText("Line Chart");
        options.setTitle(title);

        lineModel.setOptions(options);
        lineModel.setData(data);
    }

	public LineChartModel getLineModel() {
		return lineModel;
	}

	public void setLineModel(LineChartModel lineModel) {
		this.lineModel = lineModel;
	}
    
    
	
}
