package ru.sevn.mytutor.dao;

import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.DataSource;
import com.couchbase.lite.Database;
import com.couchbase.lite.Document;
import com.couchbase.lite.Expression;
import com.couchbase.lite.From;
import com.couchbase.lite.Meta;
import com.couchbase.lite.Ordering;
import com.couchbase.lite.Query;
import com.couchbase.lite.QueryBuilder;
import com.couchbase.lite.Result;
import com.couchbase.lite.SelectResult;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import org.springframework.beans.factory.annotation.Autowired;
import ru.sevn.mytutor.builders.ObjectDocumentBuilder;
import ru.sevn.mytutor.db.DbConnector;
import ru.sevn.mytutor.entities.BaseObject;

public abstract class DaoComponent<OBJ extends BaseObject, BLDR extends ObjectDocumentBuilder<OBJ>> {
    
    @Autowired
    private DbConnector dbConnector;
    
    public abstract BLDR getObjectDocumentBuilder();
    public abstract String getObjectId(OBJ obj);
    
    protected Database getDatabase() {
        return dbConnector.getDatabase();
    }

    public OBJ findById(String id) {
        var doc = dbConnector.getDatabase().getDocument(id);
        if (doc != null) {
            return getObjectDocumentBuilder().getObject(true, doc.toMutable());
        } else {
            return null;
        }
    }
    
    public List<OBJ> findObjects(Function<Expression, Expression> whereExpression, Ordering... orderings) throws CouchbaseLiteException {
        return findObjects(whereExpression.apply(getObjectDocumentBuilder().getWhereExpression()), orderings);
    }
    
    public List<OBJ> findObjects() throws CouchbaseLiteException {
        return findObjects(getObjectDocumentBuilder().getWhereExpression());
    }
    
    public List<OBJ> findObjects(Expression exp, Ordering... orderings) throws CouchbaseLiteException {
        Query listQuery;
        var qfrom = QueryBuilder.select(SelectResult.expression(Meta.id).as("metaID"))
                .from(DataSource.database(getDatabase()));
        listQuery = qfrom;
        if (exp != null) {
            var qwhere = qfrom.where(exp);
            listQuery = qwhere.orderBy(orderings);
        }
        var rs = listQuery.execute().allResults();
        var res = new ArrayList<OBJ>();
        for (Result result : rs) {
            var thisDocsId = result.getString("metaID");
            var thisDoc = getDatabase().getDocument(thisDocsId);
            var obj = getObjectDocumentBuilder().getObject(false, thisDoc.toMutable());
            res.add(obj);
        }
        return res;
        
    }
    
    public OBJ findObject(OBJ objSrc) throws CouchbaseLiteException {
        var thisDoc = findStoredObject(objSrc);
        if (thisDoc != null) {
            var obj = getObjectDocumentBuilder().getObject(true, thisDoc.toMutable());
            return obj;
        }
        return null;
    }
    
    private Document findStoredObject(OBJ obj) throws CouchbaseLiteException {
        Query listQuery = QueryBuilder.select(SelectResult.expression(Meta.id).as("metaID"))
                .from(DataSource.database(getDatabase()))
                .where(getObjectDocumentBuilder().getIdWhereExpression(obj));
        var rs = listQuery.execute().allResults();
        for (Result result : rs) {
            var thisDocsId = result.getString("metaID");
            var thisDoc = getDatabase().getDocument(thisDocsId);
            return thisDoc;
        }
        return null;
    }
    
    public OBJ addOnNotFound(OBJ obj) throws CouchbaseLiteException {
        var userDb = findObject(obj);
        if (userDb != null) {
            System.out.println("Found data");
            System.out.println("FOUND>" + userDb);
            return userDb;
        } else {
            System.out.println("Add data");
            return save(obj);
        }
    }
    
    public OBJ save(OBJ obj) throws CouchbaseLiteException {
        var doc = getObjectDocumentBuilder().getDocument(obj);
        getDatabase().save(doc);
        return findObject(obj);
    }
    
    public void delete(OBJ obj) throws CouchbaseLiteException {
        var doc = findStoredObject(obj);
        if (doc != null) {
            getDatabase().delete(doc);
        }        
    }
}
