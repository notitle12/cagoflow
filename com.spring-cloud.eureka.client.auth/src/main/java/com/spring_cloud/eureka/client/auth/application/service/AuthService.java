package com.spring_cloud.eureka.client.auth.application.service;

import com.spring_cloud.eureka.client.auth.presentation.requestDto.LoginRequestDto;
import com.spring_cloud.eureka.client.auth.application.responseDto.SignUpRequestDto;
import com.spring_cloud.eureka.client.auth.application.responseDto.UserInfoResponseDto;
import com.spring_cloud.eureka.client.auth.presentation.requestDto.UserInfoUpdateRequestDto;
import com.spring_cloud.eureka.client.auth.application.security.JwtUtil;
import com.spring_cloud.eureka.client.auth.application.security.UserDetailsImpl;
import com.spring_cloud.eureka.client.auth.domain.user.User;
import com.spring_cloud.eureka.client.auth.domain.repository.UserRepository;
import com.spring_cloud.eureka.client.auth.domain.user.UserRoleEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // MANAGER_TOKEN
    private final String DELIVERY_MANAGER_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";
    // MANAGER_TOKEN
    private final String HUB_MANAGER_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";
    //MASTER_TOKEN
    private final String MASTER_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";

    // 회원가입
    @Transactional
    public void signUp(SignUpRequestDto requestDto) {
        String username = requestDto.getUsername();
        String password = passwordEncoder.encode(requestDto.getPassword());

        // 회원 중복 확인
        Optional<User> checkUsername = userRepository.findByUsername(username);
        if (checkUsername.isPresent()) {
            throw new IllegalArgumentException("중복된 사용자가 존재합니다.");
        }

        // 사용자 ROLE 확인
        boolean isDeliveryManager = false;
        boolean isHubManager = false;
        boolean isMaster = false;

        // 기본고객
        UserRoleEnum role = UserRoleEnum.COMPANY;

        // 배송관리자
        if (requestDto.isDeliveryManager()) {
            if (!DELIVERY_MANAGER_TOKEN.equals(requestDto.getDeliveryManagerToken())) {
                throw new IllegalArgumentException("사업주 코드가 틀려 등록이 불가능합니다.");
            }
            isDeliveryManager = true;
            role = UserRoleEnum.DELIVERY_MANAGER;
        }

        // 허브관리자
        if (requestDto.isHubManager()) {
            if (!HUB_MANAGER_TOKEN.equals(requestDto.getHubManagerToken())) {
                throw new IllegalArgumentException("사업주 코드가 틀려 등록이 불가능합니다.");
            }
            isHubManager = true;
            role = UserRoleEnum.HUB_MANAGER;
        }

        // 마스터
        if (requestDto.isMaster()) {
            if (!MASTER_TOKEN.equals(requestDto.getMasterToken())) {
                throw new IllegalArgumentException("사업주 코드가 틀려 등록이 불가능합니다.");
            }
            isMaster = true;
            role = UserRoleEnum.MASTER;
        }

        // 사용자 등록
        User user = new User(username, password, role);
        userRepository.save(user);
    }

    // 로그인
    public String login(LoginRequestDto loginRequestDto) {
        try {
            // 사용자의 인증 정보 생성
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequestDto.getUsername(),
                            loginRequestDto.getPassword()
                    )
            );

            // 인증이 성공하면 인증된 사용자 정보 가져오기
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            User user = userDetails.getUser();

            // 사용자 삭제 여부 확인
            if (user.isDeletedSoftly()) {
                // 삭제된 사용자일 경우 콘솔에 메시지 출력
                System.out.println("삭제된 유저가 로그인 시도했습니다. Username: " + user.getUsername());
                throw new RuntimeException("삭제된 사용자입니다.");
            }

            // 인증이 성공하면 인증된 사용자 정보 가져오기
//            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            Long userId = userDetails.getUser().getUserId();
            String username = userDetails.getUsername();
            UserRoleEnum role = userDetails.getUser().getRole();

            // JWT 토큰 생성
            return jwtUtil.createToken(userId, username, role);
        } catch (AuthenticationException e) {
            throw new RuntimeException("Invalid username or password");
        }
    }

    // 사용자 정보 수정
    @Transactional
    public void updateUserInfo(String username, UserInfoUpdateRequestDto requestDto) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        // 비밀번호 수정
        if (requestDto.getPassword() != null && !requestDto.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(requestDto.getPassword()));
        }

        userRepository.save(user);
    }

    // 사용자 소프트 삭제
    @Transactional
    public void softDeleteUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        user.deleteSoftly(username); // BaseEntity의 deleteSoftly 메서드를 호출
        userRepository.save(user);
    }

    // 사용자 소프트 삭제 취소
    @Transactional
    public void restoreUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        user.undoDelete(); // User의 restore 메서드 호출
        userRepository.save(user);
    }

    // 현재 로그인한 사용자 정보 조회 (헤더에서 역할을 받아 처리)
    public UserInfoResponseDto getUserInfo(String username, String roleHeader) {
        // 사용자 조회
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        // 역할 정보를 UserRoleEnum으로 변환
        UserRoleEnum role = UserRoleEnum.valueOf(roleHeader.toUpperCase());

        // 사용자 정보와 역할 정보를 반환
        return new UserInfoResponseDto(user.getUsername(), role);
    }

    // 모든 사용자 정보 조회 (마스터 권한)
    public List<UserInfoResponseDto> getAllUsers() {
        List<User> users = userRepository.findAll();

        // 각 사용자의 정보를 UserInfoResponseDto로 변환하여 반환
        return users.stream()
                .map(user -> new UserInfoResponseDto(user.getUsername(), user.getRole()))
                .toList();
    }
}
