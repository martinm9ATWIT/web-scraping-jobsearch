package application;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;
import java.lang.*;
import java.text.*;
import java.math.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.TextField;
import javafx.scene.input.*;
import javafx.stage.Stage;

public class controlerClass{

	@FXML
	private TextField job1;
	@FXML
	private TextField job2;
	@FXML
	private TextField job3;
	@FXML
	private TextField job4;
	@FXML
	private TextField job5;
	@FXML
	private Label result1;
	@FXML
	private Label result2;
	@FXML
	private Label result3;
	@FXML
	private Label result4;
	@FXML
	private Label result5;
	@FXML
	private Label jobname1;
	@FXML
	private Label jobname2;
	@FXML
	private Label jobname3;
	@FXML
	private Label jobname4;
	@FXML
	private Label jobname5;
	@FXML
	private Button search;
	@FXML
	private Button exit;

	private HashMap<String, Integer> hm = new HashMap<String, Integer>();
	private List<String> jobss = new ArrayList<String>();
	private List<String> numss = new ArrayList<String>();
	private String[] job = new String[5];
	private int[] num = new int[5];
	NumberFormat numm = NumberFormat.getInstance();

	// rank jobs using number available
	public static Map<String, Integer> sortByValue(Map<String, Integer> map) {
		
		//create list using elements from haspmap
		List<Map.Entry<String, Integer>> list = new LinkedList(map.entrySet());
	    
	    //compare and sort values in the list
	    Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
	        @Override
	        public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
	            return o2.getValue().compareTo(o1.getValue());
	        }
	    });

	    //create hashmap of sorted values
	    Map<String, Integer> result = new LinkedHashMap<>();
	    for (Map.Entry<String, Integer> entry : list) {
	        result.put(entry.getKey(), entry.getValue());
	    }
	    return result;
	}

	public static String getJobResults(String s) {

		String html = "https://www.indeed.com/jobs?q=" + s;

		try {

			Document doc = Jsoup.connect(html).get();
			Element element = doc.select("#searchCountPages").first();
			String num_job = element.text();

			return cleanNumJobs(num_job);

		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
		}

		return "none found";

	}

	// cleaning the job result method so its all numbers
	public static String cleanNumJobs(String s) {

		String clean = s.replaceAll("Page 1 of ", "");
		String clean2 = clean.replaceAll(" jobs", "");
		String clean3 = clean2.replaceAll(",", "");
		return clean3;

	}

	public void action(ActionEvent event) {
		
		// names of top 5 jobs youre looking
		job[0] = job1.getText();
		job[1] = job2.getText();
		job[2] = job3.getText();
		job[3] = job4.getText();
		job[4] = job5.getText();

		// organize info from app to corresponding arrays
		for (int i = 0; i < job.length; i++) {
			if (job[i].isBlank()) {
				job[i] = "Empty String";
				num[i] = 0;

			} else if (getJobResults(job[i]) == "none found") {
				num[i] = 0;

			} else {
				num[i] = Integer.parseInt(getJobResults(job[i]));
			}
		}
		
		//sort info
		printSort(job, num);
	
		
		//fill in removed duplicated array elements to Empty String : 0
		int size = jobss.size();
		while(5-size != 0) {			
			jobss.add(size, "Empty String");
			numss.add(size, "0");
			size++;
		}
		
		
		//print sorted map to corresponding spaces
		jobname1.setText(fix(jobss.get(0)) + " : ");
		result1.setText(fixNum(numss.get(0)));
		
		jobname2.setText(fix(jobss.get(1)) + " : ");
		result2.setText(fixNum(numss.get(1)));
		
		jobname3.setText(fix(jobss.get(2)) + " : ");
		result3.setText(fixNum(numss.get(2)));
		
		jobname4.setText(fix(jobss.get(3)) + " : ");
		result4.setText(fixNum(numss.get(3)));
		
		jobname5.setText(fix(jobss.get(4)) + " : ");
		result5.setText(fixNum(numss.get(4)));
		
	}

	public void printSort(String[] job, int[] num) {
		for (int i = 0; i < job.length; i++) {
			hm.put(job[i], num[i]);
		}
		Map<String, Integer> hm1 = sortByValue(hm);
		for (Map.Entry<String, Integer> wen : hm1.entrySet()) {
			jobss.add(wen.getKey());
			numss.add(wen.getValue().toString());
		}
	}

	public String fix(String word) {
		if (word.length() == 1) {
			word = word.toUpperCase();
		}
		if (word.length() > 1) {
			word = word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase();
		}
		return word;
	}

	public String fixNum(String nu) {
		numm.setGroupingUsed(true);
		return numm.format(Integer.parseInt(nu));
	}
	
	public void exitAct(ActionEvent e) throws Exception {
		System.exit(0);
	}

}
