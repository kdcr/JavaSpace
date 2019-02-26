package dad.javaspace.reports;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;

public class Report {

	public Report(ServerReportBean srb) {

		try {
			ReportDataSource datasource = new ReportDataSource();
			ServerReportBean serverReportBean = srb;
			datasource.addServerReport(serverReportBean);

			JasperReport reporte = (JasperReport) JRLoader
					.loadObjectFromFile(getClass().getResource("reports/TemplateParameters.jasper").toString());
			JasperPrint jasperPrint = JasperFillManager.fillReport(reporte, null, datasource);

			OutputStream outputStream = new FileOutputStream(new File(System.getProperty("user.home")
					.concat("/Documents/JavaSpaceReport")
					.concat(new SimpleDateFormat("-yyyy-MM-dd-HH:mm:ss").format(Calendar.getInstance()).toString())
					.concat(".pdf")));
			JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);
			outputStream.close();

		} catch (JRException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
