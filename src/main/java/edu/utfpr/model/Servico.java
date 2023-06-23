package edu.utfpr.model;

public enum Servico {
    ALIMENTAR("Alimentar o animal"),
    SOLTAR("Soltar ao ar livre"),
    VACINAR("Vacinar o animal"),
    RECOLHER_TRANSPORTE("Recolher o animal para transporte"),
    BANHO("Lavar o animal");

    public String descricao;

    Servico(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    public static Servico getValor(String descricao){
        for (Servico servico : Servico.values()){
            if(servico.getDescricao().equalsIgnoreCase(descricao)){
                return servico;
            }
        }
        return null;
    }
}