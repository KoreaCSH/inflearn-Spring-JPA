package jpabook.jpapractice;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

// spring 관련 테스트에는 SpringBootTest 애노테이션 추가
// test에 Transactional 애노테이션 추가하면 실행 후 바로 rollback 한다.
// 테스트 결과를 db에 등록하고 싶다면 @Rollback(false) 메서드 추가하기.

@SpringBootTest
@Transactional
class MemberRepositoryTest {




}