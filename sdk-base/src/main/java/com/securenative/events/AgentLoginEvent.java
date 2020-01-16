package com.securenative.events;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.securenative.models.EventTypes;
import com.securenative.snlogic.Logger;
import com.securenative.snlogic.PackageManager;
import com.securenative.snlogic.SnPackage;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.ZonedDateTime;

public class AgentLoginEvent implements Event {
    private static final String PACKAGE_FILE_NAME = "pom.xml";

    @JsonProperty("eventType") public String eventType;
    @JsonProperty("ts") public Long ts;
    @JsonProperty("package") public SnPackage snPackage;
    @JsonProperty("appName") public String appName;
    @JsonProperty("process") public SnProcess process;
    @JsonProperty("runtime") public SnRuntime snRuntime;
    @JsonProperty("os") public Os os;
    @JsonProperty("framework") public Framework framework;
    @JsonProperty("agent") public Agent agent;

    public AgentLoginEvent(String framework, String frameworkVersion, String appName) {
        String cwd = System.getProperty("user.dir");

        SnPackage appPkg = PackageManager.getPackage(String.join(cwd, PACKAGE_FILE_NAME));
        SnPackage agentPkg = PackageManager.getPackage(String.join("/sdk-base/", PACKAGE_FILE_NAME));

        this.appName = appName;
        this.framework = new Framework(framework, frameworkVersion);

        this.snPackage = new SnPackage(appPkg.getName(), appPkg.getVersion(), appPkg.getDependencies(), appPkg.getDependenciesHash());

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
            Logger.getLogger().debug(String.join("Could not find hostname and/or host address; ", e.toString()));
        }
        this.os = new Os(hostId, hostname, System.getProperty("os.arch"), System.getProperty("os.name"), Runtime.getRuntime().availableProcessors(), Runtime.getRuntime().totalMemory());

        this.agent = new Agent("Java", agentPkg.getVersion(), System.getProperty("java.class.path"));

        this.ts = ZonedDateTime.now().toEpochSecond();
    }

    @Override
    public String getEventType() {
        return this.eventType;
    }
}

class SnProcess {
    @JsonProperty("pid") Long pid;
    @JsonProperty("name") String name;
    @JsonProperty("cwd") String cwd;

    public SnProcess(Long pid, String name, String cwd) {
        this.pid = pid;
        this.name = name;
        this.cwd = cwd;
    }
}

class SnRuntime {
    @JsonProperty("type") String type;
    @JsonProperty("version") String version;

    public SnRuntime(String type, String version) {
        this.type = type;
        this.version = version;
    }
}

class Os {
    @JsonProperty("hostId") String hostId;
    @JsonProperty("hostName") String hostname;
    @JsonProperty("arch") String arch;
    @JsonProperty("platform") String platform;
    @JsonProperty("cpus") Integer cpus;
    @JsonProperty("totalMemory") Long totalMemory;

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
    @JsonProperty("type") String type;
    @JsonProperty("version") String version;

    public Framework(String type, String version) {
        this.type = type;
        this.version = version;
    }
}

class Agent {
    @JsonProperty("type") String type;
    @JsonProperty("version") String version;
    @JsonProperty("path") String path;

    public Agent(String type, String version, String path) {
        this.type = type;
        this.version = version;
        this.path = path;
    }
}