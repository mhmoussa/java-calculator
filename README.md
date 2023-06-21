The CalculatorApp is a Java program that provides a graphical user interface for a calculator application. It allows users to perform various mathematical operations, including basic arithmetic, exponentiation, trigonometric functions, square root, and memory operations. The application utilizes Swing and AWT libraries to create the GUI components.

Features
Basic arithmetic operations: addition, subtraction, multiplication, and division.
Exponentiation: raising a number to a power.
Trigonometric functions: sine, cosine, and tangent.
Square root: calculating the square root of a number.
Memory operations: storing and recalling values from memory.
Constants: π (pi) and e (Euler's number).
Getting Started
To run the calculator application, follow these steps:

Set up a Java development environment with support for Swing and AWT libraries.
Copy the provided source code into a Java file (e.g., CalculatorApp.java).
Compile the Java file to generate the bytecode: javac CalculatorApp.java.
Run the application: java CalculatorApp.
User Interface
The calculator application features a graphical user interface with the following components:

Entry Field: A read-only text field that displays the current expression or calculated result.
Buttons: Various buttons representing numbers, operators, and functions for user input.
History Area: A read-only text area that displays the history of expressions and results.
Usage
Enter the mathematical expression using the buttons or the keyboard.
Press the Enter key or the = button to calculate the result.
The calculated result will be displayed in the entry field.
The history area shows the expression and result of each calculation.
Use the provided buttons for memory operations (M+, MR, MC) to store and recall values from memory.
To clear the entry field, press the C button.
To perform trigonometric functions, use the sin, cos, and tan buttons.
To calculate the square root, use the √ button.
To raise a number to a power, use the ^ button.
Constants can be accessed using the π and e buttons.
Implementation Details
The CalculatorApp class extends the JFrame class from Swing and represents the main window of the application. The user interface components are initialized and arranged within the frame's layout. The application utilizes event listeners to handle user interactions with the buttons and the keyboard.

The calculation logic is implemented in the evaluateExpression method, which evaluates a given mathematical expression using a stack-based approach. The method supports numbers, operators, parentheses, and functions. It makes use of the BigDecimal class for precise decimal arithmetic.

Dependencies
Java Development Kit (JDK)
Swing and AWT libraries
