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
 * TestWebApiClient.java
 */
package it.unitn.disi.smatch.webapi.client;

import it.unitn.disi.smatch.webapi.model.smatch.Correspondence;
import it.unitn.disi.smatch.webapi.model.smatch.CorrespondenceItem;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.apache.log4j.Logger;

/**
 *
 *
 * @author Sergey Kanshin, kanshin@disi.unitn.it
 * @date Jul 19, 2010
 */
public class TestWebApiClient {

    private static final int DEFAULT_PORT = 9090;

    /**
     * @param args
     * @throws WebApiException
     */
    public static void main(String[] args) {
        WebApiClient api = WebApiClient.getInstance(Locale.ENGLISH,
                "localhost", DEFAULT_PORT);

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

        List<CorrespondenceItem> correspondenceItems = correspondence.getCorrespondenceItems();

        for (CorrespondenceItem item : correspondenceItems) {
            String correspondenceLine = item.getSource() + "\t" + item.getRelation()
                    + "\t" + item.getTarget();

            Logger.getLogger("Test Smatch").info(correspondenceLine);

        }
    }

    protected TestWebApiClient() {
    }
}
