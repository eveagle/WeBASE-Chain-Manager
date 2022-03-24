package com.webank.webase.chain.mgr.repository.mapper;

import com.webank.webase.chain.mgr.repository.bean.TbMethod;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.UpdateProvider;
import org.apache.ibatis.type.JdbcType;
import com.webank.webase.chain.mgr.repository.bean.TbMethodExample;
import java.util.List;
import java.util.Optional;
import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;

public interface TbMethodMapper {

    @Insert({ "replace into tb_method", "(contract_id,chain_id,group_id,method_id,method_name,method_type,create_time,modify_time) VALUES", "(#{contractId},#{chainId},#{groupId},#{methodId},#{methodName},#{methodType},now(),now())" })
    int replaceInto(TbMethod record);

    @Delete({ "delete from tb_method", "where contract_id = #{contractId,jdbcType=INTEGER}" })
    int deleteByContractId(Integer contractId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_method
     *
     * @mbg.generated
     */
    @SelectProvider(type = TbMethodSqlProvider.class, method = "countByExample")
    long countByExample(TbMethodExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_method
     *
     * @mbg.generated
     */
    @DeleteProvider(type = TbMethodSqlProvider.class, method = "deleteByExample")
    int deleteByExample(TbMethodExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_method
     *
     * @mbg.generated
     */
    @Delete({ "delete from tb_method", "where id = #{id,jdbcType=INTEGER}" })
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_method
     *
     * @mbg.generated
     */
    @Insert({ "insert into tb_method (contract_id, chain_id, ", "group_id, method_id, ", "method_name, method_type, ", "create_time, modify_time)", "values (#{contractId,jdbcType=INTEGER}, #{chainId,jdbcType=INTEGER}, ", "#{groupId,jdbcType=INTEGER}, #{methodId,jdbcType=VARCHAR}, ", "#{methodName,jdbcType=VARCHAR}, #{methodType,jdbcType=VARCHAR}, ", "#{createTime,jdbcType=TIMESTAMP}, #{modifyTime,jdbcType=TIMESTAMP})" })
    @SelectKey(statement = "SELECT LAST_INSERT_ID()", keyProperty = "id", before = false, resultType = Integer.class)
    int insert(TbMethod record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_method
     *
     * @mbg.generated
     */
    @InsertProvider(type = TbMethodSqlProvider.class, method = "insertSelective")
    @SelectKey(statement = "SELECT LAST_INSERT_ID()", keyProperty = "id", before = false, resultType = Integer.class)
    int insertSelective(TbMethod record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_method
     *
     * @mbg.generated
     */
    @SelectProvider(type = TbMethodSqlProvider.class, method = "selectByExample")
    @Results({ @Result(column = "id", property = "id", jdbcType = JdbcType.INTEGER, id = true), @Result(column = "contract_id", property = "contractId", jdbcType = JdbcType.INTEGER), @Result(column = "chain_id", property = "chainId", jdbcType =JdbcType.VARCHAR), @Result(column = "group_id", property = "groupId", jdbcType =JdbcType.VARCHAR), @Result(column = "method_id", property = "methodId", jdbcType = JdbcType.VARCHAR), @Result(column = "method_name", property = "methodName", jdbcType = JdbcType.VARCHAR), @Result(column = "method_type", property = "methodType", jdbcType = JdbcType.VARCHAR), @Result(column = "create_time", property = "createTime", jdbcType = JdbcType.TIMESTAMP), @Result(column = "modify_time", property = "modifyTime", jdbcType = JdbcType.TIMESTAMP) })
    List<TbMethod> selectByExample(TbMethodExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_method
     *
     * @mbg.generated
     */
    @Select({ "select", "id, contract_id, chain_id, group_id, method_id, method_name, method_type, create_time, ", "modify_time", "from tb_method", "where id = #{id,jdbcType=INTEGER}" })
    @Results({ @Result(column = "id", property = "id", jdbcType = JdbcType.INTEGER, id = true), @Result(column = "contract_id", property = "contractId", jdbcType = JdbcType.INTEGER), @Result(column = "chain_id", property = "chainId", jdbcType =JdbcType.VARCHAR), @Result(column = "group_id", property = "groupId", jdbcType =JdbcType.VARCHAR), @Result(column = "method_id", property = "methodId", jdbcType = JdbcType.VARCHAR), @Result(column = "method_name", property = "methodName", jdbcType = JdbcType.VARCHAR), @Result(column = "method_type", property = "methodType", jdbcType = JdbcType.VARCHAR), @Result(column = "create_time", property = "createTime", jdbcType = JdbcType.TIMESTAMP), @Result(column = "modify_time", property = "modifyTime", jdbcType = JdbcType.TIMESTAMP) })
    TbMethod selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_method
     *
     * @mbg.generated
     */
    @UpdateProvider(type = TbMethodSqlProvider.class, method = "updateByExampleSelective")
    int updateByExampleSelective(@Param("record") TbMethod record, @Param("example") TbMethodExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_method
     *
     * @mbg.generated
     */
    @UpdateProvider(type = TbMethodSqlProvider.class, method = "updateByExample")
    int updateByExample(@Param("record") TbMethod record, @Param("example") TbMethodExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_method
     *
     * @mbg.generated
     */
    @UpdateProvider(type = TbMethodSqlProvider.class, method = "updateByPrimaryKeySelective")
    int updateByPrimaryKeySelective(TbMethod record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_method
     *
     * @mbg.generated
     */
    @Update({ "update tb_method", "set contract_id = #{contractId,jdbcType=INTEGER},", "chain_id = #{chainId,jdbcType=INTEGER},", "group_id = #{groupId,jdbcType=INTEGER},", "method_id = #{methodId,jdbcType=VARCHAR},", "method_name = #{methodName,jdbcType=VARCHAR},", "method_type = #{methodType,jdbcType=VARCHAR},", "create_time = #{createTime,jdbcType=TIMESTAMP},", "modify_time = #{modifyTime,jdbcType=TIMESTAMP}", "where id = #{id,jdbcType=INTEGER}" })
    int updateByPrimaryKey(TbMethod record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_method
     *
     * @mbg.generated
     */
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    @Insert({ "<script>", "insert into tb_method (contract_id, ", "chain_id, group_id, ", "method_id, method_name, ", "method_type, create_time, ", "modify_time)", "values<foreach collection=\"list\" item=\"detail\" index=\"index\" separator=\",\">(#{detail.contractId,jdbcType=INTEGER}, ", "#{detail.chainId,jdbcType=INTEGER}, #{detail.groupId,jdbcType=INTEGER}, ", "#{detail.methodId,jdbcType=VARCHAR}, #{detail.methodName,jdbcType=VARCHAR}, ", "#{detail.methodType,jdbcType=VARCHAR}, #{detail.createTime,jdbcType=TIMESTAMP}, ", "#{detail.modifyTime,jdbcType=TIMESTAMP})</foreach></script>" })
    int batchInsert(List<TbMethod> list);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_method
     *
     * @mbg.generated
     */
    @SelectProvider(type = TbMethodSqlProvider.class, method = "getOneByExample")
    @Results({ @Result(column = "id", property = "id", jdbcType = JdbcType.INTEGER, id = true), @Result(column = "contract_id", property = "contractId", jdbcType = JdbcType.INTEGER), @Result(column = "chain_id", property = "chainId", jdbcType =JdbcType.VARCHAR), @Result(column = "group_id", property = "groupId", jdbcType =JdbcType.VARCHAR), @Result(column = "method_id", property = "methodId", jdbcType = JdbcType.VARCHAR), @Result(column = "method_name", property = "methodName", jdbcType = JdbcType.VARCHAR), @Result(column = "method_type", property = "methodType", jdbcType = JdbcType.VARCHAR), @Result(column = "create_time", property = "createTime", jdbcType = JdbcType.TIMESTAMP), @Result(column = "modify_time", property = "modifyTime", jdbcType = JdbcType.TIMESTAMP) })
    Optional<TbMethod> getOneByExample(TbMethodExample example);
}
