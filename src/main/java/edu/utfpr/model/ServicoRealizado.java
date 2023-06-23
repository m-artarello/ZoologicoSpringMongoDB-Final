package edu.utfpr.model;

import org.bson.types.ObjectId;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ServicoRealizado {
    private Servico servico;

    private Profissional profissional;
    private Calendar datahora;
    private String datahoraFormatada;

    public ServicoRealizado() {
    }

    public ServicoRealizado(Servico servico, Profissional profissional, Calendar datahora) {
        this.servico = servico;
        this.profissional = profissional;
        this.datahora = datahora;
        this.datahoraFormatada = formataData(datahora);
    }

    public String getDatahoraFormatada() {
        setDatahoraFormatada();
        return datahoraFormatada;
    }

    public void setDatahoraFormatada() {
        this.datahoraFormatada = formataData(this.datahora);
    }

    public Servico getServico() {
        return servico;
    }

    public void setServico(Servico servico) {
        this.servico = servico;
    }

    public Profissional getProfissional() {
        return profissional;
    }

    public void setProfissional(Profissional profissional) {
        this.profissional = profissional;
    }

    public Calendar getDatahora() {
        return datahora;
    }

    public void setDatahora(Calendar datahora) {
        this.datahora = datahora;
    }

    private String formataData(Calendar datahora){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        String datahoraFormatada = sdf.format(datahora.getTime());
        return datahoraFormatada;
    }
}
