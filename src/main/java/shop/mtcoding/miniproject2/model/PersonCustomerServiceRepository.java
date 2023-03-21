package shop.mtcoding.miniproject2.model;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface PersonCustomerServiceRepository {

    public List<PersonCustomerService> findAll();

    public PersonCustomerService findById(int id);

    public int insert(@Param("question") String question, @Param("answer") String answer);

    public int updateById(@Param("question") String question, @Param("answer") String answer);

    public int deleteById(int id);
}
