package jpabook.jpapractice;

import com.fasterxml.jackson.datatype.hibernate5.jakarta.Hibernate5JakartaModule;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;

@SpringBootTest
class JpapracticeApplicationTests {

	@Bean
	Hibernate5JakartaModule hibernate5JakartaModule() {
		return new Hibernate5JakartaModule();
	}
	// 해당 모듈은 사용하지 말고, 즉, Entity 를 직접 반환하지 말고,
	// DTO 를 만들어서 반환하자.

}
