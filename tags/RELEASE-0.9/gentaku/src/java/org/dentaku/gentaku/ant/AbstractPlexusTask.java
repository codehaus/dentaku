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
package org.dentaku.gentaku.ant;

import org.apache.tools.ant.AntClassLoader;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.codehaus.classworlds.ClassWorld;
import org.codehaus.plexus.embed.Embedder;

import java.io.StringReader;

public abstract class AbstractPlexusTask extends Task {
    protected PlexusAntConfig config = null;
    private Embedder embedder;

    protected abstract String getRole();

    public Object getComponent() throws Exception {
        return embedder.lookup(getRole());
    }

    public void addPlexus(PlexusAntConfig p) {
        config = p;
    }

    public void execute() throws BuildException {
        embedder = new Embedder();
        AntClassLoader cl = (AntClassLoader) embedder.getClass().getClassLoader();
        ClassWorld classWorld = new ClassWorld("plexus.core", cl);
        try {
            embedder.setConfiguration(new StringReader(config.getConfig()));
            embedder.start(classWorld);
        } catch (Exception e) {
            throw new BuildException("container configuration problem", e);
        }
    }

}
