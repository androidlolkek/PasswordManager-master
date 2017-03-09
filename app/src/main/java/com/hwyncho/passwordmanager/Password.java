package com.hwyncho.passwordmanager;

public class Password {
	String account;
	String password;
	String date;

	{
		account = null;
		password = null;
		date = null;
	}

	public Password() {
		this.account = "";
		this.password = "";
		this.date = "";
	}

	public Password(String account, String password, String date) {
		this.account = account;
		this.password = password;
		this.date = date;
	}

	public String getAccount() {
		return this.account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getDate() {
		return this.date;
	}

	public void setDate(String date) {
		this.date = date;
	}
}