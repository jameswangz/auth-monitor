package com.snda.infrastructure.auth.monitor;

import java.util.Arrays;
import java.util.Collection;

public class ClassfiedAuthListener {

	private final AuthListener authListener;
	private final Collection<AuthResultType> types;

	public ClassfiedAuthListener(AuthListener authListener, AuthResultType[] types) {
		this.authListener = authListener;
		this.types = Arrays.asList(types);
	}

	public AuthListener authListener() {
		return authListener;
	}

	public Collection<AuthResultType> types() {
		return types;
	}

}
