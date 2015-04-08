package com.hadoop.beginnerhadoop.yarn.helloyarn

import groovy.transform.TypeChecked;

import org.apache.commons.cli.CommandLine
import org.apache.commons.cli.GnuParser
import org.apache.commons.cli.HelpFormatter
import org.apache.commons.cli.Options
import org.apache.commons.io.IOUtils
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.FSDataOutputStream
import org.apache.hadoop.fs.FileStatus
import org.apache.hadoop.fs.FileSystem
import org.apache.hadoop.fs.Path
import org.apache.hadoop.fs.permission.FsPermission
import org.apache.hadoop.yarn.api.ApplicationConstants;
import org.apache.hadoop.yarn.api.ApplicationConstants.Environment
import org.apache.hadoop.yarn.api.protocolrecords.GetNewApplicationResponse
import org.apache.hadoop.yarn.api.records.ApplicationId
import org.apache.hadoop.yarn.api.records.ApplicationReport
import org.apache.hadoop.yarn.api.records.ApplicationSubmissionContext
import org.apache.hadoop.yarn.api.records.ContainerLaunchContext
import org.apache.hadoop.yarn.api.records.FinalApplicationStatus
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
class MyClient {
	private static final Logger LOG = LoggerFactory.getLogger(MyClient.class)
	private Configuration conf
	private YarnClient yarnClient

	// 애플맄케이션마스터의 실행 우선순위
	private int amPriority = 0
	// 애플리케이션마스터가 사용할 큐
	private String amQueue = ""
	// 애플리케이션마스터를 실행하기 위해 요청할 메모리 크기 기본값 10메가
	private int amMemory = 10
	// 애플리케이션마스터를 실행하기 위해 요청할 코어 개수 기본값 1
	private int amVCores = 1
	// 애플리케이션마스터가 참조할 jar 파일 경로
	private String appMasterJarPath = ""
	//컨테이너 요청 우선순위
	private int requestPriority = 0
	// 컨테이너에게 요청할 메모리 크기 기본 10메가
	private int containerMemory = 10
	// 컨테이너에게 요처할 코어 개수 기본 1
	private int containerVirtualCores = 1
	// 실행할 컨테이너 개수
	private int numContainers = 1
	// 클라이언트의 타임아웃 대기 시간(밀리초)
	private long clientTimeout = 600000

	private String appName = ""

	private final long clientStartTime = System.currentTimeMillis();

	private Options opts;

