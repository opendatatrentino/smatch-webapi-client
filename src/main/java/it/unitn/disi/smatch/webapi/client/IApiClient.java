/*******************************************************************************
 * Copyright 2012-2013 University of Trento - Department of Information
 * Engineering and Computer Science (DISI)
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser General Public License
 * (LGPL) version 2.1 which accompanies this distribution, and is available at
 *
 * http://www.gnu.org/licenses/lgpl-2.1.html
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 ******************************************************************************/ 

package it.unitn.disi.smatch.webapi.client;

import it.unitn.disi.smatch.webapi.model.smatch.Correspondence;
import java.util.List;
import java.util.Locale;

/**
 * A Java interface for S-Match Web API client. It provides the methods that 
 * will be implemented by the Web API client.
 * 
 * @author Moaz Reyad <reyad@disi.unitn.it>
 * @date Jul 11, 2013
 */
public interface IApiClient {
    
    /**
     * Returns the correspondence between the source and the target contexts
     * 
     * @param sourceName - The name of the root node in the source tree
     * @param sourceNodes - Names of the source nodes under the source root node
     * @param targetName - The name of the root node in the target tree
     * @param targetNodes -Names of the target nodes under the target root node
     * 
     * @return - the correspondence
     */
    Correspondence match(String sourceName, List<String> sourceNodes,
            String targetName, List<String> targetNodes);
    
    
    /**
     * Returns the language locale which is used in the client.
     * 
     * @return - the locale
     */
    Locale getLocale();

    /**
     * Returns a string path like "http://host:port"
     *
     * @return a string path like "http://host:port"
     */
    String getServerPath();
}
