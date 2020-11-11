package shuntingYard;


import java.util.LinkedList;
import java.util.Stack;

import model.interpeter.MyInterpreter;

public class ShuntingYard{

    public static double calc(String expression) {
        LinkedList<String> queue = new LinkedList<>();
        Stack<String> stack = new Stack<>();
        String[] token = expression.split("(?<=[-+*/()])|(?=[-+*/()])");

        for (int i = 0; i < token.length; i++) {
            switch (token[i]) {
                case "+":
                case "-": {
                    while (!stack.isEmpty() && !stack.peek().equals("(")) {
                        queue.addLast(stack.pop());
                    }
                    stack.push(token[i]);
                    break;
                }
                case "(":
                case "*":
                case "/": {
                    stack.push(token[i]);
                    break;
                }
                case ")": {
                    while (!stack.isEmpty() && !(stack.peek().equals("("))) {
                        queue.addLast(stack.pop());
                    }
                    stack.pop();
                    break;
                }
                default: {
                    String val=token[i];
                    if(MyInterpreter.SymbolTbl.containsKey(val)) {
                       val=MyInterpreter.SymbolTbl.get(val).getValue().toString();
                    }
                    queue.addLast(val);
                    break;
                }
            }
        }
        while (!stack.isEmpty()) {
            queue.addLast(stack.pop());
        }
        return ExTree(queue).calculate();

    }

    public static Expression ExTree(LinkedList<String> queue) {
        Expression temp;
        if(queue.isEmpty()) return new Number(0);
        String e = queue.pollLast();
        switch (e) {
            case "+": {
                Expression right = ExTree(queue);
                Expression left = ExTree(queue);
                temp = new Plus(left, right);
                break;
            }
            case "-": {
                Expression right = ExTree(queue);
                Expression left = ExTree(queue);
                temp = new Minus(left, right);
                break;
            }
            case "*": {
                Expression right = ExTree(queue);
                Expression left = ExTree(queue);
                temp = new Multiply(left, right);
                break;
            }
            case "/": {
                Expression right = ExTree(queue);
                Expression left = ExTree(queue);
                temp = new Divide(left, right);
                break;
            }
            default: {
                double num = Double.parseDouble(e);
                temp = new Number((float)num);
                break;
            }
        }
        return temp;
    }

}

