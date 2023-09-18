package cz.mg.c.preprocessor.processors.macro.components;

import cz.mg.annotations.classes.Component;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.annotations.requirement.Optional;
import cz.mg.c.preprocessor.processors.macro.entities.Macro;
import cz.mg.c.preprocessor.processors.macro.entities.MacroCall;
import cz.mg.c.preprocessor.processors.macro.entities.Macros;
import cz.mg.collections.components.Capacity;
import cz.mg.collections.list.ListItem;
import cz.mg.collections.map.Map;

public @Component class MacroManager {
    private final @Mandatory Macros macros;
    private final @Mandatory Map<String, ListItem<Macro>> map = new Map<>(new Capacity(100));

    public MacroManager(Macros macros) {
        this.macros = macros;
        for (ListItem<Macro> item = macros.getDefinitions().getFirstItem(); item != null; item = item.getNextItem()) {
            map.set(item.get().getName().getText(), item);
        }
    }

    public @Mandatory Macro get(@Mandatory String name) {
        return map.get(name).get();
    }

    public @Optional Macro getOptional(@Mandatory String name) {
        ListItem<Macro> item = map.getOptional(name);
        return item == null ? null : item.get();
    }

    public void define(@Mandatory Macro macro) {
        String name = macro.getName().getText();
        ListItem<Macro> item = map.getOptional(name);
        if (item != null) {
            item.set(macro);
        } else {
            macros.getDefinitions().addLast(macro);
            map.set(name, macros.getDefinitions().getLastItem());
        }
    }

    public boolean defined(@Mandatory String name) {
        return map.getOptional(name) != null;
    }

    public void undefine(@Mandatory String name) {
        map.remove(name).remove();
    }

    public void called(@Mandatory MacroCall call) {
        macros.getCalls().addLast(call);
    }

    public <T> T temporary(@Mandatory ReturnRunnable<T> runnable, @Mandatory Macro... temporaryMacros) {
        try {
            for (Macro temporaryMacro : temporaryMacros) {
                define(temporaryMacro);
            }
            return runnable.run();
        } finally {
            for (Macro temporaryMacro : temporaryMacros) {
                undefine(temporaryMacro.getName().getText());
            }
        }
    }

    public interface ReturnRunnable<T> {
        T run();
    }
}
