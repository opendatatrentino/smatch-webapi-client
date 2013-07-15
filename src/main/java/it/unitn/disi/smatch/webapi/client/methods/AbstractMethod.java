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
 * AbstractMethod2.java
 */
package it.unitn.disi.smatch.webapi.client.methods;

import it.unitn.disi.smatch.webapi.model.Configuration;
import it.unitn.disi.smatch.webapi.client.exceptions.WebApiException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.URIException;
import org.apache.commons.httpclient.methods.ByteArrayRequestEntity;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.util.URIUtil;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Sergey Kanshin <kanshin@disi.unitn.it>
 * @version Mar 22, 2011
 *
 * @author vinay
 * @version 23-Jan-2012
 */
public abstract class AbstractMethod {

    private static final String RESPONSE = "response";
    private static final String RESPONSE_EMPTY = "Response body is empty";
    private static final String RESPONSE_STATUS = "Response status = ";
    private static final String METHOD = "Method ";
    private static final String ERROR_METHOD = "Error execution http method ";
    private static final String FAILED_STATUS = " failed, status: ";
    private static final int KB = 1024;
    private Logger logger = LoggerFactory.getLogger(getClass());
    private HttpClient httpClient;
    private String serverPath;
    private Locale locale;
    private boolean isHttp = true;
    private String root;

    /**
     * Creates a class to execute HTTP methods
     *
     * @param httpClient the HTTP client to be used while executing the methods
     * @param locale language.
     * @param serverPath the path of the server in the file system
     *
     */
    public AbstractMethod(HttpClient httpClient, Locale locale,
            String serverPath) {
        this.httpClient = httpClient;
        this.locale = locale;
        this.serverPath = serverPath;
        root = Configuration.getString("smatch.webapi.root");
        if (root == null) {
            root = "";
        }
    }

    protected JSONObject executeGetMethod(String methodPath,
            Map<String, ?> parameters) throws WebApiException {

        JSONObject jResponse = null;

        String methodPathParameter = methodPath;
        String temp = root + methodPathParameter;
        String methodUrl = serverPath + temp;
        try {
            methodUrl = URIUtil.encodeQuery(methodUrl);
        } catch (URIException e1) {
            if (logger.isDebugEnabled()) {
                logger.debug("URI exception: " + e1.getMessage(), e1);
            }
        }

        if (isHttp) {
            try {
                jResponse = executeHttpGetMethod(methodUrl, parameters);
            } catch (URIException e) {
                throw new WebApiException(e.getMessage(), e);
            }
        }

        return jResponse;
    }

    protected JSONObject executeDeleteMethod(String methodPath,
            Map<String, ?> parameters) throws WebApiException {
        String httpmethodPath = root + methodPath;

        try {
            return executeHttpDeleteMethod(httpmethodPath, parameters);
        } catch (URIException e) {
            throw new WebApiException(e.getMessage(), e);
        }
    }

    private JSONObject executeHttpGetMethod(String methodUrl, Map<String, ?> map)
            throws WebApiException, URIException {
        GetMethod getMethod = new GetMethod(methodUrl);
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        if (map != null) {
            for (String key : map.keySet()) {
                Object value = map.get(key);
                if (value == null) {
                    continue;
                }
                if (value instanceof Collection) {
                    Collection col = (Collection) value;
                    for (Object obj : col) {
                        params.add(new NameValuePair(key, obj.toString()));
                    }
                } else {
                    params.add(new NameValuePair(key, value.toString()));
                }
            }
        }
        getMethod.setQueryString(
                params.toArray(new NameValuePair[params.size()]));
        if (logger.isDebugEnabled()) {
            logger.debug("Send request: GET " + methodUrl + "?" + getMethod.getQueryString());
        }
        try {
            int status = httpClient.executeMethod(getMethod);
            logger.debug(RESPONSE_STATUS + status);
            if (status != HttpStatus.SC_OK) {
                logger.warn(METHOD + methodUrl + FAILED_STATUS
                        + getMethod.getStatusText());
                throw new WebApiException(METHOD + methodUrl
                        + FAILED_STATUS + getMethod.getStatusText());
            }
            return parseResponse(getMethod.getResponseBodyAsStream());
        } catch (Exception e) {
            logger.error(ERROR_METHOD + methodUrl, e);
            throw new WebApiException(e);
        } finally {
            getMethod.releaseConnection();
            getMethod = null;
        }
    }

    protected JSONObject executePostMethod(JSONObject jRequest,
            String methodPath) throws WebApiException, JSONException {

        return executeHttpPostMethod(jRequest, root + methodPath);
    }

