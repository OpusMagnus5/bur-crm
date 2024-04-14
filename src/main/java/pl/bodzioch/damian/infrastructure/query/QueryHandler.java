package pl.bodzioch.damian.infrastructure.query;

public interface QueryHandler<C extends Query<R>, R extends QueryResult> {

    Class<C> commandClass();

    R handle(C command);
}
