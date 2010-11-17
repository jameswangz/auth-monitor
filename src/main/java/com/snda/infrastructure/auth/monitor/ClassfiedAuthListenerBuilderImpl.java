package com.snda.infrastructure.auth.monitor;

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

	public ClassfiedAuthListener build() {
		return new ClassfiedAuthListener(authListener, types);
	}

}
