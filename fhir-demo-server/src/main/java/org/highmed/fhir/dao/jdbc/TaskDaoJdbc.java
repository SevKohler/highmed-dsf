package org.highmed.fhir.dao.jdbc;

import org.apache.commons.dbcp2.BasicDataSource;
import org.highmed.fhir.dao.TaskDao;
import org.highmed.fhir.search.parameters.TaskIdentifier;
import org.highmed.fhir.search.parameters.TaskRequester;
import org.highmed.fhir.search.parameters.TaskStatus;
import org.hl7.fhir.r4.model.Task;

import ca.uhn.fhir.context.FhirContext;

public class TaskDaoJdbc extends AbstractDomainResourceDaoJdbc<Task> implements TaskDao
{
	public TaskDaoJdbc(BasicDataSource dataSource, FhirContext fhirContext)
	{
		super(dataSource, fhirContext, Task.class, "tasks", "task", "task_id", TaskIdentifier::new, TaskRequester::new,
				TaskStatus::new);
	}

	@Override
	protected Task copy(Task resource)
	{
		return resource.copy();
	}
}
