package com.weddio.weddio.models;

import com.weddio.weddio.models.base.BaseEntity;
import com.weddio.weddio.models.enums.AttendenceStatus;
import com.weddio.weddio.models.enums.Gender;
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

	@Column(unique = true)
	private String name;

	@Column(length = 32)
	private String whatsappNumber;

	@Column(length = 64)
	private String address;

	@Enumerated(EnumType.STRING)
	private Gender gender;

	@Enumerated(EnumType.STRING)
	private AttendenceStatus attendenceStatus;

	@Column(length = 32)
	private String specialNickname;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn (name = "family_id")
	private Familys family;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn (name = "friend_id")
	private Friends friend;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn (name = "neighbor_id")
	private Neighbors neighbor;

	@ManyToOne
	@JoinColumn(name = "account_id",nullable = false)
	private Accounts account;
}
