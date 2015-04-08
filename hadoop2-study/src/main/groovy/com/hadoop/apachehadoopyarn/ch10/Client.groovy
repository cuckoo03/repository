package com.hadoop.apachehadoopyarn.ch10

import groovy.transform.TypeChecked

import org.apache.commons.cli.CommandLine
import org.apache.commons.cli.GnuParser
import org.apache.commons.cli.Options
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.FileStatus
import org.apache.hadoop.fs.FileSystem
import org.apache.hadoop.fs.Path
import org.apache.hadoop.yarn.api.ApplicationConstants
import org.apache.hadoop.yarn.api.ApplicationConstants.Environment
import org.apache.hadoop.yarn.api.protocolrecords.GetNewApplicationResponse
import org.apache.hadoop.yarn.api.records.ApplicationId
import org.apache.hadoop.yarn.api.records.ApplicationReport
import org.apache.hadoop.yarn.api.records.ApplicationSubmissionContext
import org.apache.hadoop.yarn.api.records.ContainerLaunchContext
import org.apache.hadoop.yarn.api.records.FinalApplicationStatus;
import org.apache.hadoop.yarn.api.records.LocalResource
import org.apache.hadoop.yarn.api.records.LocalResourceType
import org.apache.hadoop.yarn.api.records.LocalResourceVisibility
import org.apache.hadoop.yarn.api.records.Priority
import org.apache.hadoop.yarn.api.records.Resource
import org.apache.hadoop.yarn.api.records.YarnApplicationState
import org.apache.hadoop.yarn.client.api.YarnClient
import org.apache.hadoop.yarn.client.api.YarnClientApplication
import org.apache.hadoop.yarn.conf.YarnConfiguration
import org.apache.hadoop.yarn.util.ConverterUtils
import org.apache.hadoop.yarn.util.Records
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@TypeChecked
class Client {
	private static final Logger LOG = LoggerFactory.getLogger(Client.class)
	private Configuration conf
	private YarnClient yarnClient

	private String appName
	private int amPriority
	private String amQueue = ""
	private int amMemory

	private String appJar = ""
	private final String appMasterMainClass = ApplicationMaster.class.getName()

	private int priority

	private int containerMemory
	private int numContainers

	private String adminUser
	private String adminPassword
	private String appUri

	private String log4jPropFile = ""
	private boolean debugFlag = false

	private Options opts

	public Client() {
		this.conf = new YarnConfiguration()
		yarnClient = YarnClient.createYarnClient()
		yarnClient.init(conf)

		opts = new Options()

		opts.addOption("appname", true, "yarn")
		opts.addOption("priority", true, "default 0")
		opts.addOption("queue", true, "rm queue")
		opts.addOption("timeout", true, "milliseconds")
		opts.addOption("master_memory", true, "master_memory")
		opts.addOption("jar", true, "jar file")
		opts.addOption("container_memory", true, "container_memory")
		opts.addOption("num_containers", true, "num_containers")
		opts.addOption("admin_user", true, "admin_user")
		opts.addOption("admin_password", true, "admin_password")
		opts.addOption("debug", false, "debug")
		opts.addOption("help", false, "help")
	}

	public boolean init(String[] args) {
		CommandLine cliParser = new GnuParser().parse(opts, args)

		if (args.length == 0) {
			throw new IllegalArgumentException("no args")
		}
		if (cliParser.hasOption("help")) {
			printUsage()
			return false
		}
		if (cliParser.hasOption("debug")) {
			debugFlag = true
		}
		appName = cliParser.getOptionValue("appname", "yarn")
		amPriority = Integer.parseInt(cliParser.getOptionValue("priority", "0"))
		amQueue = cliParser.getOptionValue("queue", "default")
		amMemory = Integer.parseInt(cliParser.getOptionValue("master_memory",
				"512"))
		if (amMemory < 0) {
			throw new IllegalArgumentException("invalid memory:" + amMemory)
		}

		if (!cliParser.hasOption("jar")) {
			throw new IllegalArgumentException("no jar")
		}
		appJar = cliParser.getOptionValue("jar")

		containerMemory = Integer.parseInt(cliParser.getOptionValue(
				"container_memory", "1024"))

		numContainers = Integer.parseInt(cliParser.getOptionValue(
				"num_containers", "2"))

		adminUser = cliParser.getOptionValue("admin_user", "cuckoo03")
		adminPassword = cliParser.getOptionValue("admin_password", "yarn")

		if (containerMemory < 0 || numContainers < 1) {
			throw new IllegalArgumentException("invalid container memory:"
			+ containerMemory +"," + numContainers)
		}
		return true
	}

