package com.hadoop.beginnerhadoop.yarn.helloyarn

import groovy.transform.TypeChecked

import org.apache.commons.cli.CommandLine
import org.apache.commons.cli.GnuParser
import org.apache.commons.cli.Options
import org.apache.hadoop.fs.FileSystem
import org.apache.hadoop.fs.Path
import org.apache.hadoop.util.ExitUtil
import org.apache.hadoop.yarn.api.ApplicationConstants;
import org.apache.hadoop.yarn.api.protocolrecords.AllocateResponse
import org.apache.hadoop.yarn.api.records.ApplicationAttemptId
import org.apache.hadoop.yarn.api.records.Container
import org.apache.hadoop.yarn.api.records.ContainerId
import org.apache.hadoop.yarn.api.records.ContainerLaunchContext
import org.apache.hadoop.yarn.api.records.ContainerStatus
import org.apache.hadoop.yarn.api.records.FinalApplicationStatus
import org.apache.hadoop.yarn.api.records.LocalResource
import org.apache.hadoop.yarn.api.records.LocalResourceType
import org.apache.hadoop.yarn.api.records.LocalResourceVisibility
import org.apache.hadoop.yarn.api.records.Priority
import org.apache.hadoop.yarn.api.records.Resource
import org.apache.hadoop.yarn.client.api.AMRMClient
import org.apache.hadoop.yarn.client.api.NMClient
import org.apache.hadoop.yarn.client.api.AMRMClient.ContainerRequest
import org.apache.hadoop.yarn.conf.YarnConfiguration
import org.apache.hadoop.yarn.util.ConverterUtils
import org.apache.hadoop.yarn.util.Records
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@TypeChecked
class MyApplicationMaster {
	private static final Logger LOG = LoggerFactory.getLogger(MyApplicationMaster.class)
	private YarnConfiguration conf
	protected ApplicationAttemptId appAttemptID
	private int numTotalContainers = 1
	private int containerMemory = 10
	private int containerVirtualCores = 1
	private int requestPriority
	private String appJarPath = ""
	private long appJarTimestamp = 0
	private long appJarPathLen = 0

	public MyApplicationMaster() {
		conf = new YarnConfiguration()
	}

	public boolean init(String[] args) {
		Options opts = new Options();
		opts.addOption("app_attempt_id", true,
				"App Attempt ID. Not to be used unless for testing purposes");
		opts.addOption("shell_env", true,
				"Environment for shell script. Specified as env_key=env_val pairs");
		opts.addOption("container_memory", true,
				"Amount of memory in MB to be requested to run the shell command");
		opts.addOption("container_vcores", true,
				"Amount of virtual cores to be requested to run the shell command");
		opts.addOption("num_containers", true,
				"No. of containers on which the shell command needs to be executed");
		opts.addOption("priority", true, "Application Priority. Default 0");
		opts.addOption("help", false, "Print usage");
		CommandLine cliParser = new GnuParser().parse(opts, args);
		Map<String, String> envs = System.getenv();
		if (!envs.containsKey(ApplicationConstants.Environment.CONTAINER_ID.name())) {
			if (cliParser.hasOption("app_attempt_id")) {
				String appIdStr = cliParser.getOptionValue("app_attempt_id", "");
				appAttemptID = ConverterUtils.toApplicationAttemptId(appIdStr);
			} else {
				throw new IllegalArgumentException(
				"Application Attempt Id not set in the environment");
			}
		} else {
			ContainerId containerId = ConverterUtils.toContainerId(envs
					.get(ApplicationConstants.Environment.CONTAINER_ID.name()));
			appAttemptID = containerId.getApplicationAttemptId();
		}
		if (!envs.containsKey(ApplicationConstants.APP_SUBMIT_TIME_ENV)) {
			throw new RuntimeException(ApplicationConstants.APP_SUBMIT_TIME_ENV + " not set in the environment");
		}
		if (!envs.containsKey(ApplicationConstants.Environment.NM_HOST.name())) {
			throw new RuntimeException(ApplicationConstants.Environment.NM_HOST.name() + " not set in the environment");
		}
		if (!envs.containsKey(ApplicationConstants.Environment.NM_HTTP_PORT.name())) {
			throw new RuntimeException(ApplicationConstants.Environment.NM_HTTP_PORT.name() + " not set in the environment");
		}
		if (!envs.containsKey(ApplicationConstants.Environment.NM_PORT.name())) {
			throw new RuntimeException(ApplicationConstants.Environment.NM_PORT.name() + " not set in the environment");
		}
		if (envs.containsKey(Constants.AM_JAR_PATH)) {
			appJarPath = envs.get(Constants.AM_JAR_PATH);
			if (envs.containsKey(Constants.AM_JAR_TIMESTAMP)) {
				appJarTimestamp = Long.valueOf(envs.get(Constants.AM_JAR_TIMESTAMP));
			}
			if (envs.containsKey(Constants.AM_JAR_LENGTH)) {
				appJarPathLen = Long.valueOf(envs.get(Constants.AM_JAR_LENGTH));
			}
			if (!appJarPath.isEmpty() && (appJarTimestamp <= 0 || appJarPathLen <= 0)) {
				LOG.error("Illegal values in env for shell script path" + ", path="
						+ appJarPath + ", len=" + appJarPathLen + ", timestamp="+ appJarTimestamp);
				throw new IllegalArgumentException(
				"Illegal values in env for shell script path");
			}
		}
		LOG.info("Application master for app" + ", appId="
				+ appAttemptID.getApplicationId().getId() + ", clusterTimestamp="
				+ appAttemptID.getApplicationId().getClusterTimestamp()
				+ ", attemptId=" + appAttemptID.getAttemptId());
		containerMemory = Integer.parseInt(cliParser.getOptionValue("container_memory", "10"));
		containerVirtualCores = Integer.parseInt(cliParser.getOptionValue("container_vcores", "1"));
		numTotalContainers = Integer.parseInt(cliParser.getOptionValue("num_containers", "1"));
		if (numTotalContainers == 0) {
			throw new IllegalArgumentException("Cannot run MyAppliCationMaster with no containers");
		}
		requestPriority = Integer.parseInt(cliParser.getOptionValue("priority", "0"));
		return true;
	}