	public MyClient(){
		createYarnClient()
		initOptions()
	}
	private void createYarnClient() {
		yarnClient = YarnClient.createYarnClient()
		this.conf = new YarnConfiguration()
		yarnClient.init(conf)
	}
	private void initOptions(){
		opts = new Options()
		opts.addOption("appname", true, "Application Name. Default value - HelloYarn");
		opts.addOption("priority", true, "Application Priority. Default 0");
		opts.addOption("queue", true, "RM Queue in which this application is to be submitted");
		opts.addOption("timeout", true, "Application timeout in milliseconds");
		opts.addOption("master_memory", true, "Amount of memory in MB to be requested to run the application master");
		opts.addOption("master_vcores", true, "Amount of virtual cores to be requested to run the application master");
		opts.addOption("jar", true, "Jar file containing the application master");
		opts.addOption("container_memory", true, "Amount of memory in MB to be requested to run the HelloYarn");
		opts.addOption("container_vcores", true, "Amount of virtual cores to be requested to run the HelloYarn");
		opts.addOption("num_containers", true, "No. of containers on which the HelloYarn needs to be executed");
		opts.addOption("help", false, "Print usage");
	}
	private boolean run() {
		LOG.info("Running Client")
		yarnClient.start()

		YarnClientApplication app = yarnClient.createApplication()
		GetNewApplicationResponse appResponse = app.getNewApplicationResponse()

		int maxMem = appResponse.getMaximumResourceCapability().getMemory()
		LOG.info("Max mem capability of resource in this cluster={}", maxMem)
		if (amMemory > maxMem) {
			LOG.info("AM memory specified above max threshold of cluster. Using max value, specified={}, max={}", amMemory, maxMem)
			amMemory = maxMem
		}

		// 코어 개수 비교
		int maxVCores = appResponse.getMaximumResourceCapability().getVirtualCores()
		LOG.info("Max virtual cores capability of resources in this cluster={}", maxVCores)
		if (amVCores > maxVCores) {
			LOG.info("AM virtual cores specified above max threshold of cluster. Using max value. specified={}, max={}"
					, amVCores, maxVCores)
			amVCores = maxVCores
		}

		ApplicationSubmissionContext appContext = app.getApplicationSubmissionContext()
		ApplicationId appId = appContext.getApplicationId()

		// 애플리케이션 이름 설정
		appContext.setApplicationName(appName)

		//애플리케이션마스터용 시스템 리소스 설정
		Resource capability = Records.newRecord(Resource.class)
		capability.setMemory(amMemory)
		capability.setVirtualCores(amVCores)
		appContext.setResource(capability)

		//애플리케이션마스터 우선순위 설정
		Priority pri = Records.newRecord(Priority.class)
		pri.setPriority(amPriority)
		appContext.setPriority(pri)

		//애플리케이션마스터 큐 설정
		appContext.setQueue(amQueue)

		// 애플리케이션마스터 ContainerLaunchContext 설정
		appContext.setAMContainerSpec(getAmContainerSpec(appId.getId()))

		// 애플리케이션 실행 요청
		LOG.info("Submitting application to ASM")
		yarnClient.submitApplication(appContext)

		//애플리케이션 모니터링 수행
		return monitorApplication(appId)
	}
	private ContainerLaunchContext getAmContainerSpec(int appId) {
		// 애플리케이션마스터를 실행하는 컨테이너를 위한 컨텍스트 객체 생성
		ContainerLaunchContext amContainer = Records.newRecord(
			ContainerLaunchContext.class)

		Map<String, LocalResource> localResources = new HashMap<>()

		LOG.info("copy App master jar from local filesystem and add to local environment")

		// 애플리케이션마스터가 포함돼 있는 JaR 파일을 HDFS에 복사
		FileSystem fs = FileSystem.get(conf)
		
		// 복사된 내용을 LocalResource에 추가
		appToLocalResources(fs, appMasterJarPath, Constants.AM_JAR_NAME, appId,
				localResources, null)

		//애플리케이션마스터용 컨텍스트에 localReource를 추가
		amContainer.setLocalResources(localResources)

		//애플리케이션마스터용 시스템 환경변수를 설정
		LOG.info("Set the environment for the application master")
		amContainer.setEnvironment(getAMEnvironment(localResources, fs))

		// 애플리케이션마스터를 실행하기 위한 커맨드 라인 설정
		Vector<CharSequence> vargs = new Vector<CharSequence>(30)

		// 자바 실행 커맨드 라인 설정
		LOG.info("Setting up app master command")
		println "***********************"
		println Environment.JAVA_HOME.$()
		vargs.add(Environment.JAVA_HOME.$() + "/bin/java")
		//힙메모리 설정
		vargs.add("-Xmx" + amMemory + "m")
		//실행할 클래스 이름 설정
		vargs.add("com.hadoop.beginnerhadoop.yarn.helloyarn.MyApplicationMaster")
		vargs.add("--container_memory " + String.valueOf(containerMemory))
		vargs.add("--container_vcores " + String.valueOf(containerVirtualCores))
		vargs.add("--num_containers " + String.valueOf(numContainers))
		vargs.add("--priority " + String.valueOf(requestPriority))

		//로그경로 설정
		vargs.add("1>" + ApplicationConstants.LOG_DIR_EXPANSION_VAR + "/AppMaster.stdout")
		vargs.add("2>" + ApplicationConstants.LOG_DIR_EXPANSION_VAR + "/AppMaster.stderr")

		StringBuilder command = new StringBuilder()
		for (CharSequence str : vargs) {
			command.append(str).append(" ")
		}

		LOG.info("completed settting up app master command " + command.toString())
		List<String> commands = new ArrayList<>()
		commands.add(command.toString())
		amContainer.setCommands(commands)
		return amContainer
	}
	private monitorApplication(ApplicationId appId) {
		while (true) {
			try {
				sleep(1000)
			} catch (InterruptedException e) {
				LOG.error("Thread sleep in monitoring loop interrupted")
			}
			//애플리케이션 상태 조회 및 출력
			ApplicationReport report = yarnClient.getApplicationReport(appId)
			YarnApplicationState state = report.getYarnApplicationState()
			FinalApplicationStatus dsStatus = report.getFinalApplicationStatus()
			if (YarnApplicationState.FINISHED == state) {
				if (FinalApplicationStatus.SUCCEEDED == dsStatus) {
					LOG.info("application has completed successfully. Breaking monitoring loop: ApplicationID:{}",
							appId.getId())
					return true
				} else {
					LOG.info("application did finished unsuccessfully. YarnState={}, DSFinalState={}. Breaking monitoring loop:ApplicationId:{}",
							state.toString(), dsStatus, appId.getId())
					return false
				}
			} else if (YarnApplicationState.KILLED == state
			|| YarnApplicationState.FAILED == state) {
				LOG.info("application did not finish.Yarnstate={}, DSFinalState={}. breaking monitoring loop:ApplicationId:{}",
						state.toString(), dsStatus.toString(), appId.getId())
				return false
			}

			// 타임아웃이 발생할 경우 애플리케이션을 강제로 종료
			if (System.currentTimeMillis() > (clientStartTime + clientTimeout)) {
				LOG.info("reached client specified timeout for application."+
						"killing application." +
						"breaking monitoring loop:ApplicationId:{}", appId.getId())
				forceKillApplication(appId)
				return false
			}
		}
	}
	private void appToLocalResources(FileSystem fs, String fileSrcPath,
			String fileDstPath, int appId,
			Map<String, LocalResource> localResources, String resources) {
		String suffix = appName + "/" + appId + "/" + fileDstPath
		Path dst = new Path(fs.getHomeDirectory(), suffix)
		//HDFS 파일 업로드
		if (fileSrcPath == null) {
			FSDataOutputStream ostream = null
			try {
				ostream = FileSystem.create(fs, dst, new FsPermission((short)0710))
				ostream.writeUTF(resources)
			} finally {
				IOUtils.closeQuietly(ostream)
			}
		} else {
		println "**************************************"
		println "**************************************"
		println "**************************************"
		println fileSrcPath
		println fileDstPath
		println resources
		println suffix
		println dst.toString()
		
			fs.copyFromLocalFile(new Path(fileSrcPath), dst)
		}

		FileStatus scFileStatus = fs.getFileStatus(dst)

		// LocalResource를 생성해 HDFS에 업로드된 파일 정보를 설정
		LocalResource localResource = LocalResource.newInstance(
				ConverterUtils.getYarnUrlFromURI(dst.toUri()),
				LocalResourceType.FILE, LocalResourceVisibility.APPLICATION,
				scFileStatus.getLen(),
				scFileStatus.getModificationTime())
		localResources.put(fileDstPath, localResource)
	}
	private Map<String, String> getAMEnvironment(Map<String,
			LocalResource> localResources, FileSystem fs) {
		Map<String, String> env = new HashMap<>()

		//애플리케이션마스터용 JAR파일의 경로 설정
		LocalResource appJarResource = localResources.get(Constants.AM_JAR_NAME)
		Path hdfsAppJarPath = new Path(fs.getHomeDirectory(),
				appJarResource.getResource().getFile())
		FileStatus hdfsAppJarStatus = fs.getFileStatus(hdfsAppJarPath)
		long hdfsAppJarLength = hdfsAppJarStatus.getLen()
		long hdfsAppJarTimestamp = hdfsAppJarStatus.getModificationTime()

		env.put(Constants.AM_JAR_PATH, hdfsAppJarPath.toString())
		env.put(Constants.AM_JAR_TIMESTAMP, Long.toString(hdfsAppJarTimestamp))
		env.put(Constants.AM_JAR_LENGTH, Long.toString(hdfsAppJarLength))

		// 애플리케이션마스터가 JAR파일의 경로를 클래스패스에 추가
		StringBuilder classPathEnv = new StringBuilder(
				Environment.CLASSPATH.$()).append("/").append("/*")
		String[] strings = conf.getStrings(
				YarnConfiguration.YARN_APPLICATION_CLASSPATH,
				YarnConfiguration.DEFAULT_YARN_APPLICATION_CLASSPATH)
		for (String c : strings){
			classPathEnv.append("/")
			classPathEnv.append(c.trim())
		}
		env.put("CLASSPATH", classPathEnv.toString())

		return env
	}
	private void forceKillApplication(ApplicationId appId) {
		yarnClient.killApplication(appId)
	}
	public boolean init(String[] args) {
		CommandLine cliParser = new GnuParser().parse(opts, args)

		if (args.length == 0) {
			throw new IllegalArgumentException("No args")
		}

		if (cliParser.hasOption("help")) {
			printUsage()
			return false
		}

		appName = cliParser.getOptionValue("appname", "HelloYarn")
		amPriority = Integer.parseInt(cliParser.getOptionValue("priority", "0"))
		amQueue = cliParser.getOptionValue("queue", "default")
		amMemory = Integer.parseInt(cliParser.getOptionValue("master_memory", "10"))
		amVCores = Integer.parseInt(cliParser.getOptionValue("master_vcores", "1"))

		if (amMemory < 0) {
			throw new IllegalArgumentException("invalid memory:" + amMemory)
		}
		if (amVCores < 0){
			throw new IllegalArgumentException("invalid virtual cores:" + amVCores)
		}
		if (!cliParser.hasOption("jar")) {
			throw new IllegalArgumentException("no jar")
		}

		appMasterJarPath = cliParser.getOptionValue("jar")

		containerMemory = Integer.parseInt(
				cliParser.getOptionValue("container_memory", "10"))
		containerVirtualCores = Integer.parseInt(
				cliParser.getOptionValue("container_vcores", "1"))
		numContainers = Integer.parseInt(
				cliParser.getOptionValue("num_containers", "1"))

		if (containerMemory < 0 || containerVirtualCores < 0 || numContainers < 1) {
			throw new IllegalArgumentException("invalid no container specified, memory:"
			+ containerMemory + ", vcores:" + containerVirtualCores +
			",numContainer:" + numContainers )
		}

		clientTimeout = Integer.parseInt(
				cliParser.getOptionValue("timeout", "600000"))

		return true
	}
	private void printUsage() {
		new HelpFormatter().print(opts)
	}

	static main(args) {
		boolean result = false

		MyClient client = new MyClient()
		LOG.info("initializing client")

		boolean doRun = client.init((String[]) args)
		if (!doRun) {
			System.exit(0)
		}

		result = client.run()
		if (result) {
			LOG.info("application completed successfullly")
			System.exit(0)
		}
		LOG.error("application failed to complete successfullly")
		System.exit(2)
	}
}