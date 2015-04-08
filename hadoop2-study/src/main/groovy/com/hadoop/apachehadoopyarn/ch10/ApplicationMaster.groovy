package com.hadoop.apachehadoopyarn.ch10

import groovy.transform.TypeChecked

import java.util.concurrent.atomic.AtomicInteger

import org.apache.commons.cli.Options
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.yarn.api.records.ApplicationAttemptId
import org.apache.hadoop.yarn.client.api.async.AMRMClientAsync
import org.apache.hadoop.yarn.client.api.async.NMClientAsync
import org.apache.hadoop.yarn.conf.YarnConfiguration
import org.slf4j.Logger
import org.slf4j.LoggerFactory


@TypeChecked
class ApplicationMaster {
	private static final Logger LOG = LoggerFactory.getLogger(ApplicationMaster.class)
	private Configuration conf
	private AMRMClientAsync resourceManager
	private NMClientAsync nmClientSync
	private ApplicationAttemptId appAttempID
	private String appMasterHostname = ""
	private int appMasterRpcPort = 0
	private String appMasterTrackingUrl = ""
	
	private int numTotalContainers
	private int containerMemory
	private int requestPriority
	
	private String adminUser
	private String adminPassword
	
	private AtomicInteger numCompletedContainers = new AtomicInteger()
	private AtomicInteger numAllocatedContainers = new AtomicInteger()
	private AtomicInteger numFailedContainers = new AtomicInteger()
	private AtomicInteger numRequestedContainers = new AtomicInteger()
	
	private Map<String, String> shellEnv = new HashMap<>()
	
	private String home
	private String appJar
	private String domainController
	
	private volatile boolean done
	private volatile boolean success
	
	private List<Thread> launchThreads = new ArrayList<>()
	
	private Options opts
	
	public ApplicationMaster() {
		conf = new YarnConfiguration()
		opts = new Options()
		
		opts.addOption("admin_user", true, "admin_user")
	}
	
	public boolean init(String[] args) {
		return false
	}

	public boolean run() {
		return false
	}

	static main(args) {
		boolean result = false
		ApplicationMaster appMaster = new ApplicationMaster()
		
		boolean doRun = appMaster.init((String[]) args)
		if (!doRun) {
			System.exit(0)
		}
		result = appMaster.run()
		if (result) {
			LOG.info("applicationmaster successfullly")
			System.exit(0)
		} else {
			LOG.info("application master exiting")
			System.exit(2)
		}
	}
}