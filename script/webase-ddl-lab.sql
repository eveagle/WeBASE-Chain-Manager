
-- ----------------------------
-- Table structure for tb_chain
-- ----------------------------
DROP TABLE IF EXISTS tb_chain;
CREATE TABLE tb_chain (
  chain_id VARCHAR(64) NOT NULL COMMENT '区块链编号',
  chain_name VARCHAR(120) DEFAULT NULL COMMENT '区块链名称',
  chain_type TINYINT(4) DEFAULT '0' COMMENT '类型（ 0-非国密 1-国密）',
  DESCRIPTION VARCHAR(1024) COMMENT '描述',
  create_time DATETIME DEFAULT NULL COMMENT '创建时间',
  modify_time DATETIME DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (chain_id),
  UNIQUE KEY unique_name (chain_name)
) ENGINE=INNODB CHARSET=utf8 COMMENT='区块链信息表';

-- ----------------------------
-- Table structure for tb_group
-- ----------------------------
CREATE TABLE IF NOT EXISTS tb_group (
    group_id VARCHAR(64) NOT NULL COMMENT '群组ID',
    chain_id VARCHAR(64) NOT NULL COMMENT '所属区块链编号',
    group_name VARCHAR(64) NOT NULL COMMENT '群组名称',
    group_status TINYINT(4) DEFAULT '1' COMMENT '状态（1-正常 2-异常）',
    node_count INT DEFAULT '0' COMMENT '群组下节点数',
    DESCRIPTION VARCHAR(1024) COMMENT '描述',
    group_type TINYINT(4) DEFAULT '1' COMMENT '群组类型(1-同步的，2-手动创建的)',
    create_time DATETIME DEFAULT NULL COMMENT '创建时间',
    modify_time DATETIME DEFAULT NULL COMMENT '修改时间',
    PRIMARY KEY (group_id,chain_id)
) COMMENT='群组信息表' ENGINE=INNODB CHARSET=utf8;


-- ----------------------------
-- Table structure for tb_front
-- ----------------------------
CREATE TABLE IF NOT EXISTS tb_front (
    group_id VARCHAR(64) NOT NULL COMMENT '群组ID',
    front_id INT(11) NOT NULL AUTO_INCREMENT COMMENT '前置服务编号',
    chain_id VARCHAR(64) NOT NULL COMMENT '所属区块链编号',
    node_id VARCHAR(250) NOT NULL COMMENT '节点编号',
    front_ip VARCHAR(16) NOT NULL COMMENT '前置服务ip',
    front_port INT(11) DEFAULT NULL COMMENT '前置服务端口',
    agency VARCHAR(32) NOT NULL COMMENT '所属机构名称',
    DESCRIPTION VARCHAR(1024) COMMENT '描述',
    create_time DATETIME DEFAULT NULL COMMENT '创建时间',
    modify_time DATETIME DEFAULT NULL COMMENT '修改时间',
    PRIMARY KEY (front_id),
    UNIQUE KEY unique_chain_node (chain_id,node_id)
) ENGINE=INNODB AUTO_INCREMENT=200001 DEFAULT CHARSET=utf8 COMMENT='前置服务信息表';


-- ----------------------------
-- Table structure for tb_front_group_map
-- ----------------------------
CREATE TABLE IF NOT EXISTS tb_front_group_map (
    map_id INT(11) NOT NULL AUTO_INCREMENT COMMENT '映射编号',
    chain_id VARCHAR(64) NOT NULL COMMENT '区块链编号',
    front_id INT(11) NOT NULL COMMENT '前置服务编号',
    group_id VARCHAR(64) NOT NULL COMMENT '群组编号',
    create_time DATETIME DEFAULT NULL COMMENT '创建时间',
    modify_time DATETIME DEFAULT NULL COMMENT '修改时间',
    PRIMARY KEY (map_id),
    UNIQUE unique_chain_front_group (chain_id,front_id,group_id)
) ENGINE=INNODB AUTO_INCREMENT=300001 DEFAULT CHARSET=utf8 COMMENT='前置群组映射表';


