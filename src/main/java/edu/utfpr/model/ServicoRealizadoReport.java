package edu.utfpr.model;


import java.util.Calendar;

public class ServicoRealizadoReport {
    private String animalnome;
    private String treinadornome;
    private String servico;
    private String data;
    private String hora;
    private Calendar datahora;

    public ServicoRealizadoReport() {
    }

    public ServicoRealizadoReport(String animalnome, String treinadornome, String servico, String data, String hora, Calendar datahora) {
        this.animalnome = animalnome;
        this.treinadornome = treinadornome;
        this.servico = servico;
        this.data = data;
        this.hora = hora;
        this.datahora = datahora;
    }

    public String getAnimalnome() {
        return animalnome;
    }

    public void setAnimalnome(String animalnome) {
        this.animalnome = animalnome;
    }

    public String getTreinadornome() {
        return treinadornome;
    }

    public void setTreinadornome(String treinadornome) {
        this.treinadornome = treinadornome;
    }

    public String getServico() {
        return servico;
    }

    public void setServico(String servico) {
        this.servico = servico;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public Calendar getDatahora() {
        return datahora;
    }

    public void setDatahora(Calendar datahora) {
        this.datahora = datahora;
    }
}
