package pl.bodzioch.damian.service;

import lombok.RequiredArgsConstructor;
import pl.bodzioch.damian.utils.FilterField;

@RequiredArgsConstructor
public enum ServiceFilterField implements FilterField {
    ;

    private final String dbName;

    @Override
    public String dbName() {
        return null;
    }
}
