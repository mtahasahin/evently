package com.github.mtahasahin.evently.security;

import com.github.mtahasahin.evently.entity.AppUser;
import com.github.mtahasahin.evently.entity.UserProfile;
import com.github.mtahasahin.evently.enums.AuthProvider;
import com.github.mtahasahin.evently.exception.OAuth2AuthenticationProcessingException;
import com.github.mtahasahin.evently.repository.AuthorityRepository;
import com.github.mtahasahin.evently.repository.UserRepository;
import com.github.mtahasahin.evently.security.oauth2.user.OAuth2UserInfo;
import com.github.mtahasahin.evently.security.oauth2.user.OAuth2UserInfoFactory;
import com.github.mtahasahin.evently.interfaces.FileStorageService;
import com.github.mtahasahin.evently.util.ImageUtils;
import com.github.mtahasahin.evently.util.RandomStringGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.Optional;

import static com.github.mtahasahin.evently.util.UsernameUtils.createUniqueUsername;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;
    private final FileStorageService fileStorageService;

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);

        try {
            return processOAuth2User(oAuth2UserRequest, oAuth2User);
        } catch (AuthenticationException ex) {
            throw ex;
        } catch (Exception ex) {
            // Throwing an instance of AuthenticationException will trigger the OAuth2AuthenticationFailureHandler
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
        }
    }

    private OAuth2User processOAuth2User(OAuth2UserRequest oAuth2UserRequest, OAuth2User oAuth2User) {
        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(oAuth2UserRequest.getClientRegistration().getRegistrationId(), oAuth2User.getAttributes());
        if(StringUtils.isEmpty(oAuth2UserInfo.getEmail())) {
            throw new OAuth2AuthenticationProcessingException("Email not found from OAuth2 provider");
        }

        Optional<AppUser> userOptional = userRepository.findByEmail(oAuth2UserInfo.getEmail());
        AppUser user;
        if(userOptional.isPresent()) {
            user = userOptional.get();
            if(!user.getProvider().equals(AuthProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId()))) {
                throw new OAuth2AuthenticationProcessingException("Looks like you're signed up with " +
                        user.getProvider() + " account. Please use your " + user.getProvider() +
                        " account to login.");
            }
        } else {
            user = registerNewUser(oAuth2UserRequest, oAuth2UserInfo);
        }

        return user.returnWithAttributes(oAuth2User.getAttributes());
    }

    private AppUser registerNewUser(OAuth2UserRequest oAuth2UserRequest, OAuth2UserInfo oAuth2UserInfo) {
        AppUser user = new AppUser();

        user.setUsername(createUniqueUsername(oAuth2UserInfo.getName(), userRepository));
        user.setProvider(AuthProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId()));
        user.setProviderId(oAuth2UserInfo.getId());
        user.setEmail(oAuth2UserInfo.getEmail());
        userRepository.save(user);

        var userProfileEntity = new UserProfile();
        userProfileEntity.setName(oAuth2UserInfo.getName());

        try {
            BufferedImage image = ImageIO.read(new URL(oAuth2UserInfo.getImageUrl()));
            var s3Key = user.getId() + "/" + "profile-" + RandomStringGenerator.generate(5) + ".jpg";
            var s3Url = fileStorageService.uploadFile(s3Key, new ByteArrayInputStream(ImageUtils.toByteArray(image,"jpg")));
            userProfileEntity.setAvatar(s3Url);
            image.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        userProfileEntity.setProfilePublic(true);
        userProfileEntity.setRegistrationDate(LocalDateTime.now());

        userProfileEntity.setUser(user);
        userProfileEntity.setLanguage("en");
        user.setUserProfile(userProfileEntity);

        var userAuthority = authorityRepository.getByAuthority("ROLE_USER");
        user.getAuthorities().add(userAuthority);
        return userRepository.save(user);
    }

}
