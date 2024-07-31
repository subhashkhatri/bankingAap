package org.example.chandan;

import java.util.Scanner;

public class BankingApplication {
    private static AccountDAO accountDAO = new AccountDAO();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Created By Chandan Kumar");
            System.out.println("1. Create New Account");
            System.out.println("2. Check Account Balance");
            System.out.println("3. Deposit Money");
            System.out.println("4. Withdraw Money");
            System.out.println("5. Transfer Money");
            System.out.println("6. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    createAccount(scanner);
                    break;
                case 2:
                    checkBalance(scanner);
                    break;
                case 3:
                    depositMoney(scanner);
                    break;
                case 4:
                    withdrawMoney(scanner);
                    break;
                case 5:
                    transferMoney(scanner);
                    break;
                case 6:
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid. Please try again.");
            }
        }
    }

    private static void createAccount(Scanner scanner) {
        System.out.print("Enter account number for new account: ");
        String accountNumber = scanner.next();
        System.out.print("Enter account holder name: ");
        String accountHolderName = scanner.next();
        System.out.print("Enter initial balance: ");
        double balance = scanner.nextDouble();

        Account account = new Account();
        account.setAccountNumber(accountNumber);
        account.setAccountHolderName(accountHolderName);
        account.setBalance(balance);

        accountDAO.createAccount(account);
        System.out.println("Account created successfully!");
    }

    private static void checkBalance(Scanner scanner) {
        System.out.print("Enter account number: ");
        String accountNumber = scanner.next();
        Account account = accountDAO.getAccount(accountNumber);
        if (account != null) {
            System.out.println("Account Balance: " + account.getBalance());
        } else {
            System.out.println("Account not found.");
        }
    }

    private static void depositMoney(Scanner scanner) {
        System.out.print("Enter account number: ");
        String accountNumber = scanner.next();
        System.out.print("Enter amount to deposit: ");
        double amount = scanner.nextDouble();

        accountDAO.depositMoney(accountNumber, amount);
        Account account = accountDAO.getAccount(accountNumber);
        if (account != null) {
            System.out.println("Deposit successful! New Balance: " + account.getBalance());
        }
    }

    private static void withdrawMoney(Scanner scanner) {
        System.out.print("Enter account number: ");
        String accountNumber = scanner.next();
        System.out.print("Enter amount to withdraw: ");
        double amount = scanner.nextDouble();

        accountDAO.withdrawMoney(accountNumber, amount);
        Account account = accountDAO.getAccount(accountNumber);
        if (account != null) {
            System.out.println("Withdrawal successful! New Balance: " + account.getBalance());
        } else {
            System.out.println("Insufficient funds or account not found.");
        }
    }

    private static void transferMoney(Scanner scanner) {
        System.out.print("Enter source account number: ");
        String sourceAccountNumber = scanner.next();
        System.out.print("Enter destination account number: ");
        String destinationAccountNumber = scanner.next();
        System.out.print("Enter amount to transfer: ");
        double amount = scanner.nextDouble();

        accountDAO.transferMoney(sourceAccountNumber, destinationAccountNumber, amount);
        Account sourceAccount = accountDAO.getAccount(sourceAccountNumber);
        if (sourceAccount != null) {
            System.out.println("Transfer successful! New Balance: " + sourceAccount.getBalance());
        }
    }
}
