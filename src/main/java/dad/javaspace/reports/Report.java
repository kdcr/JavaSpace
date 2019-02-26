package dad.javaspace.reports;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
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
					.loadObject(new File("src/main/resources/reports/TemplateParameters.jasper"));
			// .loadObject(new
			// File(getClass().getResource("/reports/TemplateParameters.jasper").toString()));
			JasperPrint jasperPrint = JasperFillManager.fillReport(reporte, null, datasource);

			OutputStream outputStream = new FileOutputStream(new File(System.getProperty("user.home")
					.concat("/Documents/JavaSpaceReport").concat(LocalDate.now().toString().replaceAll(":", "-"))
					.concat("_").concat(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")).replaceAll(":", "-")).concat(".pdf")));
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
