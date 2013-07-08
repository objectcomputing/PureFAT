package com.javanut.purefat;

import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import org.slf4j.helpers.MessageFormatter;

class Function {

    private static final Logger     logger = LoggerFactory.getLogger(Function.class);
    
    //May be many millions of these objects so we must
    //only keep data if it can't be computed any other way.
    private String                  text;
    private String                  label;
    private byte                    paramCount;
    private final Number[]         params; //content is mutable
    private final int              privateIdx;

    Function(int idx) {
        privateIdx = idx;
        params = new Number[PFImpl.MAX_PARAMS];
    }
    
    Function(Number undef) {
        //missing value
        privateIdx = -1;
        params = new Number[]{undef};
        label = "undefined";
        text = PFImpl.LABEL_WRAP;
    }
    
    
    private final void wrapExpr(Number number, String expression, StringBuilder sb) {
        sb.append(number).append('=').append(expression).append(' ');
    }

    private final StringBuilder wrapId(int id, StringBuilder sb) {
        return sb.append('P').append(Long.toHexString(id)).append(' ');
    }
    
    private final void log(Number number, String expressionText) {
        
        //log or no log there is no if
        
//        if (PureFAT.isDebugEnabled) { //TODO: so slow its unusable!!
////            StringBuilder sb = new StringBuilder(100);
////            //TODO: need to add a machine id
////            wrapId(System.identityHashCode(number), sb);
////            wrapExpr(number, expressionText, sb);
////            int i = 0;
////            while (i<paramCount) {
////                wrapId(System.identityHashCode(params[i++]), sb);
////            }
////            //TODO: what can I use this for?
////            //Marker marker = MarkerFactory.getMarker("test");
////            PureFAT.logger.debug(sb.toString(),params);
//        }
    }
    
    public final boolean init(Number number,
                                String label, 
                                String expressionText) {
        this.paramCount = 0;
        this.params[0] = null;
        this.params[1] = null;
        this.params[2] = null;
        this.params[3] = null;
        this.params[4] = null;
        this.params[5] = null;
        this.params[6] = null;
        
        this.label = label;
        this.text = expressionText;
        log(number, expressionText);
        return true;
    }
    
    public final boolean init(Number number,
                                String label, 
                                String expressionText,
                                Number[] paramArray) {
        this.paramCount = (byte) paramArray.length;
        System.arraycopy(paramArray, 0, params, 0, paramArray.length);
        this.label = label;
        this.text = expressionText;
        log(number, expressionText);
        return true;
    }

    
    public final boolean init(Number number,
                                String label, String expressionText,
                                Number p0) {
        this.paramCount = 1;
        this.params[0] = p0;
        this.params[1] = null;
        this.params[2] = null;
        this.params[3] = null;
        this.params[4] = null;
        this.params[5] = null;
        this.params[6] = null;
        
        this.label = label;
        this.text = expressionText;
        log(number, expressionText);
        return true;
    }


    
    public final boolean init(Number number, String label, String expressionText,
                                Number p0,Number p1) {
        this.paramCount = 2;
        this.params[0] = p0;
        this.params[1] = p1;
        this.params[2] = null;
        this.params[3] = null;
        this.params[4] = null;
        this.params[5] = null;
        this.params[6] = null;
        
        this.label = label;
        this.text = expressionText;
        log(number, expressionText);
        return true;
    }
    
    public final boolean init(Number number, String label, String expressionText,
                                Number p0,Number p1,Number p2) {
        this.paramCount = 3;
        this.params[0] = p0;
        this.params[1] = p1;
        this.params[2] = p2;
        this.params[3] = null;
        this.params[4] = null;
        this.params[5] = null;
        this.params[6] = null;
        
        this.label = label;
        this.text = expressionText;
        log(number, expressionText);
        return true;
    }
    
    public final boolean init(Number number, String label, String expressionText,
                                Number p0,Number p1,Number p2,Number p3) {
        this.paramCount = 4;
        this.params[0] = p0;
        this.params[1] = p1;
        this.params[2] = p2;
        this.params[3] = p3;
        this.params[4] = null;
        this.params[5] = null;
        this.params[6] = null;
        
        this.label = label;
        this.text = expressionText;
        log(number, expressionText);
        return true;
    }
    
    public final boolean init(Number number, String label, String expressionText,
                                Number p0,Number p1,Number p2,Number p3,Number p4) {
        this.paramCount = 5;
        this.params[0] = p0;
        this.params[1] = p1;
        this.params[2] = p2;
        this.params[3] = p3;
        this.params[4] = p4;
        this.params[5] = null;
        this.params[6] = null;
        
        this.label = label;
        this.text = expressionText;
        log(number, expressionText);
        return true;
    }
    
    public final boolean init(Number number, String label, String expressionText,
                                Number p0,Number p1,Number p2,Number p3,Number p4,Number p5) {
        this.paramCount = 6;
        this.params[0] = p0;
        this.params[1] = p1;
        this.params[2] = p2;
        this.params[3] = p3;
        this.params[4] = p4;
        this.params[5] = p5;
        this.params[6] = null;
        
        this.label = label;
        this.text = expressionText;
        log(number, expressionText);
        return true;
    }
    
    public final boolean init(Number number, String label, String expressionText,
                                Number p0,Number p1,Number p2,Number p3,Number p4,Number p5,Number p6) {
        this.paramCount = 7;
        this.params[0] = p0;
        this.params[1] = p1;
        this.params[2] = p2;
        this.params[3] = p3;
        this.params[4] = p4;
        this.params[5] = p5;
        this.params[6] = p6;
        
        this.label = label;
        this.text = expressionText;
        log(number, expressionText);
        return true;
    }
    
    
    public final Number[] params() {
        return Arrays.copyOf(params, paramCount);
    }
    
    public String toString() {
        return MessageFormatter.arrayFormat(text, params()).getMessage();
    }

    public String text() {
        return text;
    }

    public void log(String label, Logger logger) {
        logger.info(label+' '+text, params);
    }

    public String label() {
        return "${"+label+"}";
    }

    public boolean isLabel() {
        return text==PFImpl.LABEL_WRAP;//special instance just for constant labels
    }


    public int getPrivateIndex() {
        return privateIdx;
    }



}
