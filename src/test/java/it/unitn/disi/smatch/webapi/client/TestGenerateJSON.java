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

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.disi.smatch.webapi.client;

import it.unitn.disi.smatch.webapi.client.methods.MatchMethods;
import java.util.Arrays;
import java.util.List;
import junit.framework.TestCase;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Moaz Reyad <reyad@disi.unitn.it>
 * @date Jul 11, 2013
 */
public class TestGenerateJSON extends TestCase {

    public TestGenerateJSON() {
        super("Test Generate JSON request");
    }

    public void testGenerateJSON() throws JSONException {
        
        MatchMethods method = new MatchMethods(null, null, null);

        String sourceContextRoot = "neve luogo";
        String targetContextRoot = "luogo";

        List<String> sourceContextNodes = Arrays.asList("codice",
                "nome",
                "nome breve",
                "quota",
                "latitudine",
                "longitudine");

        List<String> targetContextNodes = Arrays.asList("nome",
                "latitudine",
                "longitudine");

        JSONObject jRequest = method.generateJSONRequest(sourceContextRoot, sourceContextNodes,
                targetContextRoot, targetContextNodes);
          
          String expected = "{\"parameters\":{\"Contexts\":[{\"SourceContext\":{\"neve luogo\":[\"codice\",\"nome\",\"nome breve\",\"quota\",\"latitudine\",\"longitudine\"]}},{\"TargetContext\":{\"luogo\":[\"nome\",\"latitudine\",\"longitudine\"]}}]}}";
          
          assertTrue(jRequest.toString().equals(expected));
    }
}
