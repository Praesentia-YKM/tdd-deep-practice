package test.commerce;

import java.util.UUID;

public class UsernameGenerator {

    // uuid기반 항상 새로운 사용자id를 만드는 방식
    // 테스트 모듈 안에서만 쓰임(트랜잭션 롤백 방식으로 할 수 있지만 성능 상 이 방식이 더 좋다고 판단)
    public static String generateUsername() {
        return "username" + UUID.randomUUID();
    }
}