	public void printUsage() {
	}
	public boolean run() {
		yarnClient.start()
		YarnClientApplication app = yarnClient.createApplication()

		GetNewApplicationResponse appResponse = app.getNewApplicationResponse()

		int maxMemory = appResponse.getMaximumResourceCapability().getMemory()

		if (amMemory > maxMemory) {
			amMemory = maxMemory
		}

		ApplicationSubmissionContext appContext = app.getApplicationSubmissionContext()

		ApplicationId appId = appContext.getApplicationId()

		appContext.setApplicationName(appName)

		ContainerLaunchContext amContainer = Records.newRecord(
				ContainerLaunchContext.class)

		Map<String, LocalResource> localResources = new HashMap<>()

		FileSystem fs = FileSystem.get(conf)
		Path src = new Path(appJar)
		String pathSuffix = appName + File.separator + appId.getId() +
				File.separator + "App.jar"

		Path dst = new Path(fs.getHomeDirectory(), pathSuffix)

		appUri = dst.toUri().toString()
		fs.copyFromLocalFile(false, true, src, dst)
		FileStatus dstStatus = fs.getFileStatus(dst)

		LocalResource amJarRsrc = Records.newRecord(LocalResource.class)

		amJarRsrc.setType(LocalResourceType.FILE)
		amJarRsrc.setVisibility(LocalResourceVisibility.APPLICATION)
		amJarRsrc.setResource(ConverterUtils.getYarnUrlFromPath(dst))
		amJarRsrc.setTimestamp(dstStatus.getModificationTime())
		amJarRsrc.setSize(dstStatus.getLen())

		localResources.put("app.jar", amJarRsrc)

		amContainer.setLocalResources(localResources)

		Map<String, String> env = new HashMap<>()

		StringBuilder classPathEnv = new StringBuilder(
				Environment.CLASSPATH.$()).append(
				File.pathSeparatorChar).append("./*")

		String[] paths = conf.getStrings(
				YarnConfiguration.YARN_APPLICATION_CLASSPATH,
				YarnConfiguration.DEFAULT_YARN_APPLICATION_CLASSPATH)
		for (String c : paths) {
			classPathEnv.append(File.pathSeparatorChar)
			classPathEnv.append(c.trim())
		}

		env.put("CLASSPATH", classPathEnv.toString())
		println "***************"
		println classPathEnv.toString()

		amContainer.setEnvironment(env)

		Vector<CharSequence> vargs = new Vector<CharSequence>(30)

		vargs.add(Environment.JAVA_HOME.$() + "/bin/java")
		vargs.add("-Xmx" + amMemory + "m")
		vargs.add("com.hadoop.apachehadoopyarn.ch10.AppMaster")
		vargs.add("--container_memory " + containerMemory)
		vargs.add("--num_containers " + numContainers)
		vargs.add("--priority " + priority)
		vargs.add("--admin_user " + adminUser)
		vargs.add("--admin_password " + adminPassword)
		vargs.add("--jar " + appUri)

		if (debugFlag){
			vargs.add("--debug")
		}

		vargs.add("1>" + ApplicationConstants.LOG_DIR_EXPANSION_VAR +
				"/appMaster.stdout")
		vargs.add("2>" + ApplicationConstants.LOG_DIR_EXPANSION_VAR +
				"/appMaster.stderr")

		StringBuilder command = new StringBuilder()
		for (CharSequence str : vargs) {
			command.append(str).append(" ")
		}

		List<String> commands = new ArrayList<>()
		commands.add(command.toString())
		amContainer.setCommands(commands)

		Resource capability = Records.newRecord(Resource.class)
		capability.setMemory(amMemory)
		appContext.setResource(capability)

		appContext.setAMContainerSpec(amContainer)

		Priority pri = Records.newRecord(Priority.class)
		pri.setPriority(amPriority)
		appContext.setQueue(amQueue)
		yarnClient.submitApplication(appContext)
		return monitorApplication(appId)
	}
	private boolean monitorApplication(ApplicationId appId) {
		while (true) {
			sleep(1000)

			ApplicationReport report = yarnClient.getApplicationReport(appId)

			LOG.info("application report from ASM for, appId={}, clientToAMToken={}, appDiagnostics={}, appMasterHost={}, appQueue={}, appMasterRPCPort={}, appStartTime={}, yarnAppState={}, finalState={}, appTrackingUrl={}, appUser={}", appId.getId(), report.getClientToAMToken(), report.getDiagnostics(), report.getHost(), report.getQueue(), report.getRpcPort(), report.getStartTime(), report.getYarnApplicationState().toString(), report.getFinalApplicationStatus().toString(), report.getTrackingUrl(), report.getUser())

			YarnApplicationState state = report.getYarnApplicationState()

			FinalApplicationStatus status = report.getFinalApplicationStatus()

			if (YarnApplicationState.FINISHED == state) {

				if (FinalApplicationStatus.SUCCEEDED == status) {
					LOG.info("application has completed successfully.")
					return true
				} else {
					LOG.info("application did finished unsuccessfullly")
					LOG.info("YarnState={}, finalState={}", state.toString(),
							status.toString())
					return false
				}
			} else if (YarnApplicationState.KILLED == state ||
			YarnApplicationState.FAILED == state) {
				LOG.info("application did not finish")
				LOG.info("yarnstate={}, finalstate={}", state.toString(),
						status.toString())
				return false
			}
		}
		return false
	}
	static main(args) {
		boolean result = false
		try {

			Client client = new Client()

			try {

				boolean doRun = client.init((String[]) args)
				if (!doRun) {
					System.exit(0)
				}
			} catch (IllegalArgumentException e) {
				client.printUsage()
				System.exit(-1)
			}

			result = client.run()
		} catch (Throwable t) {
			System.exit(1)
		}
		if (result) {
			System.exit(0)
		}
		System.exit(2)
	}
}