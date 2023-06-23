package edu.utfpr.repository;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import edu.utfpr.codec.AnimalCodec;
import edu.utfpr.model.Animal;
import edu.utfpr.model.ServicoRealizado;
import org.bson.Document;
import org.bson.codecs.Codec;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class AnimalRepository {

    private MongoClient cliente;
    private MongoDatabase db;

    public void conecta() {
        //Instaciar um codec
        Codec<Document> codec = MongoClient.getDefaultCodecRegistry()
                .get(Document.class);

        //Qual classe ira sofrer o encode/decode
        AnimalCodec animalCodec = new AnimalCodec(codec);

        //Instanciar um registro para o codec
        CodecRegistry registro = CodecRegistries
                .fromRegistries(MongoClient.getDefaultCodecRegistry(),
                        CodecRegistries.fromCodecs(animalCodec));

        //Dar um build no registro
        MongoClientOptions op = MongoClientOptions.builder().
                codecRegistry(registro).build();

        this.cliente = new MongoClient("localhost:27017", op);
        this.db = cliente.getDatabase("Zoologico");
    }

    public void salvar(Animal animal) {
        conecta();
        MongoCollection<Animal> animais = db.getCollection("animal", Animal.class);
        if(animal.getAnimalid() == null){//se não tiver um animal, crio uma animal
            animais.insertOne(animal);
        }else{//se o animal já existir salva somente as alterações
            animais.updateOne(Filters.eq("_id", animal.getAnimalid()), new Document("$set",animal));
        }
        cliente.close();
    }

    public List<Animal> listarTodos() {
        conecta();
        MongoCollection<Animal> animais = db.getCollection("animal", Animal.class);
        MongoCursor<Animal> resultado = animais.find().iterator();
        List<Animal> animalLista = new ArrayList<>();

        while(resultado.hasNext()){
            Animal animal = resultado.next();
            animalLista.add(animal);
        }
        cliente.close();
        return animalLista;
    }

    public List<Animal> listarTodosComServicoNoDiaOuNaoRealizado() {
        conecta();
        MongoCollection<Animal> animais = db.getCollection("animal", Animal.class);
        Date dataInicio = Calendar.getInstance().getTime();
        dataInicio.setHours(0);
        dataInicio.setMinutes(0);
        dataInicio.setSeconds(0);
        Date dataFinal = Calendar.getInstance().getTime();
        dataFinal.setHours(23);
        dataInicio.setMinutes(59);
        dataInicio.setSeconds(59);

        Bson filterData = Filters.elemMatch("servicosRealizados", Filters.and(
                Filters.gte("datahora", dataInicio),
                Filters.lte("datahora", dataFinal)
        ));

        Bson filterListaVazia = Filters.or(
                Filters.eq("servicosRealizados", new ArrayList<>()),
                Filters.exists("servicosRealizados", false)
        );

        Bson finalFilter = Filters.or(filterData, filterListaVazia);


        MongoCursor<Animal> resultado = animais.find().filter(finalFilter).iterator();
        List<Animal> animalLista = new ArrayList<>();

        while(resultado.hasNext()){
            Animal animal = resultado.next();
            animalLista.add(animal);
        }

        for (Animal animal : animalLista) {
            if (animal.getServicosRealizados() != null) {
                List<ServicoRealizado> servicosRealizados = animal.getServicosRealizados();
                ArrayList<ServicoRealizado> servicosFiltrados = new ArrayList<>();

                for (ServicoRealizado servicoRealizado : servicosRealizados) {
                    Calendar dataHora = servicoRealizado.getDatahora();

                    if (!(dataHora.before(dataInicio) || dataHora.after(dataFinal))) {
                        servicosFiltrados.add(servicoRealizado);
                        animal.setServicosRealizados(servicosFiltrados);
                    }
                }
            }
        }

        cliente.close();
        return animalLista;
    }

    public Animal obterId(String id){
        conecta();
        MongoCollection<Animal> animais = db.getCollection("animal", Animal.class);
        Animal animal = animais.find(Filters.eq("_id", new ObjectId(id))).first();
        return animal;
    }

    public List<Animal> buscarPorProfissional(String id){
        conecta();
        MongoCollection<Animal> animalCollection = db.getCollection("animal", Animal.class);
        FindIterable<Animal> animaisDoc = animalCollection.find(Filters.eq("treinador._id", new ObjectId(id)));

        List<Animal> animais = new ArrayList<>();

        for (Animal animalDoc : animaisDoc){
            animais.add(animalDoc);
        }
        return animais;
    }

    public void excluir(String id) {
        conecta();
        MongoCollection<Animal> animais = db.getCollection("animal", Animal.class);
        animais.deleteOne(Filters.eq("_id", new ObjectId(id)));
    }

}
