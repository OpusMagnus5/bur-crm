package pl.bodzioch.damian.value_object;

public record PageQuery(

        int pageNumber,
        int pageSize
) {

    public int getFirstResult() {
        return pageSize * pageNumber - pageSize;
    }

    public int getMaxResult() {
        return pageNumber * pageSize;
    }
}
