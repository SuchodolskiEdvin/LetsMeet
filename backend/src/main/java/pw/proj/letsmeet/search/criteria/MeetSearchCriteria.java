package pw.proj.letsmeet.search.criteria;

import com.querydsl.core.types.dsl.EntityPathBase;
import lombok.Getter;
import lombok.Setter;
import pw.proj.letsmeet.model.Meet;
import pw.proj.letsmeet.model.QMeet;

import pw.proj.letsmeet.search.PredicateBuilder;

@Getter
@Setter
public class MeetSearchCriteria extends AbstractSearchCriteria<Meet> {

	@Override
	protected void buildPredicates(PredicateBuilder predicateBuilder) {

	}

	@Override
	protected EntityPathBase<Meet> getEntity() {
		return QMeet.meet;
	}
}
