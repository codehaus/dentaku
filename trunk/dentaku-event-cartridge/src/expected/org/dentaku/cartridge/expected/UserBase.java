/**
 * Generated file. Do not edit.
 */
package org.dentaku.cartridge.expected;


/**
  * @hibernate.class table="User"
  */
public abstract class UserBase
        extends org.dentaku.services.persistence.ModelEntity
        implements org.dentaku.services.persistence.XMLBean {
    public void getXML(org.dom4j.Element el) throws org.dentaku.services.exception.XMLBeanException {
        try {
            org.dom4j.Element beanElement = el.addElement("User");
            org.dentaku.services.persistence.XMLUtil.addChild(beanElement, "id", org.apache.commons.beanutils.BeanUtils.getSimpleProperty(this, "id"));
            org.dentaku.services.persistence.XMLUtil.addChild(beanElement, "username", org.apache.commons.beanutils.BeanUtils.getSimpleProperty(this, "username"));
            org.dentaku.services.persistence.XMLUtil.addChild(beanElement, "password", org.apache.commons.beanutils.BeanUtils.getSimpleProperty(this, "password"));
            org.dentaku.services.persistence.XMLUtil.addChild(beanElement, "notes", org.apache.commons.beanutils.BeanUtils.getSimpleProperty(this, "notes"));
        } catch (Exception e) {
            throw new org.dentaku.services.exception.XMLBeanException("error creating DOM", e);
        }
    }

    public void setXML(org.dom4j.Element el) throws org.dentaku.services.exception.XMLBeanException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    // --------------- attributes ---------------------
    private java.lang.Long id;

   /**
    *
    * @hibernate.id generator-class="native"
    *     column="id"
    * @hibernate.column name="id" sql-type="BIGINT"
    */
    public java.lang.Long getId()
    {
        return this.id;
    }

    public void setId(java.lang.Long newValue)
    {
        this.id = newValue;
    }


    private java.lang.String username;

   /**
    *
    * @hibernate.property
    *     column="username"
    * @hibernate.column name="username" sql-type="VARCHAR(255)"
    */
    public java.lang.String getUsername()
    {
        return this.username;
    }

    public void setUsername(java.lang.String newValue)
    {
        this.username = newValue;
    }


    private java.lang.String password;

   /**
    *
    * @hibernate.property
    *     column="password"
    * @hibernate.column name="password" sql-type="VARCHAR(255)"
    */
    public java.lang.String getPassword()
    {
        return this.password;
    }

    public void setPassword(java.lang.String newValue)
    {
        this.password = newValue;
    }


    private java.lang.String notes;

   /**
    *
    * @hibernate.property
    *     column="notes"
    * @hibernate.column name="notes" sql-type="VARCHAR(255)"
    */
    public java.lang.String getNotes()
    {
        return this.notes;
    }

    public void setNotes(java.lang.String newValue)
    {
        this.notes = newValue;
    }



    // ------------- relations ------------------

           /**
    * multiplicity:     Multiplicity$Impl  ID: .:00000000000007FC  MID: .:0000000000000662  OPCKG: .:0000000000000694
    * source navigable: true
    * target navigable: true
    *
    * @hibernate.set lazy="true"
    * @hibernate.key column="USER_FK"
    * @hibernate.one-to-many class="org.dentaku.cartridge.expected.Account"
    */
    public java.util.Collection getAccounts()
    {
        return (this.accounts);
    }

    public void setAccounts(java.util.Collection accounts)
    {
        this.accounts = accounts;
    }

    private java.util.Collection accounts;




    // ---------------- business methods  ----------------------


    // ---------------- test methods  ----------------------
    // test methods, should go away (don't put test code in production classes :-)
    private static User zeroObject;
    private static User oneObject;
    private static java.util.List oneCollection;
    private static java.util.List manyObject;

    public static Object getTestClassZero() {
        if (zeroObject == null) {
            zeroObject = new User();
            zeroObject.setId(new Long(0));
            // attributes
            zeroObject.setUsername("username");
            zeroObject.setPassword("password");
            zeroObject.setNotes("notes");
            // relations
            zeroObject.setAccounts(new java.util.ArrayList(0));

        }
        return zeroObject;
    }


    public static Object getTestClassOne() {
        if (oneObject == null) {
            oneObject = new User();
            oneObject.setId(new Long(1));
            // attributes
            oneObject.setUsername("username");
            oneObject.setPassword("password");
            oneObject.setNotes("notes");
            // relations
            oneObject.setAccounts(org.dentaku.cartridge.expected.Account.getTestClassOneCollection());
        }
        return oneObject;
    }

    public static java.util.List getTestClassOneCollection() {
        if (oneCollection == null) {
            oneCollection = new java.util.ArrayList(1);
            oneCollection.add(getTestClassOne());
        }
        return oneCollection;
    }

    public static java.util.List getTestClassMany() {
        if (manyObject == null) {
            manyObject = new java.util.ArrayList(getTestCollectionSize());
            manyObject.add(getTestClassZero());
            manyObject.add(getTestClassOne());
            for (int i = 2; i < getTestCollectionSize(); i++) {
                User obj = new User();
                obj.setId(new Long(i));
            // attributes
            obj.setUsername("username");
            obj.setPassword("password");
            obj.setNotes("notes");
            // relations
            obj.setAccounts(org.dentaku.cartridge.expected.Account.getTestClassMany());
                manyObject.add(obj);
            }
        }
        return manyObject;
    }

    public static int getTestCollectionSize() {
        return User.class.hashCode() % 100;
    }

}
