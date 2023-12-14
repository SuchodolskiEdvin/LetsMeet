package pw.proj.letsmeet.search.criteria;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.ComparableExpressionBase;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import pw.proj.letsmeet.model.AbstractEntity;
import pw.proj.letsmeet.search.Page;
import pw.proj.letsmeet.search.PredicateBuilder;

import javax.persistence.EntityManager;
import java.util.List;

public abstract class AbstractSearchCriteria<T extends AbstractEntity> {

	@Getter
	@Setter
	private Page page;

	public AbstractSearchCriteria() {
		this.page = new Page();
	}

	public List<T> load(EntityManager entityManager) {
		JPAQuery<T> query = this.prepareQuery(entityManager);

		if (page.getLimit() > 0) {
			query = query.limit(page.getLimit());
		}

		query = query.offset(page.getOffset());

		return query.fetch();
	}

	public long count(EntityManager entityManager) {
		return this.prepareQuery(entityManager).stream().count();
	}

	protected abstract EntityPathBase<T> getEntity();

	protected void buildPredicates(PredicateBuilder predicateBuilder) { }

	protected JPAQuery<T> buildFetch(JPAQuery<T> query) {
		return query;
	}

	private Predicate[] getPredicates() {
		PredicateBuilder predicateBuilder = new PredicateBuilder();

		this.buildPredicates(predicateBuilder);

		return predicateBuilder.getPredicates();
	}

	private JPAQuery<T> prepareQuery(EntityManager entityManager) {
		JPAQuery<T> query= (new JPAQueryFactory(entityManager))
				.selectFrom(getEntity());

		query = this.buildFetch(query);
		query =	query.where(this.getPredicates());

		if (isOrderByPassed()) {
			OrderSpecifier<?> orderSpecifier = getOrderBy();

			if (orderSpecifier != null) {
				query = query.orderBy(orderSpecifier);
			}
		}

		return query;
	}

	private OrderSpecifier<?> getOrderBy() {
		Class<?> entityClass = getEntity().getClass();

		try {
			ComparableExpressionBase<?> comparableExpressionBase =
					(ComparableExpressionBase<?>) entityClass.getField(page.getSortField()).get(getEntity());

			return page.getSortOrder() == 1 ? comparableExpressionBase.asc() : comparableExpressionBase.desc();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	private boolean isOrderByPassed() {
		return StringUtils.isNotBlank(page.getSortField()) && page.getSortOrder() != null && page.getSortOrder() != 0;
	}
}

