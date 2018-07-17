package com.capgemini.mypayment.bean;

public class Customer {

	private String name;
	private String mobileNo;
	private Wallet wallet;
	
	public Customer() {
		super();
	}
	public Customer(String name2, String mobileNo2, Wallet wallet2) {
		this.name=name2;
		this.mobileNo=mobileNo2;
		this.wallet=wallet2;
}
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getMobileNo() {
		return mobileNo;
	}
	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}
	public Wallet getWallet() {
		return wallet;
	}
	public void setWallet(Wallet wallet) {
		this.wallet = wallet;
	}
	
	@Override
	public String toString() {
		return "Customer [name=" + name + ", mobileNo=" + mobileNo
				+ ", wallet=" + wallet + "]";
	}
	
	
	
	
	
}
