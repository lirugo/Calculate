package com.lirugo.calculate;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

class RPN {
    private static final char END_CHAR = '$';

    private static StringBuilder input;
    private static Stack<String> output;
    private static Double solution;
    private static Stack<Character> operators;
    private static Stack<Double> operand;

    //Get solution
    public static Double getSolution(String string){
        //Init
        solution = 0.0;
        operand = new Stack<>();
        output = getExpression(string);

        if(output == null)
            return 0.00;

        List<String> list = new ArrayList<>(output);

        for (int i=0; i<list.size(); i++){
            if (isNumeric(list.get(i)))
                operand.push(Double.valueOf(list.get(i)));
            else if("+-*/".contains(list.get(i))){
                char c = list.get(i).charAt(0);
                double a = operand.pop();
                if(operand.empty()){
                    operand.push(a);
                    continue;
                }
                double b = operand.pop();
                operand.push(makeOperation(b, a, c));
            } else return 0.00;
        }


        if(operand.size() == 1)
            return operand.peek();
        else
            return 0.00;
    }

    //Get Polish Expression
    private static Stack<String> getExpression(String string){
        //Init global variable
        input = new StringBuilder(string);
        output = new Stack<>();
        operators = new Stack<>();

        //Set end and start point
        input.insert(0, END_CHAR);
        input.insert(input.length(), END_CHAR);

        //Push start char
        operators.push(input.charAt(0));

        //Main cycle for getting Reverse Polish Notation
        for(int i=1; i<input.length(); i++) {
            //If it's start or end point
            if(input.charAt(i) == END_CHAR){
                if(getPriority(operators.peek()) == getPriority(input.charAt(i))){
                    //It's a answer
                    return output;
                } else if(getPriority(operators.peek()) > getPriority(input.charAt(i))){
                    //From Texas to California
                    while(operators.peek() != END_CHAR)
                        output.push(operators.pop().toString());
                } else if(getPriority(operators.peek()) < getPriority(input.charAt(i))){
                    return null;
                }
                //If it's digit
            } else if(Character.isDigit(input.charAt(i))){
                StringBuilder digit = new StringBuilder();
                while(Character.isDigit(input.charAt(i))){
                    digit.append(input.charAt(i));
                    i++;
                }
                i--;
                output.push(digit.toString());
                //If it's operator
            } else if(isOperator(input.charAt(i))){
                if(input.charAt(i) == '(')
                    operators.push(input.charAt(i));
                else if(input.charAt(i) == ')'){
                    while(operators.peek() != '(') {
                        if(operators.peek() == END_CHAR)
                            return null;
                        else if (getPriority(operators.peek()) > getPriority(input.charAt(i)))
                            output.push(operators.pop().toString());
                        else if (getPriority(operators.peek()) < getPriority(input.charAt(i)))
                            return null;
                    }
                    if (getPriority(operators.peek()) == getPriority(input.charAt(i)))
                        operators.pop();
                } else if(input.charAt(i) == END_CHAR){
                    if(getPriority(operators.peek()) > getPriority(input.charAt(i)))
                        output.push(operators.pop().toString());
                    else if(getPriority(operators.peek()) < getPriority(input.charAt(i)))
                        return null;
                    else if(getPriority(operators.peek()) == getPriority(input.charAt(i)))
                        return output;
                } else if(getPriority(input.charAt(i)) == 2){
                    if(getPriority(operators.peek()) < getPriority(input.charAt(i)))
                        operators.push(input.charAt(i));
                    else if(getPriority(operators.peek()) >= getPriority(input.charAt(i))) {
                        while (getPriority(operators.peek()) >= getPriority(input.charAt(i)))
                            output.push(operators.pop().toString());
                        operators.push(input.charAt(i));
                    }
                } else if(getPriority(input.charAt(i)) == 3){
                    if(getPriority(operators.peek()) > getPriority(input.charAt(i)))
                        return null;
                    else if(getPriority(operators.peek()) < getPriority(input.charAt(i)))
                        operators.push(input.charAt(i));
                    else if(getPriority(operators.peek()) == getPriority(input.charAt(i))) {
                        output.push(operators.pop().toString());
                        operators.push(input.charAt(i));
                    }
                }
                else return null;
            }

//            Log.d("EXPRESSION ----------", "---------------------");
//            Log.d("OUTPUT ---", String.valueOf(output));
//            Log.d("STACK ----", String.valueOf(operators));
        }

        return output;
    }

    //Make operation with two operands
    private static Double makeOperation(double a, double b, char operator){
        switch (operator){
            case '*' : return a * b;
            case '/' : return a / b;
            case '+' : return a + b;
            case '-' : return a - b;
            default  : return null;
        }
    }

    //Check it's operator
    private static boolean isOperator(char c){
        return "+-*/()".indexOf(c) != -1;
    }

    //Check it's numeric
    private static boolean isNumeric(String string){
        try {
            double d = Double.parseDouble(string);
        } catch (NumberFormatException | NullPointerException nfe) {
            return false;
        }
        return true;
    }

    //Get operation priority
    private static byte getPriority(char c){
        switch (c){
            case '(' : return 0;
            case ')' : return 0;
            case '$' : return 1;
            case '+' : return 2;
            case '-' : return 2;
            case '*' : return 3;
            case '/' : return 3;
            default  : return -1;
        }
    }
}
