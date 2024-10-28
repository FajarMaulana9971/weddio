package com.weddio.weddio.models;

import com.weddio.weddio.models.base.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table (name = "tb_m_guests")
public class Guest extends BaseEntity {
	@Column (length = 64)
	private String firstName;

	@Column(length = 64)
	private String lastName;

	@Column(length = 32)
	private String whatsappNumber;

	@Column(length = 64)
	private String address;

	@OneToOne
	@JoinColumn (name = "family_id")
	private Familys family;

	@OneToOne
	@JoinColumn (name = "friend_id")
	private Friends friend;

	@OneToOne
	@JoinColumn (name = "neighbor_id")
	private Neighbors neighbor;

	@ManyToOne
	@JoinColumn(name = "account_id",nullable = false)
	private Accounts account;
}
