package com.github.mtahasahin.evently.entity;

import com.github.mtahasahin.evently.domainevent.UserFollowedEvent;
import com.github.mtahasahin.evently.domainevent.UserUnfollowedEvent;
import com.github.mtahasahin.evently.enums.AuthProvider;
import lombok.*;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.IndexedEmbedded;
import org.springframework.data.domain.DomainEvents;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import javax.persistence.*;
import java.util.*;

@Indexed(index = "user_index")
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "USERS")
public class AppUser extends Auditable implements UserDetails, OAuth2User, CredentialsContainer {

    @Transient
    private List<Object> domainEvents = new ArrayList<>();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @FullTextField
    @Column(name = "USERNAME", unique = true)
    private String username;

    @Column(name = "EMAIL", unique = true)
    private String email;

    @Column(name = "PASSWORD")
    private String password;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "USER_AUTHORITIES",
            joinColumns = @JoinColumn(name = "USER_ID"),
            inverseJoinColumns = @JoinColumn(name = "AUTHORITY_ID"))
    private Set<Authority> authorities = new HashSet<>();

    @Transient
    private Map<String, Object> attributes;

    @IndexedEmbedded
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private UserProfile userProfile;

    @OneToMany(mappedBy = "organizer", orphanRemoval = true)
    private Set<Event> organizedEvents = new HashSet<>();

    public boolean isOrganizing(Event event) {
        return organizedEvents.contains(event);
    }

    @OneToMany(mappedBy = "applicant", orphanRemoval = true)
    private Set<EventApplication> eventApplications = new HashSet<>();

    public boolean isJoiningEvent(Event event) {
        return eventApplications.stream().anyMatch(e -> e.getEvent() == event && e.isConfirmed());
    }

    public boolean isWaitingForApprovalForEvent(Event event) {
        return eventApplications.stream().anyMatch(e -> e.getEvent() == event && !e.isConfirmed());
    }

    @OneToMany(
            mappedBy = "following",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<FollowerFollowing> followers = new ArrayList<>();

    @OneToMany(
            mappedBy = "follower",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<FollowerFollowing> followings = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private AuthProvider provider;

    private String providerId;

    public void addFollower(AppUser user) {
        FollowerFollowing followerFollowing = new FollowerFollowing(user, this, this.userProfile.isProfilePublic());
        followers.add(followerFollowing);
        user.followings.add(followerFollowing);

        if (this.userProfile.isProfilePublic()) {
            domainEvents.add(new UserFollowedEvent(user.getId(), this.getId()));
        }
    }

    public void removeFollower(AppUser user) {
        for (Iterator<FollowerFollowing> iterator = followers.iterator(); iterator.hasNext(); ) {
            FollowerFollowing followerFollowing = iterator.next();

            if (followerFollowing.getFollowing().equals(this) &&
                    followerFollowing.getFollower().equals(user)) {
                domainEvents.add(new UserUnfollowedEvent(followerFollowing.getFollower().getId(), followerFollowing.getFollowing().getId()));
                iterator.remove();
                followerFollowing.getFollower().getFollowings().remove(followerFollowing);
                followerFollowing.setFollower(null);
                followerFollowing.setFollowing(null);
            }
        }
    }

    public boolean isFollowing(AppUser user) {
        return followings.stream().anyMatch(e -> e.getFollowing() == user && e.isConfirmed());
    }

    public boolean hasFollowingRequest(AppUser user) {
        return followings.stream().anyMatch(e -> e.getFollowing() == user && !e.isConfirmed());
    }

    @DomainEvents
    public List<Object> getDomainEvents() {
        return domainEvents;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public void eraseCredentials() {
        password = null;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public AppUser returnWithAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
        return this;
    }

    @Override
    public String getName() {
        return String.valueOf(id);
    }
}
