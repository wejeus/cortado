package se.cortado.visitors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import se.cortado.frame.Access;
import se.cortado.frame.Frame;
import se.cortado.frame.Record;
import se.cortado.syntaxtree.ClassDecl;
import se.cortado.syntaxtree.Formal;
import se.cortado.syntaxtree.FormalList;
import se.cortado.syntaxtree.MainClass;
import se.cortado.syntaxtree.MethodDecl;
import se.cortado.syntaxtree.Type;

public class SymbolTable {
	private HashMap<String, ClassScope> symbolTable;
	private List<ClassScope> classScopes = new ArrayList<ClassScope>();

	public SymbolTable() {
		symbolTable = new HashMap<String, ClassScope>();
	}

	public void put(String key, ClassScope value) {
		classScopes.add(value);
		symbolTable.put(key, value);
	}

	public ClassScope get(String key) {
		return symbolTable.get(key);
	}

	public boolean containsKey(String key) {
		return get(key) != null;
	}

	public Record getRecord(ClassDecl classDecl) {
		ClassScope cs = symbolTable.get(classDecl.i.s);

		return cs.getRecord();
	}

	public Frame getFrame(ClassDecl classDecl, MethodDecl methodDecl) {
		ClassScope cs = symbolTable.get(classDecl.i.s);
		MethodScope ms = cs.getMethodMatching(methodDecl);

		return ms.getFrame();
	}

	public Access getAccess(ClassDecl classDecl, MethodDecl methodDecl, String variableIdentifier) {
		ClassScope cs = symbolTable.get(classDecl.i.s);
		MethodScope ms = cs.getMethodMatching(methodDecl);
		
		Access a = ms.getAccess(variableIdentifier);
		if (a == null) {
			// no variable in method, look for variable in class fields
			a = cs.getAccess(variableIdentifier);
		}

		return a;
	}
	
	public Access getAccess(ClassDecl classDecl, String variableIdentifier) {
		ClassScope cs = symbolTable.get(classDecl.i.s);
		return cs.getAccess(variableIdentifier);
	}

	private void indent(int i) {
		for (int k = 0; k < i; k++)
			System.out.print("\t");
	}

	public void print() {
		int indent = 0;

		MainClass mc = (MainClass) classScopes.get(0).getClassDecl();
		System.out.println("Main class: " + mc.i);

		for (int i = 0; i < classScopes.size(); i++) {
			ClassScope cs = classScopes.get(i);
			ClassDecl cd = cs.getClassDecl();

			indent(indent = 1);
			System.out.println("Class: " + cd.i.s);

			indent(++indent);
			System.out.println("Fields:");
			indent++;

			HashMap<String, Type> fieldMap = cs.getVariables();
			for (Map.Entry<String, Type> entry : fieldMap.entrySet()) {
				indent(indent);
				System.out.println(entry.getValue().getTypeName() + " "
						+ entry.getKey());
			}

			indent(--indent);
			System.out.println("Methods:");
			indent++;

			HashMap<String, List<MethodScope>> methodMap = cs.getMethods();
			for (Map.Entry<String, List<MethodScope>> entry : methodMap
					.entrySet()) {

				for (MethodScope ms : entry.getValue()) {
					// print method return type and method name
					indent(indent);
					System.out.println("" + ms.getReturnType() + " "
							+ ms.getMethodDecl().identifier.s);

					// parameters print
					indent(++indent);
					System.out.println("Parameters:");
					indent++;

					FormalList paramList = ms.getParameterList();
					for (int p = 0; p < paramList.size(); p++) {
						indent(indent);
						Formal f = paramList.elementAt(p);
						System.out.println(f.t.getTypeName() + " " + f.i.s);
					}

					// print locals
					indent(--indent);
					System.out.println("Locals:");
					indent++;

					HashMap<String, Type> locals = ms.getVariables();

					for (Map.Entry<String, Type> localPair : locals.entrySet()) {
						indent(indent);
						System.out.println(localPair.getValue().getTypeName()
								+ " " + localPair.getKey());
					}
					indent--;

					indent--;
				}
			}

		}

	}
}
