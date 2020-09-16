/**
 * Copyright 2014-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.webank.webase.chain.mgr.base.properties;

import static java.io.File.separator;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.MutablePair;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.webank.webase.chain.mgr.base.tools.JsonTools;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * constants.
 */
@Slf4j
@Data
@Component
@ConfigurationProperties(prefix = ConstantProperties.CONSTANT_PREFIX)
public class ConstantProperties {
    // constant
    public static final String CONSTANT_PREFIX = "constant";
    public static final String PREFIX_RESULT_CODE = "0x";

    private String groupInvalidGrayscaleValue; // y:year, M:month, d:day of month, h:hour, m:minute,
                                               // n:forever valid
    // front http request
    private String frontUrl;
    private Integer httpTimeOut = 5000;
    private Integer contractDeployTimeOut = 30000;
    private Integer maxRequestFail = 3;
    private Long sleepWhenHttpMaxFail = 60000L; // default 1min


    //************************8 add by deploy
    public static final int DEFAULT_GROUP_ID = 1;

    private boolean useDockerSDK = false;
    public int dockerDaemonPort = 3000;
    public String sshDefaultUser = "root";
    public int sshDefaultPort = 22;


    // TODO. write tbchain's id in db into config.ini
    private int defaultJsonrpcPort = 8545;
    private int defaultP2pPort = 30300;
    private int defaultChannelPort = 20200;
    private int defaultFrontPort = 5002;

    // timeout config
    private long execHostInitTimeout = 2 * 60 * 60 * 1000L;
    private long startNodeTimeout = 5 * 60 * 1000L;
    private long execBuildChainTimeout = 10 * 60 * 1000L;
    private long execShellTimeout = 2 * 60 * 1000L;
    private long dockerRestartPeriodTime = 30 * 1000L;
    private int dockerClientConnectTimeout = 10 * 60 * 1000;
    private int dockerPullTimeout = 10 * 60 * 1000;
    private int dockerClientReadTimeout = 10 * 60 * 1000;

    private String dockerRepository= "fiscoorg/fisco-webase";
    private String imageTagUpdateUrl = "https://registry.hub.docker.com/v1/repositories/%s/tags";
    private String dockerRegistryMirror = "";
    private String nodesRootDir = "NODES_ROOT";
    private String nodesRootTmpDir = "NODES_ROOT_TMP";
    // docker image tar file
    private String imageCDNUrl = "https://osp-1257653870.cos.ap-guangzhou.myqcloud.com/WeBASE/download/docker/image/%s";
    private String imageTar = "fisco-webase-%s.tar";

    // shell script
    private String nodeOperateShell = "./script/deploy/host_operate.sh";
    private String buildChainShell = "./script/deploy/build_chain.sh";
    private String genAgencyShell = "./script/deploy/gen_agency_cert.sh";
    private String genNodeShell = "./script/deploy/gen_node_cert.sh";
    private String scpShell = "./script/deploy/file_trans_util.sh";
    private String privateKey = System.getProperty("user.home") + File.separator + ".ssh" + File.separator + "id_rsa";
    private String fiscoBcosBinary =  "";

    private String  webaseSignAddress = "127.0.0.1:5004";


    private Map<Integer,String> transactionMap = new HashMap<>();

    /**
     * Docker client connect daemon ip with proxy ip.
     */
    private Map<String, MutablePair<String, Integer>> dockerProxyMap = new ConcurrentHashMap<>();

    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        nodesRootDir = initDirectory(nodesRootDir, "NODES_ROOT/");
        nodesRootTmpDir = initDirectory(nodesRootTmpDir, "NODES_ROOT_TMP/");

        log.info("Init constant properties, generate nodes root dir:[{}]", nodesRootDir);
        log.info("Init constant properties, generate nodes root temp dir:[{}]", nodesRootTmpDir);


        this.imageTagUpdateUrl = String.format(this.imageTagUpdateUrl,dockerRepository);
        log.info("Init constant properties, imageTagUpdateUrl: [{}]", this.imageTagUpdateUrl);

        log.info("Init constant properties, dockerProxyMap: [{}]", dockerProxyMap);

        log.info("Init constant properties, check FISCO-BCOS binary path: [{}]", fiscoBcosBinary);
        if (!Files.exists(Paths.get(fiscoBcosBinary))) {
            log.warn("FISCO-BCOS binary path: [{}] not exists.", fiscoBcosBinary);
            fiscoBcosBinary = "";
        }

        log.info("Init constant properties, private key: [{}]", privateKey);

        log.info("Init constant properties, webaseSignAddress: [{}]", webaseSignAddress);

        log.info("Init constant properties, transactionMap : [{}]", JsonTools.toJSONString(transactionMap));

        log.info("Init constant properties, defaultP2pPort:[{}], defaultChannelPort:[{}], defaultJsonrpcPort:[{}], defaultFrontPort:[{}]",
                defaultP2pPort, defaultChannelPort, defaultJsonrpcPort, defaultFrontPort);
    }

    /**
     *
     * @param injectedValue
     * @param defaultValue
     * @return
     */
    private static String initDirectory(String injectedValue, String defaultValue){
        String newDirectory = injectedValue;

        if (StringUtils.isBlank(newDirectory)) {
            newDirectory = defaultValue;
        }

        if (newDirectory.trim().endsWith(separator)) {
            // ends with separator
            newDirectory = newDirectory.trim();
        } else {
            // append a separator
            newDirectory = String.format("%s%s", newDirectory.trim(), separator);
        }

        if (! newDirectory.startsWith("/")){
            // not an absolute path
            return String.format("%s/%s",new File(".").toPath().toAbsolutePath().toString(), newDirectory);
        }
        return newDirectory;
    }
    //******************* Add in v1.4.0 end. *******************

    public String getDockerTarFileName(String version){
        return String.format(imageTar, version);
    }
    public String getCdnUrl(String version){
        String dockerTarFileName = this.getDockerTarFileName(version);
        return String.format(imageCDNUrl, dockerTarFileName);
    }
}