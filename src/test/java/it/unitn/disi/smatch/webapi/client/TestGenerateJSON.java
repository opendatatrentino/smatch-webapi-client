/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.disi.smatch.webapi.client;

import it.unitn.disi.smatch.webapi.client.methods.MatchMethods;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import junit.framework.TestCase;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Moaz
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
