/*
 * Copyright and authors: see LICENSE.txt in base repository.
 *
 * This software is a web portal for pipeline execution on distributed systems.
 *
 * This software is governed by the CeCILL-B license under French law and
 * abiding by the rules of distribution of free software.  You can  use, 
 * modify and/ or redistribute the software under the terms of the CeCILL-B
 * license as circulated by CEA, CNRS and INRIA at the following URL
 * "http://www.cecill.info". 
 *
 * As a counterpart to the access to the source code and  rights to copy,
 * modify and redistribute granted by the license, users are provided only
 * with a limited warranty  and the software's author,  the holder of the
 * economic rights,  and the successive licensors  have only  limited
 * liability. 
 *
 * In this respect, the user's attention is drawn to the risks associated
 * with loading,  using,  modifying and/or developing or reproducing the
 * software by the user in light of its specific status of free software,
 * that may mean  that it is complicated to manipulate,  and  that  also
 * therefore means  that it is reserved for developers  and  experienced
 * professionals having in-depth computer knowledge. Users are therefore
 * encouraged to load and test the software's suitability as regards their
 * requirements in conditions enabling the security of their systems and/or 
 * data to be ensured and,  more generally, to use and operate it in the 
 * same conditions as regards security. 
 *
 * The fact that you are presently reading this means that you have had
 * knowledge of the CeCILL-B license and that you accept its terms.
 */
package fr.insalyon.creatis.vip.api.soap;

import fr.insalyon.creatis.vip.api.bean.Execution;
import fr.insalyon.creatis.vip.api.bean.Execution.ExecutionStatus;
import fr.insalyon.creatis.vip.api.bean.GlobalProperties;
import fr.insalyon.creatis.vip.api.bean.Pipeline;
import fr.insalyon.creatis.vip.api.bean.pairs.StringKeyParameterValuePair;
import fr.insalyon.creatis.vip.api.bean.pairs.StringKeyValuePair;
import fr.insalyon.creatis.vip.api.bean.Response;
import fr.insalyon.creatis.vip.api.business.*;

import java.util.ArrayList;
import javax.annotation.Resource;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.ws.WebServiceContext;

/**
 *
 * @author Tristan Glatard
 */
@WebService(serviceName = "api", targetNamespace = "http://france-life-imaging.fr/api")
/**
 * Entry-point class for SOAP API.
 * This is where the methods of the Carmin API must be defined.
 * These methods must:
 * * Print a detailed log showing which method was called and the parameters.
 * * Check that the mandatory parameters are not null.
 * * Check access control rights 
 * * Call the proper methods of an ApiBusiness class.
 * * Build the response object (including warnings generated by the ApiBusiness method).
 * * Return the response object.
 */
public class Carmin {

    private final static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(Carmin.class);

    @Resource
    private WebServiceContext wsContext;

    /*
     * Execution
     */
    
    @WebMethod(operationName = "getExecution")
    public @XmlElement(required = true)
    Response getExecution(
            @XmlElement(required = true) @WebParam(name = "executionId") String executionId) {
        Response r;
        try {
            ApiUtils.methodInvocationLog("getExecution", executionId);
            ApiUtils.throwIfNull(executionId, "Execution id");
            ApiContext apiContext = new ApiBusiness().getApiContext(wsContext, true);
            ExecutionBusiness eb = new ExecutionBusiness(apiContext);
            eb.checkIfUserCanAccessExecution(executionId);
            Execution e = eb.getExecution(executionId,false);
            r = new Response(0, ApiUtils.getMessage(apiContext), e);
        } catch (ApiException ex) {
            logger.error(ex);
            r = new Response(1, ex.getMessage(), null);
        } 
        return r;
    }
    
    @WebMethod(operationName = "listExecutions")
    public @XmlElement(required = true)
    Response listExecutions() {
        Response r;
        try {
            ApiUtils.methodInvocationLog("listExecutions");
            ApiContext apiContext = new ApiBusiness().getApiContext(wsContext, true);
            ExecutionBusiness eb = new ExecutionBusiness(apiContext);
            Execution[] executions = eb.listExecutions(500); // will not return more than 500 executions.
            r = new Response(0, ApiUtils.getMessage(apiContext), executions);
        } catch (ApiException ex) {
            logger.error(ex);
            r = new Response(1, ex.getMessage(), null);
        } 
        return r;
    }

