package edu.utfpr.model;

import org.bson.types.ObjectId;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class Animal {

    private ObjectId animalid;
    private String nome;
    private String raca;
    public Profissional treinador;
    private ArrayList<ServicoRealizado> servicosRealizados;

    public Animal() {
    }

    public Animal(String nome, String raca, Profissional treinador, ArrayList<ServicoRealizado> servicosRealizados) {
        this.nome = nome;
        this.raca = raca;
        this.treinador = treinador;
        this.servicosRealizados = servicosRealizados;
    }

    public void addServicoRealizado(String servicoDescricao, Profissional profissional, String data, String hora){
        Servico servico = Servico.getValor(servicoDescricao);

        String dataHoraString = data + " " + hora; // Combinação de data e hora
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Calendar dataHora = Calendar.getInstance();

        try {
            dataHora.setTime(sdf.parse(dataHoraString));
        } catch (ParseException e) {
            System.out.println("Erro ao converter data e hora: " + e.getMessage());
        }

        ServicoRealizado servicoRealizado = new ServicoRealizado(servico, profissional, dataHora);

        if(this.servicosRealizados == null){
            this.servicosRealizados = new ArrayList<>();
        }

        this.servicosRealizados.add(servicoRealizado);
    }

    public ObjectId getAnimalid() {
        return animalid;
    }

    public void setAnimalid(ObjectId animalid) {
        this.animalid = animalid;
    }

    public Animal criaId() {
        setAnimalid(new ObjectId());
        return this;
    }

    public void setTreinador(Profissional treinador) {
        this.treinador = treinador;
    }

    public void setServicosRealizados(ArrayList<ServicoRealizado> servicosRealizados) {
        this.servicosRealizados = servicosRealizados;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getRaca() {
        return raca;
    }

    public void setRaca(String raca) {
        this.raca = raca;
    }

    public Profissional getTreinador() {
        return treinador;
    }

    public ArrayList<ServicoRealizado> getServicosRealizados() {
        return servicosRealizados;
    }

    @Override
    public String toString() {
        return "Animal{" +
                "animalid=" + animalid +
                ", nome='" + nome + '\'' +
                ", raca='" + raca + '\'' +
                ", treinador=" + treinador.getNome() +
                ", servicosRealizados=" + servicosRealizados +
                '}';
    }
}
