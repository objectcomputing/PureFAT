/**
 * Copyright (c) 2013, Nathan Tippy
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * 
 * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN 
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * 
 * 
 * @author  Nathan Tippy <tippyn@ociweb.com>
 * bitcoin:1NBzAoTTf1PZpYTn7WbXDTf17gddJHC8eY?amount=0.01&message=PFAT%20donation
 *
 */
package com.ociweb.purefat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;

import com.ociweb.purefat.impl.DualAuditTrail;
import com.ociweb.purefat.impl.FunctionAuditTrail;
import com.ociweb.purefat.impl.FunctionAuditTrailExternal;
import com.ociweb.purefat.impl.FunctionAuditTrailInternal;
import com.ociweb.purefat.impl.PFDefault;
import com.ociweb.purefat.impl.PFImpl;
import com.ociweb.purefat.impl.PFNone;
import com.ociweb.purefat.impl.PFVerbose;

public class PureFAT {
    
    //System property to prevent any storage or checking
    private final static String PUREFAT_NONE_KEY = "purefat.none";
    
    //System property to select internal (in memory) setup
    private final static String PUREFAT_INTERNAL_KEY = "purefat.internal";
    
    //System property to select external (log file) setup
    private final static String PUREFAT_EXTERNAL_KEY = "purefat.external";
    
    //System property to select verbose logging of errors without assertions
    private final static String PUREFAT_VERBOSE_KEY = "purefat.verbose";
    
 //   static final ch.qos.logback.classic.Logger logger = 
 //           (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(PureFAT.class);
    
    //package protected because this is the logger used for writing all the 
    //expressions. Writing expressions externally requires debug level.
    static final Logger logger = LoggerFactory.getLogger(PureFAT.class);
    private static final PFImpl pf = chooseImpl();
    
    
    private static PFImpl chooseImpl() {

        
        ///////////////
        //do nothing
        //in memory
        //external log
        //both
        /////////////
        
        if (System.getProperties().containsKey(PUREFAT_NONE_KEY)) {
            return new PFNone();
        } 
        
        FunctionAuditTrail fat;
        boolean isInternal = System.getProperties().containsKey(PUREFAT_INTERNAL_KEY);
        boolean isExternal = System.getProperties().containsKey(PUREFAT_EXTERNAL_KEY);
        
        if (isInternal) {
            if (isExternal) {
                fat = new DualAuditTrail(new FunctionAuditTrailInternal(),new FunctionAuditTrailExternal(logger));
            } else {
                fat = new FunctionAuditTrailInternal();
            }
        } else {
            if (isExternal) {
                fat = new FunctionAuditTrailExternal(logger);
            } else {
                //if nothing is set use both (default)
                fat = new DualAuditTrail(new FunctionAuditTrailInternal(),new FunctionAuditTrailExternal(logger));
            }
        }
        
        ////////////////
        //always on OR use assert
        ///////////////
        if (System.getProperties().containsKey(PUREFAT_VERBOSE_KEY)) {
            return new PFVerbose(fat);
        }
        return new PFDefault(fat);
    }
    
    public static final void auditIsPositiveRadian(Number number) {
        pf.auditIsPositiveRadian(number);
    }
    
    public static final void auditIsTightRadian(Number number) {
        pf.auditIsTightRadian(number);
    }
    
    public static final void auditIsFinite(Number number) {
        pf.auditIsFinite(number);
    }
    
    public static final void auditIsGT(Number number,Number gt) {
        pf.auditIsGT(number, gt);
    }
    
    public static final void auditIsGTE(Number number,Number gte) {
        pf.auditIsGTE(number, gte);
    }
    
    public static final void auditIsLT(Number number,Number lt) {
        pf.auditIsLT(number, lt);
    }
    
    public static final void auditIsLTE(Number number,Number lte) {
        pf.auditIsLTE(number, lte);
    }
    
    public static final void auditIsNear(Number number, Number near, double epsilon) {
        pf.auditIsNear(number, near, epsilon);
    }

    public static final void auditIsNotZero(Number number) {
        pf.auditIsNotZero(number);
    }
    
    public static final void auditIsPositive(Number number) {
        pf.auditIsPositive(number);
    }
    
    public static final void logAuditTrail(Number keyNumber, FATTemplate format) {
        pf.logAuditTrail(keyNumber, format);
    }
    
    public static final Double audit(double value, String label) {
        Double boxed = new Double(value);
        pf.audit(boxed, label);
        return boxed;
    }

    public static final Integer audit(int value, String label) {
        Integer boxed = new Integer(value);
        pf.audit(boxed, label);
        return boxed;
    }

    public static final <T extends Number> T audit(T boxed, String label) {
        pf.audit(boxed, label);
        return boxed;
    }

    public static final Double audit(double value, String label, String expressionText) {
        Double boxed = new Double(value);
        pf.audit(boxed, label, expressionText);
        return boxed;
    }
    
    public static final Double audit(double value, String label, String expressionText, Number p1) {
        Double boxed = new Double(value);
        pf.audit(boxed, label, expressionText, p1);
        return boxed;
    }

    public static final Double audit(double value, String label, String expressionText, Number p1, Number p2) {
        Double boxed = new Double(value);
        pf.audit(boxed, label, expressionText, p1, p2);
        return boxed;
    }

    public static final Double audit(double value, String label, String expressionText, Number p1, Number p2, Number p3) {
        Double boxed = new Double(value);
        pf.audit(boxed, label, expressionText, p1, p2, p3);
        return boxed;
    }

    public static final Double audit(double value, String label, String expressionText, Number p1, Number p2, Number p3, Number p4) {
        Double boxed = new Double(value);
        pf.audit(boxed, label, expressionText, p1, p2, p3, p4);
        return boxed;
    }

