import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Stack;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Stack;

/**
 * CalculatorApp is a Java-based calculator application with a graphical user interface.
 * It provides users with the ability to perform various mathematical operations such as addition, subtraction,
 * multiplication, division, exponentiation, square root, and trigonometric functions.
 * The application supports keyboard and button inputs, displays the calculated results in a text field,
 * and maintains a history of expressions and results.
 *
 * For any inquiries, please contact Mohammad Moussa at me@mhmoussa.com.
 * © 2023 Mohammad Moussa. All rights reserved.
 */

public class CalculatorApp extends JFrame {
    private JTextField entryField;
    private JButton[] buttons;
    private String[] buttonLabels = {
            "7", "8", "9", "/",
            "4", "5", "6", "*",
            "1", "2", "3", "-",
            "0", ".", "=", "+",
            "√", "^", "C",
            "M+", "MR", "MC",
            "sin", "cos", "tan",
            "π", "e"
    };

    private double memory;
    private StringBuilder expressionBuilder;
    private JTextArea historyArea;

    public CalculatorApp() {
        try {
            UIManager.setLookAndFeel(new NimbusLookAndFeel());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        setTitle("Calculator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setResizable(false);

        entryField = new JTextField();
        entryField.setHorizontalAlignment(JTextField.RIGHT);
        entryField.setEditable(false);
        entryField.setFont(new Font("Arial", Font.PLAIN, 20));

        expressionBuilder = new StringBuilder();
        historyArea = new JTextArea();
        historyArea.setEditable(false);
        historyArea.setFont(new Font("Arial", Font.PLAIN, 14));
        JScrollPane historyScrollPane = new JScrollPane(historyArea);
        historyScrollPane.setPreferredSize(new Dimension(300, 100));

        // Add a KeyListener to the entryField
        entryField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (Character.isDigit(c) || c == '.' || "+-*/^√".indexOf(c) != -1) {
                    addToEntryField(String.valueOf(c));
                } else if (c == KeyEvent.VK_ENTER) {
                    calculateResult();
                }
            }
        });

        JPanel buttonPanel = new JPanel(new GridLayout(6, 4, 10, 10));
        buttonPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        buttons = new JButton[buttonLabels.length];

        ButtonHandler buttonHandler = new ButtonHandler();

        for (int i = 0; i < buttonLabels.length; i++) {
            buttons[i] = new JButton(buttonLabels[i]);
            buttons[i].setFont(new Font("Arial", Font.BOLD, 16));
            buttons[i].addActionListener(buttonHandler);
            buttons[i].setMargin(new Insets(10, 10, 10, 10));
            buttonPanel.add(buttons[i]);
        }

        JPanel operatorPanel = new JPanel(new GridLayout(1, 4, 10, 10));
        operatorPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        operatorPanel.add(buttons[15]); // +
        operatorPanel.add(buttons[11]); // -
        operatorPanel.add(buttons[7]); // *
        operatorPanel.add(buttons[3]); // /

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(operatorPanel, BorderLayout.SOUTH);
        bottomPanel.add(buttonPanel, BorderLayout.CENTER);