-- ----------------------------
-- Table structure for tb_node
-- ----------------------------
CREATE TABLE IF NOT EXISTS tb_node (
    node_id VARCHAR(250) NOT NULL  COMMENT '节点编号',
    chain_id VARCHAR(64) NOT NULL COMMENT '所属区块链编号',
    group_id VARCHAR(64) NOT NULL COMMENT '所属群组编号',
    node_name VARCHAR(120) NOT NULL COMMENT '节点名称',
    node_ip VARCHAR(16) DEFAULT NULL COMMENT '节点ip',
    p2p_port INT(11) DEFAULT NULL COMMENT '节点p2p端口',
    block_number BIGINT(20) DEFAULT '0' COMMENT '节点块高',
    pbft_view BIGINT(20) DEFAULT '0' COMMENT 'pbft_view',
    node_active TINYINT(4) DEFAULT '2' COMMENT '节点存活标识(1存活，2不存活)',
    DESCRIPTION VARCHAR(1024) COMMENT '描述',
    create_time DATETIME DEFAULT NULL COMMENT '创建时间',
    modify_time DATETIME DEFAULT NULL COMMENT '修改时间',
    PRIMARY KEY (node_id,chain_id,group_id)
) ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT='节点表';


-- ----------------------------
-- Table structure for tb_contract
-- ----------------------------
CREATE TABLE IF NOT EXISTS tb_contract (
    contract_id INT(11) NOT NULL AUTO_INCREMENT COMMENT '合约编号',
    contract_path VARCHAR(24) BINARY NOT NULL COMMENT '合约所在目录',
    contract_name VARCHAR(120) BINARY NOT NULL COMMENT '合约名称',
    chain_id VARCHAR(64) NOT NULL COMMENT '所属区块链编号',
    group_id VARCHAR(64) NOT NULL COMMENT '所属群组编号',
    contract_source MEDIUMTEXT COMMENT '合约源码',
    contract_abi MEDIUMTEXT COMMENT '编译合约生成的abi文件内容',
    contract_bin MEDIUMTEXT COMMENT '合约运行时binary，用于合约解析',
    bytecode_bin MEDIUMTEXT COMMENT '合约bytecode binary，用于部署合约',
    contract_address VARCHAR(64) DEFAULT NULL COMMENT '合约地址',
    deploy_time DATETIME DEFAULT NULL COMMENT '部署时间',
    contract_status TINYINT(4) DEFAULT '1' COMMENT '部署状态（1：未部署，2：部署成功，3：部署失败）',
    contract_type TINYINT(4) DEFAULT '0' COMMENT '合约类型(0-普通合约，1-系统合约)',
    DESCRIPTION VARCHAR(1024) COMMENT '描述',
    create_time DATETIME DEFAULT NULL COMMENT '创建时间',
    modify_time DATETIME DEFAULT NULL COMMENT '修改时间',
    PRIMARY KEY (contract_id),
    UNIQUE KEY uk_group_path_name (chain_id,group_id,contract_path,contract_name)
) ENGINE=INNODB AUTO_INCREMENT=400001 DEFAULT CHARSET=utf8 COMMENT='合约表';


alter table tb_contract modify column contract_abi longtext;
alter table tb_contract modify column contract_bin longtext;
alter table tb_contract modify column bytecode_bin longtext;



-- tb_chain
ALTER TABLE tb_chain ADD COLUMN version varchar(64) NULL DEFAULT '' COMMENT '创建链时选择的镜像版本';
ALTER TABLE tb_chain ADD COLUMN consensus_type varchar(16) NULL DEFAULT 'pbft' COMMENT '共识算法 pbft,raft';
ALTER TABLE tb_chain ADD COLUMN storage_type varchar(16) NULL DEFAULT 'RocksDB' COMMENT '存储类型（支持：RocksDB, LevelDB, MySQL）';
ALTER TABLE tb_chain ADD COLUMN chain_status tinyint(8) unsigned NOT NULL DEFAULT '0' COMMENT '链状态';
ALTER TABLE tb_chain ADD COLUMN webase_sign_addr varchar(255) NOT NULL DEFAULT '127.0.0.1:5004' COMMENT 'WeBASE-Sign 的访问地址';
ALTER TABLE tb_chain ADD COLUMN deploy_type tinyint(4) unsigned NOT NULL DEFAULT '0' COMMENT '部署类型：0，手动添加；1，部署接口';
ALTER TABLE tb_chain ADD COLUMN remark text COMMENT '部署结果';
ALTER TABLE tb_chain MODIFY COLUMN consensus_type varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '共识算法 pbft,raft';
ALTER TABLE tb_chain MODIFY COLUMN storage_type varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '存储类型（支持：RocksDB, LevelDB, MySQL）';