	public void shutdown() {
	}
	public void run() {
		LOG.info("running myapplicationmaster")

		AMRMClient<ContainerRequest> amrmClient = AMRMClient.createAMRMClient()
		amrmClient.init(conf)
		amrmClient.start()

		amrmClient.registerApplicationMaster("", 0, "")

		Resource capability = Records.newRecord(Resource.class)
		capability.setMemory(containerMemory)
		capability.setVirtualCores(containerVirtualCores)

		Priority priority = Records.newRecord(Priority.class)
		priority.setPriority(requestPriority)

		for (int i = 0; i < numTotalContainers; i++) {
			ContainerRequest containerAsk = new ContainerRequest(capability,
					null, null, priority)
			amrmClient.addContainerRequest(containerAsk)
		}

		NMClient nmClient = NMClient.createNMClient()
		nmClient.init(conf)
		nmClient.start()

		Map<String, String> containerEnv = new HashMap<>()
		containerEnv.put("CLASSPATH", "./*")

		LocalResource appMasterJar = createAppMasterJar()

		int allocatedContainers = 0
		int completedContainers = 0
		while (allocatedContainers < numTotalContainers) {
			AllocateResponse response = amrmClient.allocate(0)
			for (Container container : response.getAllocatedContainers()) {
				allocatedContainers++
				ContainerLaunchContext appContainer =
						createContainerLaunchContext(appMasterJar, containerEnv)
				LOG.info("launching container {}", allocatedContainers)

				nmClient.startContainer(container, appContainer)
			}

			for (ContainerStatus status : response.getCompletedContainersStatuses()) {
				++completedContainers
				LOG.info("containerID:{}, state:{}", status.getContainerId(),
						status.getState().name())
			}
			sleep(100)
		}

		while (completedContainers < numTotalContainers) {
			AllocateResponse response = amrmClient.allocate(
					completedContainers/numTotalContainers)
			for (ContainerStatus status : response.getCompletedContainersStatuses()) {
				++completedContainers
				LOG.info("containerID:{}, status:{}", status.getContainerId(),
						status.getState().name())
			}
			sleep(100)
		}
		LOG.info("completed containers:{}", completedContainers)

		amrmClient.unregisterApplicationMaster(FinalApplicationStatus.SUCCEEDED,
				"", "")
		LOG.info("finished myapplicationmaster")
	}

	private LocalResource createAppMasterJar() {
		LocalResource appMasterJar = Records.newRecord(LocalResource.class)
		if (!appJarPath.isEmpty()) {
			appMasterJar.setType(LocalResourceType.FILE)
			Path jarPath = new Path(appJarPath)
			jarPath = FileSystem.get(conf).makeQualified(jarPath)
			appMasterJar.setResource(ConverterUtils.getYarnUrlFromPath(jarPath))
			appMasterJar.setTimestamp(appJarTimestamp)
			appMasterJar.setSize(appJarPathLen)
			appMasterJar.setVisibility(LocalResourceVisibility.PUBLIC)
		}
		return appMasterJar
	}
	private ContainerLaunchContext createContainerLaunchContext(
			LocalResource appMasterJar, Map<String, String> containerEnv) {
		ContainerLaunchContext appContainer = Records.newRecord(
				ContainerLaunchContext.class)
		appContainer.setLocalResources(Collections.singletonMap(
				Constants.AM_JAR_NAME, appMasterJar))
		StringBuilder  comd = new StringBuilder()
		comd.append("\$JAVA_HOME/bin/java")
		comd.append(" -Xmx256M")
		comd.append(" com.hadoop.beginnerhadoop.yarn.helloyarn.HelloYarn")
		comd.append(" 1>" + ApplicationConstants.LOG_DIR_EXPANSION_VAR + "/stdout")
		comd.append(" 2>" + ApplicationConstants.LOG_DIR_EXPANSION_VAR + "/stderr")
		appContainer.setCommands(Collections.singletonList(comd.toString()))
		return appContainer
	}

	static main(args) {
		try {

			MyApplicationMaster appMaster = new MyApplicationMaster()
			LOG.info("initializing myapplicationmaster")
			boolean doRun = appMaster.init((String[]) args)
			if (!doRun) {
				System.exit(0)
			}
			appMaster.run()
		} catch (Throwable t) {
			LOG.error("erro myapplicationmaster", t)
			ExitUtil.terminate(1, t)
		}
	}
}