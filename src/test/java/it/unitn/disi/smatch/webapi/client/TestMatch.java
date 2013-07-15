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

import it.unitn.disi.smatch.webapi.client.exceptions.WebApiException;
import it.unitn.disi.smatch.webapi.model.smatch.Correspondence;
import it.unitn.disi.smatch.webapi.model.smatch.CorrespondenceItem;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import static junit.framework.Assert.assertNotNull;
import junit.framework.TestCase;

/**
 * A unit test for S-Match
 * 
 * @author Moaz Reyad <reyad@disi.unitn.it>
 * @date Jul 11, 2013
 */
public class TestMatch extends TestCase {

    public TestMatch() {
        super("S-Match tests");
    }

    public void testSMatch() throws WebApiException {
        WebApiClient api = WebApiClient.getInstance(Locale.ENGLISH);

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

        Correspondence correspondence = api.match(sourceContextRoot, sourceContextNodes, targetContextRoot, targetContextNodes);
        
        assertNotNull("Correspondence is null", correspondence);
        
        List<CorrespondenceItem> correspondenceItems = correspondence.getCorrespondenceItems();
        
        assertNotNull("Correspondence Items is null", correspondenceItems);
        
        for(CorrespondenceItem item : correspondenceItems){
            System.out.print(item.getSource());
            System.out.print(item.getRelation());
            System.out.println(item.getTarget());
        }        
    }
}
