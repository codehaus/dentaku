package org.dentaku.gentaku.cartridge.qtags.impl.test;

import org.generama.QDoxCapableMetadataProvider;

/**
 * @author Aslak Helles&oslash;y
 * @version $Revision$
 */
public class TagLibrary {
    public TagLibrary(QDoxCapableMetadataProvider metadataProvider) {
        metadataProvider.getDocletTagFactory().registerTag(FooBarTagImpl.NAME,
                FooBarTagImpl.class);

    }
}
