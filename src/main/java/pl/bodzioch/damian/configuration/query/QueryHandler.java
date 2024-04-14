package pl.bodzioch.damian.configuration.query;

public interface QueryHandler<C extends Query<R>, R extends QueryResult> {

    Class<C> commandClass();

    R handle(C command);
}
