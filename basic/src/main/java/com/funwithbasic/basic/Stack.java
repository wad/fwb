package com.funwithbasic.basic;

import java.util.ArrayList;
import java.util.List;

public class Stack<T> {

    private List<T> stack;

    public Stack() {
        stack = new ArrayList<T>();
    }

    public Stack(int initialSize) {
        stack = new ArrayList<T>(initialSize);
    }

    public T pop() throws BasicException {
        if (stack.isEmpty()) {
            throw new BasicException("Attempted to pop from empty stack");
        }
        T result = stack.get(0);
        stack.remove(0);
        return result;
    }

    public void push(T t) {
        stack.add(0, t);
    }

    public T peek() {
        if (stack.isEmpty()) {
            return null;
        }
        return stack.get(0);
    }

    public int size() {
        return stack.size();
    }

    public boolean isEmpty() {
        return stack.isEmpty();
    }

}
