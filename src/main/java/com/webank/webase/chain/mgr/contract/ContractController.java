/**
 * Copyright 2014-2019 the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.webank.webase.chain.mgr.contract;

import com.webank.webase.chain.mgr.base.code.ConstantCode;
import com.webank.webase.chain.mgr.base.controller.BaseController;
import com.webank.webase.chain.mgr.base.entity.BasePageResponse;
import com.webank.webase.chain.mgr.base.entity.BaseResponse;
import com.webank.webase.chain.mgr.base.enums.DataStatus;
import com.webank.webase.chain.mgr.base.enums.SqlSortType;
import com.webank.webase.chain.mgr.base.exception.BaseException;
import com.webank.webase.chain.mgr.contract.entity.CompileInputParam;
import com.webank.webase.chain.mgr.contract.entity.Contract;
import com.webank.webase.chain.mgr.contract.entity.ContractParam;
import com.webank.webase.chain.mgr.contract.entity.ContractPathParam;
import com.webank.webase.chain.mgr.contract.entity.DeployInputParam;
import com.webank.webase.chain.mgr.contract.entity.QueryContractParam;
import com.webank.webase.chain.mgr.contract.entity.ReqContractByPath;
import com.webank.webase.chain.mgr.contract.entity.ReqContractVO;
import com.webank.webase.chain.mgr.contract.entity.ReqDeployByContractIdVO;
import com.webank.webase.chain.mgr.contract.entity.ReqQueryContractPage;
import com.webank.webase.chain.mgr.contract.entity.ReqSaveContractBatchVO;
import com.webank.webase.chain.mgr.contract.entity.ReqTransSendInfoVO;
import com.webank.webase.chain.mgr.contract.entity.RspContractCompile;
import com.webank.webase.chain.mgr.contract.entity.TransactionInputParam;
import com.webank.webase.chain.mgr.front.entity.ContractManageParam;
import com.webank.webase.chain.mgr.group.GroupService;
import com.webank.webase.chain.mgr.repository.bean.TbContract;
import com.webank.webase.chain.mgr.repository.bean.TbContractPath;
import com.webank.webase.chain.mgr.repository.bean.TbGroup;
import com.webank.webase.chain.mgr.util.JsonTools;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
@RequestMapping("contract")
public class ContractController extends BaseController {

    @Autowired
    private ContractService contractService;
    @Autowired
    private ContractManager contractManager;
    @Autowired
    private GroupService groupService;
    @Autowired
    private CompileService compileService;
    @Autowired
    private ContractPathService contractPathService;

    /**
     * compile deployInputParam.
     */
    @PostMapping(value = "/compile")
    public BaseResponse compileContract(@RequestBody @Valid CompileInputParam compileInputParam,
        BindingResult result) throws BaseException, IOException {
        checkBindResult(result);
        BaseResponse baseResponse = new BaseResponse(ConstantCode.SUCCESS);
        Instant startTime = Instant.now();
        log.info("start compileContract startTime:{} compileInputParam:{}",
            startTime.toEpochMilli(), JsonTools.toJSONString(compileInputParam));

        List<RspContractCompile> rspContractCompile =
            contractService.compileContract(compileInputParam);
        baseResponse.setData(rspContractCompile);

        log.info("end compileContract useTime:{}",
            Duration.between(startTime, Instant.now()).toMillis());

        return baseResponse;
    }


    /**
     * @param contractId
     * @return
     * @throws BaseException
     * @throws IOException
     */
    @PostMapping(value = "/compile/{contractId}")
    public BaseResponse compileByContractId(@PathVariable("contractId") Integer contractId) throws BaseException {
        BaseResponse baseResponse = new BaseResponse(ConstantCode.SUCCESS);
        Instant startTime = Instant.now();
        log.info("start compileByContractId startTime:{} contractId:{}", startTime.toEpochMilli(), contractId);

        TbContract contract = compileService.compileByContractId(contractId);
        baseResponse.setData(contract);

        log.info("end compileContract useTime:{}", Duration.between(startTime, Instant.now()).toMillis());
        return baseResponse;
    }

    /**
     * add new contract info.
     */
    @PostMapping(value = "/save")
    public BaseResponse saveContract(@RequestBody @Valid Contract contract, BindingResult result)
        throws BaseException {
        checkBindResult(result);
        BaseResponse baseResponse = new BaseResponse(ConstantCode.SUCCESS);
        Instant startTime = Instant.now();
        log.info("start saveContract startTime:{} contract:{}", startTime.toEpochMilli(),
            JsonTools.toJSONString(contract));

        // add contract row
        TbContract tbContract = contractService.saveContract(contract);

        baseResponse.setData(tbContract);

        log.info("end saveContract useTime:{}",
            Duration.between(startTime, Instant.now()).toMillis());
        return baseResponse;
    }

    /**
     * @param reqSaveContractBatchVO
     * @param result
     * @return
     */
    @PostMapping(value = "/batch")
    public BaseResponse saveContractBatch(@RequestBody @Valid ReqSaveContractBatchVO reqSaveContractBatchVO, BindingResult result) {
        checkBindResult(result);
        BaseResponse baseResponse = new BaseResponse(ConstantCode.SUCCESS);
        Instant startTime = Instant.now();
        log.info("start saveContractBatch startTime:{} ReqSaveContractBatchVO:{}", startTime.toEpochMilli(),
            JsonTools.toJSONString(reqSaveContractBatchVO));

        // add contract row
        List<TbContract> tbContract = contractService.saveContractBatch(reqSaveContractBatchVO);
        baseResponse.setData(tbContract);

        log.info("end saveContractBatch useTime:{}",
            Duration.between(startTime, Instant.now()).toMillis());
        return baseResponse;
    }


    /**
     * delete contract by id.
     */
    @DeleteMapping(value = "/{chainId}/{groupId}/{contractId}")
    public BaseResponse deleteContract(@PathVariable("chainId") Integer chainId,
        @PathVariable("groupId") Integer groupId,
        @PathVariable("contractId") Integer contractId,
        @RequestParam(value = "force", defaultValue = "false", required = false) Boolean force)
        throws BaseException, Exception {
        BaseResponse baseResponse = new BaseResponse(ConstantCode.SUCCESS);
        Instant startTime = Instant.now();
        log.info("start deleteContract startTime:{} contractId:{} groupId:{}",
            startTime.toEpochMilli(), contractId, groupId);

        contractService.deleteContract(chainId, contractId, groupId, force);

        log.info("end deleteContract useTime:{}",
            Duration.between(startTime, Instant.now()).toMillis());
        return baseResponse;
    }


    /**
     * remove contract
     *
     * @param param
     * @param result
     * @return
     * @throws BaseException
     */
    @PostMapping(value = "remove")
    public BaseResponse deleteContract(@RequestBody @Valid ReqContractVO param, BindingResult result) throws BaseException {
        checkBindResult(result);

        BaseResponse baseResponse = new BaseResponse(ConstantCode.SUCCESS);
        Instant startTime = Instant.now();
        log.info("start deleteContract startTime:{} param:{}", startTime.toEpochMilli(), JsonTools.objToString(param));

        contractService.deleteByContractId(param.getContractId(), param.getForce());

        log.info("end deleteContract useTime:{}",
            Duration.between(startTime, Instant.now()).toMillis());
        return baseResponse;
    }


    /**
     * qurey contract info list.
     */
    @PostMapping(value = "/contractList")
    public BasePageResponse queryContractList(@RequestBody @Valid QueryContractParam inputParam,
        BindingResult result) throws BaseException {
        checkBindResult(result);
        BasePageResponse pagesponse = new BasePageResponse(ConstantCode.SUCCESS);
        Instant startTime = Instant.now();
        log.info("start contractList. startTime:{} inputParam:{}", startTime.toEpochMilli(),
            JsonTools.toJSONString(inputParam));

        //get groupId which status is normal
        List<TbGroup> groupList = groupService.getGroupList(inputParam.getChainId(), DataStatus.NORMAL.getValue());
        List<Integer> groupIds = null;
        if (CollectionUtils.isNotEmpty(groupList))
            groupIds = groupList.stream().map(g -> g.getGroupId()).distinct().collect(Collectors.toList());

        // param
        ContractParam queryParam = new ContractParam();
        BeanUtils.copyProperties(inputParam, queryParam);
        queryParam.setGroupIdList(groupIds);


        int count = contractService.countOfContract(queryParam);
        if (count > 0) {
            Integer start = Optional.ofNullable(inputParam.getPageNumber())
                .map(page -> (page - 1) * inputParam.getPageSize()).orElse(0);
            queryParam.setStart(start);
            queryParam.setFlagSortedByTime(SqlSortType.DESC.getValue());
            // query list
            List<TbContract> listOfContract = contractService.qureyContractList(queryParam);

            pagesponse.setData(listOfContract);
            pagesponse.setTotalCount(count);
        }

        log.info("end contractList. useTime:{}",
            Duration.between(startTime, Instant.now()).toMillis());
        return pagesponse;
    }


    /**
     * @param inputParam
     * @param result
     * @return
     * @throws BaseException
     */
    @PostMapping(value = "/page")
    public BasePageResponse queryContractPage(@RequestBody @Valid ReqQueryContractPage inputParam,
        BindingResult result) throws BaseException {
        checkBindResult(result);
        BasePageResponse pagesponse = new BasePageResponse(ConstantCode.SUCCESS);
        Instant startTime = Instant.now();
        log.info("start queryContractPage. startTime:{} inputParam:{}", startTime.toEpochMilli(),
            JsonTools.toJSONString(inputParam));

        BasePageResponse basePageResponse = contractService.queryContractPage(inputParam);

        log.info("end queryContractPage useTime:{} result:{}",
            Duration.between(startTime, Instant.now()).toMillis(), JsonTools.objToString(basePageResponse));
        return basePageResponse;
    }


    /**
     * query by contract id.
     */
    @GetMapping(value = "/{contractId}")
    public BaseResponse queryContract(@PathVariable("contractId") Integer contractId)
        throws BaseException, Exception {
        BaseResponse baseResponse = new BaseResponse(ConstantCode.SUCCESS);
        Instant startTime = Instant.now();
        log.info("start queryContract startTime:{} contractId:{}", startTime.toEpochMilli(),
            contractId);

        TbContract contractRow = contractService.getByContractId(contractId);
        baseResponse.setData(contractRow);

        log.info("end queryContract useTime:{}",
            Duration.between(startTime, Instant.now()).toMillis());
        return baseResponse;
    }

    /**
     * deploy deployInputParam.
     */
    @PostMapping(value = "/deploy")
    public BaseResponse deployContract(@RequestBody @Valid DeployInputParam deployInputParam,
        BindingResult result) throws BaseException {
        checkBindResult(result);
        BaseResponse baseResponse = new BaseResponse(ConstantCode.SUCCESS);
        Instant startTime = Instant.now();
        log.info("start queryContract startTime:{} deployInputParam:{}", startTime.toEpochMilli(),
            JsonTools.toJSONString(deployInputParam));

        TbContract tbContract = contractService.deployContract(deployInputParam);
        baseResponse.setData(tbContract);

        log.info("end deployContract useTime:{}",
            Duration.between(startTime, Instant.now()).toMillis());

        return baseResponse;
    }


    @PostMapping(value = "/deployByContractId")
    public BaseResponse deployByContractId(@RequestBody @Valid ReqDeployByContractIdVO deployInputParam,
        BindingResult result) throws BaseException {
        checkBindResult(result);
        BaseResponse baseResponse = new BaseResponse(ConstantCode.SUCCESS);
        Instant startTime = Instant.now();
        log.info("start queryContract startTime:{} deployInputParam:{}", startTime.toEpochMilli(),
            JsonTools.toJSONString(deployInputParam));

        TbContract tbContract = contractService.deployByContractId(deployInputParam);
        baseResponse.setData(tbContract);

        log.info("end deployContract useTime:{}",
            Duration.between(startTime, Instant.now()).toMillis());

        return baseResponse;
    }


    /**
     * send transaction.  改用：trans/sendByContractId
     */
    @Deprecated
    @PostMapping(value = "/transaction")
    public BaseResponse sendTransaction(@RequestBody @Valid TransactionInputParam param,
        BindingResult result) throws BaseException {
        checkBindResult(result);
        Instant startTime = Instant.now();
        log.info("start sendTransaction startTime:{} param:{}", startTime.toEpochMilli(),
            JsonTools.toJSONString(param));
        BaseResponse baseResponse = new BaseResponse(ConstantCode.SUCCESS);
        Object transRsp = contractService.sendTransaction(param);
        baseResponse.setData(transRsp);
        log.info("end sendTransaction useTime:{}",
            Duration.between(startTime, Instant.now()).toMillis());

        return baseResponse;
    }

    /**
     * contract status manage.
     */
    @PostMapping(value = "/statusManage")
    public Object statusManage(@RequestBody @Valid ContractManageParam param, BindingResult result)
        throws BaseException {
        checkBindResult(result);
        Instant startTime = Instant.now();
        log.info("start statusManage startTime:{} param:{}", startTime.toEpochMilli(),
            JsonTools.toJSONString(param));

        Object contractStatusManageResult = contractService.statusManage(param);

        log.info("end statusManage useTime:{}",
            Duration.between(startTime, Instant.now()).toMillis());
        return contractStatusManageResult;
    }

    /**
     * deploy deployInputParam.
     */
    @PostMapping(value = "/deploy/transaction/{contractId}/{signUserId}")
    public Object deployByTransaction(
        @PathVariable int contractId,
        @PathVariable String signUserId,
        @RequestBody List<Object> constructorParams
    ) throws BaseException {
        Instant startTime = Instant.now();
        log.info("start deployByTransaction startTime:{}, signUserId:{}, contractId:{}",
            startTime.toEpochMilli(), signUserId, contractId);

        Object result = contractService.deployByTransactionServer(contractId, signUserId, constructorParams);
        log.info("end deployByTransaction useTime:{}", Duration.between(startTime, Instant.now()).toMillis());

        return result;
    }


    @PostMapping(value = "/send/transaction")
    public Object sendByTransaction(@RequestBody ReqTransSendInfoVO param) {
        Instant startTime = Instant.now();
        log.info("start sendByTransaction startTime:{}, param:{}",
            startTime.toEpochMilli(), JsonTools.objToString(param));
        Object result = contractService.sendByTransactionServer(param.getContractId(), param.getSignUserId(), param.getFuncName(), param.getFuncParam());
        log.info("end sendByTransaction useTime:{}", Duration.between(startTime, Instant.now()).toMillis());

        return result;
    }

    /**
     * 查询链下合约数
     *
     * @param chainId
     * @param groupId
     * @param agencyId
     * @return
     */
    @GetMapping(value = "/count")
    public BaseResponse getContractCount(@RequestParam("chainId") Integer chainId,
        @RequestParam(value = "groupId", required = false) Integer groupId,
        @RequestParam(value = "agencyId", required = false) Integer agencyId) {
        Instant startTime = Instant.now();
        log.info("start getContractCount startTime:{}, chainId:{} groupId:{} agencyId:{}",
            startTime.toEpochMilli(), chainId, groupId, agencyId);
        long count = contractManager.getCountOfContract(chainId, groupId, agencyId);
        BaseResponse baseResponse = new BaseResponse(ConstantCode.SUCCESS);
        baseResponse.setData(count);
        log.info("end getContractCount useTime:{} result:{}", Duration.between(startTime, Instant.now()).toMillis(), JsonTools.objToString(baseResponse));
        return baseResponse;
    }


    /**
     * 批量删除合约
     * @param chainId
     * @param groupId
     * @param contractPath
     * @return
     */
    @DeleteMapping("/batch/{chainId}/{groupId}")
    public BaseResponse deleteContractBatch(@PathVariable int chainId,
        @PathVariable int groupId,
        @RequestParam(value = "contractPath") String contractPath) {
        Instant startTime = Instant.now();
        log.info("start deleteContractBatch startTime:{}, chainId:{} groupId:{} contractPath:{}", startTime, chainId, groupId, contractPath);
        contractManager.deleteByChainAndGroupAndPath(chainId, groupId, contractPath);
        BaseResponse baseResponse = new BaseResponse(ConstantCode.SUCCESS);
        log.info("end deleteContractBatch useTime:{} result:{}", Duration.between(startTime, Instant.now()).toMillis(), JsonTools.objToString(baseResponse));
        return baseResponse;
    }

    /* contract path related api */
    /**
     * add contract path
     */
    @PostMapping(value = "/contractPath")
    public BaseResponse addContractPath(@Valid @RequestBody ContractPathParam param) {
        BaseResponse response = new BaseResponse(ConstantCode.SUCCESS);
        Instant startTime = Instant.now();
        log.info("start addContractPath. startTime:{} param:{}", startTime.toEpochMilli(), param);

        String contractPath = param.getContractPath();
        if ("".equals(contractPath)) {
            contractPath = "/";
        }
        int result = contractPathService.save(param.getChainId(), param.getGroupId(), contractPath,false);
        response.setData(result);

        log.info("end addContractPath. useTime:{} add result:{}",
            Duration.between(startTime, Instant.now()).toMillis(), result);
        return response;
    }


    /**
     * query contract info list.
     */
    @PostMapping(value = "/contractPath/list/{chainId}/{groupId}")
    public BasePageResponse queryContractPathList(@PathVariable("chainId") Integer chainId,
        @PathVariable("groupId") Integer groupId) {
        BasePageResponse pageResponse = new BasePageResponse(ConstantCode.SUCCESS);
        Instant startTime = Instant.now();
        log.info("start queryContractPathList. startTime:{},chainId:{},groupId:{}",
            startTime.toEpochMilli(), chainId, groupId);
        List<TbContractPath> result = contractService.queryContractPathList(chainId, groupId);
        pageResponse.setData(result);
        pageResponse.setTotalCount(result.size());

        log.info("end queryContractPathList. useTime:{} result:{}",
            Duration.between(startTime, Instant.now()).toMillis(),
            JsonTools.toJSONString(pageResponse));
        return pageResponse;
    }

    /**
     * delete contract by id. only admin batch delete contract
     */
    @DeleteMapping(value = "/batch/path")
    public BaseResponse deleteContractByPath(@Valid @RequestBody ContractPathParam param) {
        BaseResponse baseResponse = new BaseResponse(ConstantCode.SUCCESS);
        Instant startTime = Instant.now();
        log.info("start deleteContractByPath startTime:{} ContractPathParam:{}",
            startTime.toEpochMilli(), param);

        contractService.deleteByContractPath(param);

        log.info("end deleteContractByPath useTime:{}",
            Duration.between(startTime, Instant.now()).toMillis());
        return baseResponse;
    }

    /**
     * query contract info list by multi path
     */
    @PostMapping(value = "/contractList/multiPath")
    public BasePageResponse listContractByMultiPath(@RequestBody ReqContractByPath inputParam) throws BaseException {
        BasePageResponse pageResponse = new BasePageResponse(ConstantCode.SUCCESS);
        Instant startTime = Instant.now();
        log.info("start listContractByMultiPath. startTime:{} inputParam:{}",
            startTime.toEpochMilli(), JsonTools.toJSONString(inputParam));

        List<TbContract> contractList = contractService.queryContractListMultiPath(inputParam);
        pageResponse.setTotalCount(contractList.size());
        pageResponse.setData(contractList);
        log.info("end listContractByMultiPath. useTime:{} result count:{}",
            Duration.between(startTime, Instant.now()).toMillis(), contractList.size());
        return pageResponse;
    }

}
