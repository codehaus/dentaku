/*
 * Created on Jan 21, 2005
 *
 * Copyright STPenable Ltd. (c) 2004
 * 
 */
package com.stpenable.dentaku;

import java.io.File;
import java.net.MalformedURLException;

import org.dentaku.gentaku.cartridge.core.POJOPlugin;
import org.dentaku.gentaku.cartridge.event.EventBasePlugin;
import org.dentaku.gentaku.cartridge.summit.BasePullToolPlugin;
import org.dentaku.gentaku.cartridge.summit.CrudActionPlugin;
import org.dentaku.gentaku.cartridge.summit.PullToolPlugin;
import org.dentaku.gentaku.cartridge.summit.VelocityFormPlugin;
import org.dentaku.gentaku.tools.cgen.plugin.GenGenPlugin;
import org.dentaku.services.metadata.JMICapableMetadataProvider;
import org.dentaku.services.metadata.JMIUMLMetadataProvider;
import org.dentaku.services.metadata.RepositoryReader;
import org.dentaku.services.metadata.Utils;
import org.dentaku.services.metadata.nbmdr.MagicDrawRepositoryReader;
import org.generama.VelocityTemplateEngine;
import org.generama.defaults.FileWriterMapper;

import junit.framework.TestCase;

/**
 * @author David Wynter
 *
 * Add comment here
 */
public class SummitWebappTest extends TestCase {

    private static BasePullToolPlugin plugin1;
    private static PullToolPlugin plugin2;
    private static CrudActionPlugin plugin3;
    private static VelocityFormPlugin plugin4;
    private static POJOPlugin plugin5;
    private static GenGenPlugin plugin6;
    private static EventBasePlugin plugin7;
    private static VelocityTemplateEngine vte;
    private static FileWriterMapper wm;

	public static void main(String[] args) {
	}

	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
        String filename = "example-summit-webapp/src/uml/SummitTestModel.xml.zip";
        RepositoryReader rr = null;
		try {
			rr = new MagicDrawRepositoryReader(Utils.checkURL(new File(Utils.getRootDir(), filename).toURL()));
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JMICapableMetadataProvider mp = new JMIUMLMetadataProvider(rr);
        vte = new VelocityTemplateEngine();
        wm = new FileWriterMapper();
        plugin6 = new GenGenPlugin(mp);
        plugin5 = new POJOPlugin(vte, mp, wm);
        plugin7 = new EventBasePlugin(vte, mp, wm);
        plugin1 = new BasePullToolPlugin(vte, mp, wm);
        plugin2 = new PullToolPlugin(vte, mp, wm);
        plugin3 = new CrudActionPlugin(vte, mp, wm);
        plugin4 = new VelocityFormPlugin(vte, mp, wm);
	}

    public void testAll()
    {
        plugin6.start();
        plugin5.start();
        plugin7.start();
        plugin1.start();
        plugin2.start();
        plugin3.start();
        plugin4.start();
        assertTrue(true);
    }
	
}
