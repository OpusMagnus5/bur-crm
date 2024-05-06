package pl.bodzioch.damian.operator;

import lombok.RequiredArgsConstructor;
import pl.bodzioch.damian.utils.FilterField;

@RequiredArgsConstructor
public enum OperatorFilterField implements FilterField {
    NAME("_opr_name");

    private final String dbName;

    public String dbName() {
        return dbName;
    }
}
