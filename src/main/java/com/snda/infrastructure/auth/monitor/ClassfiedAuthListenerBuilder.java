package com.snda.infrastructure.auth.monitor;

public interface ClassfiedAuthListenerBuilder {

	void on(AuthResultType... types);

	ClassfiedAuthListener build();
	
}
