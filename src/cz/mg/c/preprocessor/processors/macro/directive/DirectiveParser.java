package cz.mg.c.preprocessor.processors.macro.directive;

import cz.mg.annotations.classes.Service;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.c.preprocessor.processors.macro.entities.directives.Directive;
import cz.mg.collections.list.List;
import cz.mg.tokenizer.entities.Token;

public @Service interface DirectiveParser {
    @Mandatory String getName();
    @Mandatory Directive parse(@Mandatory List<Token> tokens);
}
