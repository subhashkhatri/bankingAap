package org.example.chandan;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AccountDAO {
    public void createAccount(Account account) {
        String sql = "INSERT INTO accounts (account_number, account_holder_name, balance) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, account.getAccountNumber());
            stmt.setString(2, account.getAccountHolderName());
            stmt.setDouble(3, account.getBalance());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Account getAccount(String accountNumber) {
        String sql = "SELECT * FROM accounts WHERE account_number = ?";
        Account account = null;
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, accountNumber);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                account = new Account();
                account.setAccountId(rs.getInt("account_id"));
                account.setAccountNumber(rs.getString("account_number"));
                account.setAccountHolderName(rs.getString("account_holder_name"));
                account.setBalance(rs.getDouble("balance"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return account;
    }

    // Method to update account details
    public void updateAccount(Account account) {
        String sql = "UPDATE accounts SET balance = ? WHERE account_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDouble(1, account.getBalance());
            stmt.setInt(2, account.getAccountId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to deposit money into an account
    public void depositMoney(String accountNumber, double amount) {
        Account account = getAccount(accountNumber);
        if (account != null) {
            account.setBalance(account.getBalance() + amount);
            updateAccount(account);
            logTransaction(account.getAccountId(), "DEPOSIT", amount);
        }
    }

    // Method to withdraw money from an account
    public void withdrawMoney(String accountNumber, double amount) {
        Account account = getAccount(accountNumber);
        if (account != null && account.getBalance() >= amount) {
            account.setBalance(account.getBalance() - amount);
            updateAccount(account);
            logTransaction(account.getAccountId(), "WITHDRAW", amount);
        } else {
            System.out.println("Insufficient funds or account not found.");
        }
    }

    // Method to transfer money between different accounts
    public void transferMoney(String sourceAccountNumber, String destinationAccountNumber, double amount) {
        Account sourceAccount = getAccount(sourceAccountNumber);
        Account destinationAccount = getAccount(destinationAccountNumber);

        if (sourceAccount != null && destinationAccount != null && sourceAccount.getBalance() >= amount) {
            sourceAccount.setBalance(sourceAccount.getBalance() - amount);
            destinationAccount.setBalance(destinationAccount.getBalance() + amount);
            updateAccount(sourceAccount);
            updateAccount(destinationAccount);
            logTransaction(sourceAccount.getAccountId(), "TRANSFER_OUT", amount);
            logTransaction(destinationAccount.getAccountId(), "TRANSFER_IN", amount);
        } else {
            System.out.println("Transfer failed. Check account details or balance.");
        }
    }

    // Method to log transactions of account
    private void logTransaction(int accountId, String transactionType, double amount) {
        String sql = "INSERT INTO transactions (account_id, transaction_type, amount) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, accountId);
            stmt.setString(2, transactionType);
            stmt.setDouble(3, amount);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
