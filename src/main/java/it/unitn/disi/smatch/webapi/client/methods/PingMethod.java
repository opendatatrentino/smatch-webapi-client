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

/**
 * PingMethod.java
 */
package it.unitn.disi.smatch.webapi.client.methods;

import it.unitn.disi.smatch.webapi.model.Configuration;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.log4j.Logger;

/**
 * A method to ping the server to check if it responds
 *
 * @author Sergey Kanshin, kanshin@disi.unitn.it
 * @date Jul 23, 2010
 */
public class PingMethod {

    private Logger logger = Logger.getLogger(getClass());

    private HttpClient httpClient;

    private String methodUrl;

    public PingMethod(HttpClient httpClient, String host, String port) {
        this.httpClient = httpClient;
        methodUrl = "http://" + host;
        methodUrl += ":" + port;
        String root = Configuration.getString("smatch.webapi.root");
        if (root == null) {
            root = "";
        }
        methodUrl += root;
        methodUrl += Configuration.getString("smatch.webapi.ping.method");
    }

    public boolean ping() {
        GetMethod method = new GetMethod(methodUrl);
        try {
            logger.debug("Send ping");
            int status = httpClient.executeMethod(method);
            logger.debug("Response status = " + status);
            if (status != HttpStatus.SC_OK) {
                logger.warn("Method " + methodUrl + " failed, status: "
                        + method.getStatusText());
                return false;
            }
            byte[] responseBody = method.getResponseBody();
            if (null == responseBody || responseBody.length == 0) {
                logger.warn("Response body is empty");
                return false;
            }
            String responseStr = new String(responseBody);
            String text = responseStr.toLowerCase();
            if (text.indexOf("pong") >= 0) {
                logger.debug("pong received");
                return true;
            } else {
                logger.warn("pong not received");
                return false;
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            logger.debug("pong not received");
            return false;
        }
    }
}
