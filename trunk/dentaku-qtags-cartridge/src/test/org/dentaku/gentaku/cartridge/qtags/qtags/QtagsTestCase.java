package org.dentaku.gentaku.cartridge.qtags.qtags;

import com.thoughtworks.qdox.model.AbstractJavaEntity;
import com.thoughtworks.qdox.model.JavaClass;
import com.thoughtworks.qdox.model.JavaField;
import com.thoughtworks.qdox.model.JavaMethod;
import com.thoughtworks.qdox.model.JavaSource;
import junit.framework.TestCase;
import org.xdoclet.XDocletTag;

import java.util.Arrays;
import java.util.List;

/**
 * test suite for qtags capabilities in metacircular way.
 *
 * @author Konstantin Pribluda
 */
public class QtagsTestCase { //extends TestCase {
//    private JavaClass clazz;
//    private JavaMethod method;
//    private JavaMethod constructor;
//    private JavaField field;
//
//    protected void setUp() throws Exception {
//        super.setUp();
//        clazz = new JavaClass(new JavaSource());
//        method = new JavaMethod(clazz);
//        constructor = new JavaMethod(clazz);
//        constructor.setConstructor(true);
//        field = new JavaField(clazz);
//    }
//
//    /**
//     * qtags.allowed-value can be used on class or method level
//     */
//    public void testQtagsAllowedValueTag() throws Exception {
//        allowedValueTag(method);
//        allowedValueTag(clazz);
//        try {
//            allowedValueTag(field);
//            fail();
//        } catch (RuntimeException expected) {
//        }
//        try {
//            allowedValueTag(constructor);
//            fail();
//        } catch (RuntimeException ex) {
//        }
//    }
//
//    private XDocletTag aliasTag(AbstractJavaEntity entity) {
//        return new QtagsAliasTagImpl(QtagsAliasTagImpl.NAME, " blurge dsdh dsjk sdjk hdjkfh s", entity, 22);
//    }
//
//    private XDocletTag allowedValueTag(AbstractJavaEntity entity) {
//        return new QtagsAllowedValueTagImpl(QtagsAllowedValueTagImpl.NAME, " blurge dsdh dsjk sdjk hdjkfh s", entity, 22);
//    }
//
//    private XDocletTag defaultTag(AbstractJavaEntity entity) {
//        return new QtagsDefaultTagImpl(QtagsDefaultTagImpl.NAME, " blurge dsdh dsjk sdjk hdjkfh s", entity, 22);
//    }
//
//    private XDocletTag deprecatedTag(AbstractJavaEntity entity) {
//        return new QtagsDeprecatedTagImpl(QtagsDeprecatedTagImpl.NAME, " blurge dsdh dsjk sdjk hdjkfh s", entity, 22);
//    }
//
//    private XDocletTag ignoreTag(AbstractJavaEntity entity) {
//        return new QtagsIgnoreTagImpl(QtagsIgnoreTagImpl.NAME, " blurge dsdh dsjk sdjk hdjkfh s", entity, 22);
//    }
//
//    private XDocletTag locationTag(AbstractJavaEntity entity, String value) {
//        return new QtagsLocationTagImpl(QtagsLocationTagImpl.NAME, value, entity, 22);
//    }
//
//    private XDocletTag onceTag(AbstractJavaEntity entity) {
//        return new QtagsOnceTagImpl(QtagsOnceTagImpl.NAME, " blurge dsdh dsjk sdjk hdjkfh s", entity, 22);
//    }
//
//    private XDocletTag requiredTag(AbstractJavaEntity entity) {
//        return new QtagsRequiredTagImpl(QtagsRequiredTagImpl.NAME, " blurge dsdh dsjk sdjk hdjkfh s", entity, 22);
//    }
//
//    public void testQtagsRequiredTagShouldOnlyBeAllowedOnMethodOnce() throws Exception {
//        XDocletTag tagOne = requiredTag(method);
//        XDocletTag tagTwo = requiredTag(method);
//        List tags = Arrays.asList(new XDocletTag[]{tagOne, tagTwo});
//        try {
//            method.setTags(tags);
////            fail("only one is allowed");
//        } catch (RuntimeException expected) {
//        }
//
//        try {
//            requiredTag(clazz);
//            fail();
//        } catch (RuntimeException expected) {
//        }
//        try {
//            requiredTag(field);
//            fail();
//        } catch (RuntimeException expected) {
//        }
//        try {
//            requiredTag(constructor);
//            fail();
//        } catch (RuntimeException expected) {
//        }
//    }
//
//    public void testQtagsDefaultTagShouldOnlyBeAllowedOnMethodOnce() throws Exception {
//        XDocletTag tagOne = defaultTag(method);
//        XDocletTag tagTwo = defaultTag(method);
//        List tags = Arrays.asList(new XDocletTag[]{tagOne, tagTwo});
//        try {
//            method.setTags(tags);
////            fail("only one is allowed");
//        } catch (RuntimeException expected) {
//        }
//
//        try {
//            defaultTag(clazz);
//            fail();
//        } catch (RuntimeException expected) {
//        }
//        try {
//            defaultTag(field);
//            fail();
//        } catch (RuntimeException expected) {
//        }
//        try {
//            defaultTag(constructor);
//            fail();
//        } catch (RuntimeException expected) {
//        }
//    }
//
//    public void testQtagsDeprecatedTagShouldOnlyBeAllowedOnClassOnce() throws Exception {
//        XDocletTag tagOne = deprecatedTag(clazz);
//        XDocletTag tagTwo = deprecatedTag(clazz);
//        List tags = Arrays.asList(new XDocletTag[]{tagOne, tagTwo});
//        try {
//            clazz.setTags(tags);
////            fail("only one is allowed");
//        } catch (RuntimeException expected) {
//        }
//
//        try {
//            deprecatedTag(method);
//            fail();
//        } catch (RuntimeException expected) {
//        }
//        try {
//            deprecatedTag(field);
//            fail();
//        } catch (RuntimeException expected) {
//        }
//        try {
//            deprecatedTag(constructor);
//            fail();
//        } catch (RuntimeException expected) {
//        }
//    }
//
//    public void testQtagsIgnoreTagShouldOnlyBeAllowedOnClassOnce() throws Exception {
//        XDocletTag tagOne = ignoreTag(clazz);
//        XDocletTag tagTwo = ignoreTag(clazz);
//        List tags = Arrays.asList(new XDocletTag[]{tagOne, tagTwo});
//        try {
//            clazz.setTags(tags);
////            fail("only one is allowed");
//        } catch (RuntimeException expected) {
//        }
//
//        try {
//            ignoreTag(method);
//            fail();
//        } catch (RuntimeException expected) {
//        }
//        try {
//            ignoreTag(field);
//            fail();
//        } catch (RuntimeException expected) {
//        }
//        try {
//            ignoreTag(constructor);
//            fail();
//        } catch (RuntimeException expected) {
//        }
//    }
//
//    public void testQtagsOnceTagShouldOnlyBeAllowedOnClassOnce() throws Exception {
//        XDocletTag tagOne = onceTag(clazz);
//        XDocletTag tagTwo = onceTag(clazz);
//        List tags = Arrays.asList(new XDocletTag[]{tagOne, tagTwo});
//        try {
//            clazz.setTags(tags);
////            fail("only one is allowed");
//        } catch (RuntimeException expected) {
//        }
//
//        try {
//            onceTag(method);
//            fail();
//        } catch (RuntimeException expected) {
//        }
//        try {
//            onceTag(field);
//            fail();
//        } catch (RuntimeException expected) {
//        }
//        try {
//            onceTag(constructor);
//            fail();
//        } catch (RuntimeException expected) {
//        }
//    }
//
//    public void testQtagsLocationTagShouldOnlyBeAllowedOnClassWithConstrainedValues() throws Exception {
//        XDocletTag tagOne = locationTag(clazz, "class");
//        XDocletTag tagTwo = locationTag(clazz, "method");
//        XDocletTag tagThree = locationTag(clazz, "field");
//        XDocletTag tagFour = locationTag(clazz, "constructor");
//
//        try {
//            locationTag(clazz, "blah");
//            fail();
//        } catch (RuntimeException expected) {
//        }
//
//        List tags = Arrays.asList(new XDocletTag[]{tagOne, tagTwo, tagThree, tagFour});
//        try {
//            clazz.setTags(tags);
////            fail("only one is allowed");
//        } catch (RuntimeException expected) {
//        }
//
//        try {
//            locationTag(method, "method");
//            fail();
//        } catch (RuntimeException expected) {
//        }
//        try {
//            locationTag(field, "field");
//            fail();
//        } catch (RuntimeException expected) {
//        }
//        try {
//            locationTag(constructor, "constructor");
//            fail();
//        } catch (RuntimeException expected) {
//        }
//    }

}