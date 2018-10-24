package com.sa.sbsimplewebapp.model.form;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CifDataForm {
	@NotNull
	@JsonProperty("cif_no")
	private String cifNo;

	public String getCifNo() {
		return cifNo;
	}
	public void setCifNo(String cifNo) {
		this.cifNo = cifNo;
	}
}
