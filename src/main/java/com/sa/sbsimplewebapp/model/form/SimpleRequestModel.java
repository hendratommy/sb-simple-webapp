package com.sa.sbsimplewebapp.model.form;

public class SimpleRequestModel extends AbstractRequestModel {
	private AccountDataForm accountDataForm;
	
	@Override
	public AccountDataForm getData() {
		return accountDataForm;
	}
	public void setData(AccountDataForm data) {
		this.accountDataForm = data;
	}

}
