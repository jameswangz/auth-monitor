package com.snda.infrastructure.auth.monitor;

import com.google.common.base.Preconditions;

public class ClassfiedAuthListenerBuilderImpl implements ClassfiedAuthListenerBuilder {

	private final AuthListener authListener;
	private AuthResultType[] types;

	public ClassfiedAuthListenerBuilderImpl(AuthListener authListener) {
		this.authListener = authListener;
	}

	@Override
	public void on(AuthResultType... types) {
		this.types = types;
	}

	@Override
	public ClassfiedAuthListener build() {
		compile();
		return new ClassfiedAuthListener(authListener, types);
	}

	private void compile() {
		Preconditions.checkNotNull(types, "types required");
	}

}
