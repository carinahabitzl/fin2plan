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
import org.primefaces.model.charts.axes.cartesian.CartesianScales;
import org.primefaces.model.charts.axes.cartesian.linear.CartesianLinearAxes;
import org.primefaces.model.charts.axes.cartesian.linear.CartesianLinearTicks;
import org.primefaces.model.charts.bar.BarChartDataSet;
import org.primefaces.model.charts.bar.BarChartModel;
import org.primefaces.model.charts.bar.BarChartOptions;
import org.primefaces.model.charts.donut.DonutChartDataSet;
import org.primefaces.model.charts.donut.DonutChartModel;
import org.primefaces.model.charts.line.LineChartDataSet;
import org.primefaces.model.charts.line.LineChartModel;
import org.primefaces.model.charts.line.LineChartOptions;
import org.primefaces.model.charts.optionconfig.animation.Animation;
import org.primefaces.model.charts.optionconfig.legend.Legend;
import org.primefaces.model.charts.optionconfig.legend.LegendLabel;
import org.primefaces.model.charts.optionconfig.title.Title;
import org.primefaces.model.charts.pie.PieChartDataSet;
import org.primefaces.model.charts.pie.PieChartModel;

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
	private BarChartModel barModel;
	private PieChartModel pieModel;
			
	public Dashboard() {
	}
	
	@PostConstruct
	public void createEntityManager() {
		entityManager = EntityManagerFactoryProvider.get().createEntityManager();
		transactionList = entityManager.createQuery("from Transaction", Transaction.class).getResultList();
		createDonutModel();
		createLineModel();
		createBarModel();
		createPieModel();
	}

	@PreDestroy
	public void closeEntityManager() {
		entityManager.close();
	}
	
	
	public List<Transaction> getTransactionList() {
		return transactionList;
	}


	public void setTransactionList() {
		this.transactionList = entityManager.createQuery("from Transaction", Transaction.class).getResultList();
	}

	public double getTotalIncome() {
		this.setTransactionList();
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
		this.setTransactionList();
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
		this.setTransactionList();
		createDonutModel();
		return donutModel;
	}

	public void setDonutModel(DonutChartModel donutModel) {
		this.donutModel = donutModel;
	}
	
    private void createPieModel() {
        pieModel = new PieChartModel();
        ChartData data = new ChartData();

        PieChartDataSet dataSet = new PieChartDataSet();
        
        HashSet<String> categories = new HashSet<>();
        for (Transaction t : transactionList) {
        	if (t.getCategory().getType().equals(CategoryType.INCOME)) {
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

        pieModel.setData(data);
    }

    public PieChartModel getPieModel() {
    	this.setTransactionList();
		createPieModel();
    	return pieModel;
	}

	public void setPieModel(PieChartModel pieModel) {
		this.pieModel = pieModel;
	}
	
	

	public void createLineModel() {
        lineModel = new LineChartModel();
        ChartData data = new ChartData();

        LineChartDataSet dataSet = new LineChartDataSet();
        List<Object> values = new ArrayList<>();
        
       
        for (int i = 0; i < 13; i++) { 
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
		this.setTransactionList();
		createLineModel();
		return lineModel;
	}

	public void setLineModel(LineChartModel lineModel) {
		this.lineModel = lineModel;
	}
	
	
	
    
    public void createBarModel() {
        barModel = new BarChartModel();
        ChartData data = new ChartData();

        BarChartDataSet barDataSet = new BarChartDataSet();
        barDataSet.setLabel("Ausgaben");

        List<Number> values = new ArrayList<>();
        
        for (int i = 0; i < 13; i++) { 
    		double sum = 0;
		for (Transaction t : transactionList) {
			if (t.getCategory().getType().equals(CategoryType.EXPENSE)
					&& t.getDate().getMonth() == i) {
    					sum += t.getAmount();
				}
			}
			values.add(sum);
		}
	        
        barDataSet.setData(values);

        List<String> bgColor = new ArrayList<>();
        bgColor.add("rgba(255, 99, 132, 0.2)");
        bgColor.add("rgba(255, 159, 64, 0.2)");
        bgColor.add("rgba(255, 205, 86, 0.2)");
        bgColor.add("rgba(75, 192, 192, 0.2)");
        bgColor.add("rgba(54, 162, 235, 0.2)");
        bgColor.add("rgba(153, 102, 255, 0.2)");
        bgColor.add("rgba(201, 203, 207, 0.2)");
        barDataSet.setBackgroundColor(bgColor);

        List<String> borderColor = new ArrayList<>();
        borderColor.add("rgb(255, 99, 132)");
        borderColor.add("rgb(255, 159, 64)");
        borderColor.add("rgb(255, 205, 86)");
        borderColor.add("rgb(75, 192, 192)");
        borderColor.add("rgb(54, 162, 235)");
        borderColor.add("rgb(153, 102, 255)");
        borderColor.add("rgb(201, 203, 207)");
        barDataSet.setBorderColor(borderColor);
        barDataSet.setBorderWidth(1);

        data.addChartDataSet(barDataSet);

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
        barModel.setData(data);

        //Options
        BarChartOptions options = new BarChartOptions();
        CartesianScales cScales = new CartesianScales();
        CartesianLinearAxes linearAxes = new CartesianLinearAxes();
        linearAxes.setOffset(true);
        linearAxes.setBeginAtZero(true);
        CartesianLinearTicks ticks = new CartesianLinearTicks();
        linearAxes.setTicks(ticks);
        cScales.addYAxesData(linearAxes);
        options.setScales(cScales);

        Title title = new Title();
        title.setDisplay(true);
        title.setText("Bar Chart");
        options.setTitle(title);

        Legend legend = new Legend();
        legend.setDisplay(true);
        legend.setPosition("top");
        LegendLabel legendLabels = new LegendLabel();
        legendLabels.setFontStyle("italic");
        legendLabels.setFontColor("#2980B9");
        legendLabels.setFontSize(24);
        legend.setLabels(legendLabels);
        options.setLegend(legend);

        // disable animation
        Animation animation = new Animation();
        animation.setDuration(0);
        options.setAnimation(animation);

        barModel.setOptions(options);
    }

	public BarChartModel getBarModel() {
		this.setTransactionList();
		createBarModel();
		return barModel;
	}

	public void setBarModel(BarChartModel barModel) {
		this.barModel = barModel;
	}
    
    
	
}
