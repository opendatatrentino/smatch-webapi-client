/**
 * *****************************************************************************
 * Copyright 2012-2013 University of Trento - Department of Information
 * Engineering and Computer Science (DISI)
 *
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the GNU Lesser General Public License (LGPL)
 * version 2.1 which accompanies this distribution, and is available at
 *
 * http://www.gnu.org/licenses/lgpl-2.1.html
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 *****************************************************************************
 */

/**
 * TestPing.java
 */
package it.unitn.disi.smatch.webapi.client;

import java.util.Locale;
import junit.framework.TestCase;

/**
 * Testing Ping of the server
 *
 * @author Sergey Kanshin, kanshin@disi.unitn.it
 * @date Jul 23, 2010
 */
public class TestPing extends TestCase {

    public TestPing() {
        super("Test Ping");
    }

    public void testPing() {
        WebApiClient api = WebApiClient.getInstance(Locale.ENGLISH);
        assertTrue("ping not successfull", api.ping());
    }
}
