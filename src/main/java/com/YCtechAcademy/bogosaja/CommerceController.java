package com.YCtechAcademy.bogosaja;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



//User 타입과 product타입은 아직 만들지 않았습니다.
@RestController
@RequestMapping("/api")
public class CommerceController {

    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@RequestBody User user) {
        // 회원가입 로직 구현
        return("temp");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User user) {
        // 로그인 로직 구현
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        // 로그아웃 로직 구현
        return("temp");
    }

    @DeleteMapping("/users/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable String userId) {
        // 회원탈퇴 로직 구현
        return("temp")
    }

    @PutMapping("/users/{userId}")
    public ResponseEntity<String> updateUser(@PathVariable String userId, @RequestBody User user) {
        // 회원정보수정 로직 구현
    }

    @PostMapping("/products")
    public ResponseEntity<String> createProduct(@RequestBody Product product) {
        // 상품등록 로직 구현
    }

    @PutMapping("/products/{productId}")
    public ResponseEntity<String> updateProduct(@PathVariable String productId, @RequestBody Product product) {
        // 상품수정 로직 구현
    }

    @DeleteMapping("/products/{productId}")
    public ResponseEntity<String> deleteProduct(@PathVariable String productId) {
        // 상품삭제 로직 구현
    }

    @PostMapping("/products/{productId}/like")
    public ResponseEntity<String> likeProduct(@PathVariable String productId) {
        // 상품찜 로직 구현
    }

    @DeleteMapping("/products/{productId}/like")
    public ResponseEntity<String> unlikeProduct(@PathVariable String productId) {
        // 상품찜 해제 로직 구현
    }

    @GetMapping("/products")
    public ResponseEntity<List<Product>> searchProducts(@RequestParam("keyword") String keyword) {
        // 상품검색 로직 구현
    }

    @GetMapping("/main")
    public ResponseEntity<String> getMainPage() {
        // 메인페이지 로직 구현
    }
}
