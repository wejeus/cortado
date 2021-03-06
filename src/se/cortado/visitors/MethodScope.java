package se.cortado.visitors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import se.cortado.frame.Access;
import se.cortado.frame.Frame;
import se.cortado.ir.temp.Label;
import se.cortado.syntaxtree.Formal;
import se.cortado.syntaxtree.FormalList;
import se.cortado.syntaxtree.Identifier;
import se.cortado.syntaxtree.MethodDecl;
import se.cortado.syntaxtree.Type;
import se.cortado.syntaxtree.VarDecl;

/** @author Samuel Wejeus */
public class MethodScope {
	private static Frame motherFrame;

	public static Frame getMotherFrame() {
		if (motherFrame == null) {
			motherFrame = new se.cortado.x86_64.frame.Frame();
		}

		return motherFrame;
	}

	private MethodDecl methodDecl;
	private HashMap<String, Type> variables;
	private HashMap<String, Access> accesses = new HashMap<String, Access>();
	private HashMap<String, Integer> locals = new HashMap<String, Integer>();
	private FormalList parameters;
	private Type returnType;
	private String labelName;

	private Frame frame;

	public MethodScope(Type returnType, MethodDecl mDecl) {

		// create a list of "false" to signal that none of the parameters will
		// escape
		List<Boolean> escapeList = new ArrayList<Boolean>();
		for (int i = 0; i < mDecl.formalList.size(); i++) {
			escapeList.add(false);
		}

		// create the frame
		frame = getMotherFrame().newFrame(new Label(), escapeList);

		// add "this" access
		Access thisAccess = frame.allocLocal(false);
		accesses.put("this", thisAccess);
		locals.put("this", locals.size());
		
		parameters = new FormalList();
		variables = new HashMap<String, Type>();
		this.returnType = returnType;
		this.setMethodDecl(mDecl);
	}

	public void addParameter(Formal param, Type type) throws Exception {
		if (parameters.contains(param.i.s)) {
			throw new Exception("Duplicate parameter \"" + param.i
					+ "\" on line: " + param.i.line);
		} else {
			// add local variable to frame
			Access f = frame.allocLocal(false);
			accesses.put(param.i.s, f);
			parameters.addElement(param);
			variables.put(param.i.s, type);
			
			if (getLocal(param.i.s) == null) {
				locals.put(param.i.s, locals.size());
			}
		}
	}

	public void addVariable(VarDecl variable, Type type) throws Exception {
		if (parameters.contains(variable.identifier.s)) {
			throw new Exception("Redeclaration of method parameter");
		} else if (variables.containsKey(variable.identifier.s)) {
			throw new Exception("Redeclaration of local variable \""
					+ variable.identifier + "\" on line: "
					+ variable.identifier.line);
		} else {
			// add local variable to frame
			Access f = frame.allocLocal(false);
			accesses.put(variable.identifier.s, f);
			variables.put(variable.identifier.s, type);
			
			Integer l = locals.get(variable.identifier.s);
			if (l == null) {
				locals.put(variable.identifier.s, locals.size());
			}
		}
	}

	@Override
	public String toString() {
		return "MethodScope(" + methodDecl.identifier.s + ", formalCount: "
				+ parameters.size();
	}

	public FormalList getParameterList() {
		return parameters;
	}

	public Type getVariableType(Identifier id) {
		return variables.get(id.s);
	}
	
	public Type getVariableType(String s) {
		return variables.get(s);
	}

	public Type getFormalType(Identifier id) {
		return getFormalType(id.s);
	}
	
	public Type getFormalType(String s) {
		for (int i = 0; i < parameters.size(); i++) {
			if (s.equals(parameters.elementAt(i).i.s)) {
				return parameters.elementAt(i).t;
			}
		}

		return null;
	}

	public Type getReturnType() {
		return returnType;
	}

	public boolean hasFormal(String formalName) {
		for (int i = 0; i < parameters.size(); i++) {
			if (formalName.equals(parameters.elementAt(i).i.s)) {
				return true;
			}
		}

		return false;
	}

	public boolean hasVariable(String variableName) {
		return variables.get(variableName) != null;
	}

	public MethodDecl getMethodDecl() {
		return methodDecl;
	}

	public void setMethodDecl(MethodDecl methodDecl) {
		this.methodDecl = methodDecl;
	}

	public HashMap<String, Type> getVariables() {
		return variables;
	}

	public Frame getFrame() {
		return frame;
	}

	public Access getAccess(String variableName) {
		return accesses.get(variableName);
	}
	
	public String getLocal(String variableName) {
		Integer loc = locals.get(variableName);
		if (loc == null) {
			return null;
		} else {
			return "" + loc;
		}
	}
	
	public int getNumLocals() {
		return locals.size();
	}

	public String getLabelName() {
		return labelName;
	}

	public void setLabelName(String labelName) {
		this.labelName = labelName;
		frame.setLabel(new Label(labelName));
	}

}
