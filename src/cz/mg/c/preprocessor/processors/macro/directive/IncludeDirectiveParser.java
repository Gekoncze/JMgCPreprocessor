package cz.mg.c.preprocessor.processors.macro.directive;

import cz.mg.annotations.classes.Service;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.c.preprocessor.exceptions.PreprocessorException;
import cz.mg.c.preprocessor.processors.macro.entities.directives.IncludeDirective;
import cz.mg.collections.list.List;
import cz.mg.tokenizer.components.TokenReader;
import cz.mg.tokenizer.entities.Token;
import cz.mg.tokenizer.entities.tokens.*;

import java.nio.file.Path;

public @Service class IncludeDirectiveParser implements DirectiveParser {
    private static volatile @Service IncludeDirectiveParser instance;

    public static IncludeDirectiveParser getInstance() {
        if (instance == null) {
            synchronized (Service.class) {
                if (instance == null) {
                    instance = new IncludeDirectiveParser();
                }
            }
        }
        return instance;
    }

    private IncludeDirectiveParser() {
    }

    @Override
    public @Mandatory String getName() {
        return IncludeDirective.KEYWORD;
    }

    @Override
    public @Mandatory IncludeDirective parse(@Mandatory List<Token> line) {
        IncludeDirective directive = new IncludeDirective();
        TokenReader reader = new TokenReader(line, PreprocessorException::new);
        reader.skip(WhitespaceToken.class);
        reader.read("#", SpecialToken.class);
        reader.skip(WhitespaceToken.class);
        directive.setKeyword(reader.read(IncludeDirective.KEYWORD, WordToken.class));
        reader.skip(WhitespaceToken.class);
        if (reader.has(DoubleQuoteToken.class)) {
            directive.setLibrary(false);
            directive.setPath(Path.of(readLocalPath(reader)));
        } else {
            directive.setLibrary(true);
            directive.setPath(Path.of(readLibraryPath(reader)));
        }
        reader.skip(WhitespaceToken.class);
        reader.readEnd();
        return directive;
    }

    private @Mandatory String readLocalPath(@Mandatory TokenReader reader) {
        return reader.read(DoubleQuoteToken.class).getText();
    }

    private @Mandatory String readLibraryPath(@Mandatory TokenReader reader) {
        StringBuilder builder = new StringBuilder();
        reader.read("<", OperatorToken.class);
        while (reader.has()) {
            if (reader.has(">", OperatorToken.class)) {
                reader.read();
                break;
            } else if (reader.has(this::pathToken)) {
                builder.append(reader.read().getText());
            } else {
                Token token = reader.read();
                throw new PreprocessorException(
                    token.getPosition(),
                    "Unexpected token of type " + token.getClass().getSimpleName() + "."
                );
            }
        }
        return builder.toString().trim();
    }

    private boolean pathToken(@Mandatory Token token) {
        return token instanceof WordToken
            || token instanceof OperatorToken
            || token instanceof WhitespaceToken;
    }
}