    private JSONObject executeHttpPostMethod(JSONObject jRequest,
            String methodPath) throws WebApiException, JSONException {
        String methodUrl = serverPath + methodPath;
        jRequest.put("locale", locale.toString());
        JSONObject jContent = new JSONObject();
        jContent.put("request", jRequest);
        PostMethod postMethod = new PostMethod(methodUrl);
        RequestEntity requestEntity = new ByteArrayRequestEntity(jContent.toString(2).getBytes(), "application/json");
        postMethod.setRequestEntity(requestEntity);
        try {
            logger.debug("Send request: POST " + methodUrl + "\n" + jContent.toString(2));
            int status = httpClient.executeMethod(postMethod);
            logger.debug(RESPONSE_STATUS + status);
            if (status != HttpStatus.SC_OK) {
                logger.warn(METHOD + methodUrl + FAILED_STATUS
                        + postMethod.getStatusText());
                throw new WebApiException(METHOD + methodUrl
                        + FAILED_STATUS + postMethod.getStatusText());
            }
            return parseResponse(postMethod.getResponseBodyAsStream());
        } catch (Exception e) {
            logger.error(ERROR_METHOD + methodUrl
                    + " for content: " + jContent, e);
            throw new WebApiException(e);
        } finally {
            postMethod.releaseConnection();
            postMethod = null;
        }
    }

    private JSONObject executeHttpDeleteMethod(String methodPath,
            Map<String, ?> map) throws WebApiException,
            URIException {
        String methodUrl = serverPath + methodPath;
        methodUrl = URIUtil.encodeQuery(methodUrl);
        DeleteMethod deleteMethod = new DeleteMethod(methodUrl);
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        if (map != null) {
            for (String key : map.keySet()) {
                Object value = map.get(key);
                if (value == null) {
                    continue;
                }
                if (value instanceof Collection) {
                    Collection col = (Collection) value;
                    for (Object obj : col) {
                        params.add(new NameValuePair(key, obj.toString()));
                    }
                } else {
                    params.add(new NameValuePair(key, value.toString()));
                }
            }
        }
        deleteMethod.setQueryString(
                params.toArray(new NameValuePair[params.size()]));
        logger.debug("Sending the DELETE request: " + methodUrl);
        try {
            int status = httpClient.executeMethod(deleteMethod);
            logger.debug(RESPONSE_STATUS + status);
            if (status != HttpStatus.SC_OK) {
                logger.warn(METHOD + methodUrl + FAILED_STATUS
                        + deleteMethod.getStatusText());
                throw new WebApiException(METHOD + methodUrl
                        + FAILED_STATUS + deleteMethod.getStatusText());
            }
            return parseResponse(deleteMethod.getResponseBodyAsStream());
        } catch (Exception e) {
            logger.error(ERROR_METHOD + methodUrl, e);
            throw new WebApiException(e);
        } finally {
            deleteMethod.releaseConnection();
            deleteMethod = null;
        }
    }

    private StringBuilder fillResponseBuffer(InputStream in) throws WebApiException,
            IOException {
        byte[] bytes = new byte[KB];
        int len;
        StringBuilder buf = new StringBuilder();
        while ((len = in.read(bytes)) > 0) {
            buf.append(new String(bytes, 0, len));
        }
        if (buf.length() == 0) {
            logger.warn(RESPONSE_EMPTY);
            throw new WebApiException(RESPONSE_EMPTY);
        }
        if (logger.isDebugEnabled()) {
            logger.debug("Response body:\n" + buf.toString());
        }

        return buf;
    }

    protected JSONObject parseResponse(InputStream in) throws WebApiException {
        try {
            StringBuilder buf = fillResponseBuffer(in);

            JSONObject jResponse = new JSONObject(buf.toString());
            if (jResponse.has(RESPONSE)
                    && jResponse.getJSONObject(RESPONSE).has("status")) {
                if ("ok".equalsIgnoreCase(jResponse.getJSONObject(RESPONSE).
                        getString("status"))) {
                    return jResponse;
                } else {
                    String message = jResponse.getJSONObject(RESPONSE).
                            getJSONObject("error").optString("message");
                    if (message == null) {
                        message = "Unknown error";
                    }
                    throw new WebApiException(message);
                }
            } else {
                logger.warn(RESPONSE_EMPTY);
                throw new WebApiException(RESPONSE_EMPTY);
            }
        } catch (IOException e) {
            logger.error("Error retrieve response content", e);
            throw new WebApiException(RESPONSE_EMPTY, e);
        } catch (JSONException e) {
            logger.error("Error parse JSON content", e);
            throw new WebApiException("Error parse JSON content", e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                }
            }
        }
    }

    protected boolean parseDeleteRespone(JSONObject jResponse)
            throws JSONException {
        if (jResponse.getJSONObject(RESPONSE).has("deleted")) {
            boolean response = jResponse.getJSONObject(RESPONSE).optBoolean(
                    "deleted");
            return response;
        }
        return false;
    }

    protected int parseMultiDeleteRespone(JSONObject jResponse)
            throws JSONException {
        if (jResponse.getJSONObject(RESPONSE).has("deleted")) {
            int count = jResponse.getJSONObject(RESPONSE).optInt(
                    "count", 0);
            return count;
        }
        return 0;
    }

    protected Long parseCreateResponse(JSONObject jResponse) throws
            JSONException {
        if (jResponse.getJSONObject(RESPONSE).has("id")) {
            return jResponse.getJSONObject(RESPONSE).getLong("id");
        }
        return null;
    }

    protected String parseCreateResponseOfManyObject(JSONObject jResponse)
            throws JSONException {
        if (jResponse.getJSONObject(RESPONSE).has("id")) {
            return jResponse.getJSONObject(RESPONSE).getString("id");
        }
        return null;
    }
}
