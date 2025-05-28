package mx.unam.aragon.elZorro.converter;

import java.beans.PropertyEditorSupport;

public class DoubleConverter extends PropertyEditorSupport {

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        if (text == null || text.trim().isEmpty()) {
            setValue(0.0);
            return;
        }

        try {
            Double value = Double.parseDouble(text.trim());
            setValue(value);
        } catch (NumberFormatException ex) {
            setValue(0.0);
        }
    }
}