    public static final Double audit(double value, String label, String expressionText, Number p1, Number p2, Number p3, Number p4, Number p5) {
        Double boxed = new Double(value);
        pf.audit(boxed, label, expressionText, p1, p2 ,p3, p4, p5);
        return boxed;
    }

    public static final Double audit(double value, String label, String expressionText, Number p1, Number p2, Number p3, Number p4, Number p5, Number p6) {
        Double boxed = new Double(value);
        pf.audit(boxed, label, expressionText, p1, p2, p3, p4, p5, p6);
        return boxed;
    }

    public static final Double audit(double value, String label, String expressionText, Number p1, Number p2, Number p3, Number p4, Number p5, Number p6, Number p7) {
        Double boxed = new Double(value);
        pf.audit(boxed, label, expressionText, p1, p2, p3, p4, p5, p6, p7);
        return boxed;
    }
    
    public static final Double audit(double value, String label, String expressionText, Number[] params) {
        Double boxed = new Double(value);
        pf.audit(boxed, label, expressionText, params);
        return boxed;
    }
    
    public static final Integer audit(int value, String label, String expressionText, Number p1) {
        Integer boxed = new Integer(value);
        pf.audit(boxed, label, expressionText, p1);
        return boxed;
    }

    public static final Integer audit(int value, String label, String expressionText, Number p1, Number p2) {
        Integer boxed = new Integer(value);
        pf.audit(boxed, label, expressionText, p1, p2);
        return boxed;
    }

    public static final Integer audit(int value, String label, String expressionText, Number p1, Number p2, Number p3) {
        Integer boxed = new Integer(value);
        pf.audit(boxed, label, expressionText, p1, p2, p3);
        return boxed;
    }

    public static final Integer audit(int value, String label, String expressionText, Number p1, Number p2, Number p3, Number p4) {
        Integer boxed = new Integer(value);
        pf.audit(boxed, label, expressionText, p1, p2, p3, p4);
        return boxed;
    }

    public static final Integer audit(int value, String label, String expressionText, Number p1, Number p2, Number p3, Number p4, Number p5) {
        Integer boxed = new Integer(value);
        pf.audit(boxed, label, expressionText, p1, p2 ,p3, p4, p5);
        return boxed;
    }

    public static final Integer audit(int value, String label, String expressionText, Number p1, Number p2, Number p3, Number p4, Number p5, Number p6) {
        Integer boxed = new Integer(value);
        pf.audit(boxed, label, expressionText, p1, p2, p3, p4, p5, p6);
        return boxed;
    }

    public static final Integer audit(int value, String label, String expressionText, Number p1, Number p2, Number p3, Number p4, Number p5, Number p6, Number p7) {
        Integer boxed = new Integer(value);
        pf.audit(boxed, label, expressionText, p1, p2, p3, p4, p5, p6, p7);
        return boxed;
    }
    
    public static final Integer audit(int value, String label, String expressionText, Number[] params) {
        Integer boxed = new Integer(value);
        pf.audit(boxed, label, expressionText, params);
        return boxed;
    }
    
    public static final <T extends Number> T auditNumber(T boxed, String label, String expressionText, Number p1) {
        pf.audit(boxed, label, expressionText, p1);
        return boxed;
    }

    public static final <T extends Number> T auditNumber(T boxed, String label, String expressionText, Number p1, Number p2) {
        pf.audit(boxed, label, expressionText, p1, p2);
        return boxed;
    }

    public static final <T extends Number> T auditNumber(T boxed, String label, String expressionText, Number p1, Number p2, Number p3) {
        pf.audit(boxed, label, expressionText, p1, p2, p3);
        return boxed;
    }

    public static final <T extends Number> T auditNumber(T boxed, String label, String expressionText, Number p1, Number p2, Number p3, Number p4) {
        pf.audit(boxed, label, expressionText, p1, p2, p3, p4);
        return boxed;
    }

    public static final <T extends Number> T auditNumber(T boxed, String label, String expressionText, Number p1, Number p2, Number p3, Number p4, Number p5) {
        pf.audit(boxed, label, expressionText, p1, p2 ,p3, p4, p5);
        return boxed;
    }

    public static final <T extends Number> T auditNumber(T boxed, String label, String expressionText, Number p1, Number p2, Number p3, Number p4, Number p5, Number p6) {
        pf.audit(boxed, label, expressionText, p1, p2, p3, p4, p5, p6);
        return boxed;
    }

    public static final <T extends Number> T auditNumber(T boxed, String label, String expressionText, Number p1, Number p2, Number p3, Number p4, Number p5, Number p6, Number p7) {
        pf.audit(boxed, label, expressionText, p1, p2, p3, p4, p5, p6, p7);
        return boxed;
    }
    
    public static final <T extends Number> T auditNumber(T boxed, String label, String expressionText, Number[] params) {
        pf.audit(boxed, label, expressionText, params);
        return boxed;
    }
    
    public static final void continueAuditTo(String channelId, Number boxed) {
        pf.continueAuditTo(channelId,boxed);
    }
    
    public static final Double continueAuditFrom(String channelId, double value) {
        Double boxed = new Double(value);
        pf.continueAuditFrom(channelId, boxed);
        return boxed;
    }
    
    public static final Integer continueAuditFrom(String channelId, int value) {
        Integer boxed = new Integer(value);
        pf.continueAuditFrom(channelId, boxed);
        return boxed;
    }
    
    public static final Number continueAuditNumberFrom(String channelId, Number boxed) {
        pf.continueAuditFrom(channelId, boxed);
        return boxed;
    }
    
}
