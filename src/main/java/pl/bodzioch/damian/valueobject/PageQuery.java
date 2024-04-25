package pl.bodzioch.damian.valueobject;

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
