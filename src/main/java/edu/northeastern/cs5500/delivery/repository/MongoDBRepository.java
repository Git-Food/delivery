package edu.northeastern.cs5500.delivery.repository;

import static com.mongodb.client.model.Filters.eq;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import edu.northeastern.cs5500.delivery.model.Model;
import edu.northeastern.cs5500.delivery.service.MongoDBService;
import java.util.ArrayList;
import java.util.Collection;
import javax.annotation.Nullable;
import javax.inject.Inject;
import org.bson.types.ObjectId;

public class MongoDBRepository<T extends Model> implements GenericRepository<T> {

    MongoCollection<T> collection;

    @Inject
    public MongoDBRepository(Class<T> clazz, MongoDBService mongoDBService) {
        MongoDatabase mongoDatabase = mongoDBService.getMongoDatabase();
        String[] className = clazz.getName().split("\\.");
        collection = mongoDatabase.getCollection(className[className.length - 1], clazz);
    }

    @Nullable
    public T get(ObjectId id) {
        // return collection.find(eq("id", id)).first();
        return collection.find(eq("_id", id)).first();
    }

    @Override
    public T add(T item) {
        if (item.getId() == null) {
            item.setId(new ObjectId());
        }
        collection.insertOne(item);
        return item;
    }

    @Override
    public T update(T item) {
        return collection.findOneAndReplace(eq("_id", item.getId()), item);
    }

    @Override
    public void delete(ObjectId id) {
        collection.deleteOne(eq("_id", id));
    }

    @Override
    public Collection<T> getAll() {
        return collection.find().into(new ArrayList<>());
    }

    @Override
    public long count() {
        return collection.countDocuments();
    }
}
