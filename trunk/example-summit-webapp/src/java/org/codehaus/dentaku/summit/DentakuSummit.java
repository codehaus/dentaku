/*
 * Copyright 2001-2004 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License")
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
 *
 * Created on Feb 25, 2005
 *
 * Copyright STPenable Ltd. (c) 2005
 * 
 */
package org.codehaus.dentaku.summit;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.codehaus.plexus.embed.Embedder;

/**
 * @author David Wynter
 *
 * Simple kicker-offer, for demo only, once shown it runs then maybe move it to a 
 * more suitable runtime environment
 */
public class DentakuSummit {
	
	private static Embedder embedder = new Embedder();
	
	public DentakuSummit() throws MalformedURLException, IOException, Exception {
        // Setup, configure, and start Plexus
        URL aPlexus = null;
		aPlexus = new URL("./plexus.xml");
		embedder.setConfiguration(aPlexus);
		embedder.start();

        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
            	shutdown();
            }
        });;
	}

	public static void main(String[] args) throws MalformedURLException, IOException, Exception {
		DentakuSummit started = new DentakuSummit();
	}
	
	private static void shutdown() {
        try {
			// Tell the Plexus container to shutdown
			embedder.stop();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
