package pl.bodzioch.damian.operator;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.bodzioch.damian.infrastructure.query.QueryHandler;
import pl.bodzioch.damian.operator.query_dto.GetOperatorsPageQuery;
import pl.bodzioch.damian.operator.query_dto.GetOperatorsPageQueryResult;
import pl.bodzioch.damian.value_object.PageQuery;
import pl.bodzioch.damian.value_object.PageQueryResult;

import java.util.List;

@Component
@RequiredArgsConstructor
class GetOperatorsPageQueryHandler implements QueryHandler<GetOperatorsPageQuery, GetOperatorsPageQueryResult> {

    private final IOperatorReadRepository readRepository;

    @Override
    public Class<GetOperatorsPageQuery> queryClass() {
        return GetOperatorsPageQuery.class;
    }

    @Override
    public GetOperatorsPageQueryResult handle(GetOperatorsPageQuery query) {
        PageQuery pageQuery = new PageQuery(query.pageNumber(), query.pageSize());
        PageQueryResult<Operator> result = readRepository.getPage(pageQuery);
        List<OperatorDto> operators = result.elements().stream()
                .map(OperatorDto::new)
                .toList();
        return new GetOperatorsPageQueryResult(operators, result.totalElements());
    }
}
