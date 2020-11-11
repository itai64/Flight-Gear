package model.server;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class State<T> implements Comparable<State<T>> {
	private T currentState;
	private double cost;
	private State<T> cameFrom;

	public State(T state, double cost,State<T> father) {
		this.currentState = state;
		this.cameFrom = father;
		this.cost=cost;

	}

	public State(State<T> other) {
		this.currentState = other.currentState;
		this.cost = other.cost;
		this.cameFrom = null;

	}


	public List<State<T>> backTrace() {
		ArrayList<State<T>> backtrace = new ArrayList<>();
		State<T> temp = this;
		while (temp.getCameFrom() != null) {
			backtrace.add(temp);
			temp = temp.getCameFrom();
		}
		backtrace.add(temp);
		Collections.reverse(backtrace);
		return backtrace;
	}

	@Override
	public boolean equals(Object o) {
		State<T> s = (State<T>)o;
		return s.getCurrentState().equals(this.getCurrentState());
	}

	public double getCost() {
		return cost;
	}

	public void setCost(double cost) {
		this.cost = cost;
	}
	public State<T> getCameFrom() {
		return cameFrom;
	}

	public void setCameFrom(State<T> cameFrom) {
		this.cameFrom = cameFrom;
	}

	public T getCurrentState() {
		return currentState;
	}

	public void setCurrentState(T currentState) {
		this.currentState = currentState;
	}

	@Override
	public int compareTo(State<T> o) {
		if(this.getCost() > o.getCost())
			return 1;
		return -1;
	}
	
	@Override 
	public String toString() {
		return this.getCurrentState().toString(); 
	}

}
