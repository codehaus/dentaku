/**
 *
 *  Copyright 2004 Brian Topping
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.dentaku.gentaku;

import org.netbeans.jmiimpl.omg.uml.foundation.core.ModelElementImpl;
import org.omg.uml.foundation.core.Attribute;
import org.omg.uml.foundation.core.Method;

import java.io.IOException;
import java.io.StreamTokenizer;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Map;
import java.util.LinkedHashMap;

public abstract class GentakuTagImpl  {
    private ModelElementImpl element;
    private String tagName;
    private String tagValue;
    private int tagLineNumber;
    private boolean isOnConstructor;
    private boolean isOnMethod;
    private boolean isOnField;
    private boolean isOnClass;
    private String[] parameters;
    private Map namedParameters;

    protected GentakuTagImpl(String name, String value, ModelElementImpl context, int lineNumber) {
        tagName = name;
        tagValue = value;
        tagLineNumber = lineNumber;
        if (context instanceof Method) {
            Method method = (Method) context;
            isOnConstructor = context.getStereotypeNames().contains("Constructor");
            isOnMethod = !isOnConstructor;
        } else if (context instanceof Attribute) {
            isOnField = true;
        } else {
            isOnClass = true;
        }
        validateLocation();
    }

    protected abstract void validateLocation();

    public final void bomb(String message) {
        throw new RuntimeException("@" + getName() + " " + getValue() + ":\n" + message);
    }

    public String getName() {
        return tagName;
    }

    public String getValue() {
        return tagValue;
    }

    public String[] getParameters() {
        if (parameters == null) {
            StreamTokenizer tokenizer = makeTokenizer(tagValue);
            ArrayList wordList = new ArrayList();
            try {
                while (tokenizer.nextToken() != StreamTokenizer.TT_EOF) {
                    if (tokenizer.sval == null) {
                        wordList.add(Character.toString((char) tokenizer.ttype));
                    } else {
                        wordList.add(tokenizer.sval);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("error tokenizing tag");
            }
            parameters = new String[wordList.size()];
            wordList.toArray(parameters);
        }
        return parameters;
    }

    public Map getNamedParameterMap() {
        if (namedParameters == null) {
            namedParameters = new LinkedHashMap();
            StreamTokenizer tokenizer = makeTokenizer(tagValue);
            try {
                while (tokenizer.nextToken() == StreamTokenizer.TT_WORD) {
                    String key = tokenizer.sval;
                    if (tokenizer.nextToken() != '=') {
                        break;
                    }
                    switch (tokenizer.nextToken()) {
                        case StreamTokenizer.TT_WORD:
                        case '"':
                        case '\'':
                            namedParameters.put(key, tokenizer.sval);
                        default:
                            break;
                    }
                }
            } catch (IOException e) {
                // ignore
            }
        }
        return namedParameters;
    }

    public String getNamedParameter(String key) {
        return (String) getNamedParameterMap().get(key);
    }

    public int getLineNumber() {
        return tagLineNumber;
    }

    protected boolean isOnConstructor() {
        return isOnConstructor;
    }

    protected void setOnConstructor(boolean onConstructor) {
        isOnConstructor = onConstructor;
    }

    protected boolean isOnMethod() {
        return isOnMethod;
    }

    protected void setOnMethod(boolean onMethod) {
        isOnMethod = onMethod;
    }

    protected boolean isOnField() {
        return isOnField;
    }

    protected void setOnField(boolean onField) {
        isOnField = onField;
    }

    protected boolean isOnClass() {
        return isOnClass;
    }

    protected void setOnClass(boolean onClass) {
        isOnClass = onClass;
    }

    static StreamTokenizer makeTokenizer(String tagValue) {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader(tagValue));
        tokenizer.resetSyntax();
        tokenizer.wordChars('A', 'Z');
        tokenizer.wordChars('a', 'z');
        tokenizer.wordChars('0', '9');
        tokenizer.wordChars('-', '-');
        tokenizer.wordChars('_', '_');
        tokenizer.wordChars('.', '.');
        tokenizer.quoteChar('\'');
        tokenizer.quoteChar('"');
        tokenizer.whitespaceChars(' ', ' ');
        tokenizer.whitespaceChars('\n', '\n');
        tokenizer.whitespaceChars('\r', '\r');
        return tokenizer;
    }

}
