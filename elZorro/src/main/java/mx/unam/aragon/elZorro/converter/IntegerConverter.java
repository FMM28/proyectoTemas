package mx.unam.aragon.elZorro.converter;

import java.beans.PropertyEditorSupport;

public class IntegerConverter extends PropertyEditorSupport {
    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        if (text == null || text.trim().isEmpty()) {
            setValue(0);
            return;
        }

        try {
            Integer value = Integer.parseInt(text.trim());
            setValue(value);
        } catch (NumberFormatException ex) {
            setValue(0);
        }
    }
}
