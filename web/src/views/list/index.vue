/*
 * Copyright 2014-2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
<template>
  <div>
    <v-content-head
      :headTitle="'合约列表'"
      @changGroup="changGroup"
    ></v-content-head>
    <div class="module-wrapper">
      <div class="search-part">
        <!-- <div class="search-part-left" style="padding-top: 20px;">
                    <el-button type="primary" class="search-part-left-btn" @click="generateAbi">{{this.$t("nodes.addAbi")}}</el-button>
                    <el-button type="primary" class="search-part-left-btn" @click="routeAbi">{{$t('title.parseAbi')}}</el-button>
                </div> -->
        <div class="search-part-right">
          群组切换：
          <el-select v-model="groupId" placeholder="请选择" @change="search">
            <el-option
              v-for="item in groupList"
              :key="item.groupId"
              :label="item.groupName"
              :value="item.groupId"
            >
            </el-option>
          </el-select>
        </div>
      </div>
      <div class="search-table">
        <el-table
          :data="contractList"
          tooltip-effect="dark"
          v-loading="loading"
          stripe
        >
          <el-table-column
            prop="contractId"
            label="合约ID"
            show-overflow-tooltip
            width="100"
          >
            <template slot-scope="scope">
              <span>{{ scope.row.contractId }}</span>
            </template>
          </el-table-column>
          <el-table-column
            prop="contractAddress"
            label="合约地址"
            show-overflow-tooltip
            width="150"
            align="center"
          >
            <template slot-scope="scope">
              <i
                class="el-icon-document-copy"
                v-if="scope.row.contractAddress"
                @click="copyPubilcKey(scope.row.contractAddress)"
                title="复制合约地址"
              ></i>
              <span>{{ scope.row.contractAddress }}</span>
            </template>
          </el-table-column>
          <el-table-column
            prop="contractName"
            label="合约名称"
            show-overflow-tooltip
            width="120"
            align="center"
          >
            <template slot-scope="scope">
              <!-- <span class="link" @click='open(scope.row)' v-if='scope.row.contractId'>{{scope.row.contractName}}</span> -->
              <span>{{ scope.row.contractName }}</span>
            </template>
          </el-table-column>
          <el-table-column
            prop="contractPath"
            label="合约目录"
            show-overflow-tooltip
            width="135"
            align="center"
          >
            <template slot-scope="scope">
              <!-- <span class="link" @click='openPath(scope.row)' v-if='scope.row.contractPath'>{{scope.row.contractPath}}</span> -->
              <span>{{ scope.row.contractPath }}</span>
            </template>
          </el-table-column>
          <el-table-column
            prop="contractStatus"
            label="合约状态"
            show-overflow-tooltip
            width="135"
            align="center"
          >
            <template slot-scope="scope">
              <span :class="{ deployed: scope.row.contractStatus == 2 }">{{
                isDeploy(scope.row.contractStatus)
              }}</span>
            </template>
          </el-table-column>
          <el-table-column
            prop="contractAbi"
            label="合约ABI"
            show-overflow-tooltip
            width="400"
            align="center"
          >
            <template slot-scope="scope">
              <i
                class="el-icon-document-copy"
                v-if="scope.row.contractAbi"
                @click="copyPubilcKey(scope.row.contractAbi)"
                title="复制合约ABI"
              ></i>
              <span>{{ scope.row.contractAbi }}</span>
            </template>
          </el-table-column>
          <el-table-column
            prop="contractBin"
            label="合约BIN"
            show-overflow-tooltip
            width="400"
            align="center"
          >
            <template slot-scope="scope">
              <i
                class="el-icon-document-copy"
                v-if="scope.row.contractBin"
                @click="copyPubilcKey(scope.row.contractBin)"
                title="复制合约BIN"
              ></i>
              <span>{{ scope.row.contractBin }}</span>
            </template>
          </el-table-column>
          <el-table-column
            prop="createTime"
            label="创建时间"
            show-overflow-tooltip
            width="150"
            align="center"
          >
            <!-- <template slot-scope="scope">
                            <span>{{format(scope.row.createTime,"HH:mm:ss") }}</span>
                        </template> -->
          </el-table-column>
          <!-- <el-table-column fixed="right" label="操作" width="360">
                        <template slot-scope="scope">
                           
                            <el-button :disabled="disabled" :class="{'grayColor': disabled}" @click="deleteAbi(scope.row)" type="text" size="small">删除</el-button>
                          
                        </template>
                    </el-table-column> -->
        </el-table>
        <el-pagination
          v-if="total > 10"
          class="page"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="currentPage"
          :page-sizes="[10, 20, 30, 50]"
          :page-size="pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total"
        >
        </el-pagination>
      </div>
    </div>
    <!-- <abi-dialog :show="abiDialogShow" v-if="abiDialogShow" :data='abiData' @close="abiClose"></abi-dialog>
        <el-dialog :title="$t('contracts.sendTransaction')" :visible.sync="dialogVisible" width="580px" :before-close="sendClose" v-if="dialogVisible" center class="send-dialog">
            <send-transation @success="sendSuccess($event)" @close="handleClose" ref="send" :data="data" :abi='abiData' :version='version'></send-transation>
        </el-dialog>
        <v-editor v-if='editorShow' :show='editorShow' :data='editorData' :input='editorInput' :editorOutput="editorOutput" :sendConstant="sendConstant" @close='editorClose'></v-editor>
        <el-dialog title="" :visible.sync="freezeThawVisible" width="500px" v-if="freezeThawVisible" center>
            <freeze-thaw @freezeThawSuccess="freezeThawSuccess" @freezeThawClose="freezeThawClose" :contractInfo="contractInfo" :handleFreezeThawType="handleFreezeThawType"></freeze-thaw>
        </el-dialog>
        <el-dialog v-if="checkEventVisible" :title="$t('table.checkEvent')" :visible.sync="checkEventVisible" width="470px" center class="send-dialog">
            <check-event-dialog @checkEventSuccess="checkEventSuccess($event)" @checkEventClose="checkEventClose" :contractInfo="contractInfo"></check-event-dialog>
        </el-dialog>
        <el-dialog v-if="checkEventResultVisible" :title="$t('table.checkEventResult')" :visible.sync="checkEventResultVisible" width="670px" center class="send-dialog">
            <check-event-result @checkEventResultSuccess="checkEventResultSuccess($event)" @checkEventResultClose="checkEventResultClose" :checkEventResult="checkEventResult"></check-event-result>
        </el-dialog>
        <el-dialog v-if="mgmtCnsVisible" :title="$t('text.cns')" :visible.sync="mgmtCnsVisible" width="470px" center class="send-dialog">
            <mgmt-cns :mgmtCnsItem="mgmtCnsItem" :contractName="contractName" @mgmtCnsResultSuccess="mgmtCnsResultSuccess($event)" @mgmtCnsResultClose="mgmtCnsResultClose"></mgmt-cns>
        </el-dialog>
        <el-dialog :title="$t('nodes.addAbi')" :visible.sync="importVisibility" width="500px" v-if="importVisibility" center class="send-dialog">
            <import-abi @importSuccess="importSuccess" @closeImport="closeImport"></import-abi>
        </el-dialog>
        <el-dialog :title="$t('nodes.updateAbi')" :visible.sync="updateVisibility" width="500px" v-if="updateVisibility" center class="send-dialog">
            <update-abi @updateSuccess="updateSuccess" @closeUpdate="closeUpdate" :updateItem="updateItem"></update-abi>
        </el-dialog> -->
  </div>
</template>
<script>
// import sendTransation from "@/components/sendTransaction";
// import editor from "@/components/editor"
// import abiDialog from "./dialog/abiDialog"
// import freezeThaw from "./dialog/freezeThaw"
// import checkEventDialog from "./dialog/checkEventDialog"
// import checkEventResult from "./dialog/checkEventResult"
// import mgmtCns from "./dialog/mgmtCns"
import { queryContractListUsingPOST, getGroups } from "@/api/api";
// import importAbi from "../abiList/components/importAbi"
// import updateAbi from "../abiList/components/updateAbi"
import router from "@/router";
import contentHead from "@/components/contentHead";
import errCode from "@/util/errCode";
import { format, numberFormat } from "@/util/util.js";
// import func from 'vue-editor-bridge';
export default {
  name: "registeredContract",
  components: {
    // "v-editor": editor,
    // "abi-dialog": abiDialog,
    // "send-transation": sendTransation,
    // freezeThaw,
    // checkEventDialog,
    // checkEventResult,
    // mgmtCns,
    // importAbi,
    // updateAbi
    "v-content-head": contentHead,
  },
  data: function () {
    return {
      contractList: [],
      loading: false,
      currentPage: 1,
      pageSize: 10,
      editorShow: false,
      editorData: null,
      abiDialogShow: false,
      abiData: null,
      contractData: "",
      contractName: "",
      contractAddress: "",
      version: "",
      data: null,
      dialogVisible: false,
      total: 0,
      disabled: false,
      editorInput: null,
      editorOutput: null,
      freezeThaw: "",
      freezeThawVisible: false,
      contractInfo: {},
      handleFreezeThawType: "",
      historyCurrentPage: 1,
      historyPageSize: 10,
      historyTotal: 0,
      btnIndex: "",
      btnLoading: false,
      adminRivateKeyList: [],
      contractHistoryList: [],
      checkEventVisible: false,
      checkEventResultVisible: false,
      // contractInfo: null,
      checkEventResult: null,
      groupId: localStorage.getItem("groupId"),
      mgmtCnsVisible: false,
      mgmtCnsItem: {},
      importVisibility: false,
      updateVisibility: false,
      updateItem: null,
      groupList: [],
    };
  },
  created() {
    if (this.$route.query) {
      this.contractName = this.$route.query.contractName;
    }
  },
  mounted: function () {
    if (localStorage.getItem("chainId")) {
      this.getGroupList();
    }
  },
  methods: {
    isDeploy(num) {
      if (num == 1) {
        return "未部署";
      } else {
        return "已部署";
      }
    },
    formatterPath: function (row) {
      let str = row ? row : "-";
      return str;
    },
    changGroup: function (data) {
      //this.groupId = data;
      this.getGroupList();
    },
    getGroupList: function () {
      getGroups(localStorage.getItem("chainId"))
        .then((res) => {
          if (res.data.code === 0) {
            this.groupList = res.data.data;
            if (res.data.data.length) {
              this.groupId = res.data.data[0].groupId;
              this.getList();
            } else {
              this.groupId = "";
              this.contractList = [];
            }
          } else {
            this.$message({
              type: "error",
              message: errCode.errCode[res.data.code].zh,
            });
          }
        })
        .catch((err) => {
          this.$message({
            type: "error",
            message: "系统错误",
          });
        });
    },
    getList() {
      const data = {
        // groupId: this.groupId,
        // pageNumber: this.currentPage,
        // pageSize: this.pageSize,

        chainId: localStorage.getItem("chainId"),
        groupId: this.groupId,
        pageNumber: this.currentPage,
        pageSize: this.pageSize,
      };
      queryContractListUsingPOST(data)
        .then((res) => {
          if (res.data.code == 0) {
            this.total = res.data.totalCount || 0;
            this.contractList = res.data.data;
            // console.log(dataArray);
            this.contractList.forEach(function (item, index, arr) {
              arr[index].createTime = format(
                item.createTime,
                "yyyy-MM-dd HH:mm:ss"
              );
            });
          } else {
            this.$message({
              type: "error",
              message: errCode.errCode[res.data.code].zh,
            });
          }
        })
        .catch((err) => {
          this.$message({
            type: "error",
            message: "系统错误",
          });
        });
    },
    getContracts: function () {
      const data = {
        groupId: this.groupId,
        pageNumber: this.currentPage,
        pageSize: this.pageSize,
        // contractName: this.contractName,
        // contractAddress: this.contractAddress,
        // contractStatus: 2
      };
      const query = {};
      if (localStorage.getItem("root") === "developer") {
        query.account = localStorage.getItem("user");
      }
      if (this.contractName) {
        query.contractName = this.contractName;
      }
      if (this.contractAddress) {
        query.contractAddress = this.contractAddress;
      }
    },
    queryAllContractStatus(contractAddressList, dataArray) {
      let data = {
        groupId: this.groupId,
        addressList: contractAddressList,
      };
      getAllContractStatus(data)
        .then((res) => {
          if (res.data.code == 0) {
            let statusList = res.data.data;
            for (let key in statusList) {
              dataArray.forEach((item) => {
                if (item.contractBin) {
                  item.bin = item.contractBin.substring(0, 6) + "...";
                } else {
                  item.bin = item.contractBin;
                }
                if (key == item.contractAddress) {
                  item.handleType = statusList[key];
                }
              });
            }
            this.contractList = dataArray;
          } else {
            this.$message({
              message: this.$chooseLang(res.data.code),
              type: "error",
              duration: 2000,
            });
          }
        })
        .catch((err) => {
          this.$message({
            message: err.data || this.$t("text.systemError"),
            type: "error",
            duration: 2000,
          });
        });
    },
    getUserData() {
      let reqData = {
        groupId: this.groupId,
        pageNumber: 1,
        pageSize: 1000,
      };
      getUserList(reqData, {})
        .then((res) => {
          if (res.data.code === 0) {
            let arr = res.data.data;
            this.adminRivateKeyList = arr;
          } else {
            this.$message({
              message: this.$chooseLang(res.data.code),
              type: "error",
              duration: 2000,
            });
          }
        })
        .catch((err) => {
          this.$message({
            message: err.data || this.$t("text.systemError"),
            type: "error",
            duration: 2000,
          });
        });
    },
    deleteHistory(val) {
      this.$confirm(this.$t("govCommittee.delete") + "?", {
        center: true,
      })
        .then((_) => {
          this.btnIndex = val.id;
          this.btnLoading = true;
          deleteHandleHistory(val.id)
            .then((res) => {
              this.btnLoading = false;
              if (res.data.code === 0) {
                this.$message({
                  type: "success",
                  message: this.$t("govCommittee.success"),
                });
              } else {
                this.$message({
                  message: this.$chooseLang(res.data.code),
                  type: "error",
                  duration: 2000,
                });
              }
            })
            .catch((err) => {
              this.btnLoading = false;
              this.$message({
                message: err.data || this.$t("text.systemError"),
                type: "error",
                duration: 2000,
              });
            });
        })
        .catch((_) => {});
    },
    // 打开添加abi弹窗
    generateAbi() {
      this.importVisibility = true;
    },
    closeImport() {
      this.importVisibility = false;
    },
    importSuccess() {
      this.importVisibility = false;
      this.getContracts();
    },
    closeUpdate() {
      this.updateVisibility = false;
    },
    updateAbi(val) {
      this.updateItem = val;
      this.updateVisibility = true;
    },
    updateSuccess() {
      this.updateVisibility = false;
      this.getContracts();
    },
    routeAbi() {
      this.$router.push("/parseAbi");
    },
    deleteAbi(val) {
      this.$confirm(this.$t("text.confirmDelete"))
        .then((_) => {
          this.deleteData(val);
        })
        .catch((_) => {});
    },
    // 删除功能，当仅存在abiId是调用删除abi接口
    // 当仅存在contractId时调用删除合约接口
    // 当同时存在abiId和contractId时，两个接口都调用
    async deleteData(val) {
      if (val.abiId && !val.contractId) {
        this.deleteAbiData(val);
      } else if (!val.abiId && val.contractId) {
        this.deleteContractData(val);
      } else {
        await this.deleteAbiData(val, "wait");
        await this.deleteContractData(val);
      }
    },
    async deleteAbiData(val, type) {
      this.loading = true;
      await deleteImportAbi(val.abiId)
        .then((res) => {
          this.loading = false;
          if (type) {
            this.loading = true;
          }
          if (res.data.code === 0) {
            this.$message({
              type: "success",
              message: this.$t("text.resetSuccess"),
            });
            this.getContracts();
          } else {
            this.$message({
              type: "error",
              message: this.$chooseLang(res.data.code),
            });
          }
        })
        .catch((err) => {
          this.loading = false;
          this.$message({
            type: "error",
            message: err.data || this.$t("text.systemError"),
          });
        });
    },
    async deleteContractData(val) {
      this.loading = true;
      let data = {
        groupId: localStorage.getItem("groupId"),
        contractId: val.contractId,
      };
      await deleteCode(data, {})
        .then((res) => {
          this.loading = false;
          if (res.data.code === 0) {
            // 更新vuex contractList
            let allContractList = this.$store.state.contractDataList;
            let list = allContractList.filter((item) => {
              return item.contractId !== val.contractId;
            });
            this.$store.dispatch("set_contract_dataList_action", list);
            this.getContracts();
          } else {
            this.$message({
              type: "error",
              message: this.$chooseLang(res.data.code),
            });
          }
        })
        .catch((err) => {
          this.loading = false;
          this.$message({
            type: "error",
            message: err.data || this.$t("text.systemError"),
          });
        });
    },
    copyPubilcKey: function (val) {
      if (!val) {
        this.$message({
          type: "fail",
          showClose: true,
          message: this.$t("text.copyErrorMsg"),
          duration: 2000,
        });
      } else {
        this.$copyText(val).then((e) => {
          this.$message({
            type: "success",
            showClose: true,
            message: this.$t("text.copySuccessMsg"),
            duration: 2000,
          });
        });
      }
    },
    open: function (val) {
      router.push({
        path: "/contract",
        query: {
          contractId: val.contractId,
          contractPath: val.contractPath,
        },
      });
    },
    openPath(val) {
      router.push({
        path: "/contract",
        query: {
          contractPath: val.contractPath,
        },
      });
    },
    editorClose: function () {
      this.editorShow = false;
    },
    openAbi: function (val) {
      this.abiData = val.contractAbi;
      this.abiDialogShow = true;
    },
    abiClose: function () {
      this.abiDialogShow = false;
      this.abiData = null;
    },
    search: function () {
      this.currentPage = 1;
      this.getList();
    },
    send: function (val) {
      this.data = val;
      this.abiData = val.contractAbi;
      this.version = val.contractVersion;
      this.dialogVisible = true;
    },
    sendClose: function () {
      this.dialogVisible = false;
    },
    handleClose: function () {
      this.dialogVisible = false;
    },
    sendSuccess: function (val) {
      this.sendConstant = val.constant;
      this.dialogVisible = false;
      this.editorShow = true;
      this.editorData = val.resData;
      this.editorInput = val.input;
      this.editorOutput = val.data.outputs;
    },
    handleSizeChange: function (val) {
      this.pageSize = val;
      this.currentPage = 1;
      this.getList();
    },
    handleCurrentChange: function (val) {
      this.currentPage = val;
      this.getList();
    },
    historySizeChange: function (val) {
      this.historyPageSize = val;
      this.historyCurrentPage = 1;
    },
    historyCurrentChange: function (val) {
      this.historyCurrentPage = val;
    },
    handleStatusBtn(val) {
      this.freezeThawVisible = true;
      this.contractInfo = val;
      if (val.handleType == 0) {
        this.handleFreezeThawType = "freeze";
      } else if (val.handleType == 1) {
        this.handleFreezeThawType = "unfreeze";
      }
    },
    freezeThawSuccess() {
      this.freezeThawVisible = false;
      this.getContracts();
    },
    freezeThawClose() {
      this.freezeThawVisible = false;
    },
    contractStatusZh(val) {
      switch (val) {
        case "0":
          return this.$t("contracts.normal");
          break;
        case "1":
          return this.$t("contracts.freeze");
          break;
      }
    },
    handleContractStatusZh(val) {
      switch (val) {
        case 0:
          return this.$t("contracts.unfreeze");
          break;
        case 1:
          return this.$t("contracts.freeze");
          break;
      }
    },
    freezeThawBtn(val) {
      switch (val.handleType) {
        case "0":
          return this.$t("contracts.freeze");
          break;
        case "1":
          return this.$t("contracts.unfreeze");
          break;
      }
    },
    checkEvent: function (val) {
      this.contractInfo = val;
      this.$router.push({
        path: "/eventCheck",
        query: {
          groupId: this.groupId,
          type: "abi",
          contractAddress: val.contractAddress,
        },
      });
    },
    checkEventSuccess(val) {
      this.checkEventResult = val;
      this.checkEventResultVisible = true;
    },
    checkEventClose() {
      this.checkEventVisible = false;
    },
    checkEventResultSuccess() {
      this.checkEventResultVisible = false;
    },
    checkEventResultClose() {
      this.checkEventResultVisible = false;
    },
    handleMgmtCns(item) {
      this.mgmtCnsVisible = true;
      this.mgmtCnsItem = item;
    },
    mgmtCnsResultSuccess() {
      this.mgmtCnsVisible = false;
    },
    mgmtCnsResultClose() {
      this.mgmtCnsVisible = false;
    },
    clearInput() {
      this.contractName = "";
      this.contractAddress = "";
      this.contractData = "";
      this.currentPage = 1;
      this.getContracts();
    },
  },
};
</script>
<style scoped>
.input-with-select >>> .el-input__inner {
  border-top-left-radius: 20px;
  border-bottom-left-radius: 20px;
  border: 1px solid #eaedf3;
  box-shadow: 0 3px 11px 0 rgba(159, 166, 189, 0.11);
}
.input-with-select >>> .el-input-group__append {
  border-top-right-radius: 20px;
  border-bottom-right-radius: 20px;
  box-shadow: 0 3px 11px 0 rgba(159, 166, 189, 0.11);
}
.input-with-select >>> .el-button {
  border: 1px solid #20d4d9;
  border-radius: inherit;
  background: #20d4d9;
  color: #fff;
}
.grayColor {
  color: #666 !important;
}
.search-table >>> .el-tooltip__popper {
  max-width: 80%;
}
</style>
<style>
.el-tooltip__popper {
  max-width: 80%;
  display: -webkit-box;
  overflow: hidden;

  padding-bottom: 5px !important;
  -webkit-line-clamp: 15;
  -webkit-box-orient: vertical;

  text-overflow: ellipsis;
}
.deployed {
  color: #20d4d9;
}
</style>


