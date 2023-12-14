package pw.proj.letsmeet.search.criteria;

import com.querydsl.core.types.dsl.EntityPathBase;
import lombok.Getter;
import lombok.Setter;
import pw.proj.letsmeet.enums.SystemRole;
import pw.proj.letsmeet.model.QUser;
import pw.proj.letsmeet.model.User;
import pw.proj.letsmeet.search.PredicateBuilder;

@Getter
@Setter
public class UserSearchCriteria extends AbstractSearchCriteria<User> {

	private String firstName;

	private String lastName;

	private String email;

	private SystemRole systemRole;

	@Override
	protected void buildPredicates(PredicateBuilder predicateBuilder) {
		predicateBuilder.addLike(QUser.user.firstName, this.firstName);
		predicateBuilder.addLike(QUser.user.lastName, this.lastName);
		predicateBuilder.addLike(QUser.user.email, this.email);
		predicateBuilder.addEq(QUser.user.systemRole, this.systemRole);
	}

	@Override
	protected EntityPathBase<User> getEntity() {
		return QUser.user;
	}
}
