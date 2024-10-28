package com.weddio.weddio.specifications;

import com.weddio.weddio.models.Guest;
import com.weddio.weddio.models.enums.FamilyFrom;
import com.weddio.weddio.models.enums.FriendType;
import org.springframework.data.jpa.domain.Specification;

public class GuestSpecification {
	public static Specification<Guest> hasFirstNameLike(String firstName) {
		return ((root, query, criteriaBuilder) ->
				criteriaBuilder.like (root.get("firstName"), "%" + firstName + "%"));
	}

	public static Specification<Guest> hasLastNameLike(String lastName) {
		return (root, query, criteriaBuilder) ->
				criteriaBuilder.like(root.get("lastName"), "%" + lastName + "%");
	}

	public static Specification<Guest> hasFriendType(FriendType friendType) {
		return (root, query, criteriaBuilder) ->
				criteriaBuilder.equal(root.join("friend").get("friendType"), friendType);
	}

	public static Specification<Guest> hasFamilyFrom(FamilyFrom familyFrom) {
		return (root, query, criteriaBuilder) ->
				criteriaBuilder.equal(root.join("family").get("familyFrom"), familyFrom);
	}

	public static Specification<Guest> isFamily() {
		return (root, query, criteriaBuilder) ->
				criteriaBuilder.isNotNull(root.get("family"));
	}

	public static Specification<Guest> isFriend() {
		return (root, query, criteriaBuilder) ->
				criteriaBuilder.isNotNull(root.get("friend"));
	}

	public static Specification<Guest> isNeighbor() {
		return (root, query, criteriaBuilder) ->
				criteriaBuilder.isNotNull(root.get("neighbor"));
	}
}
