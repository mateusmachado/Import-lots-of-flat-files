package com.ilegra;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;

import org.apache.log4j.Logger;

import com.ilegra.listener.Listener;

public class Main {

	final static Logger logger = Logger.getLogger(Main.class);
	private static String inputFolderPath = "/Users/mateus/test/data/in/";
	
	public static void main(String[] args) throws FileNotFoundException, IOException, Exception {
		new Main().processFiles();

		Listener listener = new Listener();
		
		new Main().directoryListener(listener, inputFolderPath);
	}

	public void processFiles() throws FileNotFoundException, IOException {
		Main main = new Main();
		int salesMan = 0;
		int customers = 0;
		String idBestSales = "";
		String worstSalesman = "";
		BigDecimal bigSale = BigDecimal.ZERO;
		BigDecimal smallSale = BigDecimal.ZERO;

		BufferedReader br = null;
		 File[] listOfFiles = main.getListOfFiles(inputFolderPath);
		BufferedWriter bw = main.getOutputFile("/Users/mateus/test/data/out/flat_file_name.done.dat");

		for (File file : listOfFiles) {
			if (file.isFile() && file.getName().endsWith(".dat")) {
				FileReader reader = new FileReader(file);
				br = new BufferedReader(reader);

				String line;
				try {
					while ((line = br.readLine()) != null) {
						String[] sales = line.split("ç");

						switch (sales[0]) {
						case "001":
							salesMan += 1;
							break;
						case "002":
							customers += 1;
							break;
						case "003":
							String[] item = sales[2].replace("[", "").replace("]", "").split(",");
							String[] quantity = item[1].split("-");
							String[] price = item[2].split("-");
							BigDecimal total = BigDecimal.ZERO;
							for (int i = 0; i < quantity.length; i++) {
								BigDecimal priceItem = new BigDecimal(quantity[i]).multiply(new BigDecimal(price[i]));
								total = total.add(priceItem);
							}

							if (total.compareTo(bigSale) == 1) {
								bigSale = bigSale.add(total);
								idBestSales = sales[1];
							}

							if ((total.compareTo(smallSale) == -1) || (smallSale.compareTo(BigDecimal.ZERO) == 0)) {
								smallSale = smallSale.add(total);
								worstSalesman = sales[3];
							}

							logger.info(sales[3] + bigSale);
							logger.info(sales[3] + smallSale);
							logger.info(total);
							logger.info(idBestSales);
							logger.info(worstSalesman);
							break;
						}
					}

				} catch (IOException e) {
					logger.error("Error when processing the file! " + e.getMessage());
				}
			}
		}

		bw.write("Customers: " + customers + "\nSalesMan: " + salesMan);
		bw.newLine();
		bw.write("ID of the most expensive sale: " + idBestSales + "\nWorst salesman ever: " + worstSalesman);
		bw.newLine();
		br.close();
		bw.close();
	}

	private void directoryListener(Listener listener, String folderPath) throws IOException {
		File file = new File(folderPath);
		listener.listenForChanges(file);
	}

	public File[] getListOfFiles(String folderPath) {
		File folder = new File(folderPath);
		return folder.listFiles();
	}

	private BufferedWriter getOutputFile(String filePath) throws IOException {
		File outputFile = new File(filePath);
		return new BufferedWriter(new FileWriter(outputFile));
	}
}
