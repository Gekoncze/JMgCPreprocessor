package cz.mg.c.preprocessor.expression;

import cz.mg.annotations.classes.Test;
import cz.mg.c.preprocessor.processors.TokenFactory;
import cz.mg.c.preprocessor.processors.TokenValidator;
import cz.mg.collections.list.List;
import cz.mg.test.Assert;
import cz.mg.tokenizer.exceptions.CodeException;

public @Test class ExpressionParserTest {
    public static void main(String[] args) {
        System.out.print("Running " + ExpressionParserTest.class.getSimpleName() + " ... ");

        ExpressionParserTest test = new ExpressionParserTest();
        test.testParse();

        System.out.println("OK");
    }

    private void testParse() {
        ExpressionParser parser = ExpressionParser.getInstance();
        TokenValidator validator = TokenValidator.getInstance();
        TokenFactory f = TokenFactory.getInstance();

        validator.check(
            parser.parse(new List<>(
                f.special("#"),
                f.name("if"),
                f.whitespace(" "),
                f.number("69")
            )),
            "69"
        );

        validator.check(
            parser.parse(new List<>(
                f.special("#"),
                f.name("if"),
                f.whitespace(" "),
                f.number("6"),
                f.whitespace(" "),
                f.special("+"),
                f.whitespace(" "),
                f.special("9")
            )),
            "6", "+", "9"
        );

        Assert
            .assertThatCode(() -> {
                parser.parse(new List<>());
            })
            .throwsException(CodeException.class);

        Assert
            .assertThatCode(() -> {
                parser.parse(new List<>(
                    f.special("#")
                ));
            })
            .throwsException(CodeException.class);

        Assert
            .assertThatCode(() -> {
                parser.parse(new List<>(
                    f.special("#"),
                    f.name("if")
                ));
            })
            .throwsException(CodeException.class);
    }
}
