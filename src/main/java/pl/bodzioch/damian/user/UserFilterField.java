package pl.bodzioch.damian.user;

import lombok.RequiredArgsConstructor;
import pl.bodzioch.damian.utils.FilterField;

@RequiredArgsConstructor
public enum UserFilterField implements FilterField {
    EXCLUDED_USER_ID("_usr_id_excluded");

    private final String dbName;

    @Override
    public String dbName() {
        return dbName;
    }
}
