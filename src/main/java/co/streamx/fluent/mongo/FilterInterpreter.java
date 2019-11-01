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
            bsons.push(Filters.eq(paths.pop(), constants.pop()));
            break;
        case ExpressionType.NotEqual:
            bsons.push(Filters.ne(paths.pop(), constants.pop()));
            break;

        case ExpressionType.GreaterThan:
            bsons.push(Filters.gt(paths.pop(), constants.pop()));
            break;
        case ExpressionType.GreaterThanOrEqual:
            bsons.push(Filters.gte(paths.pop(), constants.pop()));
            break;
        case ExpressionType.LessThan:
            bsons.push(Filters.lt(paths.pop(), constants.pop()));
            break;
        case ExpressionType.LessThanOrEqual:
            bsons.push(Filters.lte(paths.pop(), constants.pop()));
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

        switch (e.getExpressionType()) {
        case ExpressionType.IsNull:
            bsons.push(Filters.eq(paths.pop(), null));
            break;
        case ExpressionType.IsNonNull:
            bsons.push(Filters.ne(paths.pop(), null));
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
