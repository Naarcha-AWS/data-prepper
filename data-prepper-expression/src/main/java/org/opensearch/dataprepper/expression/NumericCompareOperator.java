/*
 * Copyright OpenSearch Contributors
 * SPDX-License-Identifier: Apache-2.0
 */

package org.opensearch.dataprepper.expression;

import org.antlr.v4.runtime.RuleContext;
import org.opensearch.dataprepper.expression.antlr.DataPrepperExpressionParser;

import java.util.Map;
import java.util.function.BiFunction;

import static com.google.common.base.Preconditions.checkArgument;

class NumericCompareOperator implements Operator<Boolean> {
    private final int symbol;
    private final String displayName;
    private final Map<Class<? extends Number>, Map<Class<? extends Number>, BiFunction<Object, Object, Boolean>>> operandsToOperationMap;

    public NumericCompareOperator(final int symbol,
            final Map<Class<? extends Number>, Map<Class<? extends Number>, BiFunction<Object, Object, Boolean>>> operandsToOperationMap) {
        this.symbol = symbol;
        displayName = DataPrepperExpressionParser.VOCABULARY.getDisplayName(symbol);
        this.operandsToOperationMap = operandsToOperationMap;
    }


    @Override
    public int getNumberOfOperands() {
        return 2;
    }

    @Override
    public boolean shouldEvaluate(final RuleContext ctx) {
        return ctx.getRuleIndex() == DataPrepperExpressionParser.RULE_relationalOperatorExpression;
    }

    @Override
    public int getSymbol() {
        return symbol;
    }

    @Override
    public Boolean evaluate(final Object ... args) {
        checkArgument(args.length == 2, displayName + " requires operands length needs to be 2.");
        final Object leftValue = args[0];
        final Object rightValue = args[1];
        final Class<?> leftValueClass = leftValue.getClass();
        final Class<?> rightValueClass = rightValue.getClass();
        if (!operandsToOperationMap.containsKey(leftValueClass)) {
            throw new IllegalArgumentException(displayName + " requires left operand to be either Float or Integer.");
        }
        Map<Class<? extends Number>, BiFunction<Object, Object, Boolean>> rightOperandToOperation =
                operandsToOperationMap.get(leftValueClass);
        if (!rightOperandToOperation.containsKey(rightValueClass)) {
            throw new IllegalArgumentException(displayName + " requires right operand to be either Float or Integer.");
        }
        final BiFunction<Object, Object, Boolean> operation = rightOperandToOperation.get(rightValueClass);
        return operation.apply(leftValue, rightValue);
    }
}
