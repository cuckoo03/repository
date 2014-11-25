package com.hadoop.mapreduce;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.mapreduce.JobContext;
import org.apache.hadoop.mapreduce.OutputCommitter;
import org.apache.hadoop.mapreduce.TaskAttemptContext;

public class ComplexWordCountOutputCommitter extends OutputCommitter {
	static final Log LOG = LogFactory
			.getLog(ComplexWordCountOutputCommitter.class);

	/**
	 * 태스크에 문제가 발생하거나 사용자에 의해 강제 종료될 때 호출된다.
	 */
	@Override
	public void abortTask(TaskAttemptContext arg0) throws IOException {
		LOG.info("ComplexWordCountOutputCommitter:abortTask");
	}

	/**
	 * 리듀스 태스크가 종료될 때 호출
	 */
	@Override
	public void commitTask(TaskAttemptContext arg0) throws IOException {
		LOG.info("ComplexWordCountOutputCommitter:commitTask");
	}

	/**
	 * 리듀스 태스크가 종료될 때 commitTask() 메서드를 호출할지 여부를 반환, 호출이 필요하면 true를 반환
	 */
	@Override
	public boolean needsTaskCommit(TaskAttemptContext arg0) throws IOException {
		LOG.info("ComplexWordCountOutputCommitter:needsTaskCommit");
		return true;
	}

	/**
	 * 작업이 시작될 때 호출
	 */
	@Override
	public void setupJob(JobContext arg0) throws IOException {
		LOG.info("ComplexWordCountOutputCommitter:setupJob");
	}

	/**
	 * 리듀스 태스크가 시작될 때 호출
	 */
	@Override
	public void setupTask(TaskAttemptContext arg0) throws IOException {
		LOG.info("ComplexWordCountOutputCommitter:setupTask");
	}
}