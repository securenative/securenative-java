package com.securenative.events;

import com.securenative.models.EventTypes;
import com.securenative.packagemanager.PackageManager;
import com.securenative.packagemanager.SnPackage;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.ZonedDateTime;

public class AgentLoginEvent {
    private static final String PACKAGE_FILE_NAME = "pom.xml";

    public String eventType;
    public Long ts;
    public SnPackage snPackage;
    public String appName;
    public String env;
    public SnProcess process;
    public SnRuntime snRuntime;
    public Os os;
    public Framework framework;
    public Agent agent;

    public AgentLoginEvent(String framework, String frameworkVersion, String appName) {
        String cwd = System.getProperty("user.dir");

        SnPackage appPkg = PackageManager.getPackage(String.join(cwd, PACKAGE_FILE_NAME));
        SnPackage agentPkg = PackageManager.getPackage(String.join("/sdk-base/", PACKAGE_FILE_NAME));

        this.appName = appName;
        this.framework = new Framework(framework, frameworkVersion);

        this.snPackage = new SnPackage(appPkg.getArtifactId(), appPkg.getGroupId(), appPkg.getVersion(), appPkg.getDependencies(), appPkg.getDependenciesHash());

        this.eventType = EventTypes.AGENT_LOG_IN.getType();

        this.snRuntime = new SnRuntime("java", System.getProperty("java.version"));

        this.process = new SnProcess(
                ProcessHandle.current().pid(),
                ProcessHandle.current().getClass().getName(),
                System.getProperty("user.dir")
        );

        String hostId = null;
        String hostname = null;
        try {
            hostId = InetAddress.getLocalHost().getHostAddress();
            hostname = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            // TODO add logging
        }
        this.os = new Os(hostId, hostname, System.getProperty("os.arch"), System.getProperty("os.name"), Runtime.getRuntime().availableProcessors(), Runtime.getRuntime().totalMemory());

        this.agent = new Agent("Java", agentPkg.getVersion(), System.getProperty("java.class.path"));
        this.env = System.getenv("JAVA_ENV");

        this.ts = ZonedDateTime.now().toEpochSecond();
    }
}

class SnProcess {
    Long pid;
    String name;
    String cwd;

    public SnProcess(Long pid, String name, String cwd) {
        this.pid = pid;
        this.name = name;
        this.cwd = cwd;
    }
}

class SnRuntime {
    String type;
    String version;

    public SnRuntime(String type, String version) {
        this.type = type;
        this.version = version;
    }
}

class Os {
    String hostId;
    String hostname;
    String arch;
    String platform;
    Integer cpus;
    Long totalMemory;

    public Os(String hostId, String hostname, String arch, String platform, Integer cpus, Long totalMemory) {
        this.hostId = hostId;
        this.hostname = hostname;
        this.arch = arch;
        this.platform = platform;
        this.cpus = cpus;
        this.totalMemory = totalMemory;
    }
}

class Framework {
    String type;
    String version;

    public Framework(String type, String version) {
        this.type = type;
        this.version = version;
    }
}

class Agent {
    String type;
    String version;
    String path;

    public Agent(String type, String version, String path) {
        this.type = type;
        this.version = version;
        this.path = path;
    }
}