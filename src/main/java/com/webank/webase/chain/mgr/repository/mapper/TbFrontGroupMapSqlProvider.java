package com.webank.webase.chain.mgr.repository.mapper;

import org.apache.ibatis.jdbc.SQL;
import com.webank.webase.chain.mgr.frontgroupmap.entity.MapListParam;
import com.webank.webase.chain.mgr.repository.bean.TbFrontGroupMap;
import com.webank.webase.chain.mgr.repository.bean.TbFrontGroupMapExample.Criteria;
import com.webank.webase.chain.mgr.repository.bean.TbFrontGroupMapExample.Criterion;
import com.webank.webase.chain.mgr.repository.bean.TbFrontGroupMapExample;
import java.util.List;
import java.util.Map;

public class TbFrontGroupMapSqlProvider {

    public String countByParam(MapListParam param) {
        SQL sql = new SQL();
        sql.SELECT("count(1)").FROM("tb_front_group_map");
        this.applyContractParam(sql, param);
        return sql.toString();
    }

    public SQL applyContractParam(SQL sql, MapListParam param) {
        if (param.getMapId() != null) {
            sql.WHERE("map_id = #{mapId}");
        }
        if (param.getFrontId() != null) {
            sql.WHERE("front_id = #{frontId}");
        }
        if (param.getGroupId() != null) {
            sql.WHERE("group_id = #{groupId}");
        }
        if (param.getChainId() != null) {
            sql.WHERE("chain_id = #{chainId}");
        }
        return sql;
    }

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table tb_front_group_map
     *
     * @mbg.generated
     */
    public static final String ALL_COLUMN_FIELDS = "map_id,chain_id,front_id,group_id,create_time,modify_time,front_status";

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_front_group_map
     *
     * @mbg.generated
     */
    public String countByExample(TbFrontGroupMapExample example) {
        SQL sql = new SQL();
        sql.SELECT("count(*)").FROM("tb_front_group_map");
        applyWhere(sql, example, false);
        return sql.toString();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_front_group_map
     *
     * @mbg.generated
     */
    public String deleteByExample(TbFrontGroupMapExample example) {
        SQL sql = new SQL();
        sql.DELETE_FROM("tb_front_group_map");
        applyWhere(sql, example, false);
        return sql.toString();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_front_group_map
     *
     * @mbg.generated
     */
    public String insertSelective(TbFrontGroupMap record) {
        SQL sql = new SQL();
        sql.INSERT_INTO("tb_front_group_map");
        if (record.getChainId() != null) {
            sql.VALUES("chain_id", "#{chainId,jdbcType=INTEGER}");
        }
        if (record.getFrontId() != null) {
            sql.VALUES("front_id", "#{frontId,jdbcType=INTEGER}");
        }
        if (record.getGroupId() != null) {
            sql.VALUES("group_id", "#{groupId,jdbcType=INTEGER}");
        }
        if (record.getCreateTime() != null) {
            sql.VALUES("create_time", "#{createTime,jdbcType=TIMESTAMP}");
        }
        if (record.getModifyTime() != null) {
            sql.VALUES("modify_time", "#{modifyTime,jdbcType=TIMESTAMP}");
        }
        if (record.getFrontStatus() != null) {
            sql.VALUES("front_status", "#{frontStatus,jdbcType=TINYINT}");
        }
        return sql.toString();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_front_group_map
     *
     * @mbg.generated
     */
    public String selectByExample(TbFrontGroupMapExample example) {
        SQL sql = new SQL();
        if (example != null && example.isDistinct()) {
            sql.SELECT_DISTINCT("map_id");
        } else {
            sql.SELECT("map_id");
        }
        sql.SELECT("chain_id");
        sql.SELECT("front_id");
        sql.SELECT("group_id");
        sql.SELECT("create_time");
        sql.SELECT("modify_time");
        sql.SELECT("front_status");
        sql.FROM("tb_front_group_map");
        applyWhere(sql, example, false);
        if (example != null && example.getOrderByClause() != null) {
            sql.ORDER_BY(example.getOrderByClause());
        }
        // add pagination for mysql with limit clause 
        StringBuilder sqlBuilder = new StringBuilder(sql.toString());
        if (example != null && (example.getStart() > -1 || example.getCount() > -1)) {
            sqlBuilder.append(" limit ");
            if (example.getStart() > -1 && example.getCount() > -1) {
                sqlBuilder.append(example.getStart()).append(",").append(example.getCount());
            } else if (example.getStart() > -1) {
                sqlBuilder.append(example.getStart());
            } else if (example.getCount() > -1) {
                sqlBuilder.append(example.getCount());
            }
        }
        return sqlBuilder.toString();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_front_group_map
     *
     * @mbg.generated
     */
    public String updateByExampleSelective(Map<String, Object> parameter) {
        TbFrontGroupMap record = (TbFrontGroupMap) parameter.get("record");
        TbFrontGroupMapExample example = (TbFrontGroupMapExample) parameter.get("example");
        SQL sql = new SQL();
        sql.UPDATE("tb_front_group_map");
        if (record.getMapId() != null) {
            sql.SET("map_id = #{record.mapId,jdbcType=INTEGER}");
        }
        if (record.getChainId() != null) {
            sql.SET("chain_id = #{record.chainId,jdbcType=INTEGER}");
        }
        if (record.getFrontId() != null) {
            sql.SET("front_id = #{record.frontId,jdbcType=INTEGER}");
        }
        if (record.getGroupId() != null) {
            sql.SET("group_id = #{record.groupId,jdbcType=INTEGER}");
        }
        if (record.getCreateTime() != null) {
            sql.SET("create_time = #{record.createTime,jdbcType=TIMESTAMP}");
        }
        if (record.getModifyTime() != null) {
            sql.SET("modify_time = #{record.modifyTime,jdbcType=TIMESTAMP}");
        }
        if (record.getFrontStatus() != null) {
            sql.SET("front_status = #{record.frontStatus,jdbcType=TINYINT}");
        }
        applyWhere(sql, example, true);
        return sql.toString();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_front_group_map
     *
     * @mbg.generated
     */
    public String updateByExample(Map<String, Object> parameter) {
        SQL sql = new SQL();
        sql.UPDATE("tb_front_group_map");
        sql.SET("map_id = #{record.mapId,jdbcType=INTEGER}");
        sql.SET("chain_id = #{record.chainId,jdbcType=INTEGER}");
        sql.SET("front_id = #{record.frontId,jdbcType=INTEGER}");
        sql.SET("group_id = #{record.groupId,jdbcType=INTEGER}");
        sql.SET("create_time = #{record.createTime,jdbcType=TIMESTAMP}");
        sql.SET("modify_time = #{record.modifyTime,jdbcType=TIMESTAMP}");
        sql.SET("front_status = #{record.frontStatus,jdbcType=TINYINT}");
        TbFrontGroupMapExample example = (TbFrontGroupMapExample) parameter.get("example");
        applyWhere(sql, example, true);
        return sql.toString();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_front_group_map
     *
     * @mbg.generated
     */
    public String updateByPrimaryKeySelective(TbFrontGroupMap record) {
        SQL sql = new SQL();
        sql.UPDATE("tb_front_group_map");
        if (record.getChainId() != null) {
            sql.SET("chain_id = #{chainId,jdbcType=INTEGER}");
        }
        if (record.getFrontId() != null) {
            sql.SET("front_id = #{frontId,jdbcType=INTEGER}");
        }
        if (record.getGroupId() != null) {
            sql.SET("group_id = #{groupId,jdbcType=INTEGER}");
        }
        if (record.getCreateTime() != null) {
            sql.SET("create_time = #{createTime,jdbcType=TIMESTAMP}");
        }
        if (record.getModifyTime() != null) {
            sql.SET("modify_time = #{modifyTime,jdbcType=TIMESTAMP}");
        }
        if (record.getFrontStatus() != null) {
            sql.SET("front_status = #{frontStatus,jdbcType=TINYINT}");
        }
        sql.WHERE("map_id = #{mapId,jdbcType=INTEGER}");
        return sql.toString();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_front_group_map
     *
     * @mbg.generated
     */
    protected void applyWhere(SQL sql, TbFrontGroupMapExample example, boolean includeExamplePhrase) {
        if (example == null) {
            return;
        }
        String parmPhrase1;
        String parmPhrase1_th;
        String parmPhrase2;
        String parmPhrase2_th;
        String parmPhrase3;
        String parmPhrase3_th;
        if (includeExamplePhrase) {
            parmPhrase1 = "%s #{example.oredCriteria[%d].allCriteria[%d].value}";
            parmPhrase1_th = "%s #{example.oredCriteria[%d].allCriteria[%d].value,typeHandler=%s}";
            parmPhrase2 = "%s #{example.oredCriteria[%d].allCriteria[%d].value} and #{example.oredCriteria[%d].criteria[%d].secondValue}";
            parmPhrase2_th = "%s #{example.oredCriteria[%d].allCriteria[%d].value,typeHandler=%s} and #{example.oredCriteria[%d].criteria[%d].secondValue,typeHandler=%s}";
            parmPhrase3 = "#{example.oredCriteria[%d].allCriteria[%d].value[%d]}";
            parmPhrase3_th = "#{example.oredCriteria[%d].allCriteria[%d].value[%d],typeHandler=%s}";
        } else {
            parmPhrase1 = "%s #{oredCriteria[%d].allCriteria[%d].value}";
            parmPhrase1_th = "%s #{oredCriteria[%d].allCriteria[%d].value,typeHandler=%s}";
            parmPhrase2 = "%s #{oredCriteria[%d].allCriteria[%d].value} and #{oredCriteria[%d].criteria[%d].secondValue}";
            parmPhrase2_th = "%s #{oredCriteria[%d].allCriteria[%d].value,typeHandler=%s} and #{oredCriteria[%d].criteria[%d].secondValue,typeHandler=%s}";
            parmPhrase3 = "#{oredCriteria[%d].allCriteria[%d].value[%d]}";
            parmPhrase3_th = "#{oredCriteria[%d].allCriteria[%d].value[%d],typeHandler=%s}";
        }
        StringBuilder sb = new StringBuilder();
        List<Criteria> oredCriteria = example.getOredCriteria();
        boolean firstCriteria = true;
        for (int i = 0; i < oredCriteria.size(); i++) {
            Criteria criteria = oredCriteria.get(i);
            if (criteria.isValid()) {
                if (firstCriteria) {
                    firstCriteria = false;
                } else {
                    sb.append(" or ");
                }
                sb.append('(');
                List<Criterion> criterions = criteria.getAllCriteria();
                boolean firstCriterion = true;
                for (int j = 0; j < criterions.size(); j++) {
                    Criterion criterion = criterions.get(j);
                    if (firstCriterion) {
                        firstCriterion = false;
                    } else {
                        sb.append(" and ");
                    }
                    if (criterion.isNoValue()) {
                        sb.append(criterion.getCondition());
                    } else if (criterion.isSingleValue()) {
                        if (criterion.getTypeHandler() == null) {
                            sb.append(String.format(parmPhrase1, criterion.getCondition(), i, j));
                        } else {
                            sb.append(String.format(parmPhrase1_th, criterion.getCondition(), i, j, criterion.getTypeHandler()));
                        }
                    } else if (criterion.isBetweenValue()) {
                        if (criterion.getTypeHandler() == null) {
                            sb.append(String.format(parmPhrase2, criterion.getCondition(), i, j, i, j));
                        } else {
                            sb.append(String.format(parmPhrase2_th, criterion.getCondition(), i, j, criterion.getTypeHandler(), i, j, criterion.getTypeHandler()));
                        }
                    } else if (criterion.isListValue()) {
                        sb.append(criterion.getCondition());
                        sb.append(" (");
                        List<?> listItems = (List<?>) criterion.getValue();
                        boolean comma = false;
                        for (int k = 0; k < listItems.size(); k++) {
                            if (comma) {
                                sb.append(", ");
                            } else {
                                comma = true;
                            }
                            if (criterion.getTypeHandler() == null) {
                                sb.append(String.format(parmPhrase3, i, j, k));
                            } else {
                                sb.append(String.format(parmPhrase3_th, i, j, k, criterion.getTypeHandler()));
                            }
                        }
                        sb.append(')');
                    }
                }
                sb.append(')');
            }
        }
        if (sb.length() > 0) {
            sql.WHERE(sb.toString());
        }
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_front_group_map
     *
     * @mbg.generated
     */
    public String getOneByExample(TbFrontGroupMapExample example) {
        SQL sql = new SQL();
        if (example != null && example.isDistinct()) {
            sql.SELECT_DISTINCT("map_id");
        } else {
            sql.SELECT("map_id");
        }
        sql.SELECT("chain_id");
        sql.SELECT("front_id");
        sql.SELECT("group_id");
        sql.SELECT("create_time");
        sql.SELECT("modify_time");
        sql.SELECT("front_status");
        sql.FROM("tb_front_group_map");
        applyWhere(sql, example, false);
        if (example != null && example.getOrderByClause() != null) {
            sql.ORDER_BY(example.getOrderByClause());
        }
        StringBuilder sqlBuilder = new StringBuilder(sql.toString());
        sqlBuilder.append(" limit 1 ");
        return sqlBuilder.toString();
    }
}
