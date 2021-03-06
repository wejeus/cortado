package se.cortado.syntaxtree;

import se.cortado.visitors.TypeVisitor;
import se.cortado.visitors.Visitor;

public class VarDecl extends Node {
	public Type type;
	public Identifier identifier;

	public VarDecl(Type at, Identifier ai) {
		type = at;
		identifier = ai;
	}

	public void accept(Visitor v) {
		v.visit(this);
	}

	public Type accept(TypeVisitor v) {
		return v.visit(this);
	}
	
}
