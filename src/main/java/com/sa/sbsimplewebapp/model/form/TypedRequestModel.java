package com.sa.sbsimplewebapp.model.form;

import java.time.LocalDate;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TypedRequestModel<T> {
	@NotNull
	@JsonProperty("timestamp")
	@JsonFormat(pattern="yyyyMMdd")
	private LocalDate timestamp;
	@NotNull
	@JsonProperty("name")
	private String name;
	
	@NotNull
	@JsonProperty("data")
	public T data;

	public T getData() {
		return this.data;
	}
	
	public void setData(T data) {
		this.data = data;
	}
}
