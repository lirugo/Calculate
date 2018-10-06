package com.lirugo.calculate;

import android.util.Log;

import java.util.Stack;

class RPN {
    private static final char END_CHAR = '$';

    private static StringBuilder input;
    private static StringBuilder output;
    private static Stack<Character> operators;

    public static String getExpression(String string){
        //Init global variable
        input = new StringBuilder(string);
        output = new StringBuilder();
        operators = new Stack<>();

        //Set end and start point
        input.insert(0, END_CHAR);
        input.insert(input.length(), END_CHAR);

        //Push start char
        operators.push(input.charAt(0));

        //Main cycle for getting Reverse Polish Notation
        for(int i=1; i<input.length(); i++) {
            //If it's start or end point
            if(input.charAt(i) == '$'){
                if(getPriority(operators.peek()) == getPriority(input.charAt(i))){
                    //It's a answer
                    return String.valueOf(output);
                } else if(getPriority(operators.peek()) > getPriority(input.charAt(i))){
                    //From Texas to California
                    output.append(operators.pop());
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
                output.append(digit);
            //If it's operator
            } else if(isOperator(input.charAt(i))){
                if(input.charAt(i) == '(')
                    operators.push(input.charAt(i));
                else if(input.charAt(i) == ')'){
                    while(operators.peek() != '(') {
                        if(operators.peek() == END_CHAR)
                            return "ERROR";
                        else if (getPriority(operators.peek()) > getPriority(input.charAt(i)))
                            output.append(operators.pop());
                        else if (getPriority(operators.peek()) < getPriority(input.charAt(i)))
                            return "ERROR";
                    }
                    if (getPriority(operators.peek()) == getPriority(input.charAt(i)))
                        operators.pop();
                } else if(input.charAt(i) == END_CHAR){
                    if(getPriority(operators.peek()) > getPriority(input.charAt(i)))
                        output.append(operators.pop());
                    else if(getPriority(operators.peek()) < getPriority(input.charAt(i)))
                        return "ERROR";
                    else if(getPriority(operators.peek()) == getPriority(input.charAt(i)))
                        return String.valueOf(output);
                } else if(getPriority(input.charAt(i)) == 2){
                    if(getPriority(operators.peek()) < getPriority(input.charAt(i)))
                        operators.push(input.charAt(i));
                    else if(getPriority(operators.peek()) >= getPriority(input.charAt(i))) {
                        while (getPriority(operators.peek()) >= getPriority(input.charAt(i)))
                            output.append(operators.pop());
                        operators.push(input.charAt(i));
                    }
                } else if(getPriority(input.charAt(i)) == 3){
                    if(getPriority(operators.peek()) > getPriority(input.charAt(i)))
                        return "ERROR";
                    else if(getPriority(operators.peek()) < getPriority(input.charAt(i)))
                        operators.push(input.charAt(i));
                    else if(getPriority(operators.peek()) == getPriority(input.charAt(i)))
                        output.append(operators.pop());
                }
                else return "ERROR";

            }


            Log.d("ALERT ----------", "---------------------");
            Log.d("ALERT OUTPUT ---", String.valueOf(output));
            Log.d("ALERT STACK ----", String.valueOf(operators));
        }


        return String.valueOf(output);
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
