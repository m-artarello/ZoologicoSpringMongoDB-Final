package edu.utfpr.codec;

import edu.utfpr.model.*;
import org.bson.*;
import org.bson.codecs.Codec;
import org.bson.codecs.CollectibleCodec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AnimalCodec implements CollectibleCodec<Animal> {
    private Codec<Document> codec;

    public AnimalCodec(Codec codec) {
        this.codec = codec;
    }

    @Override
    public Animal generateIdIfAbsentFromDocument(Animal animal) {
        return documentHasId(animal) ? animal.criaId() : animal;
    }

    @Override
    public boolean documentHasId(Animal animal) {
        return animal.getAnimalid() == null;
    }

    @Override
    public BsonValue getDocumentId(Animal animal) {
        if(!documentHasId(animal)){
            throw new IllegalStateException("Esse documento não tem ID");
        } else {
            return new BsonString(animal.getAnimalid().toHexString());
        }
    }

    @Override
    public Animal decode(BsonReader bsonReader, DecoderContext decoderContext) {
        Document doc = codec.decode(bsonReader, decoderContext);
        Animal animal = new Animal();
        Profissional treinador = new Profissional();
        Document docTreinador = doc.get("treinador", Document.class);

        animal.setAnimalid(doc.getObjectId("_id"));
        animal.setNome(doc.getString("nome"));
        animal.setRaca(doc.getString("raca"));
        if (docTreinador != null){
            animal.setTreinador(decodeProfissional(docTreinador));
        }

        ArrayList<Document> servicosRealizados = (ArrayList<Document>) doc.get("servicosRealizados");

        if (servicosRealizados != null) {
            ArrayList<ServicoRealizado> servicoRealizadosDoAnimal = new ArrayList<>();

            for (Document document : servicosRealizados){
                Document docProfissional = document.get("profissional", Document.class);
                if (docProfissional != null){
                    Calendar datahora = Calendar.getInstance();
                    datahora.setTime(document.getDate("datahora"));
                    servicoRealizadosDoAnimal.add(new ServicoRealizado(
                            Servico.getValor((String) document.get("servico")),
                            decodeProfissional(docProfissional),
                            datahora
                    ));
                }

            }
            animal.setServicosRealizados(servicoRealizadosDoAnimal);
        }

        return animal;
    }

    @Override
    public void encode(BsonWriter bsonWriter, Animal animal, EncoderContext encoderContext) {
        ObjectId animalId = animal.getAnimalid();
        String nome = animal.getNome();
        String raca = animal.getRaca();
        Profissional treinador = animal.getTreinador();

        ArrayList<ServicoRealizado> servicosRealizados = animal.getServicosRealizados();

        Document doc = new Document();
        doc.put("_id", animalId);
        doc.put("nome", nome);
        doc.put("raca", raca);
        doc.put("treinador", encodeProfissional(treinador));

        if (servicosRealizados != null) {
            //Se não tiver um servico cadastrada,
            //cria-se uma lista para um NPE
            List<Document> servicoDoc = new ArrayList<>();

            //se houver servicos itera sobre elas para criar
            //um array de servicos no MONGODB
            for (ServicoRealizado servicoRealizado : servicosRealizados) {
                servicoDoc.add(new Document(
                        "servico", servicoRealizado.getServico().getDescricao())
                        .append("profissional", encodeProfissional(servicoRealizado.getProfissional()))
                        .append("datahora", servicoRealizado.getDatahora().getTime())
                );
            }
            doc.put("servicosRealizados",servicoDoc);
        }

        codec.encode(bsonWriter, doc, encoderContext);
    }

    private Document encodeProfissional(Profissional profissional) {
        Document document = new Document();
        document.append("_id", profissional.getProfissionalid());
        document.append("nome", profissional.getNome());
        document.append("funcao", profissional.getFuncao());
        return document;
    }

    private Profissional decodeProfissional(Document document) {
        Profissional profissional = new Profissional();
        profissional.setProfissionalid(document.getObjectId("_id"));
        profissional.setNome(document.getString("nome"));
        profissional.setFuncao(document.getString("funcao"));
        return profissional;
    }

    @Override
    public Class<Animal> getEncoderClass() {
        return Animal.class;
    }
}
