/*
 * Generated file - Do not edit!
 */
package org.dentaku.foundation.connector.ejb;

/**
 * Utility class for EJBConnector.
 * @clover ///CLOVER:OFF
 */
public class EJBConnectorUtil
{

   private static Object lookupHome(java.util.Hashtable environment, String jndiName, Class narrowTo) throws javax.naming.NamingException {
      // Obtain initial context
      javax.naming.InitialContext initialContext = new javax.naming.InitialContext(environment);
      try {
         Object objRef = initialContext.lookup(jndiName);
         // only narrow if necessary
         if (narrowTo.isInstance(java.rmi.Remote.class))
            return javax.rmi.PortableRemoteObject.narrow(objRef, narrowTo);
         else
            return objRef;
      } finally {
         initialContext.close();
      }
   }

   // Home interface lookup methods

   /**
    * Obtain remote home interface from default initial context
    * @return Home interface for EJBConnector. Lookup using JNDI_NAME
    */
   public static org.dentaku.foundation.connector.ejb.EJBConnectorHome getHome() throws javax.naming.NamingException
   {
         return (org.dentaku.foundation.connector.ejb.EJBConnectorHome) lookupHome(null, org.dentaku.foundation.connector.ejb.EJBConnectorHome.JNDI_NAME, org.dentaku.foundation.connector.ejb.EJBConnectorHome.class);
   }

   /**
    * Obtain remote home interface from parameterised initial context
    * @param environment Parameters to use for creating initial context
    * @return Home interface for EJBConnector. Lookup using JNDI_NAME
    */
   public static org.dentaku.foundation.connector.ejb.EJBConnectorHome getHome( java.util.Hashtable environment ) throws javax.naming.NamingException
   {
       return (org.dentaku.foundation.connector.ejb.EJBConnectorHome) lookupHome(environment, org.dentaku.foundation.connector.ejb.EJBConnectorHome.JNDI_NAME, org.dentaku.foundation.connector.ejb.EJBConnectorHome.class);
   }

   /**
    * Obtain local home interface from default initial context
    * @return Local home interface for EJBConnector. Lookup using JNDI_NAME
    */
   public static org.dentaku.foundation.connector.ejb.EJBConnectorLocalHome getLocalHome() throws javax.naming.NamingException
   {
      return (org.dentaku.foundation.connector.ejb.EJBConnectorLocalHome) lookupHome(null, org.dentaku.foundation.connector.ejb.EJBConnectorLocalHome.JNDI_NAME, org.dentaku.foundation.connector.ejb.EJBConnectorLocalHome.class);
   }

}

