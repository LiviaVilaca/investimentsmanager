package com.liviavilaca.investimentsmanager.controller.v1.security;

import com.liviavilaca.investimentsmanager.dto.model.security.JwtTokenDTO;
import com.liviavilaca.investimentsmanager.dto.model.security.JwtUserDTO;
import com.liviavilaca.investimentsmanager.util.security.JwtTokenUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;


    @PostMapping
    @ApiOperation(value = "Route to authentication a Client to use the API", consumes = "application/json", produces = "application/json")
    public ResponseEntity<JwtTokenDTO> generateTokenJwt(@Valid @RequestBody JwtUserDTO user){
        Authentication authentication = authenticationManager.authenticate
                (new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
        String token = jwtTokenUtil.getToken(userDetails);

        return new ResponseEntity<JwtTokenDTO>(JwtTokenDTO.builder().token(token).build(), HttpStatus.OK);
    }

}
