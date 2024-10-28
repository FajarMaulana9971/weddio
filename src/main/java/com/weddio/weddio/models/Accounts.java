package com.weddio.weddio.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.weddio.weddio.models.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "tb_m_accounts")
public class Accounts extends BaseEntity {
	@Column(length = 64)
	private String username;

	private String password;

	@OneToMany(mappedBy = "account")
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private List<Guest> guests;
}
