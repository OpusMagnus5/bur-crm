package pl.bodzioch.damian.intermediary;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import pl.bodzioch.damian.infrastructure.query.QueryHandler;
import pl.bodzioch.damian.intermediary.query_dto.GetAllIntermediariesQuery;
import pl.bodzioch.damian.intermediary.query_dto.GetAllIntermediariesQueryResult;

import java.util.List;

@Component
@RequiredArgsConstructor
class GetAllIntermediariesQueryHandler implements QueryHandler<GetAllIntermediariesQuery, GetAllIntermediariesQueryResult> {

    private final IIntermediaryReadRepository readRepository;

    @Override
    public Class<GetAllIntermediariesQuery> queryClass() {
        return GetAllIntermediariesQuery.class;
    }

    @Override
    @Cacheable("intermediaries")
    public GetAllIntermediariesQueryResult handle(GetAllIntermediariesQuery query) {
        List<IntermediaryDto> intermediaries = readRepository.getAll().stream()
                .map(IntermediaryDto::new)
                .toList();
        return new GetAllIntermediariesQueryResult(intermediaries);
    }
}
