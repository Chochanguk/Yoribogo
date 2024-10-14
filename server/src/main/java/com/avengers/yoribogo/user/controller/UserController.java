package com.avengers.yoribogo.user.controller;

import com.avengers.yoribogo.common.ResponseDTO;
import com.avengers.yoribogo.common.exception.CommonException;
import com.avengers.yoribogo.common.exception.ErrorCode;
import com.avengers.yoribogo.user.domain.enums.SignupPath;
import com.avengers.yoribogo.user.domain.vo.email.EmailVerificationSignupVO;
import com.avengers.yoribogo.user.domain.vo.email.EmailVerificationVO;
import com.avengers.yoribogo.user.domain.vo.email.ResponseEmailMessageVO;
import com.avengers.yoribogo.user.dto.UserDTO;
import com.avengers.yoribogo.user.dto.email.EmailVerificationUserIdRequestDTO;
import com.avengers.yoribogo.user.dto.email.EmailVerificationUserPasswordRequestDTO;
import com.avengers.yoribogo.user.service.EmailVerificationService;
import com.avengers.yoribogo.user.service.OAuth2LoginService;
import com.avengers.yoribogo.user.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController("userCommandController")
@RequestMapping("/api/users")
public class UserController {
    private final Environment env;
    private final UserService userService;
    private final ModelMapper modelMapper;

    private final OAuth2LoginService oAuth2LoginService;
    @Autowired
    public UserController(Environment env, UserService userService,ModelMapper modelMapper,
                          OAuth2LoginService oAuth2LoginService) {
        this.env = env;
        this.userService = userService;
        this.modelMapper=modelMapper;
        this.oAuth2LoginService = oAuth2LoginService;
    }

    @GetMapping("/health")
    public String status() {
        return "I'm Working in User Service on port "
                + env.getProperty("local.server.port");
    }

    // 설명. 이메일 인증 서비스
    @Autowired
    private EmailVerificationService emailVerificationService;

    // 설명. 1. 이메일 전송 API (회원가입시 실행)
    @PostMapping("/verification-email/signup")
    public ResponseDTO<?> sendVerificationEmail(@RequestBody @Validated EmailVerificationSignupVO request) {
        emailVerificationService.sendVerificationEmail(request.getEmail());

        ResponseEmailMessageVO responseEmailMessageVO = new ResponseEmailMessageVO();
        responseEmailMessageVO.setMessage("인증 코드가 이메일로 전송되었습니다.");
        return ResponseDTO.ok(responseEmailMessageVO);
    }

    // 설명. 1.2. 이메일 전송 API(아이디 찾기시 실행, 일반 회원만 아이디찾기 가능)
    @PostMapping("/verification-email/auth-id")
    public ResponseDTO<?> sendVerificationEmailForUserId(@RequestBody @Validated EmailVerificationUserIdRequestDTO request) {
        // 닉네임, 가입 구분, 이메일이 일치하는 사용자가 있는지 확인
        UserDTO user = userService.findUserByUserNicknameAndSignupPathAndEmail(request.getNickname(), SignupPath.NORMAL, request.getEmail());

        if (user == null) {
            throw new CommonException(ErrorCode.NOT_FOUND_USER);
        }

        // 유효성 검사후 가능하면 이메일 전송
        emailVerificationService.sendVerificationEmail(request.getEmail());

        ResponseEmailMessageVO responseEmailMessageVO = new ResponseEmailMessageVO();
        responseEmailMessageVO.setMessage("아이디 찾기를 위한 인증 코드가 이메일로 전송되었습니다.");
        return ResponseDTO.ok(responseEmailMessageVO);
    }

    // 설명. 1.3. 이메일 전송 API (비밀번호 찾기시 실행)
    @PostMapping("/verification-email/user-password")
    public ResponseDTO<?> sendVerificationEmailForUserPassword(@RequestBody @Validated EmailVerificationUserPasswordRequestDTO request) {
        // NORMAL_{userAuthId}와 이메일이 일치하는 사용자가 있는지 확인
        UserDTO user = userService.findUserByUserAuthIdAndEmail(request.getUserAuthId(), request.getEmail());

        if (user == null) {
            throw new CommonException(ErrorCode.NOT_FOUND_USER);
        }

        // 유효성 검사후 가능하면 이메일 전송
        emailVerificationService.sendVerificationEmail(request.getEmail());

        ResponseEmailMessageVO responseEmailMessageVO = new ResponseEmailMessageVO();
        responseEmailMessageVO.setMessage("비밀번호 찾기를 위한 인증 코드가 이메일로 전송되었습니다.");
        return ResponseDTO.ok(responseEmailMessageVO);
    }


    //설명. 2.1 이메일 인증번호 검증 API (회원가입,아이디,비밀번호 찾기 실행)
    @PostMapping("/verification-email/confirmation")
    public ResponseDTO<?> verifyEmail(@RequestBody @Validated EmailVerificationVO request) {
        boolean isVerified = emailVerificationService.verifyCode(request.getEmail(), request.getCode());

        ResponseEmailMessageVO responseEmailMessageVO =new ResponseEmailMessageVO();
        responseEmailMessageVO.setMessage("이메일 인증이 완료되었습니다.");
        if (isVerified) {
            return ResponseDTO.ok(responseEmailMessageVO);
        } else {
            return ResponseDTO.fail(new CommonException(ErrorCode.INVALID_VERIFICATION_CODE));
        }
    }

}
