package pl.bodzioch.damian.infrastructure.query;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StopWatch;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Component
public class QueryExecutor {

    private final Map<Class<? extends Query<?>>, QueryHandler<? extends Query<?>, ? extends QueryResult>> handlerMap;

    QueryExecutor(Set<QueryHandler<? extends Query<?>, ?>> handlers) {
        if (CollectionUtils.isEmpty(handlers)) {
            log.warn("There are no query handlers in application context.");
            this.handlerMap = new HashMap<>();
            return;
        }
        handlers.forEach(handler -> log.info("Adding support for query with class {}", handler.queryClass()));
        this.handlerMap = handlers.stream()
                .collect(Collectors.toMap(QueryHandler::queryClass, Function.identity()));
    }


    @SuppressWarnings("unchecked")
    public <C extends Query<R>, R extends QueryResult> R execute(C query) {
        QueryHandler<C, R> handler = (QueryHandler<C, R>) handlerMap.get(query.getClass());
        log.info("Executing query: {}", query);
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        QueryStatus queryStatus = QueryStatus.NOT_FOUND;
        R result = null;
        try {
            result = handler.handle(query);
            queryStatus = QueryStatus.FOUND;
        } catch (Throwable e) {
            log.error("Error occurred while handling query.", e);
            throw e;
        } finally {
            stopWatch.stop();
            log.info("Query with class: {} executed in {}ms with status {} and result: {}",
                    query.getClass().getSimpleName(), stopWatch.getTotalTimeMillis(), queryStatus, result);
        }
        return result;
    }
}
