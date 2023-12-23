package pw.proj.letsmeet.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDate;

@MappedSuperclass
@Getter
@Setter
public class AbstractEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(insertable = false, updatable = false)
	protected Long id;

	@CreationTimestamp
	protected LocalDate creationDate;

	@UpdateTimestamp
	protected LocalDate modificationDate;

	@Version
	protected int version;

}
