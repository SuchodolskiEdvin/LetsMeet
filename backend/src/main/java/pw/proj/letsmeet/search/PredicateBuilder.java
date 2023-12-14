package pw.proj.letsmeet.search;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.*;
import org.apache.commons.lang3.StringUtils;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

public class PredicateBuilder {

	private final List<Predicate> predicates = new ArrayList<>();

	public Predicate[] getPredicates() {
		return predicates.toArray(new Predicate[0]);
	}

	public void addLike(StringPath stringPath, String value) {
		if (StringUtils.isNotBlank(value)) {
			predicates.add(stringPath.likeIgnoreCase("%" + value + "%"));
		}
	}

	public <T> void addEq(SimpleExpression<T> path, T value) {
		if (value != null) {
			predicates.add(path.eq(value));
		}
	}

	public <T> void addIn(SimpleExpression<T> path, List<T> values) {
		if (values != null && values.size() > 0) {
			predicates.add(path.in(values));
		}
	}

	public void addEq(SimpleExpression<String> path, String value) {
		if (StringUtils.isNotBlank(value)) {
			predicates.add(path.eq(value));
		}
	}

	public <T extends Comparable> void addLessOrEq(ComparableExpression<T> path, T value) {
		if (value != null) {
			predicates.add(path.loe(value));
		}
	}

	public <T extends Comparable> void addGreaterOrEq(ComparableExpression<T> path, T value) {
		if (value != null) {
			predicates.add(path.goe(value));
		}
	}


	public <T> void addIsNotNull(SimpleExpression<T> path) {
		predicates.add(path.isNotNull());
	}

	public <T> void addNotEq(SimpleExpression<T> path, T value) {
		if (value != null) {
			predicates.add(path.ne(value));
		}
	}

	public <T> void addInEvenIfZero(SimpleExpression<T> path, List<T> values) {
		if (values != null) {
			predicates.add(path.in(values));
		}
	}

	public void addIsNull(SimpleExpression<String> path) {
		predicates.add(path.isNull());
	}

	public <T> void addOr(Predicate predicateLeft, Predicate predicateRight) {
		BooleanBuilder bb = new BooleanBuilder();
		bb.or(predicateLeft);
		bb.or(predicateRight);
		predicates.add(bb.getValue());
	}

	public <T> void addOr(Predicate... args) {
		BooleanBuilder bb = new BooleanBuilder();
		bb.andAnyOf(args); // or wszystkich
		predicates.add(bb.getValue());
	}

	public <T> Predicate getOr(Predicate predicateLeft, Predicate predicateRight) {
		BooleanBuilder bb = new BooleanBuilder();
		bb.or(predicateLeft);
		bb.or(predicateRight);
		return bb.getValue();
	}

	public <T> void addAnd(Predicate predicateLeft, Predicate predicateRight) {
		BooleanBuilder bb = new BooleanBuilder();
		bb.and(predicateLeft);
		bb.and(predicateRight);
		predicates.add(bb.getValue());
	}

	public <T> Predicate getAnd(Predicate predicateLeft, Predicate predicateRight) {
		BooleanBuilder bb = new BooleanBuilder();
		bb.and(predicateLeft);
		bb.and(predicateRight);
		return bb.getValue();
	}

	public <T> Predicate getEq(SimpleExpression<T> path, T value) {
		if (value != null) {
			return path.eq(value);
		}
		return null;
	}

	public <T> Predicate getNotEq(SimpleExpression<T> path, T value) {
		if (value != null) {
			return path.ne(value);
		}
		return null;
	}

	public <T> Predicate getIsNull(DateTimePath<OffsetDateTime> path) {
		return path.isNull();
	}

	public <T extends Comparable> Predicate getGreaterOrEq(ComparableExpression<T> path, T value) {
		if (value != null) {
			return path.goe(value);
		}
		return null;
	}

	public <T> Predicate getIn(SimpleExpression<T> path, List<T> values) {
		if (values != null && values.size() > 0) {
			return path.in(values);
		}
		return null;
	}

	public <T> Predicate getInEvenIfZero(SimpleExpression<T> path, List<T> values) {
		if (values != null) {
			return path.in(values);
		}
		return null;
	}

	public <T> Predicate getIsNull(SimpleExpression<T> path) {
		return path.isNull();
	}

	public <T> Predicate getIsNotNull(SimpleExpression<T> path) {
		return path.isNotNull();
	}
	public <T, V, E extends SimpleExpression<? super V>> Predicate getNotExistKeyInMap(MapPath<T, V, E> path, T key) {
		if (key != null) {
			return path.containsKey(key).not();
		}
		return null;
	}

	public Predicate getLike(StringPath stringPath, String value) {
		if (StringUtils.isNotBlank(value)) {
			return stringPath.likeIgnoreCase("%" + value + "%");
		}
		return null;
	}
}
