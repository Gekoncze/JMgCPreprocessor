package cz.mg.c.preprocessor.processors.macro.components;

import cz.mg.annotations.classes.Component;
import cz.mg.annotations.classes.Service;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.c.entities.directives.Directive;
import cz.mg.c.preprocessor.processors.macro.exceptions.MacroException;
import cz.mg.c.preprocessor.processors.macro.branch.MacroBranchProcessor;
import cz.mg.c.preprocessor.processors.macro.branch.MacroCompletedBranchProcessor;
import cz.mg.c.preprocessor.processors.macro.branch.MacroFalseBranchProcessor;
import cz.mg.c.preprocessor.processors.macro.branch.MacroTrueBranchProcessor;
import cz.mg.collections.list.List;
import cz.mg.collections.pair.Pair;

public @Component class MacroBranches {
    private final @Service MacroTrueBranchProcessor trueBranch = MacroTrueBranchProcessor.getInstance();
    private final @Service MacroFalseBranchProcessor falseBranch = MacroFalseBranchProcessor.getInstance();
    private final @Service MacroCompletedBranchProcessor completedBranch = MacroCompletedBranchProcessor.getInstance();
    private final @Mandatory List<Pair<Directive, MacroBranchProcessor>> levels = new List<>();

    public MacroBranches() {
        levels.addLast(new Pair<>(null, trueBranch));
    }

    public @Mandatory MacroBranchProcessor getBranch() {
        return levels.getLast().getValue();
    }

    public void nest(@Mandatory Directive directive) {
        levels.addLast(new Pair<>(directive, trueBranch));
    }

    public void unnest(@Mandatory Directive directive) {
        validateNotRoot(directive);
        levels.removeLast();
        levels.getLast().setValue(completedBranch);
    }

    public void end() {
        levels.getLast().setValue(trueBranch);
    }

    public void skip() {
        levels.getLast().setValue(falseBranch);
    }

    public void validateNotRoot(@Mandatory Directive directive) {
        if (levels.getLast().getKey() == null) {
            throw new MacroException(
                directive.getKeyword().getPosition(),
                "Missing start of macro condition."
            );
        }
    }

    public void validateIsRoot() {
        if (levels.getLast().getKey() != null) {
            throw new MacroException(
                levels.getLast().getKey().getKeyword().getPosition(),
                "Missing end of macro condition."
            );
        }
    }
}
