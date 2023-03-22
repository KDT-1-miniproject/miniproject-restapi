package shop.mtcoding.miniproject2.model;

import java.sql.Timestamp;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import shop.mtcoding.miniproject2.dto.company.CompanyInfoOutDto;

@Mapper
public interface CompanyRepository {
        public List<Company> findAll();

        public Company findById(int id);

        // public int insert(@Param("name") String name, @Param("number") String number,
        // @Param("address") String address,
        // @Param("managerName") String managerName);

        public int insert(Company company);

        public int updateById(@Param("id") int id,
                        @Param("logo") String logo,
                        @Param("name") String name,
                        @Param("number") String number,
                        @Param("bossName") String bossName,
                        @Param("address") String address,
                        @Param("managerName") String managerName,
                        @Param("managerPhone") String managerPhone,
                        @Param("size") int size,
                        @Param("cyear") int cyear,
                        @Param("createdAt") Timestamp createdAt);

        public int deleteById(int id);

        public CompanyInfoOutDto findByIdWithUser(int id);

        public Company findByCompanyNameAndNumber(@Param("name") String name, @Param("number") String number);

}
