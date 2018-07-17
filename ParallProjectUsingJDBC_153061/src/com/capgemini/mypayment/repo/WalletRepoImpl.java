package com.capgemini.mypayment.repo;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import com.capgemini.mypayment.bean.Customer;
import com.capgemini.mypayment.bean.Wallet;
import com.capgemini.mypayment.exception.InvalidInputException;

public class WalletRepoImpl implements WalletRepo {
	Connection con;
	Customer customer;
	Wallet wallet;

	public WalletRepoImpl() {

		try {
			Class.forName("com.mysql.jdbc.Driver");
			String url = "jdbc:mysql://localhost:3306/test";
			String uid = "root";
			String pwd = "corp123";
			con = DriverManager.getConnection(url, uid, pwd);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public boolean save(Customer customer) throws InvalidInputException {
		// int cust_id=0;
		Wallet wallet = customer.getWallet();
		BigDecimal balance = wallet.getBalance();
		try {

			PreparedStatement pstmt = con.prepareStatement("insert into customer values(?,?,?)");
			pstmt.setString(1, customer.getMobileNo());
			pstmt.setString(2, customer.getName());
			pstmt.setBigDecimal(3, balance);
			pstmt.executeUpdate();
			// System.out.println(rs+ " rows inserted..");
			// con.commit();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return true;

	}

	@Override
	public Customer findOne(String mobileNo) throws InvalidInputException {
		//
		try {
			PreparedStatement pstmt = con.prepareStatement("select * from customer where mobile=?");
			pstmt.setString(1, mobileNo);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next() == false)
				throw new InvalidInputException("No customer with such mobile number");

			customer = new Customer();

			wallet = new Wallet();

			customer.setMobileNo(rs.getString(1));
			customer.setName(rs.getString(2));
			wallet.setBalance(rs.getBigDecimal(3));
			customer.setWallet(wallet);
			// con.commit();
			return customer;

		} catch (Exception e) {

		}
		return null;
	}

	@Override
	public Customer updateRecords(Customer customer) {
		// TODO Auto-generated method stub
		wallet = customer.getWallet();
		BigDecimal balance = wallet.getBalance();
		try {
			PreparedStatement pstmt = con.prepareStatement("update customer set balance=? where mobile=?");
			pstmt.setString(2, customer.getMobileNo());
			pstmt.setBigDecimal(1, balance);
			pstmt.executeUpdate();
			return customer;

		} catch (SQLException e1) {
			// TODO Auto-generated catch block

			e1.printStackTrace();
		}

		return customer;

	}

	public boolean truncate() {
		String query = "truncate customer";
		try {
			Statement st = con.createStatement();
			st.executeUpdate(query);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}
}