package pl.bodzioch.damian.infrastructure.database;

import java.util.List;

record CustomType(
        @DbId
        @DbColumn(name = "name")
        String typeName,
        @DbColumn(name = "attribute_name")
        List<String> attributeNames
) {
    @DbConstructor
    CustomType {
    }
}
