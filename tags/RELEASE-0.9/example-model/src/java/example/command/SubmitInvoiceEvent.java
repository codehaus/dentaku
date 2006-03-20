/*
 * SubmitInvoiceEvent.java
 * Copyright 2004-2004 Bill2, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package example.command;

import example.entity.Country;
//import example.entity.CountryFactory;
import example.entity.Invoice;
import org.dentaku.services.container.ContainerManager;
import org.dentaku.services.persistence.PersistenceManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Date;

public class SubmitInvoiceEvent extends SubmitInvoiceEventBase {
    private static Log log = LogFactory.getLog(SubmitInvoiceEvent.class);
    public boolean execute(Object o) throws Exception {
        boolean result = true;
        PersistenceManager pm = (PersistenceManager) ContainerManager.getInstance().getContainer().lookup(PersistenceManager.ROLE);
        try {
            Invoice inv = getInvoice();

            // relate the country
//            CountryFactory countryFactory = (CountryFactory) pm.getPersistenceFactory(Country.class.getName());
            Country c = null; // (Country)countryFactory.findByCountryAbbr("US").iterator().next();
            //YUCK
            inv.getBillAddress().setCountry(c);
            inv.getShipAddress().setCountry(c);

            inv.setTxnDate(new Date());

            // now save everything out
            // BRAIN DAMAGE: need to go through and find records that already exist in the DB instead of duplicating them
            pm.saveOrUpdate(inv.getBillAddress());
            pm.saveOrUpdate(inv.getShipAddress());
            pm.saveOrUpdate(inv.getCreditCard());
            pm.saveOrUpdate(inv);
        } catch (Exception e) {
            e.printStackTrace();
            pm.rollback();
            result = false;
        } finally {
            pm.releaseSession();
        }
        return result;
    }
}
