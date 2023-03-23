package shop.mtcoding.miniproject2.model;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import shop.mtcoding.miniproject2.dto.customerService.CustomerServDto.CompanyCustomerServiceDto;
import shop.mtcoding.miniproject2.dto.customerService.CustomerServDto.PersonCustomerServiceDto;

@Mapper
public interface CustomerServRepository {

    public List<PersonCustomerServiceDto> findAllpersonCS();

    public List<CompanyCustomerServiceDto> findAllcompanyCS();

    public CustomerServ findById(int id);

    public int insert(@Param("question") String question, @Param("answer") String answer);

    public int updateById(@Param("question") String question, @Param("answer") String answer);

    public int deleteById(int id);
}
