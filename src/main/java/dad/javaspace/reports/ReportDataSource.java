package dad.javaspace.reports;

import java.util.ArrayList;
import java.util.List;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

public class ReportDataSource implements JRDataSource {

	private List<ServerReportBean> listaServerReport = new ArrayList<ServerReportBean>();
	private int index = -1;

	public ReportDataSource() {
	}

	@Override
	public boolean next() throws JRException {
		return ++index < listaServerReport.size();
	}

	@Override
	public Object getFieldValue(JRField jrField) throws JRException {
		Object valor = null;
		if ("disparos".equals(jrField.getName())) {
			valor = listaServerReport.get(index).getDisparos();
		} else if ("ganador".equals(jrField.getName())) {
			valor = listaServerReport.get(index).getGanador();
		} else if ("ranking".equals(jrField.getName())) {
			valor = listaServerReport.get(index).getRanking();
		} else if ("fechaInicio".equals(jrField.getName())) {
			valor = listaServerReport.get(index).getFechaInicio();
		} else if ("fechaFin".equals(jrField.getName())) {
			valor = listaServerReport.get(index).getFechaFin();
		}
		return valor;
	}

	public void addServerReport(ServerReportBean serverReport) {
		this.listaServerReport.add(serverReport);
	}

}
