package pl.bodzioch.damian.customer;

import lombok.RequiredArgsConstructor;
import pl.bodzioch.damian.utils.FilterField;

@RequiredArgsConstructor
public enum CustomerFilterField implements FilterField {
    NAME("_cst_name"),
    NIP("_cst_nip");

    private final String dbName;

    @Override
    public String dbName() {
        return dbName;
    }
}
