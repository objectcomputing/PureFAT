PureFAT
=======
pure function audit trail for Java 

NOTE: Under active development so much of this is subject to change.


Overview
---------

Helps investigate the source of unexpected numeric values.  Data about each 
value is record from the point that values are sampled from feeds or sensors 
all the way to the end where rolled up summaries are displayed in a report.

In the event that a "clearly wrong" value is detected in the report an audit
trail can be generated which will show from the original inputs how that value 
was derived.  The audit trail provides something that resembles a stack trace
so an investigator can look at each line of code that contributed to the 
questionable value.

Working Features
----------------

    # Full concurrent (thread safe) usage within one JVM.
    # Can be disabled, always on or triggered by assertions.
    # Has many constraint rules which throw upon "ConstraintViolation"
    # Logs fully detailed trace for investigations off-line.
    # On demand audit trails can be produced for "questionable situations"
    # Supports internal memory mode or external slf4j mode or both.
    # Audit can be printed as detailed table, summary, expression or tree.
    # Minimal dependencies (SLF4J)
    
Example
------

    See Unit tests (still under construction)
    MotorRPMUseCase attempts to capture how one might expect to use it.
    
    Here is an example audit trail after an Infinity was detected.
    Upon constraint failure a detailed table audit trail will be generated.
    
         1024     = 1024       com.ociweb.purefat.useCase.MotorRPMUseCase.<init>(MotorRPMUseCase.java:61)           samplesPerSecond[PF5c8843dc]                                                    
         61440    = 60*1024    com.ociweb.purefat.useCase.MotorRPMUseCase.<init>(MotorRPMUseCase.java:63)           samplesPerMinute[PF761f4ff9] = 60*samplesPerSecond[PF5c8843dc]                  
         0        = 0          com.ociweb.purefat.useCase.MotorRPMUseCase.<init>(MotorRPMUseCase.java:69)           initial[PF1bba105]                                                              
         0        = (0*2)      com.ociweb.purefat.useCase.MotorRPMUseCase.simulateCompute(MotorRPMUseCase.java:93)  samplesPerRevolution[PF45485026] = (initial[PF1bba105]*2)                       
         Infinity = (61440/0)  com.ociweb.purefat.useCase.MotorRPMUseCase.simulateCompute(MotorRPMUseCase.java:95)  rpm[PFe861253] = (samplesPerMinute[PF761f4ff9]/samplesPerRevolution[PF45485026])

   Here is a summary report generated by calling logAuditTrail(result, FATTemplate.summary);
   Using summary is a good idea when the functions are deeply nested or recursive, note the execution count column.
   
        com.ociweb.purefat.useCase.MotorRPMUseCase.<init>(MotorRPMUseCase.java:61)            1    ${samplesPerSecond}                                     1024 = 1024
        com.ociweb.purefat.useCase.MotorRPMUseCase.<init>(MotorRPMUseCase.java:63)            1    ${samplesPerMinute} = 60*${samplesPerSecond}            61440 = 60*1024
        com.ociweb.purefat.useCase.MotorRPMUseCase.simulateCompute(MotorRPMUseCase.java:101)  1    ${first}                                                1 = 1
        com.ociweb.purefat.useCase.MotorRPMUseCase.simulateCompute(MotorRPMUseCase.java:88)   1    ${count} = (${first}+1)                                 2 = (1+1)
        com.ociweb.purefat.useCase.MotorRPMUseCase.simulateCompute(MotorRPMUseCase.java:88)   125  ${count} = (${count}+1)                                 
        com.ociweb.purefat.useCase.MotorRPMUseCase.simulateCompute(MotorRPMUseCase.java:93)   1    ${samplesPerRevolution} = (${count}*2)                  254 = (127*2)
        com.ociweb.purefat.useCase.MotorRPMUseCase.simulateCompute(MotorRPMUseCase.java:95)   1    ${rpm} = (${samplesPerMinute}/${samplesPerRevolution})  241.88976377952756 = (61440/254)
   
   An evaluatable version of the entire expression (suitable for google or wolfram-alpha) can be generated with logAuditTrail(result, FATTemplate.expression);
   These can quickly become very large but they can be very helpfull at times.
   
        241.88976377952756 = (60*1024/(((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((1+1)+1)+1)+1)+1)+1)+1)+1)+1)+1)+1)+1)+1)+1)+1)+1)+1)+1)+1)+1)+1)+1)+1)+1)+1)+1)+1)+1)+1)+1)+1)+1)+1)+1)+1)+1)+1)+1)+1)+1)+1)+1)+1)+1)+1)+1)+1)+1)+1)+1)+1)+1)+1)+1)+1)+1)+1)+1)+1)+1)+1)+1)+1)+1)+1)+1)+1)+1)+1)+1)+1)+1)+1)+1)+1)+1)+1)+1)+1)+1)+1)+1)+1)+1)+1)+1)+1)+1)+1)+1)+1)+1)+1)+1)+1)+1)+1)+1)+1)+1)+1)+1)+1)+1)+1)+1)+1)+1)+1)+1)+1)+1)+1)+1)+1)+1)+1)+1)+1)+1)+1)+1)+1)+1)+1)+1)*2))

Roadmap 
-------

    Unimplemented features needing developers.

        * By design the ringbuffer dynamically grows depending on the usage.
        On startup it takes a few minutes to find the optimium size. Save
        the optimum size to speed up restarts if the max RAM size did not change.
    
        * Use java.lang.instrument.ClassFileTransformer to augment any usages of
        audit() so the expression template and params are injected. The only arguments
        required in the source will be the Number result and a unique label. This
        code will also validate/ensure that the labels are unique.
    
        * Use ToolProvider.getSystemJavaCompiler() to ensure that each label
        is unique. Validating compiler to throw compile error if they are NOT. 
    
        * logback module for analysis of logged events for stack reconstruction
        off line. https://github.com/qos-ch/logback-decoder
        
        * mongoDB logback module to support analysis of multiple JVM instances 
        from one place.
        
        * Expression template validating parser.  Critical when template is not 
        injected and will also be used as a test that the injected code is valid.
        Must enforce a simple math BNF that allows Wolfram-alpha, google and other
        tools to parse and evaluate the text. Each template must also be 
        surrounded by () to ensure precedence is maintained in the nesting. 
        Evaluate to ensure the Java code does the same thing as the template.
    
        * Gather more meta data about each audited function.  Values like the 
        min, max, mean, and standard deviation of the results may be helpful.

        * Add mode to use ${myname} for named args in order to better support 
        maven, puppet, bash, ant, groovy  when requested or when unknown. 
        Insert these tags for interpolation by external tools.


Expression formatting
---------------------

     To make nesting of expressions work work the * operator must be used in all cases for multiplication.
     Expressions need to be wrapped in () to ensue consistency when injecting
     Always use {} for injection point to match slf4j conventions


