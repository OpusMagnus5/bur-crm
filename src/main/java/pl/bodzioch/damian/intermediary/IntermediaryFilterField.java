package pl.bodzioch.damian.intermediary;

import lombok.RequiredArgsConstructor;
import pl.bodzioch.damian.utils.FilterField;

@RequiredArgsConstructor
public enum IntermediaryFilterField implements FilterField {
    NAME("_itr_name"),
    NIP("_itr_nip");

    private final String dbName;

    @Override
    public String dbName() {
        return dbName;
    }
}
