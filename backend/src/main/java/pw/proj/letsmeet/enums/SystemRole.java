package pw.proj.letsmeet.enums;

import java.util.Arrays;
import java.util.List;

public enum SystemRole {

	//TODO: została zmieniona metoda toSelectEnumValues. Naprawić błedy
	ADMIN,
	USER;

	public boolean isAdmin() {
		return this.equals(SystemRole.ADMIN);
	}

	public static List<String> toSelectEnumValues() {
		return Arrays.stream(SystemRole.values()).map(Enum::toString).toList();
	}

}