        add(entryField, BorderLayout.NORTH);
        add(historyScrollPane, BorderLayout.WEST);
        add(bottomPanel, BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void calculateResult() {
        String expression = entryField.getText();

        try {
            BigDecimal result = evaluateExpression(expression);
            entryField.setText(result.toPlainString());
            addToHistory(expression + " = " + result.toPlainString());
        } catch (IllegalArgumentException e) {
            showError("Invalid expression: " + e.getMessage());
        } catch (ArithmeticException e) {
            showError("Mathematical error: " + e.getMessage());
        }
    }

    private BigDecimal evaluateExpression(String expression) {
        Stack<BigDecimal> operands = new Stack<>();
        Stack<Character> operators = new Stack<>();

        for (int i = 0; i < expression.length(); i++) {
            char c = expression.charAt(i);

            if (Character.isDigit(c) || c == '.') {
                StringBuilder number = new StringBuilder();
                while (i < expression.length()
                        && (Character.isDigit(expression.charAt(i)) || expression.charAt(i) == '.')) {
                    number.append(expression.charAt(i));
                    i++;
                }
                i--;

                operands.push(new BigDecimal(number.toString()));
            } else if (c == '+' || c == '-' || c == '*' || c == '/' || c == '^') {
                while (!operators.isEmpty() && hasPrecedence(operators.peek(), c)) {
                    calculate(operands, operators);
                }
                operators.push(c);
            } else if (c == '√') {
                operators.push(c);
            } else if (c == '(') {
                operators.push(c);
            } else if (c == ')') {
                while (!operators.isEmpty() && operators.peek() != '(') {
                    calculate(operands, operators);
                }
                operators.pop();
            } else if (c == 'π') {
                operands.push(BigDecimal.valueOf(Math.PI));
            } else if (c == 'e') {
                operands.push(BigDecimal.valueOf(Math.E));
            } else if (c == 's' || c == 'c' || c == 't') {
                String function = expression.substring(i, i + 3);
                if (function.equals("sin")) {
                    operators.push('s');
                    i += 2;
                } else if (function.equals("cos")) {
                    operators.push('c');
                    i += 2;
                } else if (function.equals("tan")) {
                    operators.push('t');
                    i += 2;
                } else {
                    throw new IllegalArgumentException("Unknown function: " + function);
                }
            }
        }

        while (!operators.isEmpty()) {
            calculate(operands, operators);
        }

        if (operands.isEmpty()) {
            throw new IllegalArgumentException("Invalid expression");
        }

        return operands.pop();
    }

    private void calculate(Stack<BigDecimal> operands, Stack<Character> operators) {
        char operator = operators.pop();
        if (operator == '√') {
            BigDecimal operand = operands.pop();
            operands.push(sqrt(operand));
        } else if (operator == 's' || operator == 'c' || operator == 't') {
            BigDecimal operand = operands.pop();
            BigDecimal radians = toRadians(operand);

            if (operator == 's') {
                operands.push(sin(radians));
            } else if (operator == 'c') {
                operands.push(cos(radians));
            } else if (operator == 't') {
                operands.push(tan(radians));
            }
        } else {
            BigDecimal rightOperand = operands.pop();
            BigDecimal leftOperand = operands.pop();

            switch (operator) {
                case '+':
                    operands.push(leftOperand.add(rightOperand));
                    break;
                case '-':
                    operands.push(leftOperand.subtract(rightOperand));
                    break;
                case '*':
                    operands.push(leftOperand.multiply(rightOperand));
                    break;
                case '/':
                    if (rightOperand.compareTo(BigDecimal.ZERO) == 0) {
                        throw new ArithmeticException("Division by zero");
                    }
                    operands.push(leftOperand.divide(rightOperand, MathContext.DECIMAL128));
                    break;
                case '^':
                    operands.push(leftOperand.pow(rightOperand.intValue()));
                    break;
            }
        }
    }

    private boolean hasPrecedence(char operator1, char operator2) {
        if (operator1 == '(' || operator1 == ')') {
            return false;
        }
        return operator2 != '^' && (operator1 == '*' || operator1 == '/');
    }

    private BigDecimal sqrt(BigDecimal operand) {
        return operand.sqrt(MathContext.DECIMAL128);
    }

    private BigDecimal sin(BigDecimal radians) {
        double result = Math.sin(radians.doubleValue());
        return BigDecimal.valueOf(result);
    }

    private BigDecimal cos(BigDecimal radians) {
        double result = Math.cos(radians.doubleValue());
        return BigDecimal.valueOf(result);
    }

    private BigDecimal tan(BigDecimal radians) {
        double result = Math.tan(radians.doubleValue());
        return BigDecimal.valueOf(result);
    }

    private BigDecimal toRadians(BigDecimal degrees) {
        double result = Math.toRadians(degrees.doubleValue());
        return BigDecimal.valueOf(result);
    }

    private void clearEntryField() {
        entryField.setText("");
    }

    private void addToEntryField(String value) {
        entryField.setText(entryField.getText() + value);
    }

    private void addToMemory() {
        String valueStr = entryField.getText();
        if (!valueStr.isEmpty()) {
            double value = Double.parseDouble(valueStr);
            memory += value;
        }
    }

    private void recallMemory() {
        entryField.setText(Double.toString(memory));
    }

    private void clearMemory() {
        memory = 0;
    }

    private void addToHistory(String history) {
        historyArea.append(history + "\n");
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private class ButtonHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String buttonLabel = e.getActionCommand();

            if (buttonLabel.equals("=")) {
                calculateResult();
            } else if (buttonLabel.equals("C")) {
                clearEntryField();
            } else if (buttonLabel.equals("M+")) {
                addToMemory();
            } else if (buttonLabel.equals("MR")) {
                recallMemory();
            } else if (buttonLabel.equals("MC")) {
                clearMemory();
            } else {
                addToEntryField(buttonLabel);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new CalculatorApp();
            }
        });
    }
}
