package shop.mtcoding.miniproject2.model;

import java.sql.Timestamp;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserRepository {
        public List<User> findAll();

        public User findById(int id);

        public User findByPersonId(int pInfoId);

        public User findByCInfoId(int cInfoId);

        public User findPersonByEmailAndPassword(@Param("email") String email, @Param("password") String password);

        public User findCompanyByEmailAndPassword(@Param("email") String email, @Param("password") String password);

        public User findByEmail(@Param("email") String email);

        // 먼저 Company나 Person 생성한 후, 그 id를 들고와서 insert 하면된다.
        public int insert(@Param("email") String email, @Param("password") String password, @Param("salt") String salt,
                        @Param("pInfoId") int pInfoId,
                        @Param("cInfoId") int cInfoId);

        public int updateById(@Param("id") int id,
                        @Param("email") String email,
                        @Param("password") String password,
                        @Param("pInfoId") int pInfoId,
                        @Param("cInfoId") int cInfoId,
                        @Param("createdAt") Timestamp createdAt);

        public int deleteById(int id);

}
