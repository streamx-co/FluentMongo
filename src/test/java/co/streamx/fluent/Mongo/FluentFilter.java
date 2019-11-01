package co.streamx.fluent.Mongo;

import org.bson.conversions.Bson;

import com.mongodb.client.ClientSession;
import com.mongodb.client.DistinctIterable;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.CountOptions;
import com.mongodb.client.model.DeleteOptions;
import com.mongodb.client.model.ReplaceOptions;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;

public interface FluentFilter<TDocument> {
    long countDocuments(MongoCollection<TDocument> collection);

    long countDocuments(Bson filter,
                        CountOptions options);

    long countDocuments(ClientSession clientSession,
                        Bson filter);

    long countDocuments(ClientSession clientSession,
                        Bson filter,
                        CountOptions options);

    <TResult> DistinctIterable<TResult> distinct(String fieldName,
                                                 Bson filter,
                                                 Class<TResult> resultClass);

    <TResult> DistinctIterable<TResult> distinct(ClientSession clientSession,
                                                 String fieldName,
                                                 Bson filter,
                                                 Class<TResult> resultClass);

    FindIterable<TDocument> find(Bson filter);

    <TResult> FindIterable<TResult> find(Bson filter,
                                         Class<TResult> resultClass);

    FindIterable<TDocument> find(ClientSession clientSession,
                                 Bson filter);

    <TResult> FindIterable<TResult> find(ClientSession clientSession,
                                         Bson filter,
                                         Class<TResult> resultClass);

    DeleteResult deleteOne(Bson filter);

    DeleteResult deleteOne(Bson filter,
                           DeleteOptions options);

    DeleteResult deleteOne(ClientSession clientSession,
                           Bson filter);

    DeleteResult deleteOne(ClientSession clientSession,
                           Bson filter,
                           DeleteOptions options);

    DeleteResult deleteMany(Bson filter);

    DeleteResult deleteMany(Bson filter,
                            DeleteOptions options);

    DeleteResult deleteMany(ClientSession clientSession,
                            Bson filter);

    DeleteResult deleteMany(ClientSession clientSession,
                            Bson filter,
                            DeleteOptions options);

    UpdateResult replaceOne(Bson filter,
                            TDocument replacement);

    UpdateResult replaceOne(Bson filter,
                            TDocument replacement,
                            ReplaceOptions replaceOptions);

    UpdateResult replaceOne(ClientSession clientSession,
                            Bson filter,
                            TDocument replacement);

    UpdateResult replaceOne(ClientSession clientSession,
                            Bson filter,
                            TDocument replacement,
                            ReplaceOptions replaceOptions);
}