    @WebMethod(operationName = "getStdOut")
    public @XmlElement(required = true)
    Response getStdOut(@XmlElement(required = true) @WebParam(name = "executionId") String executionId) {
        Response r;
        try {
            ApiUtils.methodInvocationLog("getStdOut",executionId);
            ApiContext apiContext = new ApiBusiness().getApiContext(wsContext, true);
            ExecutionBusiness eb = new ExecutionBusiness(apiContext);
            String stdout = eb.getStdOut(executionId);
            r = new Response(0, ApiUtils.getMessage(apiContext), stdout);
        } catch (ApiException ex) {
            logger.error(ex);
            r = new Response(1, ex.getMessage(), null);
        } 
        return r;
    }
   
    @WebMethod(operationName = "getStdErr")
    public @XmlElement(required = true)
    Response getStdErr(@XmlElement(required = true) @WebParam(name = "executionId") String executionId) {
        Response r;
        try {
            ApiUtils.methodInvocationLog("getStdErr",executionId);
            ApiContext apiContext = new ApiBusiness().getApiContext(wsContext, true);
            ExecutionBusiness eb = new ExecutionBusiness(apiContext);
            String stderr = eb.getStdErr(executionId);
            r = new Response(0, ApiUtils.getMessage(apiContext), stderr);
        } catch (ApiException ex) {
            logger.error(ex);
            r = new Response(1, ex.getMessage(), null);
        } 
        return r;
    }
    
    @WebMethod(operationName = "updateExecution")
    public @XmlElement(required = true)
    Response updateExecution(
            @XmlElement(required = true) @WebParam(name = "executionId") String executionId,
            @XmlElement(required = true) @WebParam(name = "keyValuePair") ArrayList<StringKeyValuePair> keyValuePairs) {
        Response r;
        try {
            ApiUtils.methodInvocationLog("updateExecution", executionId, keyValuePairs);
            ApiUtils.throwIfNull(executionId, "Execution id"); 
            ApiUtils.throwIfNull(keyValuePairs, "Values");
            ApiContext apiContext = new ApiBusiness().getApiContext(wsContext, true);
            ExecutionBusiness eb = new ExecutionBusiness(apiContext);
            eb.checkIfUserCanAccessExecution(executionId);
            eb.updateExecution(executionId, keyValuePairs);
            r = new Response(0, ApiUtils.getMessage(apiContext), null);
        } catch (ApiException ex) {
            logger.error(ex);
            r = new Response(1, ex.getMessage(), null);
        } 
        return r;
    }

    @WebMethod(operationName = "initExecution")
    public @XmlElement(required = true)
    Response initExecution(
            @XmlElement(required = true) @WebParam(name = "pipelineId") String pipelineId,
            @XmlElement(required = true) @WebParam(name = "inputValue") ArrayList<StringKeyParameterValuePair> inputValues,
            @WebParam(name = "timeout") Integer timeout,
            @WebParam(name = "executionName") String executionName,
            @WebParam(name = "studyId") String studyId,
            @WebParam(name = "playExecution") Boolean playExecution) {
        Response r;
        try {
            ApiUtils.methodInvocationLog("initExecution", pipelineId, inputValues, timeout, executionName, studyId, playExecution);
            ApiUtils.throwIfNull(pipelineId, "Pipeline id");
            ApiUtils.throwIfNull(inputValues, "Input values");
            if (playExecution == null || !playExecution) {
                throw new ApiException("Cannot initialize an execution in VIP without starting it (we know it breaks the specification). Please set playExecution to 'true'");
            }
            if(executionName == null)
                executionName = "Untitled";
            ApiContext apiContext = new ApiBusiness().getApiContext(wsContext, true);
            PipelineBusiness pb = new PipelineBusiness(apiContext);
            pb.checkIfUserCanAccessPipeline(pipelineId);
            ExecutionBusiness eb = new ExecutionBusiness(apiContext);
            String id = eb.initExecution(pipelineId, inputValues, timeout, executionName, studyId, playExecution);
            r = new Response(0, ApiUtils.getMessage(apiContext), id);
        } catch (ApiException ex) {
            logger.error(ex);
            r = new Response(1, ex.getMessage(), null);
        } 
        return r;
    }

