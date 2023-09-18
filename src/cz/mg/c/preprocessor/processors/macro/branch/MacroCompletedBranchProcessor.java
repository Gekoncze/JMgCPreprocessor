package cz.mg.c.preprocessor.processors.macro.branch;

import cz.mg.annotations.classes.Service;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.c.preprocessor.processors.macro.components.MacroBranches;
import cz.mg.c.preprocessor.processors.macro.components.MacroExpander;
import cz.mg.c.preprocessor.processors.macro.components.MacroManager;
import cz.mg.c.preprocessor.processors.macro.directive.DirectiveParsers;
import cz.mg.c.preprocessor.processors.macro.entities.directives.Directive;
import cz.mg.c.preprocessor.processors.macro.entities.directives.EndifDirective;
import cz.mg.collections.list.List;
import cz.mg.tokenizer.entities.Token;

public @Service class MacroCompletedBranchProcessor implements MacroBranchProcessor {
    private static volatile @Service MacroCompletedBranchProcessor instance;

    public static @Service MacroCompletedBranchProcessor getInstance() {
        if (instance == null) {
            synchronized (Service.class) {
                if (instance == null) {
                    instance = new MacroCompletedBranchProcessor();
                    instance.parsers = DirectiveParsers.getInstance();
                }
            }
        }
        return instance;
    }

    private @Service DirectiveParsers parsers;

    @Override
    public void process(
        @Mandatory List<Token> line,
        @Mandatory MacroManager macros,
        @Mandatory MacroBranches branches,
        @Mandatory MacroExpander expander
    ) {
        Directive directive = parsers.parse(line);
        if (directive instanceof EndifDirective) {
            branches.end();
        }
    }
}
