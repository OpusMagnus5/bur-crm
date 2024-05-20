package pl.bodzioch.damian.coach;

import lombok.RequiredArgsConstructor;
import pl.bodzioch.damian.utils.FilterField;

@RequiredArgsConstructor
public enum CoachFilterField implements FilterField {
    FIRST_NAME("_coa_first_name"),
    LAST_NAME("_coa_last_name");

    private final String dbName;

    public String dbName() {
        return dbName;
    }
}
