package pl.bodzioch.damian.coach;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import pl.bodzioch.damian.coach.query_dto.GetAllCoachesQuery;
import pl.bodzioch.damian.coach.query_dto.GetAllCoachesQueryResult;
import pl.bodzioch.damian.infrastructure.query.QueryHandler;

import java.util.List;

@Component
@RequiredArgsConstructor
class GetAllCoachesQueryHandler implements QueryHandler<GetAllCoachesQuery, GetAllCoachesQueryResult> {

    private final ICoachReadRepository readRepository;

    @Override
    public Class<GetAllCoachesQuery> queryClass() {
        return GetAllCoachesQuery.class;
    }

    @Override
    @Cacheable("coaches")
    public GetAllCoachesQueryResult handle(GetAllCoachesQuery query) {
        List<CoachDto> coaches = readRepository.getAll().stream()
                .map(CoachDto::new)
                .toList();
        return new GetAllCoachesQueryResult(coaches);
    }
}