-- tb_group
ALTER TABLE tb_group ADD COLUMN group_timestamp varchar(64) NULL DEFAULT '' COMMENT '群组创世块时间戳';
ALTER TABLE tb_group ADD COLUMN node_id_list text COMMENT '群组创世块成员节点的ID';
ALTER TABLE tb_group ADD COLUMN epoch_sealer_num int DEFAULT '0' COMMENT '群组创世块共识节点数';


-- tb_front
ALTER TABLE tb_front ADD COLUMN front_status int(11) DEFAULT 1 COMMENT '前置服务状态：0，未创建；1，停止；2，启动；';
ALTER TABLE tb_front ADD COLUMN version varchar(64) DEFAULT '' COMMENT '运行的镜像版本标签';
ALTER TABLE tb_front ADD COLUMN container_name varchar(255) DEFAULT '' COMMENT 'Docker 启动的容器名称';
ALTER TABLE tb_front ADD COLUMN jsonrpc_port int(6) DEFAULT '8545' COMMENT 'jsonrpc 端口';
ALTER TABLE tb_front ADD COLUMN p2p_port int(6) DEFAULT '30303' COMMENT 'p2p 端口';
ALTER TABLE tb_front ADD COLUMN channel_port int(6) DEFAULT '20200' COMMENT 'channel 端口';
ALTER TABLE tb_front ADD COLUMN chain_name varchar(64) DEFAULT '' COMMENT '所属链名称，冗余字段';
ALTER TABLE tb_front ADD COLUMN ext_company_id int(10) unsigned DEFAULT '0' COMMENT '节点所在主机的所属公司 ID(Web Server)';
ALTER TABLE tb_front ADD COLUMN ext_agency_id int(10) unsigned DEFAULT '0' COMMENT '节点部署时的机构 ID(Web Server)';
ALTER TABLE tb_front ADD COLUMN ext_host_id int(10) unsigned DEFAULT '0' COMMENT '所属主机';
ALTER TABLE tb_front ADD COLUMN host_index int(6) DEFAULT '0' COMMENT '一台主机可能有多个节点。表示在主机中的编号，从 0 开始编号';
ALTER TABLE tb_front ADD COLUMN ssh_user varchar(32) NOT NULL DEFAULT 'root' COMMENT 'SSH 登录账号';
ALTER TABLE tb_front ADD COLUMN ssh_port int(11) NOT NULL DEFAULT 22 COMMENT 'SSH 登录 端口';
ALTER TABLE tb_front ADD COLUMN docker_port int(11) NOT NULL DEFAULT 3000 COMMENT 'Docker demon 端口';
ALTER TABLE tb_front ADD COLUMN root_on_host varchar(256) NULL DEFAULT '/opt/fisco' COMMENT 'front 所在主机存放节点数据的根目录，比如: /opt/fisco';
ALTER TABLE tb_front ADD COLUMN node_root_on_host varchar(256) NULL DEFAULT '' COMMENT '节点根目录，比如: /opt/fisco/[chain_name]/node0';
ALTER TABLE tb_front ADD COLUMN front_peer_name varchar(64) DEFAULT null COMMENT 'k8s节点名称' AFTER chain_id;
ALTER TABLE tb_group ADD CONSTRAINT group_name_un UNIQUE KEY (group_name);
ALTER TABLE tb_front MODIFY COLUMN ssh_user varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT 'SSH 登录账号';
ALTER TABLE tb_front MODIFY COLUMN ssh_port int(11) NULL COMMENT 'SSH 登录 端口';
ALTER TABLE tb_front MODIFY COLUMN docker_port int(11) NULL COMMENT 'Docker demon 端口';
ALTER TABLE tb_front MODIFY COLUMN root_on_host varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT 'front 所在主机存放节点数据的根目录，比如: /opt/fisco';
ALTER TABLE tb_front MODIFY COLUMN jsonrpc_port int(6) NULL COMMENT 'jsonrpc 端口';
ALTER TABLE tb_front MODIFY COLUMN p2p_port int(6) NULL COMMENT 'p2p 端口';
ALTER TABLE tb_front MODIFY COLUMN channel_port int(6) NULL COMMENT 'channel 端口';
ALTER TABLE tb_front ADD CONSTRAINT unique_frontip_frontport_frontPeerName UNIQUE KEY (front_ip,front_port,front_peer_name);
CREATE UNIQUE INDEX unique_chainid_agencyid_frontip_frontport USING BTREE ON tb_front (`chain_id`, `ext_agency_id`,`front_ip`,`front_port`);



