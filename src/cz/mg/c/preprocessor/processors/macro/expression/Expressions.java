package cz.mg.c.preprocessor.processors.macro.expression;

import cz.mg.annotations.classes.Service;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.annotations.requirement.Optional;
import cz.mg.c.preprocessor.processors.macro.exceptions.MacroException;
import cz.mg.collections.list.List;
import cz.mg.collections.list.ListItem;
import cz.mg.tokenizer.components.TokenReader;
import cz.mg.tokenizer.entities.Token;
import cz.mg.tokenizer.entities.tokens.BracketToken;
import cz.mg.tokenizer.entities.tokens.NumberToken;
import cz.mg.tokenizer.entities.tokens.OperatorToken;

public @Service class Expressions {
    private static volatile @Service Expressions instance;

    public static @Service Expressions getInstance() {
        if (instance == null) {
            synchronized (Service.class) {
                if (instance == null) {
                    instance = new Expressions();
                }
            }
        }
        return instance;
    }

    private Expressions() {
    }

    public boolean evaluate(@Mandatory List<Token> tokens) {
        return evaluateResult(evaluateExpression(new TokenReader(tokens, MacroException::new), false));
    }

    private boolean evaluateResult(@Mandatory Token token) {
        return parse(token) != 0;
    }

    private @Mandatory Token evaluateExpression(@Mandatory TokenReader reader, boolean requireClosed) {
        boolean closed = false;
        int position = -1;
        List<Token> tokens = new List<>();

        while (reader.has() && !closed) {
            Token token = reader.read();
            position = token.getPosition();
            if (isOpeningBracket(token)) {
                tokens.addLast(
                    evaluateExpression(reader, true)
                );
            } else if (isClosingBracket(token)) {
                closed = true;
            } else {
                tokens.addLast(token);
            }
        }

        if (!requireClosed && closed) {
            throw new MacroException(position, "Missing left parenthesis.");
        }

        if (requireClosed && !closed) {
            throw new MacroException(position, "Missing right parenthesis.");
        }

        List<Token> result = evaluateOperators(tokens);

        if (result.count() < 1) {
            throw new MacroException(position, "Empty expression.");
        } else if (result.count() > 1) {
            throw new MacroException(position, "Unresolved expression.");
        } else {
            return result.getFirst();
        }
    }

    private @Mandatory List<Token> evaluateOperators(@Mandatory List<Token> tokens) {
        for (List<Operator> operators : Operators.OPERATORS) {
            for (ListItem<Token> item = tokens.getFirstItem(); item != null; item = item.getNextItem()) {
                if (isOperator(item)) {
                    for (Operator operator : operators) {
                        if (isOperator(item, operator)) {
                            evaluateOperator(item, operator);
                        }
                    }
                }
            }
        }
        return tokens;
    }

    private void evaluateOperator(@Mandatory ListItem<Token> item, @Mandatory Operator operator) {
        switch (operator.getType()) {
            case BINARY: evaluateBinary(item, operator); break;
            case LUNARY: evaluateLunary(item, operator); break;
            case RUNARY: evaluateRunary(item, operator); break;
        }
    }

    private void evaluateBinary(@Mandatory ListItem<Token> item, @Mandatory Operator operator) {
        int left = parse(item.removePrevious());
        int right = parse(item.removeNext());
        int result = operator.getOperation().evaluate(left, right);
        item.set(create(String.valueOf(result)));
    }

    private void evaluateLunary(@Mandatory ListItem<Token> item, @Mandatory Operator operator) {
        int right = parse(item.removeNext());
        int result = operator.getOperation().evaluate(0, right);
        item.set(create(String.valueOf(result)));
    }

    private void evaluateRunary(@Mandatory ListItem<Token> item, @Mandatory Operator operator) {
        int left = parse(item.removePrevious());
        int result = operator.getOperation().evaluate(left, 0);
        item.set(create(String.valueOf(result)));
    }

    private boolean isOperator(@Optional ListItem<Token> item) {
        return item != null && item.get() instanceof OperatorToken;
    }

    private boolean isNumber(@Optional ListItem<Token> item) {
        return item != null && item.get() instanceof NumberToken;
    }

    private boolean isOpeningBracket(@Mandatory Token token) {
        return token instanceof BracketToken && token.getText().equals("(");
    }

    private boolean isClosingBracket(@Mandatory Token token) {
        return token instanceof BracketToken && token.getText().equals(")");
    }

    private boolean isOperator(@Mandatory ListItem<Token> item, @Mandatory Operator operator) {
        if (item.get().getText().equals(operator.getText())) {
            switch (operator.getType()) {
                case BINARY: return isBinaryOperator(item);
                case LUNARY: return isLunaryOperator(item);
                case RUNARY: return isRunaryOperator(item);
            }
        }
        return false;
    }

    private boolean isBinaryOperator(@Mandatory ListItem<Token> item) {
        return isNumber(item.getPreviousItem()) && isNumber(item.getNextItem());
    }

    private boolean isLunaryOperator(@Mandatory ListItem<Token> item) {
        return !isNumber(item.getPreviousItem()) && isNumber(item.getNextItem());
    }

    private boolean isRunaryOperator(@Mandatory ListItem<Token> item) {
        return isNumber(item.getPreviousItem()) && !isNumber(item.getNextItem());
    }

    private @Mandatory Token create(@Mandatory String value) {
        return new NumberToken(value, -1);
    }

    private int parse(@Mandatory Token token) {
        try {
            return Integer.parseInt(token.getText());
        } catch (NumberFormatException e) {
            throw new MacroException(
                token.getPosition(),
                "Expected number, but got '" + token.getText() + "'.",
                e
            );
        }
    }
}
