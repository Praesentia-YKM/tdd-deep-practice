package commerce.api.controller;

import commerce.Seller;
import commerce.SellerRepository;
import commerce.command.CreateSellerCommand;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public record SellerSignUpController(
        SellerRepository repository,
        PasswordEncoder passwordEncoder
) {

    public static final String EMAIL_REGEX = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
    public static final String USERNAME_REGEX = "^[a-zA-Z0-9_-]{3,20}$";

    @PostMapping("/seller/signUp")
    public ResponseEntity<?> signUp(@RequestBody CreateSellerCommand command) {
        // 요청 데이터 유효성 검사
        if (!isCommandValid(command)) {
            return ResponseEntity.badRequest().build();
        }

        // 비밀번호 암호화
        String hashedPassword = passwordEncoder.encode(command.password());

        // Seller 엔티티 생성 및 데이터 설정
        var seller = new Seller();
        seller.setEmail(command.email());
        seller.setUsername(command.username());
        seller.setHashedPassword(hashedPassword);

        try {
            // 데이터베이스에 저장
            repository.save(seller);
        } catch (DataIntegrityViolationException exception) {
            // DB 제약 조건 위반 시 (e.g., 이메일/사용자명 중복) 400 에러 반환
            return ResponseEntity.badRequest().build();
        }

        // 성공 시 204 No Content 응답 반환
        return ResponseEntity.noContent().build();
    }


    private static boolean isCommandValid(CreateSellerCommand command) {
        return isEmailValid(command.email())
                && isUsernameValid(command.username())
                && isPasswordValid(command.password());
    }

    private static boolean isEmailValid(String email) {
        return email != null && email.matches(EMAIL_REGEX);
    }

    private static boolean isUsernameValid(String username) {
        return username != null && username.matches(USERNAME_REGEX);
    }

    private static boolean isPasswordValid(String password) {
        return password != null && password.length() >= 8;
    }
}
