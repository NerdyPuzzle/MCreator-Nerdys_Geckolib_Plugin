package net.nerdypuzzle.geckolib.parts;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class RefactoredSystemsICopyPastedBecauseIWasTooBloodyLazyToActuallyProperlyReplace {

    private static final Pattern nonescapedCommaSplitter = Pattern.compile("(?<!\\\\),");
    public static List<String> splitCommaSeparatedStringListWithEscapes(String specialInfoString) {
        List<String> retval = new ArrayList<>();
        if (!specialInfoString.isEmpty()) {
            String[] info = nonescapedCommaSplitter.split(specialInfoString);
            for (String infoelement : info) {
                String data = infoelement.trim().replace("\\,", ",");
                if (!data.isBlank())
                    retval.add(data);
            }
        }
        return retval;
    }

}
