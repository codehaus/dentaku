/*
 * Copyright (c) 2004 Your Corporation. All Rights Reserved.
 */
package org.dentaku.services.persistence.tranql;

import org.tranql.query.QueryCommandView;
import org.tranql.query.QueryCommand;
import org.tranql.ql.QueryException;
import org.tranql.ql.QuerySource;
import org.tranql.ql.Node;
import org.tranql.ql.Select;
import org.tranql.ql.EntityReference;
import org.tranql.ql.From;
import org.tranql.ql.Where;
import org.tranql.ql.BinaryOperation;
import org.tranql.ql.AttributeReference;
import org.tranql.ql.ParameterReference;
import org.tranql.ql.Query;
import org.tranql.schema.Attribute;
import org.tranql.schema.Entity;
import org.tranql.field.FieldTransform;
import org.tranql.field.FieldAccessor;
import org.tranql.field.NestedRowAccessor;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

public class QueryBuilder {
    private Schema schema;

    public QueryBuilder(Schema schema) {
        this.schema = schema;
    }

//    public QueryCommand buildSelectById(Entity entity, boolean forUpdate) throws QueryException {
//        List params = new ArrayList();
//
//        Node select = new Select(false);
//        List attrs = entity.getAttributes();
//        for (int i = 0; i < attrs.size(); i++) {
//            Attribute attribute = (Attribute) attrs.get(i);
//            select.addChild(new AttributeReference(null, attribute));
//        }
//
//        FieldTransform[] resultTransforms = new FieldTransform[attrs.size()];
//        for (int i = 0; i < resultTransforms.length; i++) {
//            Attribute attribute = (Attribute) attrs.get(i);
//            resultTransforms[i] = new FieldAccessor(i, attribute.getType());
//        }
//
//
//        Node expr = getWhereClause(entity, params);
//
//        // 041205 - this is the line I was using...
//        Query query = new Query((FieldTransform[]) params.toArray(new FieldTransform[params.size()]), resultTransforms);
//
//        // just to get a compile
//        Query query = null;
//        query.addChild(select).addChild(new From().addChild(new QuerySource(entity, null, forUpdate))).addChild(new Where().addChild(expr));
//        return schema.getCommandFactory().createQuery(query);
//    }


//        /**
//     * SELECT OBJECT(entity) FROM ejb AS entity WHERE entity.pk = ?
//     * @param name the global name of the EJB
//     * @return a select Query
//     */
//    public QueryCommandView buildFindByPrimaryKey(String name, boolean local) throws QueryException {
//        ModelEntity ejb = (ModelEntity)schema.getEntity(name);
//        QuerySource source = new QuerySource(ejb, ejb.getName().substring(0, 1));
//
//        List pkFields = ejb.getPrimaryKeyFields();
//        FieldTransform[] resultTransforms = new FieldTransform[pkFields.size()];
//        for (int i = 0; i < resultTransforms.length; i++) {
//            Attribute attribute = (Attribute) pkFields.get(i);
//            resultTransforms[i] = new FieldAccessor(i, attribute.getType());
//        }
//
//        Node select = new Select(false);
//        select.addChild(new EntityReference(source));
//        Node from = new From().addChild(source);
//
//        FieldTransform[] paramTransforms = new FieldTransform[pkFields.size()];
//
////        IdentityDefiner identityDefiner = identityDefinerBuilder.getIdentityDefiner(ejb);
////        IdentityTransform identityTransform = identityDefinerBuilder.getPrimaryKeyTransform(ejb);
//
//        Where where = new Where();
//        Node rootCond = null;
//        for (int i = 0; i < paramTransforms.length; i++) {
//            Attribute att = (Attribute) pkFields.get(i);
//            Node localCond = new BinaryOperation(BinaryOperation.EQ);
//            localCond.addChild(new AttributeReference(source, att));
//            localCond.addChild(new ParameterReference(i));
//            rootCond = null == rootCond ? localCond : new BinaryOperation(BinaryOperation.AND).addChild(rootCond).addChild(localCond);
////            paramTransforms[i] = new NestedRowAccessor(
////                new IdentityExtractorAccessor(new GlobalIdentityAccessor(new FieldAccessor(0, null), identityTransform), identityDefiner),
////                i,
////                att.getType());
//        }
//        where.addChild(rootCond);
//
//        Query query = (Query) new Query(paramTransforms, resultTransforms).addChild(select).addChild(from).addChild(where);
//        QueryCommand queryCommand = schema.getCommandFactory().createQuery(query);
//
////        identityDefiner = identityDefinerBuilder.getIdentityDefiner(ejb, 0);
////        EJBProxyFactory proxyFactory = ejb.getProxyFactory();
////        FieldTransform[] view = new FieldTransform[1];
////        view[0] = new ReferenceAccessor(identityDefiner);
////        view[0] = new DomainIdentityAccessor(view[0], identityTransform);
////        if ( local ) {
////            view[0] = new EJBLocalObjectAsIdTransform(view[0], proxyFactory, ejb.getPrimaryKeyClass());
////        } else {
////            view[0] = new EJBObjectAsIdTransform(view[0], proxyFactory, ejb.getPrimaryKeyClass());
////        }
//
//        return null; // new QueryCommandView(queryCommand, view);
//    }

    private static Node getWhereClause(Entity entity, List params) {
        Node expr = null;

        List attrs = entity.getAttributes();
        for (Iterator i = attrs.iterator(); i.hasNext();) {
            Attribute attribute = (Attribute) i.next();
            if (attribute.isIdentity()) {
                int param = params.size();
                params.add(new FieldAccessor(param, attribute.getType()));

                Node term = new BinaryOperation(BinaryOperation.EQ).addChild(new AttributeReference(null, attribute)).addChild(new ParameterReference(param));
                if (expr == null) {
                    expr = term;
                } else {
                    expr = new BinaryOperation(BinaryOperation.AND).addChild(expr).addChild(term);
                }
            }
        }
        return expr;
    }
}
