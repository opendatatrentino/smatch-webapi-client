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

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.disi.smatch.webapi.client.methods;

import it.unitn.disi.smatch.webapi.client.exceptions.WebApiException;
import it.unitn.disi.smatch.webapi.model.exceptions.UnknownRelationException;
import it.unitn.disi.smatch.webapi.model.smatch.Context;
import it.unitn.disi.smatch.webapi.model.smatch.Correspondence;
import it.unitn.disi.smatch.webapi.model.smatch.CorrespondenceItem;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.httpclient.HttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A method to return the correspondence between a source and a target contexts
 *
 * @author Moaz Reyad <reyad@disi.unitn.it>
 * @date Jul 11, 2013
 */
public class MatchMethods extends AbstractMethod {

    public MatchMethods(HttpClient httpClient, Locale locale,
            String serverPath) {
        super(httpClient, locale, serverPath);
    }

    /*
     * Returns the correspondence between the source and the target contexts
     * 
     * @param sourceName - The name of the root node in the source tree
     * @param sourceNodes - Names of the source nodes under the source root node
     * @param targetName - The name of the root node in the target tree
     * @param targetNodes -Names of the target nodes under the target root node
     * 
     * @return - the correspondence
     */
    public Correspondence match(String sourceName, List<String> sourceNodes,
            String targetName, List<String> targetNodes) throws WebApiException {

        String methodPath = "/match";
        try {
            JSONObject jRequest = generateJSONRequest(sourceName, sourceNodes,
                    targetName, targetNodes);

            JSONObject jResponse = executePostMethod(jRequest, methodPath);
            return convert(jResponse);
        } catch (JSONException e) {
            throw new WebApiException(e);
        }
    }

    /**
     * Generates JSON request from input parameters
     *
     * @param sourceName - The name of the root node in the source tree
     * @param sourceNodes - Names of the source nodes under the source root node
     * @param targetName - The name of the root node in the target tree
     * @param targetNodes -Names of the target nodes under the target root node
     * @return JSON request to S-Match Web API Server
     * @throws JSONException
     */
    public JSONObject generateJSONRequest(String sourceName, List<String> sourceNodes,
            String targetName, List<String> targetNodes) throws JSONException {
        
        Context sourceContext = new Context("SourceContext", sourceName, sourceNodes);
        Context targetContext = new Context("TargetContext", targetName, targetNodes);

        JSONObject jRequest = new JSONObject();
        JSONObject jContexts = new JSONObject();
        JSONArray jContextList = new JSONArray();

        JSONObject jsourceContext = sourceContext.toJsonObject();
        JSONObject jtargetContext = targetContext.toJsonObject();

        jContextList.put(jsourceContext);
        jContextList.put(jtargetContext);

        jContexts.put("Contexts", jContextList);

        jRequest.put("parameters", jContexts);

        return jRequest;
    }

     /**
     * Converts from JSON Correspondence to Java Correspondence
     *
     * @param jResponse The JSON response with Correspondence
     * @return the Correspondence Java object
     * @throws JSONException
     */
    public Correspondence convert(JSONObject jResponse) throws JSONException {
        Correspondence correspondence = null;

        if (jResponse.getJSONObject("response").has("Correspondence")) {

            JSONObject jResponseItem = jResponse.getJSONObject("response");

            JSONArray jcorrespondenceItems = jResponseItem.getJSONArray("Correspondence");
            List<CorrespondenceItem> correspondenceItems = new ArrayList<CorrespondenceItem>();
            for (int i = 0; i < jcorrespondenceItems.length(); i++) {
                JSONObject jCorrespondenceItem = jcorrespondenceItems.getJSONObject(i);

                String relation = jCorrespondenceItem.optString("Relation");
                String source = jCorrespondenceItem.optString("Source");
                String target = jCorrespondenceItem.optString("Target");
                char cRelation = relation.charAt(0);
                
                try {
                    correspondenceItems.add(new CorrespondenceItem(source, target, cRelation));
                } catch (UnknownRelationException ex) {
                    Logger.getLogger(MatchMethods.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            correspondence = new Correspondence(correspondenceItems);

        }
        return correspondence;
    }
}
