package se.cortado.ir.translate;

import se.cortado.ir.temp.Label;
import se.cortado.ir.tree.IR_Stm;
import se.cortado.ir.tree.IR_Exp;

/** @author Samuel Wejeus */
public class TR_Nx extends TR_Exp {
	
	IR_Stm stm;

	public TR_Nx(IR_Stm s) {
		stm = s;
	}

	/** Should never occur in a well types MiniJava program
	 * Why you say? How can a non value returning function return a value I say! */
	public IR_Exp build_EX() {
		// TODO: Maybe throw error instead?
		return null;
	}

	public IR_Stm build_NX() {
		return stm;
	}

	/** Should never occur in a well types MiniJava program
	 * Why you say? Not interesting for an action to performed as a conditional I say! */
	public IR_Stm build_CX(Label t, Label f) {
		// TODO: Maybe throw error instead?
		return null;
	}
}