    @WebMethod(operationName = "playExecution")
    public @XmlElement(required = true)
    Response playExecution(@XmlElement(required = true) @WebParam(name = "executionId") String executionId) {
        Response r;
        try {
            ApiUtils.methodInvocationLog("playExecution", executionId);
            ApiUtils.throwIfNull(executionId, "Execution id");
            ApiContext apiContext = new ApiBusiness().getApiContext(wsContext, true);
            ExecutionBusiness eb = new ExecutionBusiness(apiContext);
            eb.checkIfUserCanAccessExecution(executionId);
            ExecutionStatus s = eb.playExecution(executionId);
            r = new Response(0, ApiUtils.getMessage(apiContext), s);
        } catch (ApiException ex) {
            logger.error(ex);
            r = new Response(1, ex.getMessage(), null);
        } 
        return r;
    }

    @WebMethod(operationName = "killExecution")
    public @XmlElement(required = true)
    Response killExecution(@XmlElement(required = true) @WebParam(name = "executionId") String executionId) {
        Response r;
        try {
            ApiUtils.methodInvocationLog("killExecution", executionId);
            ApiUtils.throwIfNull(executionId, "Execution id");
            ApiContext apiContext = new ApiBusiness().getApiContext(wsContext, true);
            ExecutionBusiness eb = new ExecutionBusiness(apiContext);
            eb.checkIfUserCanAccessExecution(executionId);
            eb.killExecution(executionId);
            r = new Response(0, ApiUtils.getMessage(apiContext), null);
        } catch (ApiException ex) {
            logger.error(ex);
            r = new Response(1, ex.getMessage(), null);
        } 
        return r;
    }

    @WebMethod(operationName = "deleteExecution")
    public @XmlElement(required = true)
    Response deleteExecution(
            @XmlElement(required = true) @WebParam(name = "executionId") String executionId,
            @XmlElement(required = true) @WebParam(name = "deleteFiles") Boolean deleteFiles) {
        Response r;
        try {
            ApiUtils.methodInvocationLog("deleteExecution", executionId, deleteFiles);
            ApiUtils.throwIfNull(executionId, "Execution id");
            ApiContext apiContext = new ApiBusiness().getApiContext(wsContext, true);
            ExecutionBusiness eb = new ExecutionBusiness(apiContext);
            eb.checkIfUserCanAccessExecution(executionId);
            eb.deleteExecution(executionId, deleteFiles);
            r = new Response(0, ApiUtils.getMessage(apiContext), null);
        } catch (ApiException ex) {
            logger.error(ex);
            r = new Response(1, ex.getMessage(), null);
        } 
        return r;
    }

    @WebMethod(operationName = "getExecutionResults")
    public @XmlElement(required = true)
    Response getExecutionResults(
            @XmlElement(required = true) @WebParam(name = "executionId") String executionId,
            @WebParam(name = "protocol") String protocol) {
        Response r;
        try {
            ApiUtils.methodInvocationLog("getExecutionResults", executionId, protocol);
            ApiUtils.throwIfNull(executionId, "Execution id");
            ApiContext apiContext = new ApiBusiness().getApiContext(wsContext, true);
            ExecutionBusiness eb = new ExecutionBusiness(apiContext);
            eb.checkIfUserCanAccessExecution(executionId);
            String[] results = eb.getExecutionResults(executionId, protocol);
            r = new Response(0, ApiUtils.getMessage(apiContext), results);
        } catch (ApiException ex) {
            logger.error(ex);
            r = new Response(1, ex.getMessage(), null);
        } 
        return r;
    }

    /**
     * Global Properties
     * 
     */
    
    @WebMethod(operationName = "getGlobalProperties")
    public @XmlElement(required = true)
    Response getGlobalProperties() {
        Response r;
        try {
            ApiUtils.methodInvocationLog("getGlobalProperties");
            ApiContext apiContext = new ApiBusiness().getApiContext(wsContext, false);
            GlobalPropertiesBusiness bpb = new GlobalPropertiesBusiness(apiContext);
            GlobalProperties gp = bpb.getGlobalProperties();
            r = new Response(0, ApiUtils.getMessage(apiContext), gp);
        } catch (ApiException ex) {
            logger.error(ex);
            r = new Response(1, ex.getMessage(), null);
        }
        return r;
    }

