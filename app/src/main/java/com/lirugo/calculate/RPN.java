package com.lirugo.calculate;

import android.util.Log;

import java.util.Stack;

class RPN {
    private static final char END_CHAR = '$';

    private static StringBuilder input;
    private static Stack<String> output;
    private static Double solution;
    private static Stack<Character> operators;
    private static Stack<Double> operand;

    //Get Polish Expression
    public static String getExpression(String string){
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
                    return String.valueOf(output);
                } else if(getPriority(operators.peek()) > getPriority(input.charAt(i))){
                    //From Texas to California
                    while(operators.peek() != END_CHAR)
                        output.push(operators.pop().toString());
                } else if(getPriority(operators.peek()) < getPriority(input.charAt(i))){
                    return "ERROR";
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
                            return "ERROR";
                        else if (getPriority(operators.peek()) > getPriority(input.charAt(i)))
                            output.push(operators.pop().toString());
                        else if (getPriority(operators.peek()) < getPriority(input.charAt(i)))
                            return "ERROR";
                    }
                    if (getPriority(operators.peek()) == getPriority(input.charAt(i)))
                        operators.pop();
                } else if(input.charAt(i) == END_CHAR){
                    if(getPriority(operators.peek()) > getPriority(input.charAt(i)))
                        output.push(operators.pop().toString());
                    else if(getPriority(operators.peek()) < getPriority(input.charAt(i)))
                        return "ERROR";
                    else if(getPriority(operators.peek()) == getPriority(input.charAt(i)))
                        return String.valueOf(output);
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
                        return "ERROR";
                    else if(getPriority(operators.peek()) < getPriority(input.charAt(i)))
                        operators.push(input.charAt(i));
                    else if(getPriority(operators.peek()) == getPriority(input.charAt(i))) {
                        output.push(operators.pop().toString());
                        operators.push(input.charAt(i));
                    }
                }
                else return "ERROR";

            }


            Log.d("ALERT ----------", "---------------------");
            Log.d("ALERT OUTPUT ---", String.valueOf(output));
            Log.d("ALERT STACK ----", String.valueOf(operators));
        }


        return String.valueOf(output);
    }

    //Get solution
    public static Double getSolution(String string){
        //Init
        solution = 0.0;
        operand = new Stack<>();
        input = new StringBuilder(string);

        for(int i=0; i<input.length(); i++) {
            if (Character.isDigit(input.charAt(i))){
                operand.push(Double.valueOf(input.charAt(i)));
            }
        }
        return solution;
    }

    //Check it's operator
    private static boolean isOperator(char c){
        return "+-*/()".indexOf(c) != -1;
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
