package pl.bodzioch.damian.program;

import lombok.RequiredArgsConstructor;
import pl.bodzioch.damian.utils.FilterField;

@RequiredArgsConstructor
public enum ProgramFilterField implements FilterField {
    NAME("_prg_name");

    private final String dbName;

    public String dbName() {
        return dbName;
    }
}
