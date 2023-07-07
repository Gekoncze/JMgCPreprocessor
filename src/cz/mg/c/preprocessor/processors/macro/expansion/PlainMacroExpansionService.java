package cz.mg.c.preprocessor.processors.macro.expansion;

import cz.mg.annotations.classes.Service;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.c.preprocessor.processors.macro.entities.MacroCall;
import cz.mg.c.preprocessor.processors.macro.entities.Macros;
import cz.mg.collections.components.Capacity;
import cz.mg.collections.list.List;
import cz.mg.collections.map.Map;
import cz.mg.tokenizer.entities.Token;

import java.util.Iterator;

public @Service class PlainMacroExpansionService implements MacroExpansionService {
    private static volatile @Service PlainMacroExpansionService instance;

    public static @Service PlainMacroExpansionService getInstance() {
        if (instance == null) {
            synchronized (Service.class) {
                if (instance == null) {
                    instance = new PlainMacroExpansionService();
                }
            }
        }
        return instance;
    }

    private PlainMacroExpansionService() {
    }

    @Override
    public @Mandatory List<Token> expand(@Mandatory Macros macros, @Mandatory MacroCall call) {
        Map<String, List<Token>> map = createMap(call);
        List<Token> expandedTokens = new List<>();
        for (Token macroToken : call.getMacro().getTokens()) {
            List<Token> replacement = map.getOptional(macroToken.getText());
            if (replacement != null) {
                expandedTokens.addCollectionLast(replacement);
            } else {
                expandedTokens.addLast(macroToken);
            }
        }
        return expandedTokens;
    }

    /**
     * Creates map where key is macro parameter name and value is list of argument tokens.
     *
     * Example:
     *     #define PLUS(x, y) x + y
     *     PLUS(3, 7!)
     * will create map:
     *     x -> [3]
     *     y -> [7, !]
     */
    private @Mandatory Map<String, List<Token>> createMap(@Mandatory MacroCall call) {
        Map<String, List<Token>> map = new Map<>(new Capacity(100));
        if (call.getMacro().getParameters() != null && call.getArguments() != null) {
            Iterator<Token> parameterIterator = call.getMacro().getParameters().iterator();
            Iterator<List<Token>> argumentIterator = call.getArguments().iterator();
            while (parameterIterator.hasNext() && argumentIterator.hasNext()) {
                Token parameter = parameterIterator.next();
                List<Token> arguments = argumentIterator.next();
                map.set(parameter.getText(), arguments);
            }
        }
        return map;
    }
}
