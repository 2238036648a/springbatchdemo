package com.example.springbatch.readdb.batch.write;

import com.example.springbatch.readdb.entity.UserCopyEntity;
import com.example.springbatch.readdb.entity.UserEntity;
import com.example.springbatch.readdb.service.UserServiceDB;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.hibernate5.HibernateTemplate;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;
//@Transactional
public class JdbcItemWriter implements ItemWriter<UserEntity>, InitializingBean{

        private JdbcTemplate jdbcTemplate;

        @Autowired
        private UserServiceDB userServiceDB;

        public JdbcItemWriter(JdbcTemplate jdbcTemplate) {
            this.jdbcTemplate = jdbcTemplate;
        }


   /* @Resource/@PersistenceContext
    protected EntityManager em;
    private Session getCurrentSession() {
        Session session = em.unwrap(Session.class);
        return session;
    }
*/

    @Override
        public void write(List<? extends UserEntity> dealBeans) {

            //备份clear_atm

            try {
                for( int i=0 ;i<dealBeans.size();i++ ){
                    //删除当前表，然后更新
                   // jdbcTemplate.update( "SQL语句,传入参数");
                    System.out.println("-----------"+dealBeans.get(i));
                    //dealBean.getUser_name(),dealBean.getSex(),dealBean.getAge(),dealBean.getAddress(),dealBean.getStatus(),dealBean.getCreate_time(),dealBean.getUpdate_time()

                    UserEntity dealBean = dealBeans.get(i);
                    String sql  = "insert into sys_user_copy1(user_name, sex, age, address, status, create_time) values(?,?,?,?,?,?)";
                    jdbcTemplate.update(sql,dealBean.getUser_name(),dealBean.getSex(),dealBean.getAge(),dealBean.getAddress(),dealBean.getStatus(),i+1);
       //             userServiceDB.insert(dealBean);
//
//                    UserCopyEntity userEntity = new UserCopyEntity();
//                    userEntity.setUser_name(dealBean.getUser_name());
//                    userEntity.setSex(dealBean.getSex());
//                    userEntity.setAge(dealBean.getAge());
//                    userEntity.setAddress(dealBean.getAddress());
//                    userEntity.setStatus(dealBean.getStatus());
//                    userEntity.setCreate_time(dealBean.getCreate_time());
                    //this.getCurrentSession().save(userEntity);


                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void afterPropertiesSet() throws Exception {
            // TODO Auto-generated method stub

        }
}
