package com.capgemini.mypayment.pl;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Scanner;

import com.capgemini.mypayment.bean.Customer;
import com.capgemini.mypayment.bean.Wallet;
import com.capgemini.mypayment.exception.InsufficientBalanceException;
import com.capgemini.mypayment.exception.InvalidInputException;
import com.capgemini.mypayment.service.WalletService;
import com.capgemini.mypayment.service.WalletServiceImpl;

public class Client {

	public static void main(String[] args) throws InvalidInputException ,InsufficientBalanceException{
		WalletService service;
		Customer customer;
		{
			service=new WalletServiceImpl();
		}
		int choice;
		String response="";
		Scanner sc=new Scanner(System.in);
		do
		{
			System.out.println("******MyPaymentApp*******");
			System.out.println("1.Create Account");
			System.out.println("2.Show Balance");
			System.out.println("3.Fund Transfer");
			System.out.println("4.Deposit Amount");
			System.out.println("5.Withdraw Amount");
			System.out.println("6.Exit");
			System.out.println("Please enter your choice");
			choice=sc.nextInt();
			switch(choice)
			{
			case 1:
				System.out.println("Enter your name");
				String name=sc.next();
				System.out.println("Enter your mobile number");
				String mobile=sc.next();
				System.out.println("Enter the amount");

				BigDecimal amount = sc.nextBigDecimal();
				
				try {
					customer=service.createAccount(name, mobile, amount);
					if(customer==null)
						{
							System.out.println("Cant create account");
						}
						else
						{
							System.out.println("Account created with following details:");
							System.out.println(customer);
						}
				} catch (InvalidInputException e) {
					// TODO Auto-generated catch block
					System.err.println("Incorrect format for phone no");
				}
				
				break;
				
			case 2:
				try {
					System.out.println("Enter the mobile no to display your balance");
					mobile=sc.next();
					customer=service.showBalance(mobile);
					System.out.println(customer);
				} catch (InvalidInputException e) {
					// TODO Auto-generated catch block
					System.err.println(e.getMessage());
				}
				break;
			case 3:
				System.out.println("Enter the source mobile number");
				String src=sc.next();
				System.out.println("Enter the destination mobile number");
				String target=sc.next();
				System.out.println("Enter the amount you want to transfer");
				amount = sc.nextBigDecimal();
				try {
				customer=service.fundTransfer(src, target, amount);
				System.out.println(customer);
			} catch (InvalidInputException e) {
				System.err.println(e.getMessage());
			}
			catch (InsufficientBalanceException e) {
				
				System.err.println(e.getMessage());
				
			}
				break;
			case 4:
				System.out.println("Enter the mobile no ");
				mobile=sc.next();
				System.out.println("Enter the amount you want to deposit");
				amount=sc.nextBigDecimal();
				try {
					customer=service.depositAmount(mobile, amount);
					System.out.println(customer);
				} catch (InvalidInputException e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
					System.err.println(e.getMessage());
				}
				
				
				break;
			case 5:
				System.out.println("Enter the mobile no ");
				mobile=sc.next();
				System.out.println("Enter the amount you want to withdraw");
				amount=sc.nextBigDecimal();
				
				try{
					customer=service.withdrawAmount(mobile, amount);
				System.out.println(customer);	
				} catch (InvalidInputException e) {
					System.err.println(e.getMessage());
				}
				catch (InsufficientBalanceException e) {
					
					System.err.println(e.getMessage());
					
				}
				break;
			case 6:
				System.exit(0);
				break;
			}
			System.out.println("Do you want to continue");
			response=sc.next();
		}while(response.equalsIgnoreCase("Yes")||response.equalsIgnoreCase("y"));

	}

	

}
