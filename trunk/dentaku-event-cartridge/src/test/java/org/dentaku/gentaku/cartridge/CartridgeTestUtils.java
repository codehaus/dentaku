/*
 * CartridgeTestUtils.java
 * Copyright 2002-2004 Bill2, Inc.
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
package org.dentaku.gentaku.cartridge;

import junit.framework.Assert;

import java.net.URL;

public class CartridgeTestUtils {
    public static URL getResourceURLRelativeToParentPackage(Class clazz, String resourceName) {
        String className = clazz.getName();
        String packageName = className.substring(0, className.lastIndexOf('.'));
        packageName = packageName.substring(0, packageName.lastIndexOf('.'));
        String resourcePath = "/" + packageName.replace('.', '/' ) + "/" + resourceName;
        URL resource = clazz.getResource(resourcePath);
        Assert.assertNotNull("Resource not found at path: " + resourcePath, resource);
        return resource;
    }
}
