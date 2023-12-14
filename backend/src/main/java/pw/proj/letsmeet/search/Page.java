package pw.proj.letsmeet.search;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Page {

	private int limit;

	private int offset;

	private String sortField;

	private Integer sortOrder;
}

