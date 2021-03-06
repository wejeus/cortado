package se.cortado.ir.translate;

import se.cortado.ir.temp.Label;
import se.cortado.ir.tree.IR_Exp;
import se.cortado.ir.tree.IR_Stm;

/** @author Samuel Wejeus */
public class TR_Nx extends Translate {
	
	private IR_Stm stm;

	public TR_Nx(IR_Stm s) {
    	if (s == null) {
    		throw new Error("Initializing TR_Nx with s = null");
    	}
    	
		stm = s;
	}

	/** Should never occur in a well types MiniJava program
	 * Why you say? How can a non value returning function return a value I say! */
	public IR_Exp getValue() {
		throw new Error("Can't convert TR_Nx to a EX!");
	}

	public IR_Stm getNoValue() {
		return stm;
	}

	/** Should never occur in a well types MiniJava program
	 * Why you say? Not interesting for an action to performed as a conditional I say! */
	public IR_Stm getConditional(Label t, Label f) {
		throw new Error("Can't convert TR_Nx to a CX!");
	}
}
