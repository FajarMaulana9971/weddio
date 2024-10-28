package com.weddio.weddio.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.weddio.weddio.models.base.BaseEntity;
import com.weddio.weddio.models.enums.FamilyFrom;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "tb_m_familys")
public class Familys extends BaseEntity {
	private FamilyFrom familyFrom;
	
	@OneToOne(mappedBy = "family", cascade = CascadeType.ALL)
	@JsonProperty (access = JsonProperty.Access.WRITE_ONLY)
	private Guest guest;
}
