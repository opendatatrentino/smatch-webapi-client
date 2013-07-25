= User Manual =

== Introduction ==
   * Name of the project: (smatch-webapi-client)
   * Version of the project: 1.0.0
   * Release notes: https://sweb.science.unitn.it/trac/sweb/wiki/ReleaseNotes/smatch-webapi-client/1.0.0
   * Javadocs: https://sweb.science.unitn.it/javadocs/smatch-webapi-client/1.0.0 


== Goal of the module ==

This module is used as a Java client for the S-Match Web API.

It hides the HTTP API request and response details from the client user by providing a Java interface to S-Match services.

== Getting started ==
   * Requirements: Java and Maven.
   * The maven pom.xml dependency (POM snippet)

{{{
<dependency>
    <groupId>it.unitn.disi</groupId>
    <artifactId>smatch-webapi-client</artifactId>
    <version>1.0.0</version>
</dependency>
}}}

   * How to install: include the POM snippet in your project.
   * How to configure: configuration of the HTTP connection is done in the smatch-webapi-model

== How to use ==
   1. Basic use: 
If we want to match the following two contexts:

Source Context:

 * neve luogo
   * codice
   * nome
   * nome breve
   * quota
   * latitudine
   * longitudine

Target Context:

 * luogo
   * nome
   * latitudine
   * longitudine

Call the match method in the WebAPIClient class to match two contexts. See the example below. 
 
{{{
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
}}}

A complete use case is given in {{{TestMatch.java}}}.
 
   1. Advanced use: a theorical explanation of S-Match can be found at http://semanticmatching.org/s-match.html.

== Troubleshooting ==


== FAQ ==
frequently asked questions
   1. How can I change the Host and the Port of the S-Match Web API server that the client is calling?
 
This is done in the smatch-webapi-model. In the file smatch-webapi-model.properties, the values of the host and port can be specified. The default values are localhost and 9090.

== Contacts ==
Moaz Reyad: reyad @ disi.unitn.it	