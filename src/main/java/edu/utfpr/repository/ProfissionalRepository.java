package edu.utfpr.repository;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import edu.utfpr.codec.ProfissionalCodec;
import edu.utfpr.model.Profissional;
import org.bson.Document;
import org.bson.codecs.Codec;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ProfissionalRepository {

    private MongoClient cliente;
    private MongoDatabase db;

    public void conecta() {
        //Instaciar um codec
        Codec<Document> codec = MongoClient.getDefaultCodecRegistry()
                .get(Document.class);

        //Qual classe ira sofrer o encode/decode
        ProfissionalCodec profissionalCodec = new ProfissionalCodec(codec);

        //Instanciar um registro para o codec
        CodecRegistry registro = CodecRegistries
                .fromRegistries(MongoClient.getDefaultCodecRegistry(),
                        CodecRegistries.fromCodecs(profissionalCodec));

        //Dar um build no registro
        MongoClientOptions op = MongoClientOptions.builder().
                codecRegistry(registro).build();

        this.cliente = new MongoClient("localhost:27017", op);
        this.db = cliente.getDatabase("Zoologico");
    }

    public void salvar(Profissional profissional) {
        conecta();
        MongoCollection<Profissional> profissionais = db.getCollection("profissional", Profissional.class);
        if(profissional.getProfissionalid() == null){//se não tiver um profissional, crio uma profissional
            profissionais.insertOne(profissional);
        }else{//se o profissional já existir salva somente as alterações
            profissionais.updateOne(Filters.eq("_id", profissional.getProfissionalid()), new Document("$set",profissional));
        }
        cliente.close();
    }

    public List<Profissional> listarTodos() {
        conecta();
        MongoCollection<Profissional> profissionais = db.getCollection("profissional", Profissional.class);
        MongoCursor<Profissional> resultado = profissionais.find().iterator();
        List<Profissional> profissionalLista = new ArrayList<>();

        while(resultado.hasNext()){
            Profissional profissional = resultado.next();
            profissionalLista.add(profissional);
        }
        cliente.close();
        return profissionalLista;
    }

    public Profissional obterId(String id){
        conecta();
        MongoCollection<Profissional> profissionais = db.getCollection("profissional", Profissional.class);
        Profissional profissional = profissionais.find(Filters.eq("_id", new ObjectId(id))).first();
        return profissional;
    }

    public void excluir(String id) {
        conecta();
        MongoCollection<Profissional> profissionais = db.getCollection("profissional", Profissional.class);
        profissionais.deleteOne(Filters.eq("_id", new ObjectId(id)));
    }
}
