package org.dentaku.gentaku.metacartridge;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.Collection;
import java.util.Map;

import org.dentaku.services.metadata.JMICapableMetadataProvider;
import org.generama.Plugin;
import org.generama.TemplateEngine;
import org.generama.WriterMapper;
import org.generama.defaults.FileWriterMapper;
import org.omg.uml.foundation.core.ModelElement;
import org.omg.uml.foundation.datatypes.ChangeableKindEnum;
import org.omg.uml.foundation.datatypes.OrderingKindEnum;
import org.omg.uml.foundation.datatypes.ScopeKindEnum;
import org.omg.uml.foundation.datatypes.VisibilityKindEnum;

public abstract class JavaPluginBase extends org.generama.Plugin {

	protected JMIHelper jmiHelper = new JMIHelper();
	protected JavaHelper javaHelper = new JavaHelper();

    private JMICapableMetadataProvider metadataProvider;

    private boolean createonly;
    protected String[] stereotypes;

    public JavaPluginBase(String[] stereotypes, TemplateEngine templateEngine, JMICapableMetadataProvider metadataProvider, WriterMapper writerMapper) {
        super(templateEngine, metadataProvider, new CheckFileWriterMapper(writerMapper));
        this.metadataProvider = metadataProvider;
        this.setCreateonly(true);
        this.stereotypes = stereotypes;
    }

    public boolean shouldGenerate(Object metadata) {
        // It must be at least instance of ModelElement.
        if (!(metadata instanceof ModelElement)) {
            return false;
        }

        for (int i = 0; i < this.stereotypes.length; i++) {
            if (this.jmiHelper.matchesStereotype((ModelElement) metadata, this.stereotypes[i])) {
                return true;
            }
        }

        return false;
    }

    public Collection getMetadata() {
        return metadataProvider.getJMIMetadata();
    }

    protected void populateContextMap(Map ctx) {

        super.populateContextMap(ctx);
        
        // helpers
        ctx.put("jmiHelper", this.jmiHelper);
        ctx.put("javaHelper", this.javaHelper);

        // Constants for OrderingKind
        ctx.put("OK_ORDERED", OrderingKindEnum.OK_ORDERED);
        ctx.put("OK_UNORDERED", OrderingKindEnum.OK_UNORDERED);
        
        // Constants for ChangeableKind
        ctx.put("CK_ADD_ONLY", ChangeableKindEnum.CK_ADD_ONLY);
        ctx.put("CK_CHANGEABLE", ChangeableKindEnum.CK_CHANGEABLE);
        ctx.put("CK_FROZEN", ChangeableKindEnum.CK_FROZEN);

        // Constants for VisibilityKind
        ctx.put("VK_PACKAGE", VisibilityKindEnum.VK_PACKAGE);
        ctx.put("VK_PRIVATE", VisibilityKindEnum.VK_PRIVATE);
        ctx.put("VK_PROTECTED", VisibilityKindEnum.VK_PROTECTED);
        ctx.put("VK_PUBLIC", VisibilityKindEnum.VK_PUBLIC);

        // Constants for ScopeKind
        ctx.put("SK_CLASSIFIER", ScopeKindEnum.SK_CLASSIFIER);
        ctx.put("SK_INSTANCE", ScopeKindEnum.SK_INSTANCE);
      }

    public String getDestinationClassname(Object metadata) {
        String destinationFilename = this.getDestinationFilename(metadata);
        return destinationFilename.substring(0, destinationFilename.indexOf('.'));
    }

    public String getDestinationFullyQualifiedClassName(Object metadata) {
        String packageName = getDestinationPackage(metadata);
        packageName = packageName.equals("") ? "" : packageName + ".";
        return packageName + getDestinationClassname(metadata);
    }

    public boolean isCreateonly() {
        return createonly;
    }

    public void setCreateonly(boolean createOnly) {
        this.createonly = createOnly;
        ((CheckFileWriterMapper) getWriterMapper()).setCreateonly(createOnly);
    }

    private static class CheckFileWriterMapper implements WriterMapper {
        private WriterMapper delegate;
        private boolean createOnly;

        public CheckFileWriterMapper(WriterMapper delegate) {
            this.delegate = delegate;
        }

        public Writer getWriter(Object metadata, Plugin plugin) throws IOException {
            Writer result = null;
            if (this.delegate instanceof FileWriterMapper) {
                String pakkage = plugin.getDestinationPackage(metadata);
                String packagePath = pakkage.replace('.', '/');
                File dir = new File(plugin.getDestdirFile(), packagePath);
                dir.mkdirs();
                String filename = plugin.getDestinationFilename(metadata);
                File out = new File(dir, filename);
                if (!(createOnly && out.exists())) {
                    try {
                        result = new OutputStreamWriter(new FileOutputStream(out), plugin.getEncoding());
                    } catch (UnsupportedEncodingException e) {
                        throw new IOException(e.toString());
                    }
                }
            } else {
                result = delegate.getWriter(metadata, plugin);
            }
            return result;
        }

        public boolean isCreateonly() {
            return createOnly;
        }

        public void setCreateonly(boolean createOnly) {
            this.createOnly = createOnly;
        }
    }

    /**
     * <p>
     * Converts a string following the Java naming conventions to a database
     * attribute name. For example convert customerName to CUSTOMER_NAME.
     * </p>
     * 
     * @param s
     *            string to convert
     * @param separator
     *            character used to separate words
     * @return string converted to database attribute format
     */
    public static String toDatabaseAttributeName(String s, String separator) {
        StringBuffer databaseAttributeName = new StringBuffer();
        StringCharacterIterator iter = new StringCharacterIterator(lowerCaseFirstLetter(s));
        for (char character = iter.first(); character != CharacterIterator.DONE; character = iter.next()) {
            if (Character.isUpperCase(character)) {
                databaseAttributeName.append(separator);
            }
            character = Character.toUpperCase(character);
            databaseAttributeName.append(character);
        }
        return databaseAttributeName.toString();
    }

    public String fromDatabaseAttributeName(String s, String separator) {
        if (s == null) {
            return null;
        }
        String tok[] = s.split(separator);
        StringBuffer databaseAttributeName = new StringBuffer();
        databaseAttributeName.append(lowerCaseFirstLetter(tok[0]));
        for (int i = 1; i < tok.length; i++) {
            databaseAttributeName.append(upperCaseFirstLetter(tok[i]));
        }
        return databaseAttributeName.toString();
    }

    /**
     * <p>
     * Removes the capitalization of a string. That is, it returns
     * "hamburgerStall" when receiving a "HamburgerStall".
     * </p>
     * 
     * @param s
     *            the input string
     * @return String the output string.
     */
    public static String lowerCaseFirstLetter(String s) {
        if (s != null && s.length() > 0) {
            return s.substring(0, 1).toLowerCase() + s.substring(1);
        } else {
            return s;
        }
    }

    public String upperCaseFirstLetter(String s) {
        if (s != null && s.length() > 0) {
            return s.substring(0, 1).toUpperCase() + s.substring(1);
        } else {
            return s;
        }
    }

    public JMICapableMetadataProvider getMetadataProvider() {
        return this.metadataProvider;
    }
}