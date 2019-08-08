package org.highmed.dsf.bpe.service;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.highmed.dsf.bpe.Constants;
import org.highmed.dsf.bpe.delegate.AbstractServiceDelegate;
import org.highmed.fhir.client.WebserviceClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class CheckMultiMedicSimpleCohortSizeResult extends AbstractServiceDelegate
{
	private static final Logger logger = LoggerFactory.getLogger(CheckMultiMedicSimpleCohortSizeResult.class);

	public CheckMultiMedicSimpleCohortSizeResult(WebserviceClient webserviceClient)
	{
		super(webserviceClient);
	}

	@Override
	public void executeService(DelegateExecution execution) throws Exception
	{
		Integer result = (Integer) execution.getVariable(Constants.VARIABLE_MULTI_MEDIC_COHORT_SIZE_RESULT);
		Integer medics = (Integer) execution.getVariable(Constants.VARIABLE_MULTI_MEDIC_PARTICIPATION_RESULT);

		// TODO: implement check for result
		// TODO: implement check for ...

		Map<String, String> outputs = (Map<String, String>) execution.getVariable(Constants.VARIABLE_PROCESS_OUTPUTS);
		outputs.put(Constants.CODESYSTEM_HIGHMED_BPMN_VALUE_SIMPLE_COHORT_SIZE_QUERY_RESULT, String.valueOf(result));
		outputs.put(Constants.CODESYSTEM_HIGHMED_BPMN_VALUE_PARTICIPATION_RESULT, String.valueOf(medics));
		execution.setVariable(Constants.VARIABLE_PROCESS_OUTPUTS, outputs);
	}

	private void stopInstance(String reason)
	{
		logger.error("Result review failed, reason {}", reason);
		throw new RuntimeException("Result review failed, reason " + reason);
	}
}