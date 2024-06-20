import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

// Transaction class to represent individual transactions
class Transaction {
    private double amount;
    private boolean isDeposit;

    public Transaction(double amount, boolean isDeposit) {
        this.amount = amount;
        this.isDeposit = isDeposit;
    }

    public double getAmount() {
        return amount;
    }

    public boolean isDeposit() {
        return isDeposit;
    }

    @Override
    public String toString() {
        if (isDeposit) {
            return "+ ₹" + amount;
        } else {
            return "- ₹" + amount;
        }
    }
}

// Bank account class
class BankAccount {
    private double balance;
    private List<Transaction> transactionHistory;
    private boolean hasTransaction;

    public BankAccount(double initialBalance) {
        this.balance = initialBalance;
        this.transactionHistory = new ArrayList<>();
        this.hasTransaction = false;
    }

    public double getBalance() {
        return balance;
    }

    public void deposit(double amount) {
        balance += amount;
        transactionHistory.add(new Transaction(amount, true));
        hasTransaction = true;
    }

    public boolean withdraw(double amount) {
        if (amount <= balance) {
            balance -= amount;
            transactionHistory.add(new Transaction(amount, false));
            hasTransaction = true;
            return true;
        }
        return false;
    }

    public List<Transaction> getTransactionHistory() {
        return transactionHistory;
    }

    public boolean hasTransaction() {
        return hasTransaction;
    }
}

// ATM class
public class ATM {
    private BankAccount account;

    public ATM(BankAccount account) {
        this.account = account;
    }

    public void start() {
        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(new BoxLayout(loginPanel, BoxLayout.Y_AXIS));

        JTextField accountNumberField = new JTextField();
        JPasswordField passwordField = new JPasswordField();

        loginPanel.add(new JLabel("Enter Account Number:"));
        loginPanel.add(accountNumberField);
        loginPanel.add(new JLabel("Enter the Password:"));
        loginPanel.add(passwordField);

        accountNumberField.setDocument(new javax.swing.text.PlainDocument() {
            @Override
            public void insertString(int offs, String str, javax.swing.text.AttributeSet a) throws javax.swing.text.BadLocationException {
                if (str == null) return;
                StringBuilder modified = new StringBuilder();
                for (char ch : str.toCharArray()) {
                    if (Character.isLetter(ch)) {
                        modified.append('*');
                    } else {
                        modified.append(ch);
                    }
                }
                super.insertString(offs, modified.toString(), a);
            }
        });

        passwordField.setEchoChar('*');

        int option = JOptionPane.showConfirmDialog(null, loginPanel, "Account Login", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (option == JOptionPane.OK_OPTION) {
            String accountNumber = accountNumberField.getText();
            String password = new String(passwordField.getPassword());

            if (isValidCredentials(accountNumber, password)) {
                while (true) {
                    JPanel optionPanel = new JPanel(new GridBagLayout());
                    GridBagConstraints gbc = new GridBagConstraints();
                    gbc.fill = GridBagConstraints.HORIZONTAL;

                    JButton checkBalanceButton = new JButton("Check Balance");
                    gbc.gridx = 0;
                    gbc.gridy = 0;
                    optionPanel.add(checkBalanceButton, gbc);

                    JButton depositButton = new JButton("Deposit");
                    gbc.gridx = 1;
                    gbc.gridy = 0;
                    optionPanel.add(depositButton, gbc);

                    JButton transactionHistoryButton = new JButton("Transaction History");
                    gbc.gridx = 0;
                    gbc.gridy = 1;
                    gbc.gridwidth = 2;
                    optionPanel.add(transactionHistoryButton, gbc);

                    JButton withdrawButton = new JButton("Withdraw");
                    gbc.gridx = 0;
                    gbc.gridy = 2;
                    gbc.gridwidth = 1;
                    optionPanel.add(withdrawButton, gbc);

                    JButton exitButton = new JButton("Exit");
                    gbc.gridx = 1;
                    gbc.gridy = 2;
                    optionPanel.add(exitButton, gbc);

                    checkBalanceButton.addActionListener(e -> checkBalance());
                    depositButton.addActionListener(e -> deposit());
                    withdrawButton.addActionListener(e -> withdraw());
                    transactionHistoryButton.addActionListener(e -> showTransactionHistory());
                    exitButton.addActionListener(e -> {
                        JOptionPane.showMessageDialog(null, "Thank you for using the ATM!");
                        System.exit(0);
                    });

                    JOptionPane.showMessageDialog(null, optionPanel);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Invalid account number or password. Access denied.");
            }
        }
    }

    private boolean isValidCredentials(String accountNumber, String password) {
        // Check if account number and password match the specified values
        return accountNumber.equals("**123") && password.equals("12345");
    }

    private void checkBalance() {
        double balance = account.getBalance();
        JOptionPane.showMessageDialog(null, "Your current balance is: ₹" + balance);
    }

    private void deposit() {
        String input = JOptionPane.showInputDialog("Enter the amount to deposit (must be above ₹100):");
        if (input != null) {
            try {
                double amount = Double.parseDouble(input);
                if (amount > 100) {
                    account.deposit(amount);
                    JOptionPane.showMessageDialog(null, "Deposit successful. Your new balance is: ₹" + account.getBalance());
                } else {
                    JOptionPane.showMessageDialog(null, "Deposit amount must be above ₹100.");
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Invalid input. Please enter a valid amount.");
            }
        }
    }

    private void withdraw() {
        String input = JOptionPane.showInputDialog("Enter the amount to withdraw:");
        if (input != null) {
            try {
                double amount = Double.parseDouble(input);
                if (amount > 0) {
                    if (account.withdraw(amount)) {
                        JOptionPane.showMessageDialog(null, "Withdrawal successful. Your new balance is: ₹" + account.getBalance());
                    } else {
                        JOptionPane.showMessageDialog(null, "Insufficient funds.");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid amount entered.");
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Invalid input. Please enter a valid amount.");
            }
        }
    }

    private void showTransactionHistory() {
        if (account.hasTransaction()) {
            List<Transaction> history = account.getTransactionHistory();
            StringBuilder historyMessage = new StringBuilder("Transaction History:\n");
            for (Transaction transaction : history) {
                historyMessage.append(transaction.toString()).append("\n");
            }
            JOptionPane.showMessageDialog(null, historyMessage.toString());
        } else {
            JOptionPane.showMessageDialog(null, "No Recent Transaction History");
        }
    }

    public static void main(String[] args) {
        double initialBalance = 2607.04;
        BankAccount account = new BankAccount(initialBalance);
        ATM atm = new ATM(account);
        atm.start();
    }
}
