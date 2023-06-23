package edu.utfpr.service;

import edu.utfpr.model.Animal;
import edu.utfpr.model.ServicoRealizado;
import edu.utfpr.model.ServicoRealizadoReport;
import edu.utfpr.repository.AnimalRepository;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ServicoReport {

    @Autowired
    private AnimalRepository repository;

    public String exportaRelatorio (String formato) throws FileNotFoundException, JRException {
        List<Animal> animais = repository.listarTodosComServicoNoDiaOuNaoRealizado();
        //List<Animal> animais = repository.listarTodos();

        //criar e compilar um arquivo
        File arquivo = ResourceUtils.getFile("classpath:relatorio.jrxml");
        JasperReport report = JasperCompileManager.compileReport(arquivo.getAbsolutePath());

        //Parametros de impressao
        Map<String, Object> parametros = new HashMap<>();
        parametros.put("Relatorio Criado por Matheus", "Exemplo de Parametros");

        //Buscar os dados, mapea-los e inserir no arquivo
        List<ServicoRealizadoReport> servicos = new ArrayList<>();

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

        for(Animal animal : animais){
            if (animal.getServicosRealizados() != null){
                for (ServicoRealizado servicoRealizado : animal.getServicosRealizados()){

                    ServicoRealizadoReport servicoRealizadoReport = new ServicoRealizadoReport();
                    servicoRealizadoReport.setAnimalnome(animal.getNome());
                    servicoRealizadoReport.setTreinadornome(animal.treinador.getNome());
                    servicoRealizadoReport.setServico(servicoRealizado.getServico().getDescricao());

                    String data = dateFormat.format(servicoRealizado.getDatahora().getTime());
                    String hora = timeFormat.format(servicoRealizado.getDatahora().getTime());

                    Calendar datahora = servicoRealizado.getDatahora();

                    servicoRealizadoReport.setData(data);
                    servicoRealizadoReport.setHora(hora);
                    servicoRealizadoReport.setDatahora(datahora);

                    servicos.add(servicoRealizadoReport);
                }
            } else {
                ServicoRealizadoReport servicoRealizadoReport = new ServicoRealizadoReport();
                servicoRealizadoReport.setAnimalnome(animal.getNome());
                servicoRealizadoReport.setTreinadornome(animal.treinador.getNome());
                servicoRealizadoReport.setServico("Não realizado");

                servicoRealizadoReport.setData("-");
                servicoRealizadoReport.setHora("-");
                Calendar datahora = Calendar.getInstance();

                servicoRealizadoReport.setDatahora(datahora); // Seto a data e hora atual para listar os registros no final do relatório

                servicos.add(servicoRealizadoReport);
            }

        }

        Collections.sort(servicos, new Comparator<ServicoRealizadoReport>() {
            @Override
            public int compare(ServicoRealizadoReport sr1, ServicoRealizadoReport sr2) {
                return sr2.getDatahora().compareTo(sr1.getDatahora());
            }
        });

        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(servicos);
        JasperPrint print = JasperFillManager.fillReport(report, parametros, dataSource);

        //Caminho do Arquivo
        String path = "D:\\Matheus\\UTFPR\\OO24S\\JavaSpringMongoReport-main\\Relatórios";

        //Tipo de saída
        if(formato.equalsIgnoreCase("html")){
            JasperExportManager.exportReportToHtmlFile(print, path+"\\relatorio.html");
        }
        if(formato.equalsIgnoreCase("pdf")){
            JasperExportManager.exportReportToPdfFile(print, path+"\\relatorio.pdf");
        }
        return "relatorio gerado em: "+path;
    }
}
