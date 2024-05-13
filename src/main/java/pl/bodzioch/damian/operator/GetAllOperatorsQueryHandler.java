package pl.bodzioch.damian.operator;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import pl.bodzioch.damian.infrastructure.query.QueryHandler;
import pl.bodzioch.damian.operator.query_dto.GetAllOperatorsQuery;
import pl.bodzioch.damian.operator.query_dto.GetAllOperatorsQueryResult;

import java.util.List;

@Cacheable("operators")
@Component
@RequiredArgsConstructor
class GetAllOperatorsQueryHandler implements QueryHandler<GetAllOperatorsQuery, GetAllOperatorsQueryResult> {

    private final IOperatorReadRepository readRepository;

    @Override
    public Class<GetAllOperatorsQuery> queryClass() {
        return GetAllOperatorsQuery.class;
    }

    @Override
    public GetAllOperatorsQueryResult handle(GetAllOperatorsQuery query) {
        List<OperatorDto> operators = readRepository.getAll().stream()
                .map(OperatorDto::new)
                .toList();
        return new GetAllOperatorsQueryResult(operators);
    }
}
