/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package org.dentaku.foundation.connector.ejb;

/**
 * Session layer for EJBConnector.
 * @clover ///CLOVER:OFF
 */
public class EJBConnectorSession
   extends org.dentaku.foundation.connector.ejb.EJBConnectorBean
   implements javax.ejb.SessionBean
{
   public void ejbActivate() 
   {
   }

   public void ejbPassivate() 
   {
   }

   public void setSessionContext(javax.ejb.SessionContext ctx) 
   {
   }

   public void unsetSessionContext() 
   {
   }

   public void ejbRemove() throws javax.ejb.EJBException
   {
      super.ejbRemove();
   }

}
