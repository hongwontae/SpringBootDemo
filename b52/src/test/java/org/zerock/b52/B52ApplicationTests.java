package org.zerock.b52;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.extern.log4j.Log4j2;

@SpringBootTest
@Log4j2
class B52ApplicationTests {

	@Test
	void contextLoads() {
	}

	@Autowired
    PasswordEncoder passwordEncoder;

	   @Test
    public void passwordEncode() throws Exception{
        log.info((passwordEncoder.encode("1234")));
    }

	  @Test
    public void passwordTest() throws Exception{
    	String encodePasswd = "$2a$10$USbExG2YOZJqu5rR9eWAqO3NqwjS6c8uI0c695cnURA2gxqRnx41O";
    	String password = "1234";
    	boolean test = passwordEncoder.matches(password, encodePasswd);
    	log.info(test);
    }

}
