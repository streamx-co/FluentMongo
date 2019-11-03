package co.streamx.fluent.mongo;

import org.bson.conversions.Bson;

import com.mongodb.client.model.Filters;

import co.streamx.fluent.extree.expression.BinaryExpression;
import co.streamx.fluent.extree.expression.Expression;
import co.streamx.fluent.extree.expression.ExpressionType;
import co.streamx.fluent.extree.expression.UnaryExpression;

final class FilterInterpreter extends GenericInterpreter {

    @Override
    public Expression visit(BinaryExpression e) {

        super.visit(e);

        String path;
        Object constant;
        switch (e.getExpressionType()) {
        case ExpressionType.LogicalAnd:
            Bson b2a = bsons.pop();
            bsons.push(Filters.and(bsons.pop(), b2a));

            break;
        case ExpressionType.LogicalOr:
            Bson b2o = bsons.pop();
            bsons.push(Filters.or(bsons.pop(), b2o));

            break;

        case ExpressionType.Equal:
            path = paths.poll();
            constant = constants.pop();
            bsons.push(Filters.eq(path == null ? "$eq" : path, constant));
            break;
        case ExpressionType.NotEqual:
            path = paths.poll();
            constant = constants.pop();
            bsons.push(path == null ? Filters.eq("$ne", constant) : Filters.ne(path, constant));
            break;

        case ExpressionType.GreaterThan:
            path = paths.poll();
            constant = constants.pop();
            bsons.push(path == null ? Filters.eq("$gt", constant) : Filters.gt(path, constant));
            break;
        case ExpressionType.GreaterThanOrEqual:
            path = paths.poll();
            constant = constants.pop();
            bsons.push(path == null ? Filters.eq("$gte", constant) : Filters.gte(path, constant));
            break;
        case ExpressionType.LessThan:
            path = paths.poll();
            constant = constants.pop();
            bsons.push(path == null ? Filters.eq("$lt", constant) : Filters.lt(path, constant));
            break;
        case ExpressionType.LessThanOrEqual:
            path = paths.poll();
            constant = constants.pop();
            bsons.push(path == null ? Filters.eq("$lte", constant) : Filters.lte(path, constant));
            break;
        default:
            throw new IllegalArgumentException(
                    TranslationError.UNSUPPORTED_EXPRESSION_TYPE.getError(getOperatorSign(e.getExpressionType())));
        }

        return e;
    }

    @Override
    public Expression visit(UnaryExpression e) {
        super.visit(e);

        String path;
        switch (e.getExpressionType()) {
        case ExpressionType.IsNull:
            path = paths.poll();
            bsons.push(path == null ? Filters.eq("$eq", null) : Filters.eq(path, null));
            bsons.push(Filters.eq(paths.pop(), null));
            break;
        case ExpressionType.IsNonNull:
            path = paths.poll();
            bsons.push(path == null ? Filters.eq("$ne", null) : Filters.ne(path, null));
            break;
        case ExpressionType.Convert:
            break;

        case ExpressionType.LogicalNot:
            bsons.push(Filters.not(bsons.pop()));
            break;
        default:
            throw new IllegalArgumentException(
                    TranslationError.UNSUPPORTED_EXPRESSION_TYPE.getError(getOperatorSign(e.getExpressionType())));
        }

        return e;
    }

    private static String getOperatorSign(int expressionType) {
        switch (expressionType) {
        case ExpressionType.LogicalAnd:
            return "AND";
        case ExpressionType.LogicalOr:
            return "OR";
        case ExpressionType.NotEqual:
            return "<>";
        default:
            return ExpressionType.toString(expressionType);
        }
    }
}
