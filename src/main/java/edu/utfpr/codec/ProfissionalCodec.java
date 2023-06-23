package edu.utfpr.codec;

import edu.utfpr.model.Profissional;
import org.bson.*;
import org.bson.codecs.Codec;
import org.bson.codecs.CollectibleCodec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.bson.types.ObjectId;

import javax.print.Doc;

public class ProfissionalCodec implements CollectibleCodec<Profissional> {
    private Codec<Document> codec;

    public ProfissionalCodec(Codec<Document> codec) {
        this.codec = codec;
    }

    @Override
    public Profissional generateIdIfAbsentFromDocument(Profissional profissional) {
        return documentHasId(profissional) ? profissional.criaId() : profissional;
    }

    @Override
    public boolean documentHasId(Profissional profissional) {
        return profissional.getProfissionalid() == null;
    }

    @Override
    public BsonValue getDocumentId(Profissional profissional) {
        if(!documentHasId(profissional)){
            throw new IllegalStateException("Esse documento não tem ID");
        } else {
            return new BsonString(profissional.getProfissionalid().toHexString());
        }
    }

    @Override
    public Profissional decode(BsonReader bsonReader, DecoderContext decoderContext) {
        Document doc = codec.decode(bsonReader, decoderContext);
        Profissional profissional = new Profissional();

        profissional.setProfissionalid(doc.getObjectId("_id"));
        profissional.setNome(doc.getString("nome"));
        profissional.setFuncao(doc.getString("funcao"));

        return profissional;
    }

    @Override
    public void encode(BsonWriter bsonWriter, Profissional profissional, EncoderContext encoderContext) {
        ObjectId profissionalid = profissional.getProfissionalid();
        String nome = profissional.getNome();
        String funcao = profissional.getFuncao();

        Document doc = new Document();
        doc.put("_id", profissionalid);
        doc.put("nome", nome);
        doc.put("funcao", funcao);

        codec.encode(bsonWriter, doc, encoderContext);
    }

    @Override
    public Class<Profissional> getEncoderClass() {
        return Profissional.class;
    }
}
