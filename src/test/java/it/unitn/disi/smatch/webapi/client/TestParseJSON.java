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
import it.unitn.disi.smatch.webapi.model.smatch.Correspondence;
import static junit.framework.Assert.assertNotNull;
import junit.framework.TestCase;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Moaz
 */
public class TestParseJSON extends TestCase {

    public TestParseJSON() {
        super("Test Parse JSON response");
    }

    public void testParseJSON() throws JSONException {
        String response = "{\"response\":{\"Correspondence\":[{\"Source\":\"neve luogo\",\"Relation\":\"<\",\"Target\":\"luogo\"},{\"Source\":\"neve luogo\\nome\",\"Relation\":\"<\",\"Target\":\"luogo\\nome\"},{\"Source\":\"neve luogo\\nome breve\",\"Relation\":\"<\",\"Target\":\"luogo\\nome\"},{\"Source\":\"neve luogo\\latitudine\",\"Relation\":\"<\",\"Target\":\"luogo\\latitudine\"},{\"Source\":\"neve luogo\\longitudine\",\"Relation\":\"<\",\"Target\":\"luogo\\longitudine\"}],\"status\":\"OK\"}}";
        JSONObject jResponse = new JSONObject(response);

        MatchMethods method = new MatchMethods(null, null, null);
        Correspondence correspondence = method.convert(jResponse);

        assertNotNull(correspondence);
        assertNotNull(correspondence.getCorrespondenceItems());
        assertEquals(correspondence.getCorrespondenceItems().size(), 5);
    }
}