-- tb_front_group_map
ALTER TABLE tb_front_group_map ADD COLUMN front_status tinyint(8) unsigned NOT NULL DEFAULT '0' COMMENT '节点状态: 0 游离；1 共识；2 观察';




-- tb_method
CREATE TABLE IF NOT EXISTS tb_method (
	id int(11) NOT NULL AUTO_INCREMENT COMMENT '自增编号',
	contract_id int(11) NOT NULL COMMENT '所属合约编号',
	chain_id VARCHAR(64) NOT NULL COMMENT '所属区块链编号',
	group_id VARCHAR(64) NOT NULL COMMENT '所属群组编号',
	method_id varchar(128) COMMENT '方法id',
	method_name varchar(128) COMMENT '方法名',
	method_type varchar(32) COMMENT '方法类型',
	create_time datetime DEFAULT NULL COMMENT '创建时间',
	modify_time datetime DEFAULT NULL COMMENT '修改时间',
	PRIMARY KEY (id),
	UNIQUE KEY uk_method (contract_id,method_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='方法解析信息表';





-- tb_node
ALTER TABLE tb_node ADD COLUMN node_type varchar(64) DEFAULT 'sealer' COMMENT '节点类型（sealer、observer、remove）' AFTER node_name;


-- tb_user
CREATE TABLE IF NOT EXISTS tb_user (
  id int(11) NOT NULL AUTO_INCREMENT COMMENT '用户自增编号',
  sign_user_id varchar(64) NOT NULL COMMENT '用于唯一id',
  user_name varchar(64) binary NOT NULL COMMENT '用户名',
  chain_id VARCHAR(64) NOT NULL COMMENT '链 ID',
  group_id VARCHAR(64) NOT NULL COMMENT '所属群组编号',
  user_status int(1) NOT NULL DEFAULT '1' COMMENT '状态（1-正常 2-停用）',
  gmt_create datetime DEFAULT NULL COMMENT '创建时间',
  gmt_modified datetime DEFAULT NULL COMMENT '修改时间',
  description varchar(250) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (id),
  UNIQUE KEY unique_sign_user_id (sign_user_id),
  UNIQUE KEY unique_chain_group_user (chain_id,group_id,user_name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='密钥用户信息表';

ALTER TABLE tb_user ADD COLUMN address varchar(64) DEFAULT NULL COMMENT '用户公钥地址' AFTER user_name;



-- tb_task
CREATE TABLE IF NOT EXISTS tb_task (
  id int(11) NOT NULL AUTO_INCREMENT COMMENT '任务自增编号',
  task_type tinyint(8) NOT NULL DEFAULT '1' COMMENT '任务类型（1：将节点类型变更为sealer）',
  task_status tinyint(8) NOT NULL DEFAULT '0' COMMENT '状态（0-未开始，1-准备开始，2-处理中，3-成功，4-失败）',
  chain_id VARCHAR(64)  NOT NULL NULL COMMENT '链 ID',
  group_id VARCHAR(64) NOT NULL COMMENT '所属群组编号',
  node_id varchar(250) NOT NULL  COMMENT '节点编号',
  current_handler_host varchar(250) COMMENT '当前处理的机器',
  gmt_create datetime DEFAULT NULL COMMENT '创建时间',
  gmt_modified datetime DEFAULT NULL COMMENT '修改时间',
  description varchar(250) DEFAULT NULL COMMENT '描述',
  remark text COMMENT '备注',
  PRIMARY KEY (id),
    UNIQUE KEY unique_chain_group_node (chain_id,group_id,node_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='任务信息表';



-- tb_contract
ALTER TABLE tb_contract ADD COLUMN save_by_agency int(10) DEFAULT  NULL COMMENT '合约发起机构（合约首次保存的机构Id）' AFTER group_id;

-- tb_node
ALTER TABLE tb_node MODIFY COLUMN node_name varchar(255) NOT NULL COMMENT '节点名称';
