package org.dentaku.gentaku.cartridge.qtags.impl.test;

public class FooBarTagImpl
    extends org.xdoclet.XDocletTag implements org.dentaku.gentaku.cartridge.qtags.impl.test.FooBarTag {
    public static final String NAME = "foo.bar";
    private static final java.util.List ALLOWED_PARAMETERS = java.util.Arrays
            .asList(new String[]{"apple", "grape-fruit", "funny", "age",""});
    private static final java.util.List ALLOWED_VALUES = java.util.Arrays.asList( new String[] {""});

    public FooBarTagImpl(String name, String value, com.thoughtworks.qdox.model.AbstractJavaEntity entity, int lineNumber) {
        super(name, value, entity, lineNumber);
    }

    public java.lang.String getApple() {
        boolean required = false;
        String result = getNamedParameter("apple");

        if (required && (result == null)) {
            bomb("apple=\"???\" must be specified.");
        }

        if (result != null) {
            if (!(false || result.equals("red") || result.equals("green"))) {
                // todo we should say what file and line number too
                bomb("apple=\"" + result + "\" is an invalid parameter value.");
            }
        }

	    if(result == null) {
			result = "green";
		}
        return result;
    }

    public java.lang.String getGrapeFruit() {
        boolean required = true;
        String result = getNamedParameter("grape-fruit");

        if (required && (result == null)) {
            bomb("grape-fruit=\"???\" must be specified.");
        }


        return result;
    }

    public boolean isFunny() {
        boolean required = false;
        String result = getNamedParameter("funny");

        if (required && (result == null)) {
            bomb("funny=\"???\" must be specified.");
        }

		if(result == null) {
			result="true";
		}

        return Boolean.valueOf(result).booleanValue();
    }

    public int getAge() {
        boolean required = false;
        String result = getNamedParameter("age");
        if(required && result == null) {
            bomb("age=\"???\" must be specified.");
        }
        if (result != null) {
		try {
			return Integer.decode(result).intValue();
		} catch(NumberFormatException nfe) {
			bomb("age=\"" + result + "\" is not valid integer");
			throw nfe;
		}
        } else {
            return 0;
        }
    }

    protected void validateLocation() {
        if(isOnClass()) {
            bomb("is not allowed on classes");
        }
        if(isOnField()) {
            bomb("is not allowed on fields");
        }
		// check uniqueness


        // warn deprecation
		System.err.println("@" + getName() + ":"  + getValue());

        // check for allowed values for whole tag
        if( ALLOWED_VALUES.size() > 1 && !ALLOWED_VALUES.contains(getValue())) {
            bomb( "\"" + getValue() +"\" is not a valid value. Allowed values are ");
        }

        // Verify that all parameters are known.
        final java.util.Collection parameterNames = getNamedParameterMap().keySet();
        for (java.util.Iterator iterator = parameterNames.iterator(); iterator.hasNext();) {
            String parameterName = (String) iterator.next();
            if (!ALLOWED_PARAMETERS.contains(parameterName)) {
                bomb(parameterName + " is an invalid parameter name.");
            }
        }

        // Get all the parameters to validate their contents
        getApple();
        getGrapeFruit();
        isFunny();
        getAge();
    }
}
