package com.impactsure.artnook.service;

public interface GenericService<A, PK> {
	Iterable<A> findAll();

	A save(A a);

	void delete(A a);

	A findOne(PK a);

}