    /*
     * Pipeline
     * 
     */
    
    @WebMethod(operationName = "getPipeline")
    public @XmlElement(required = true)
    Response getPipeline(@XmlElement(required = true) @WebParam(name = "pipelineId") String pipelineId) {
        Response r;
        try {
            ApiUtils.methodInvocationLog("getPipeline", pipelineId); 
            ApiUtils.throwIfNull(pipelineId, "Pipeline id");
            ApiContext apiContext = new ApiBusiness().getApiContext(wsContext, true);
            PipelineBusiness pb = new PipelineBusiness(apiContext);
            pb.checkIfUserCanAccessPipeline(pipelineId);
            Pipeline p = pb.getPipeline(pipelineId);
            r = new Response(0, ApiUtils.getMessage(apiContext), p);
        } catch (ApiException ex) {
            logger.error(ex);
            r = new Response(1, ex.getMessage(), null);
        } 
        return r;
    }

    @WebMethod(operationName = "listPipelines")
    public @XmlElement(required = true)
    Response listPipelines(@WebParam(name = "studyIdentifier") String studyIdentifier) {
        Response r;
        try {
            ApiUtils.methodInvocationLog("listPipelines", studyIdentifier);
            ApiContext apiContext = new ApiBusiness().getApiContext(wsContext, true);
            PipelineBusiness pb = new PipelineBusiness(apiContext);
            Pipeline[] pipelines = pb.listPipelines(studyIdentifier);
            r = new Response(0, ApiUtils.getMessage(apiContext),pipelines);
        } catch (ApiException ex) {
            logger.error(ex);
            r = new Response(1, ex.getMessage(), null);
        } 
        return r;
    }

    /*
     * Authentication
     * 
     */
    
    @WebMethod(operationName = "authenticateSession")
    public @XmlElement(required = true)
    Response authenticateSession(@XmlElement(required = true) @WebParam(name = "userName") String userName, @XmlElement(required = true) @WebParam(name = "password") String password) {
        Response r;
        try {
            ApiUtils.methodInvocationLog("authenticateSession", userName, "*****");
            ApiUtils.throwIfNull(userName, "User name");
            ApiUtils.throwIfNull(password, "Password");
            ApiContext apiContext = new ApiBusiness().getApiContext(wsContext, false);
            AuthenticationBusiness ab = new AuthenticationBusiness(apiContext);
            ab.authenticateSession(userName, password);
            r = new Response(0, ApiUtils.getMessage(apiContext), null);
        } catch (ApiException ex) {
            logger.error(ex);
            r = new Response(1, ex.getMessage(), null);
        } 
        return r;
    }

    @WebMethod(operationName = "authenticateHTTP")
    public @XmlElement(required = true)
    Response authenticateHTTP(@XmlElement(required = true) @WebParam(name = "userName") String userName) {
        Response r;
        try {
            ApiContext apiContext = new ApiBusiness().getApiContext(wsContext, false);
            AuthenticationBusiness ab = new AuthenticationBusiness(apiContext);
            ab.authenticateHTTP(userName);
            r = new Response(0, ApiUtils.getMessage(apiContext), null);
        } catch (ApiException ex) {
            logger.error(ex);
            r = new Response(1, ex.getMessage(), null);
        } 
        return r;
    }

    @WebMethod(operationName = "logout")
    public @XmlElement(required = true)
    Response logout() {
        Response r;
        try {
            ApiContext apiContext = new ApiBusiness().getApiContext(wsContext, true);
            ApiUtils.methodInvocationLog("logout");
            AuthenticationBusiness ab = new AuthenticationBusiness(apiContext);
            ab.logout();
            r = new Response(0, ApiUtils.getMessage(apiContext), null);
        } catch (ApiException ex) {
            logger.error(ex);
            r = new Response(1, ex.getMessage(), null);
        } 
        return r;
    }

}