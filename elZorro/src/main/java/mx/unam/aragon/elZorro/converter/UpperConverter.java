package mx.unam.aragon.elZorro.converter;

import java.beans.PropertyEditorSupport;

public class UpperConverter extends PropertyEditorSupport {
    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        if (text == null || text.trim().isEmpty()) {
            setValue(null);
            return;
        }

        String upperCaseText = text.trim().toUpperCase();
        setValue(upperCaseText);
    }
}
