package com.capgemini.mypayment.service;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.Savepoint;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.capgemini.mypayment.bean.Customer;
import com.capgemini.mypayment.bean.Wallet;
import com.capgemini.mypayment.exception.InsufficientBalanceException;
import com.capgemini.mypayment.exception.InvalidInputException;
import com.capgemini.mypayment.repo.WalletRepo;
import com.capgemini.mypayment.repo.WalletRepoImpl;

public class WalletServiceImpl implements WalletService {

	WalletRepo repo = new WalletRepoImpl();;
	Customer customer;
	Customer customer1;
	Wallet wallet, wallet1;

	public WalletServiceImpl() {

	}

	public boolean isValid(Customer customer) throws InvalidInputException {
		Pattern pattern = Pattern.compile("[7-9][0-9]{9}");
		Matcher m = pattern.matcher(customer.getMobileNo());
		if (m.matches())

			return true;
		else
			return false;

	}

	@Override
	public Customer createAccount(String name, String mobileno, BigDecimal amount) throws InvalidInputException {

		Customer customer = new Customer(name, mobileno, new Wallet(amount));
		boolean b = isValid(customer);

		if (b) {
			boolean res = repo.save(customer);
			if (res)
				return customer;
			return null;
		} else {
			throw new InvalidInputException("Incorrect Input exception");
		}

	}

	@Override
	public Customer showBalance(String mobileno) throws InvalidInputException {

		customer = repo.findOne(mobileno);

		if (customer != null)
			return customer;
		else
			throw new InvalidInputException("Invalid mobile no ");
	}

	@Override
	public Customer fundTransfer(String sourceMobileNo, String targetMobileNo, BigDecimal amount)
			throws InvalidInputException {

		customer = repo.findOne(sourceMobileNo);
		customer1 = repo.findOne(targetMobileNo);

		if (customer == null || customer1 == null)
			throw new InvalidInputException("Invalid mobile no");

		String srcMobNo = customer.getMobileNo();
		wallet = customer.getWallet();
		BigDecimal bal1 = wallet.getBalance();

		String targetMob = customer1.getMobileNo();
		wallet1 = customer1.getWallet();
		BigDecimal bal2 = wallet1.getBalance();

		if (amount.compareTo(bal1) >= 0) {
			throw new InsufficientBalanceException("Insufficient Balance");
		} else {
			bal1 = bal1.subtract(amount);
			bal2 = bal2.add(amount);
			wallet.setBalance(bal1);
			customer.setWallet(wallet);
			wallet1.setBalance(bal2);
			customer1.setWallet(wallet1);
			Customer update = repo.updateRecords(customer);
			System.out.println(update);
			Customer update2 = repo.updateRecords(customer1);
			return customer;
		}

	}

	@Override
	public Customer depositAmount(String mobileNo, BigDecimal amount) throws InvalidInputException {

		customer = repo.findOne(mobileNo);
		if (customer == null)
			throw new InvalidInputException("Account does not exist");

		else {
			wallet = customer.getWallet();
			BigDecimal bal = wallet.getBalance();
			bal = bal.add(amount);
			wallet.setBalance(bal);
			customer.setWallet(wallet);
			Customer update = repo.updateRecords(customer);
		}
		return customer;
	}

	@Override
	public Customer withdrawAmount(String mobileNo, BigDecimal amount)
			throws InsufficientBalanceException, InvalidInputException {
		BigDecimal minBal = new BigDecimal("1000");
		customer = repo.findOne(mobileNo);
		if (customer == null)
			throw new InvalidInputException("Account does not exist");
		wallet = customer.getWallet();
		BigDecimal bal = wallet.getBalance();
		if (amount.compareTo(bal) >= 0) {
			// System.out.println("Insufficient Balance.You cant withdraw!");
			throw new InsufficientBalanceException("Insufficient Balance.You cant withdraw!");
		} else {
			if (bal.compareTo(minBal) <= 0) {
				throw new InsufficientBalanceException("Insufficient Balance.You cant withdraw!");
			} else {
				bal = bal.subtract(amount);
				wallet.setBalance(bal);
				customer.setWallet(wallet);
				Customer update = repo.updateRecords(customer);
			}

		}

		return customer;

	}

	@Override
	public boolean truncate() {
		boolean b = repo.truncate();
		return b;
	}

}
