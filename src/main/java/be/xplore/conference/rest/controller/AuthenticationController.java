package be.xplore.conference.rest.controller;

import be.xplore.conference.excpetion.AdminNameAlreadyExistsException;
import be.xplore.conference.excpetion.EmailAlreadyExistsException;
import be.xplore.conference.model.Admin;
import be.xplore.conference.rest.dto.AdminDto;
import be.xplore.conference.rest.dto.LoginDto;
import be.xplore.conference.rest.dto.RegisterDto;
import be.xplore.conference.rest.dto.TokenDto;
import be.xplore.conference.rest.jwt.JwtTokenProvider;
import be.xplore.conference.service.AdminService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/authentication")
public class AuthenticationController {

    private AdminService adminService;
    private ModelMapper modelMapper;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthenticationController(AdminService adminService,
                                    ModelMapper modelMapper,
                                    JwtTokenProvider jwtTokenProvider) {
        this.adminService = adminService;
        this.modelMapper = modelMapper;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/register")
    public ResponseEntity<AdminDto> register(@RequestBody RegisterDto registerDto) throws EmailAlreadyExistsException, AdminNameAlreadyExistsException {
        Admin adminToRegister = new Admin(registerDto.getAdminName(),registerDto.getEmail(),registerDto.getPassword());
        Admin registeredAdmin = adminService.register(adminToRegister);
        return  new ResponseEntity<>(modelMapper.map(registeredAdmin, AdminDto.class), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<TokenDto> logIn(@RequestBody LoginDto loginDTO) throws UsernameNotFoundException {
        Admin admin = adminService.loadPlayerThatHasPassword(loginDTO.getAdminNameOrEmail());
        String jwt = jwtTokenProvider.authenticateAndGenerateToken(admin.getAdminName(), loginDTO.getPassword());
        return new ResponseEntity<>(new TokenDto(jwt, admin.getAdminName()), HttpStatus.OK);
    }

}
