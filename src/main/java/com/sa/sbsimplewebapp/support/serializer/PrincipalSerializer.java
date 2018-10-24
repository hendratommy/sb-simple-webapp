package com.sa.sbsimplewebapp.support.serializer;

import java.io.IOException;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class PrincipalSerializer extends StdSerializer<User> {
	private static final long serialVersionUID = 1L;

	public PrincipalSerializer() {
		this(null);
	}

	public PrincipalSerializer(Class<User> t) {
		super(t);
	}

	@Override
	public void serialize(User value, JsonGenerator gen, SerializerProvider provider) throws IOException {
		gen.writeStartObject();
		gen.writeStringField("username", value.getUsername());
		gen.writeArrayFieldStart("authorities");
		if (value.getAuthorities() != null && !value.getAuthorities().isEmpty()) {
			for (GrantedAuthority authority : value.getAuthorities()) {
				gen.writeString(authority.getAuthority());
			}
		}
		gen.writeEndArray();
		gen.writeBooleanField("enabled", value.isEnabled());
		gen.writeBooleanField("locked", !value.isAccountNonLocked());
		gen.writeEndObject();
	}
}
